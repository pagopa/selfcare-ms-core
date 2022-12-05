package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.Institution;
import it.pagopa.selfcare.mscore.web.model.CreateInstitutionDto;
import it.pagopa.selfcare.mscore.web.model.InstitutionResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class InstitutionMapper {

    public static InstitutionResource toResource(Institution institution) {
        if (institution == null) {
            return null;
        }
        InstitutionResource resource = new InstitutionResource();
        resource.setId(institution.getId());
        resource.setExternalId(institution.getExternalId());
        return resource;
    }

    public static Institution fromDto(CreateInstitutionDto dto) {
        if (dto == null) {
            return null;
        }
        Institution institution = new Institution();
        institution.setId(dto.getId());
        institution.setExternalId(dto.getExternalId());
        return institution;
    }

}
