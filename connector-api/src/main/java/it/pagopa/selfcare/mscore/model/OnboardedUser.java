package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class OnboardedUser {

    @JsonProperty("user")
    private String user;

    private List<UserInstitution> institutions;

}
