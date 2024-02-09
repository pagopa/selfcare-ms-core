package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;

import java.util.List;

public interface DelegationConnector {

    Delegation save(Delegation delegation);
    boolean checkIfExists(Delegation delegation);
    List<Delegation> find(String from, String to, String productId, GetDelegationsMode mode);

    List<Delegation> findPaginated(String from, String to, Integer page, Integer size);
}
