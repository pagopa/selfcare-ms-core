package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreatePgInstitutionRequest {

    @NotEmpty(message = "taxId is required")
    private String taxId;

    private String description;

    @NotNull
    private boolean existsInRegistry;

}
