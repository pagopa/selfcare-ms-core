package it.pagopa.selfcare.mscore.web.model.delegation;

import it.pagopa.selfcare.mscore.constant.DelegationType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DelegationRequestFromTaxcode {

    @NotBlank
    private String fromTaxCode;
    @NotBlank
    private String toTaxCode;
    @NotBlank
    private String institutionFromName;
    @NotBlank
    private String institutionToName;
    @NotBlank
    private String productId;
    @NotNull
    private DelegationType type;
    private String fromSubunitCode;
    private String toSubunitCode;

}
