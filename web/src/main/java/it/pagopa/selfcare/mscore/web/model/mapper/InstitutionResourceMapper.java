package it.pagopa.selfcare.mscore.web.model.mapper;


import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstitutionResourceMapper {

    @Mapping(target = "aooParentCode", source = "paAttributes.aooParentCode")
    InstitutionResponse toInstitutionResponse(Institution institution);

}
