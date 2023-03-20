package it.pagopa.selfcare.mscore.web.model.onboarding;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ContractRequest {
    private String version;

    @NotEmpty(message = "contract path is required")
    private String path;
}
