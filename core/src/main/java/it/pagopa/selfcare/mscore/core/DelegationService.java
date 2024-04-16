package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;

import java.util.List;
import java.util.Optional;

public interface DelegationService {

    Delegation createDelegation(Delegation delegation);
    boolean checkIfExists(Delegation delegation);
    List<Delegation> getDelegations(String from, String to, String productId, String search, String taxCode, GetDelegationsMode mode, Order order, Optional<Integer> page, Optional<Integer> size);
    Delegation createDelegationFromInstitutionsTaxCode(Delegation delegation);
    void deleteDelegationByDelegationId(String delegationId);
}
