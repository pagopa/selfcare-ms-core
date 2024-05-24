package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequest;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequestFromTaxcode;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DelegationMapper {

    Delegation toDelegation(DelegationRequest delegation);

    Delegation toDelegation(DelegationRequestFromTaxcode delegation);

    @Mapping(source = "from", target = "institutionId")
    @Mapping(source = "to", target = "brokerId")
    @Mapping(source = "institutionFromName", target = "institutionName")
    @Mapping(source = "institutionToName", target = "brokerName")
    @Mapping(source = "institutionFromRootName", target = "institutionRootName")
    DelegationResponse toDelegationResponse(Delegation delegation);

}