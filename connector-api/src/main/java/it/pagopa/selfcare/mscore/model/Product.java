package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    private ArrayList<PartyRole> roles; //ENUM
}
