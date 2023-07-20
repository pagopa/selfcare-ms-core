package it.pagopa.selfcare.mscore.web.model.delegation;

import it.pagopa.selfcare.mscore.constant.DelegationType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelegationRequest {

    @NotNull
    private String from;
    @NotNull
    private String to;
    @NotNull
    private String institutionFromName;
    @NotNull
    private String productId;
    @NotNull
    private DelegationType type;

}
