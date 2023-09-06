package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationInstitution;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DelegationInstitutionMapper {

    @Mapping(source = "institutions", target = "taxCode", qualifiedByName = "setTaxCode")
    @Mapping(source = "institutions", target = "institutionType", qualifiedByName = "setInstitutionType")
    Delegation convertToDelegationInstitution(DelegationInstitution delegation);

    @Mapping(source = "institutions", target = "brokerTaxCode", qualifiedByName = "setTaxCode")
    @Mapping(source = "institutions", target = "brokerType", qualifiedByName = "setInstitutionType")
    Delegation convertToDelegationBroker(DelegationInstitution delegation);

    @Named("setTaxCode")
    default String setTaxCode(List<Institution> institutionList) {
        return !institutionList.isEmpty() ? institutionList.get(0).getTaxCode() : null;
    }

    @Named("setInstitutionType")
    default InstitutionType setInstitutionType(List<Institution> institutionList) {
        return !institutionList.isEmpty()? institutionList.get(0).getInstitutionType() : null;
    }

}
