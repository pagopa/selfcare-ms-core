package it.pagopa.selfcare.mscore.web.model.mapper;


import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RootParentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring")
public interface InstitutionResourceMapper {

    @Mapping(target = "aooParentCode", source = "paAttributes.aooParentCode")
    @Mapping(target = "rootParent", source = ".", qualifiedByName = "setRootParent")
    InstitutionResponse toInstitutionResponse(Institution institution);

    @Named("setRootParent")
    static RootParentResponse setRootParent(Institution institution) {
        if(StringUtils.hasText(institution.getRootParentId())){
            RootParentResponse rootParentResponse = new RootParentResponse();
            rootParentResponse.setId(institution.getRootParentId());
            rootParentResponse.setDescription(institution.getParentDescription());
            return rootParentResponse;
        }
        return null;
    }


    Billing billingRequestToBilling(BillingRequest billingRequest);

    InstitutionGeographicTaxonomies toInstitutionGeographicTaxonomies(GeoTaxonomies geoTaxonomies);


}
