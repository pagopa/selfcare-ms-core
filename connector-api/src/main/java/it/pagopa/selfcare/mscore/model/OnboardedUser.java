package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class OnboardedUser {

    @JsonProperty("user")
    private String user;

    private Map<String, Map<String,Product>> bindings;

}
