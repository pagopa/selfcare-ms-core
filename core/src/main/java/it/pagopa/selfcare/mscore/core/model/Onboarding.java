package it.pagopa.selfcare.mscore.core.model;

import lombok.Data;
import wiremock.com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Onboarding {

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("contract")
    private String contract;

    @JsonProperty("pricingPlan")
    private String pricingPlan;

    @JsonProperty("premium")
    private Premium premium;
}
