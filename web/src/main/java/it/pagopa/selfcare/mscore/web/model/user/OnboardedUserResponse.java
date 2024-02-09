package it.pagopa.selfcare.mscore.web.model.user;

import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OnboardedUserResponse {
    private String id;
    private List<UserBinding> bindings;
    private OffsetDateTime createdAt;
}
