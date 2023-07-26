package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.delegation.Delegation;

import java.util.List;

public interface DelegationService {

    Delegation createDelegation(Delegation delegation);

    List<Delegation> getDelegations(String from, String to, String productId);
}
