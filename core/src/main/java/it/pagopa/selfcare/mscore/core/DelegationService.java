package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;

import java.util.List;

public interface DelegationService {

    Delegation createDelegation(Delegation delegation);
    boolean checkIfExists(Delegation delegation);
    List<Delegation> getDelegations(String from, String to, String productId, GetDelegationsMode mode);
    Delegation createDelegationFromInstitutionsTaxCode(Delegation delegation);
    void deleteDelegationFromDelegationId(String delegationId);
}
