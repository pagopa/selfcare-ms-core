package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PdaInstitutionRequest {

    @NotEmpty(message = "InjestionInstitutionType is required")
    private String injestionInstitutionType;

    @NotEmpty(message = "TaxCode is required")
    private String taxCode;

    private String description;
    private BillingRequest billing;
}
