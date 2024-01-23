package it.pagopa.selfcare.mscore.connector.rest.mapper;

import it.pagopa.selfcare.mscore.model.institution.AsResource;
import it.pagopa.selfcare.registry_proxy.generated.openapi.v1.dto.InsuranceCompanyResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsMapper {
    @Mapping(target = "origin", source="origin.value")
    AsResource toResource(InsuranceCompanyResource response);
}
