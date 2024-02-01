package it.pagopa.selfcare.mscore.web.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersNotificationResponse {

    private List<UserNotificationBindingsResponse> users;
}
