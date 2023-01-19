package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;


@Data
public class OnboardedUser {

    private String id;

    @JsonProperty("user")
    private String user;

    private PartyRole role;

    private Map<String, Map<String,Product>> bindings;
    private OffsetDateTime createdAt;

}
