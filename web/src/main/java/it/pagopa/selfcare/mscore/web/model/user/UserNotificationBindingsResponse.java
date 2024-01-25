package it.pagopa.selfcare.mscore.web.model.user;

import lombok.Data;

import java.util.List;

@Data
public class UserNotificationBindingsResponse {

    private String id;
    private List<UserNotificationResponse> bindings;
}
