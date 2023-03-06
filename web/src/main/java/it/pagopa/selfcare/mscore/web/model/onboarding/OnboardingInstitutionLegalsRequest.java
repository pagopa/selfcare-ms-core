package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OnboardingInstitutionLegalsRequest {

    @NotEmpty(message = "productId is required")
    private String productId;

    private String productName;

    @NotEmpty(message = "at least one user is required")
    private List<Person> users;

    @NotEmpty(message = "Institution externalId is required")
    private String institutionExternalId;

    @NotEmpty(message = "InstitutionId is required")
    private String institutionId;

    private ContractRequest contract;

    private boolean signContract ;

}