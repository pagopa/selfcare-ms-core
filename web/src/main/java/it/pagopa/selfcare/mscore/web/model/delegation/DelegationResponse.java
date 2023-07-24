package it.pagopa.selfcare.mscore.web.model.delegation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegationResponse {

    private String id;

}
