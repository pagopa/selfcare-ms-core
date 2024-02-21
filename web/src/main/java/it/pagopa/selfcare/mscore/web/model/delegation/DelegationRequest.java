package it.pagopa.selfcare.mscore.web.model.delegation;

import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class DelegationRequest {

    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    private String institutionFromName;
    @NotBlank
    private String institutionToName;
    @NotBlank
    private String productId;
    @NotNull
    private DelegationType type;

}
