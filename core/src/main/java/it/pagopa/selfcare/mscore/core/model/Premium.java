package it.pagopa.selfcare.mscore.core.model;

import lombok.Data;
import wiremock.com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Premium {

    @JsonProperty("status")
    private String status;

    @JsonProperty("contract")
    private String contract;

}
