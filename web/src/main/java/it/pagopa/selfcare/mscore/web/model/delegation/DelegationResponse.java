package it.pagopa.selfcare.mscore.web.model.delegation;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegationResponse {

    @NotBlank
    private String id;
    @NotBlank
    private String institutionId;
    @NotBlank
    private String institutionName;
    private String institutionRootName;
    @NotBlank
    private DelegationType type;
    @NotBlank
    private String productId;
    private String taxCode;
    private InstitutionType institutionType;
    @NotBlank
    private String brokerId;
    private String brokerTaxCode;
    private String brokerType;
    private String brokerName;

    private String test;

}
