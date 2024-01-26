package it.pagopa.selfcare.mscore.web.model.user;

import lombok.Data;

import java.util.List;

@Data
public class UserNotificationBindingsResponse {
    private List<UserNotificationResponse> bindings;

    public UserNotificationBindingsResponse(List<UserNotificationResponse> bindings) {
        this.bindings = bindings;
    }
}
