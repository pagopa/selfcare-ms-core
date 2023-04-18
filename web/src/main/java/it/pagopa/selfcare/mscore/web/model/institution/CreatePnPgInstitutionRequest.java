package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreatePnPgInstitutionRequest {

    @NotEmpty(message = "taxId is required")
    private String taxId;

    private String description;

}
