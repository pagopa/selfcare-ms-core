package it.pagopa.selfcare.mscore.core.model;

import lombok.Data;
import wiremock.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@Data
public class Product {

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("contract")
    private String contract;

    @JsonProperty("roles")
    private ArrayList<String> roles; //ENUM
}
