package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateRequest;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OnboardingInstitutionRequest {

    @NotEmpty(message = "productId is required")
    private String productId;

    private String productName;

    @NotEmpty(message = "at least one user is required")
    private List<Person> users;

    @NotEmpty(message = "Institution externalId is required")
    private String institutionExternalId;

    @NotNull(message = "institutionData is required")
    @Valid
    private InstitutionUpdateRequest institutionUpdate;

    private String pricingPlan;
    private BillingRequest billing;
    private ContractRequest contract;
    private OnboardingImportContract contractImported;
    private Boolean signContract;

}
