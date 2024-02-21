package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;

import java.util.List;

public interface DelegationConnector {

    Delegation save(Delegation delegation);
    boolean checkIfExists(Delegation delegation);
    List<Delegation> find(String from, String to, String productId, GetDelegationsMode mode);
    Delegation findByIdAndModifyStatus(String delegationId, DelegationState status);
    boolean checkIfDelegationsAreActive(String institutionId);

}
