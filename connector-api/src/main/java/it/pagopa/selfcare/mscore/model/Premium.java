package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Premium {

    @JsonProperty("status")
    private RelationshipState status;

    @JsonProperty("contract")
    private String contract;

}
