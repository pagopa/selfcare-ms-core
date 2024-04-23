package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
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
        }

        if (checkIfExists(delegation)) {
            throw new ResourceConflictException(String.format(CustomError.CREATE_DELEGATION_CONFLICT.getMessage()),
                    CustomError.CREATE_DELEGATION_CONFLICT.getCode());
        }

        Delegation savedDelegation;
        try {
            delegation.setCreatedAt(OffsetDateTime.now());
            delegation.setUpdatedAt(OffsetDateTime.now());
            delegation.setStatus(DelegationState.ACTIVE);
            savedDelegation = delegationConnector.save(delegation);
            institutionService.updateInstitutionDelegation(delegation.getTo(), true);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode());
        }
        try {
            notificationService.sendMailForDelegation(delegation.getInstitutionFromName(), delegation.getProductId(), delegation.getTo());
        } catch (Exception e) {
            log.error(SEND_MAIL_FOR_DELEGATION_ERROR.getMessage() + ":", e.getMessage(), e);
        }
        return savedDelegation;
    }

    private void setPartnerByInstitutionTaxCode(Delegation delegation) {
        /*
            In case the api returns more institutions we always try to take the PT,
            otherwise it is okay to take the first one
         */
        List<Institution> institutions = institutionService.getInstitutions(delegation.getTo(), null);
        Institution partner = institutions.stream()
                .filter(institution -> institution.getInstitutionType() == InstitutionType.PT)
                .findFirst()
                .orElse(institutions.stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                                INSTITUTION_TAX_CODE_NOT_FOUND.getCode())
                        ));
        delegation.setTo(partner.getId());
    }

    @Override
    public Delegation createDelegationFromInstitutionsTaxCode(Delegation delegation) {

        List<Institution> institutionsTo = institutionService.getInstitutions(delegation.getTo(), delegation.getToSubunitCode());
        // TODO: remove filter when getInstitutions API will be fixed.
        /*
            If we call getInstitutions by empty subunitCode parameter, we have to filter retrieved list for institution
            with blank subunitCode field, otherwise we take first element returned by api.
        */
        String partnerIdentifier = institutionsTo.stream()
                .filter(institution -> StringUtils.hasText(delegation.getToSubunitCode()) || !StringUtils.hasText(institution.getSubunitCode()))
                .findFirst()
                .map(Institution::getId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                        INSTITUTION_TAX_CODE_NOT_FOUND.getCode()));
        delegation.setTo(partnerIdentifier);

        // TODO: remove filter when getInstitutions API will be fixed.
        /*
            If we call getInstitutions by empty subunitCode parameter, we have to filter retrieved list for institution
            with blank subunitCode field, otherwise we take first element returned by api.
        */
        List<Institution> institutionsFrom = institutionService.getInstitutions(delegation.getFrom(), delegation.getFromSubunitCode());
        String from = institutionsFrom.stream()
                .filter(institution -> StringUtils.hasText(delegation.getFromSubunitCode()) || !StringUtils.hasText(institution.getSubunitCode()))
                .findFirst()
                .map(Institution::getId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                        INSTITUTION_TAX_CODE_NOT_FOUND.getCode()));
        delegation.setFrom(from);

        if(checkIfExists(delegation)) {
            throw new ResourceConflictException(String.format(CustomError.CREATE_DELEGATION_CONFLICT.getMessage()),
                    CustomError.CREATE_DELEGATION_CONFLICT.getCode());
        }

        try {
            delegation.setCreatedAt(OffsetDateTime.now());
            delegation.setUpdatedAt(OffsetDateTime.now());
            delegation.setStatus(DelegationState.ACTIVE);
            Delegation savedDelegation = delegationConnector.save(delegation);
            institutionService.updateInstitutionDelegation(delegation.getTo(), true);
            return savedDelegation;
        } catch (Exception e) {
            throw new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode());
        }
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
    public boolean checkIfExists(Delegation delegation) {
        return delegationConnector.checkIfExists(delegation);
    }

    @Override
    public List<Delegation> getDelegations(String from, String to, String productId, String search, String taxCode, GetDelegationsMode mode,
                                           Optional<Order> order, Optional<Integer> page, Optional<Integer> size) {
        int pageSize = size.filter(s -> s > 0).filter(s -> s <= DEFAULT_DELEGATIONS_PAGE_SIZE).orElse(DEFAULT_DELEGATIONS_PAGE_SIZE);
        return delegationConnector.find(from, to, productId, search, taxCode, mode, order.orElse(Order.NONE), page.orElse(0), pageSize);
    }
}
