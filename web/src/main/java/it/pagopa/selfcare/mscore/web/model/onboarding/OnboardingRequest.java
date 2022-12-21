package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.institution.Billing;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdate;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class OnboardingRequest {

    private String institutionExternalId;
    private String productId;
    private String productName;
    private List<OnboardedUser> users;
    private String contractPath;
    private String contractVersion;
    private Billing billing;
    private InstitutionUpdate institutionUpdate;
    private InstitutionType institutionType;
    private String origin;
    private String pricingPlan;

    public List<OnboardedUser> getUsers() {
        return Optional.ofNullable(users).orElse(Collections.emptyList());
    }

}
