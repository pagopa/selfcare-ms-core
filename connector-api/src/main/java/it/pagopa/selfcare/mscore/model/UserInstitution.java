package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserInstitution {
    @JsonProperty("institutionId")
    private String institutionId;

    @JsonProperty("products")
    private List<Product> products;
}
