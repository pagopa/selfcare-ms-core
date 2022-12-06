package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Product {

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("status")
    private RelationshipState status;

    @JsonProperty("contract")
    private String contract;

    @JsonProperty("roles")
    private List<PartyRole> roles;
}
