package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationWithPagination;
import it.pagopa.selfcare.mscore.model.delegation.GetDelegationParameters;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.CustomError.INSTITUTION_TAX_CODE_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.GenericError.*;
@Slf4j
@Service
public class DelegationServiceImpl implements DelegationService {

    private static final int DEFAULT_DELEGATIONS_PAGE_SIZE = 10000;
    private final DelegationConnector delegationConnector;
    private final MailNotificationService notificationService;
    private final InstitutionService institutionService;
    private static final String PROD_PAGOPA = "prod-pagopa";

    public DelegationServiceImpl(DelegationConnector delegationConnector,
                                 MailNotificationService notificationService,
                                 InstitutionService institutionService) {
        this.delegationConnector = delegationConnector;
        this.notificationService = notificationService;
        this.institutionService = institutionService;
    }

    @Override
    public Delegation createDelegation(Delegation delegation) {
        /*
            In case of prod-pagopa product, in the attribute "to" of the delegation object a taxCode is inserted.
            So we have to retrieve the institutionId from the taxCode and set it in the "to" attribute.
         */
        if(PROD_PAGOPA.equals(delegation.getProductId())) {
            setPartnerByInstitutionTaxCode(delegation);
        } else {
            setTaxCodesByInstitutionIds(delegation);
        }

        Delegation savedDelegation = checkIfExistsAndSaveDelegation(delegation);
        try {
            notificationService.sendMailForDelegation(delegation.getInstitutionFromName(), delegation.getProductId(), delegation.getTo());
        } catch (Exception e) {
            log.error(SEND_MAIL_FOR_DELEGATION_ERROR.getMessage() + ":", e.getMessage(), e);
        }
        return savedDelegation;
    }

    private Delegation checkIfExistsAndSaveDelegation(Delegation delegation) {
        if(checkIfExistsWithStatus(delegation, DelegationState.ACTIVE)) {
            throw new ResourceConflictException(String.format(CustomError.CREATE_DELEGATION_CONFLICT.getMessage()),
                    CustomError.CREATE_DELEGATION_CONFLICT.getCode());
        }

        Delegation savedDelegation;
        try {
            if(checkIfExistsWithStatus(delegation, DelegationState.DELETED)){
                savedDelegation = delegationConnector.findAndActivate(delegation.getFrom(), delegation.getTo(), delegation.getProductId());
            } else {
                delegation.setCreatedAt(OffsetDateTime.now());
                delegation.setUpdatedAt(OffsetDateTime.now());
                delegation.setStatus(DelegationState.ACTIVE);
                savedDelegation = delegationConnector.save(delegation);
            }
            institutionService.updateInstitutionDelegation(delegation.getTo(), true);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode());
        }
        return savedDelegation;
    }

    private void setPartnerByInstitutionTaxCode(Delegation delegation) {
        /*
            In case the api returns more institutions we always try to take the PT,
            otherwise it is okay to take the first one
            It is caused by this issue https://pagopa.atlassian.net/wiki/spaces/SCP/pages/1058832442/RFC+-+Gestione+di+pi+institutionType+su+una+Institution
         */
        List<Institution> institutionsTo = institutionService.getInstitutions(delegation.getTo(), null);
        Institution partner = institutionsTo.stream()
                .filter(institution -> institution.getInstitutionType() == InstitutionType.PT)
                .findFirst()
                .orElse(institutionsTo.stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                                INSTITUTION_TAX_CODE_NOT_FOUND.getCode())
                        ));
        delegation.setToTaxCode(partner.getTaxCode());
        delegation.setBrokerType(partner.getInstitutionType());
        delegation.setTo(partner.getId());

        /*
            Retrieve delegator's institution to set taxCodeFrom
         */
        Institution institutionFrom = institutionService.retrieveInstitutionById(delegation.getFrom());
        delegation.setFromTaxCode(institutionFrom.getTaxCode());
        delegation.setInstitutionType(institutionFrom.getInstitutionType());
    }

    private void setTaxCodesByInstitutionIds(Delegation delegation){
        /*
            Retrieve both delegator's and partner's institutions to set taxCodeFrom and taxCodeTo
         */
        Institution institutionTo = institutionService.retrieveInstitutionById(delegation.getTo());
        Institution institutionFrom = institutionService.retrieveInstitutionById(delegation.getFrom());

        delegation.setToTaxCode(institutionTo.getTaxCode());
        delegation.setBrokerType(institutionTo.getInstitutionType());

        delegation.setFromTaxCode(institutionFrom.getTaxCode());
        delegation.setInstitutionType(institutionFrom.getInstitutionType());
    }

    @Override
    public Delegation createDelegationFromInstitutionsTaxCode(Delegation delegation) {

        List<Institution> institutionsTo = institutionService.getInstitutions(delegation.getToTaxCode(), delegation.getToSubunitCode());
        // TODO: remove filter when getInstitutions API will be fixed.
        /*
            If we call getInstitutions by empty subunitCode parameter, we have to filter retrieved list for institution
            with blank subunitCode field, otherwise we take first element returned by api.
        */
        Institution partner = institutionsTo.stream()
                .filter(institution -> StringUtils.hasText(delegation.getToSubunitCode()) || !StringUtils.hasText(institution.getSubunitCode()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                        INSTITUTION_TAX_CODE_NOT_FOUND.getCode()));
        delegation.setTo(partner.getId());
        delegation.setBrokerType(partner.getInstitutionType());

        // TODO: remove filter when getInstitutions API will be fixed.
        /*
            If we call getInstitutions by empty subunitCode parameter, we have to filter retrieved list for institution
            with blank subunitCode field, otherwise we take first element returned by api.
        */
        List<Institution> institutionsFrom = institutionService.getInstitutions(delegation.getFromTaxCode(), delegation.getFromSubunitCode());
        Institution from = institutionsFrom.stream()
                .filter(institution -> StringUtils.hasText(delegation.getFromSubunitCode()) || !StringUtils.hasText(institution.getSubunitCode()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                        INSTITUTION_TAX_CODE_NOT_FOUND.getCode()));
        delegation.setFrom(from.getId());
        delegation.setInstitutionType(from.getInstitutionType());

        return checkIfExistsAndSaveDelegation(delegation);
    }

    @Override
    public void deleteDelegationByDelegationId(String delegationId) {
        String institutionId;
        try{
            Delegation delegation = delegationConnector.findByIdAndModifyStatus(delegationId, DelegationState.DELETED);
            institutionId = delegation.getTo();
        } catch (Exception e) {
            throw new MsCoreException(DELETE_DELEGATION_ERROR.getMessage(), DELETE_DELEGATION_ERROR.getCode());
        }
        try{
            if(!delegationConnector.checkIfDelegationsAreActive(institutionId)) {
                institutionService.updateInstitutionDelegation(institutionId, false);
            }
        } catch (Exception e) {
            delegationConnector.findByIdAndModifyStatus(delegationId, DelegationState.ACTIVE);
            throw new MsCoreException(DELETE_DELEGATION_ERROR.getMessage(), DELETE_DELEGATION_ERROR.getCode());
        }
    }

    @Override
    public boolean checkIfExistsWithStatus(Delegation delegation, DelegationState status) {
        return delegationConnector.checkIfExistsWithStatus(delegation, status);
    }

    @Override
    public List<Delegation> getDelegations(String from, String to, String productId, String search, String taxCode,
                                           Optional<Order> order, Optional<Integer> page, Optional<Integer> size) {
        int pageSize = size.filter(s -> s > 0).filter(s -> s <= DEFAULT_DELEGATIONS_PAGE_SIZE).orElse(DEFAULT_DELEGATIONS_PAGE_SIZE);
        return delegationConnector.find(from, to, productId, search, taxCode, order.orElse(Order.NONE), page.orElse(0), pageSize);
    }

    @Override
    public DelegationWithPagination getDelegationsV2(GetDelegationParameters delegationParameters) {
        return delegationConnector.findAndCount(delegationParameters);
    }
}
