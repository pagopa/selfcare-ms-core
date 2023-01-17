package it.pagopa.selfcare.mscore.model.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Billing {

    @JsonProperty("vatNumber")
    private String vatNumber;

    @JsonProperty("recipientCode")
    private String recipientCode;

    @JsonProperty("publicServer")
    private boolean publicServices;
}
