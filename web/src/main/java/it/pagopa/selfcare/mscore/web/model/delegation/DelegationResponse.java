package it.pagopa.selfcare.mscore.web.model.delegation;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegationResponse {

    private String id;
    private String institutionId;
    private String institutionName;
    private String institutionRootName;
    private DelegationType type;
    private String productId;
    private String taxCode;
    private InstitutionType institutionType;
    private String brokerId;
    private String brokerTaxCode;
    private String brokerType;
    private String brokerName;

}
