package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingRequest {
    private String productId;
    private String productName;
    private List<OnboardedUser> users;
    private String institutionExternalId;
    private InstitutionUpdate institutionUpdate;
    private String pricingPlan;
    private Billing billingRequest;
    private Contract contract;

}
