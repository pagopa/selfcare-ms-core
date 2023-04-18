package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OnboardingInstitutionOperatorsRequest {

    @NotEmpty(message = "productId is required")
    private String productId;

    @NotEmpty(message = "at least one user is required")
    private List<Person> users;

    @NotEmpty(message = "InstitutionId is required")
    private String institutionId;

}
