package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingData {

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
    private List<GeographicTaxonomies> geographicTaxonomies;
    private List<Attributes> attributes;
}
