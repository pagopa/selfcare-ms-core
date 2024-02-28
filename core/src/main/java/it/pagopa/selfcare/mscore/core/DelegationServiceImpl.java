package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.CustomError.INSTITUTION_TAX_CODE_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_DELEGATION_ERROR;

@Service
public class DelegationServiceImpl implements DelegationService {

    private static final int DEFAULT_DELEGATIONS_PAGE_SIZE = 100;
    private static final int MAX_DELEGATIONS_PAGE_SIZE = 10000;
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
        if (checkIfExists(delegation)) {
            throw new ResourceConflictException(String.format(CustomError.CREATE_DELEGATION_CONFLICT.getMessage()),
                    CustomError.CREATE_DELEGATION_CONFLICT.getCode());
        }
        if (PROD_PAGOPA.equals(delegation.getProductId())) {
            List<Institution> institutions = institutionService.getInstitutions(delegation.getTo(), null);
            String partnerIdentifier = institutions.stream()
                    .findFirst()
                    .map(Institution::getId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                            INSTITUTION_TAX_CODE_NOT_FOUND.getCode()));
            delegation.setTo(partnerIdentifier);
        }
        try {
            Delegation savedDelegation = delegationConnector.save(delegation);
            notificationService.sendMailForDelegation(delegation.getInstitutionFromName(), delegation.getProductId(), delegation.getTo());
            return savedDelegation;
        } catch (Exception e) {
            throw new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode());
        }
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
            return delegationConnector.save(delegation);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode());
        }
    }




    @Override
    public boolean checkIfExists(Delegation delegation) {
        return delegationConnector.checkIfExists(delegation);
    }

    @Override
    public List<Delegation> getDelegations(String from, String to, String productId, GetDelegationsMode mode,
                                           Optional<Integer> page, Optional<Integer> size) {
        int pageSize = size.filter(s -> s <= MAX_DELEGATIONS_PAGE_SIZE).orElse(DEFAULT_DELEGATIONS_PAGE_SIZE);
        return delegationConnector.find(from, to, productId, mode, page.orElse(0), pageSize);
    }
}
