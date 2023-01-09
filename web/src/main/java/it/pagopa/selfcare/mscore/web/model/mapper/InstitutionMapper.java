package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.*;
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

    public static InstitutionBillingResponse toResponse(Institution institution) {
        if (institution == null) {
            return null;
        }
        InstitutionBillingResponse response = new InstitutionBillingResponse();

        response.setInstitutionId(institution.getId());
        response.setExternalId(institution.getExternalId());
        response.setOrigin(institution.getOrigin());
        response.setOriginId(institution.getOriginId());
        response.setDescription(institution.getDescription());
        response.setInstitutionType(institution.getInstitutionType());
        response.setDigitalAddress(institution.getDigitalAddress());
        response.setAddress(institution.getAddress());
        response.setZipCode(institution.getZipCode());
        response.setTaxCode(institution.getTaxCode());
        response.setPricingPlan(institution.getPricingPlan());
        response.setBilling(institution.getBilling());

        return response;
    }

    public static InstitutionProduct toResource(Onboarding onboarding) {
        InstitutionProduct institutionProduct = new InstitutionProduct();
        institutionProduct.setState(onboarding.getStatus().name());
        institutionProduct.setId(onboarding.getProductId());
        return institutionProduct;
    }

       /* public static GeoTaxonomies toResource(GeographicTaxonomies geographicTaxonomies) {
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
    }*/
}
