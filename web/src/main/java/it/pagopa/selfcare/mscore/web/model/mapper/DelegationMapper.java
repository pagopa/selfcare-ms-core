package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequest;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DelegationMapper {

    Delegation toDelegation(DelegationRequest delegation);
    DelegationResponse toDelegationResponse(Delegation delegation);

}