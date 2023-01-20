package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingInstitutionRequest {

    private String productId;
    private String productName;
    private List<Person> users;
    private String institutionExternalId;
    private InstitutionUpdate institutionUpdate;
    private String pricingPlan;
    private BillingRequest billingRequest;
    private ContractRequest contract;

}
