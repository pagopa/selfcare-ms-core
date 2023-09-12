package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequest;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;

@Mapper(componentModel = "spring")
public abstract class DelegationMapper {

    @Autowired
    private InstitutionService institutionService;

    private static final String PROD_PAGOPA = "prod-pagopa";
    @Mapping(source = ".", target = "to", qualifiedByName = "setPartnerIdentifier")
    public abstract Delegation toDelegation(DelegationRequest delegation);

    @Mapping(source = "from", target = "institutionId")
    @Mapping(source = "to", target = "brokerId")
    @Mapping(source = "institutionFromName", target = "institutionName")
    @Mapping(source = "institutionToName", target = "brokerName")
    @Mapping(source = "institutionFromRootName", target = "institutionRootName")
    public abstract DelegationResponse toDelegationResponse(Delegation delegation);

    @Named("setPartnerIdentifier")
    String setPartnerIdentifier(DelegationRequest delegation) {
        if(PROD_PAGOPA.equals(delegation.getProductId())) {
            List<Institution> institutions = institutionService.getInstitutions(delegation.getTo(), null);
            return institutions.stream()
                    .findFirst()
                    .map(Institution::getId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_TAX_CODE_NOT_FOUND.getMessage(), delegation.getTo()),
                            INSTITUTION_TAX_CODE_NOT_FOUND.getCode()));
        }
        return delegation.getTo();
    }
}
