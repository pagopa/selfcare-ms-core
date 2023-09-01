package it.pagopa.selfcare.mscore.web.model.delegation;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegationResponse {

    private String id;
    private String institutionId;
    private String institutionName;
    private String brokerName;
    private String institutionRootName;
    private DelegationType type;
    private String brokerId;
    private String productId;
    private String taxCode;
    private InstitutionType institutionType;

}
