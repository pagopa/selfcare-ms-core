package it.pagopa.selfcare.mscore.core.model;

import lombok.Data;
import wiremock.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@Data
public class User {

    @JsonProperty("user")
    private String user;

    @JsonProperty("institutionId")
    private String institutionId;

    @JsonProperty("products")
    private ArrayList<Product> products;
}
