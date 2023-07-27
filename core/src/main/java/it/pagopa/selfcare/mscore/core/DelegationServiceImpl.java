package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_DELEGATION_ERROR;

@Service
public class DelegationServiceImpl implements DelegationService {

    private final DelegationConnector delegationConnector;

    public DelegationServiceImpl(DelegationConnector delegationConnector) {
        this.delegationConnector = delegationConnector;
    }

    @Override
    public Delegation createDelegation(Delegation delegation) {
        if(checkIfExists(delegation)){
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
    public List<Delegation> getDelegations(String from, String to, String productId) {
        return delegationConnector.find(from, to, productId);
    }
}
