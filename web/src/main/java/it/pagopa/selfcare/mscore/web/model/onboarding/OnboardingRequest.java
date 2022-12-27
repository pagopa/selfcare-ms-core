package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateRequest;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class OnboardingRequest {

    private String institutionExternalId;
    private String productId;
    private String productName;
    private List<OnboardedUserRequest> users;
    private ContractRequest contract;
    private BillingRequest billingRequest;
    private InstitutionUpdateRequest institutionUpdateRequest;
    private String pricingPlan;

    public List<OnboardedUserRequest> getUsers() {
        return Optional.ofNullable(users).orElse(Collections.emptyList());
    }

}
