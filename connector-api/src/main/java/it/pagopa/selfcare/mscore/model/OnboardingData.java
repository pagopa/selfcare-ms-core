package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import lombok.Data;
import org.codehaus.commons.nullanalysis.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class OnboardingData {

    @NotNull
    private String institutionExternalId;
    private String productId;
    private String productName;
    private List<OnboardedUser> users;
    private String contractPath;
    private Billing billing;
    private InstitutionUpdate institutionUpdate;
    private String pricingPlan;

    public List<OnboardedUser> getUsers() {
        return Optional.ofNullable(users).orElse(Collections.emptyList());
    }
}
