package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.CreateInstitutionDto;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResource;
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

    public static GeoTaxonomies toResource(GeographicTaxonomies geographicTaxonomies) {
        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();

        geoTaxonomies.setDesc(geographicTaxonomies.getDesc());
        geoTaxonomies.setCode(geographicTaxonomies.getCode());
        geoTaxonomies.setEnable(geographicTaxonomies.isEnable());
        geoTaxonomies.setRegion(geographicTaxonomies.getRegion());
        geoTaxonomies.setProvince(geographicTaxonomies.getProvince());
        geoTaxonomies.setProvinceAbbreviation(geographicTaxonomies.getProvinceAbbreviation());
        geoTaxonomies.setCountry(geographicTaxonomies.getCountry());
        geoTaxonomies.setCountryAbbreviation(geographicTaxonomies.getCountryAbbreviation());
        geoTaxonomies.setStartDate(geographicTaxonomies.getStartDate());
        geoTaxonomies.setEndDate(geographicTaxonomies.getEndDate());

        return geoTaxonomies;
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
