package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
