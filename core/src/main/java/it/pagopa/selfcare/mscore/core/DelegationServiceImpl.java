package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_DELEGATION_ERROR;

@Service
public class DelegationServiceImpl implements DelegationService {

    private final DelegationConnector delegationConnector;
    private final NotificationService notificationService;

    public DelegationServiceImpl(DelegationConnector delegationConnector,
                                 NotificationService notificationService) {
        this.delegationConnector = delegationConnector;
        this.notificationService = notificationService;
    }

    @Override
    public Delegation createDelegation(Delegation delegation) {
        if(checkIfExists(delegation)){
            throw new ResourceConflictException(String.format(CustomError.CREATE_DELEGATION_CONFLICT.getMessage()),
                    CustomError.CREATE_DELEGATION_CONFLICT.getCode());
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
    public boolean checkIfExists(Delegation delegation) {
        return delegationConnector.checkIfExists(delegation);
    }

    @Override
    public List<Delegation> getDelegations(String from, String to, String productId, GetDelegationsMode mode) {
        return delegationConnector.find(from, to, productId, mode);
    }
}
