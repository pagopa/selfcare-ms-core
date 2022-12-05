package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.ArrayList;

@Data
public class Institution {

    @JsonProperty("id")
    private String id;

    @JsonProperty("external-id")
    private String externalId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("ipaCode")
    private String ipaCode;

    @JsonProperty("istitutionType")
    private InstitutionType istitutionType;

    @JsonProperty("digitalAddress")
    private String digitalAddress;

    @JsonProperty("address")
    private String address;

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("taxCode")
    private String taxCode;

    @JsonProperty("billing")
    private Billing billing;

    @JsonProperty("onboarding")
    private ArrayList<Onboarding> onboarding;
}
