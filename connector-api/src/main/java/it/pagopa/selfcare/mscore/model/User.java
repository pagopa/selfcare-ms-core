package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class User {

    @JsonProperty("user")
    private String user;

    @JsonProperty("institutionId")
    private String institutionId;

    @JsonProperty("products")
    private List<Product> products;
}
