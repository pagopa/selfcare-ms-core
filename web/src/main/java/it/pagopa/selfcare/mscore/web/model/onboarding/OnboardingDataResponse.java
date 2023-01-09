package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.web.model.institution.Attributes;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingDataResponse {

    private String id;
    private String externalId;
    private String origin;
    private String originId;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private String pricingPlan;
    private Billing billing;
    private List<GeoTaxonomies> geographicTaxonomies;
    private List<Attributes> attributes;
    private String state;
    private String role;
    private ProductInfo productInfo;

}
