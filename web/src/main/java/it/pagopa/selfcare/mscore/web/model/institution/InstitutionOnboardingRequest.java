package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class InstitutionOnboardingRequest {

    @NotEmpty(message = "productId is required")
    private String productId;

    @NotEmpty(message = "at least one user is required")
    private List<Person> users;

    private String pricingPlan;
    private BillingRequest billing;

}
