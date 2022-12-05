package it.pagopa.selfcare.mscore.core.model;

import lombok.Data;
import wiremock.com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Billing {

    @JsonProperty("vatNumber")
    private String vatNumber;

    @JsonProperty("recipientCode")
    private String recipientCode;

    @JsonProperty("publicServer")
    private String publicServer;
}
