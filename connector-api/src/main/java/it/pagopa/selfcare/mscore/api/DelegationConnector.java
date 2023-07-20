package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.delegation.Delegation;

public interface DelegationConnector {

    Delegation save(Delegation delegation);

}
