package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.Data;
import org.codehaus.commons.nullanalysis.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
