package it.pagopa.selfcare.mscore.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OnboardedUser {
    private String id;
    private List<UserBinding> bindings;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public OnboardedUser(OnboardedUser user) {
        this.id = user.getId();
        this.bindings = user.getBindings();
        this.createdAt = user.getCreatedAt();
    }
}
