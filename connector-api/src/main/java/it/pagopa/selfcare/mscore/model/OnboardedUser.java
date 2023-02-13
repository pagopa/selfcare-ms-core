package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;


@Data
@NoArgsConstructor
public class OnboardedUser {

    private String id;

    @JsonProperty("user")
    private String user;

    private Map<String, Map<String, OnboardedProduct>> bindings;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public OnboardedUser(OnboardedUser user) {
        this.id = user.getId();
        this.user = user.getUser();
        this.bindings = user.getBindings();
        this.createdAt = user.getCreatedAt();
    }
}
