package it.pagopa.selfcare.mscore.web.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserNotificationBindingsResponse {
    private List<UserNotificationResponse> bindings;

    public UserNotificationBindingsResponse(List<UserNotificationResponse> bindings) {
        this.bindings = bindings;
    }
}
