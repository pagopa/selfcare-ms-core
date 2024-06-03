package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationWithPagination;
import it.pagopa.selfcare.mscore.model.delegation.GetDelegationParameters;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface DelegationConnector {

    Delegation save(Delegation delegation);
    boolean checkIfExistsWithStatus(Delegation delegation, DelegationState status);
    List<Delegation> find(String from, String to, String productId, String search, String taxCode, Order order, Integer page, Integer size);
    DelegationWithPagination findAndCount(GetDelegationParameters delegationParameters);
    Delegation findByIdAndModifyStatus(String delegationId, DelegationState status);
    boolean checkIfDelegationsAreActive(String institutionId);
    Delegation findAndActivate(String from, String to, String productId);
    void updateDelegation(Institution update);
}
