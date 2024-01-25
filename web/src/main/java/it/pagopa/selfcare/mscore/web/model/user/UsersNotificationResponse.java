package it.pagopa.selfcare.mscore.web.model.user;

import lombok.Data;
import java.util.List;

@Data
public class UsersNotificationResponse {

    private List<UserNotificationBindingsResponse> users;
}
