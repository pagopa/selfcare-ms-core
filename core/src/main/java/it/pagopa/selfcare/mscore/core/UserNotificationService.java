package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.user.UserBinding;

import java.util.List;
public interface UserNotificationService {

    void sendCreateUserNotification(String description, String productTitle, String email, List<String> roleLabels, String loggedUserName, String loggedUserSurname);

    void sendActivatedUserNotification(String relationshipId, String userId, UserBinding binding, String loggedUserName, String loggedUserSurname);

    void sendDeletedUserNotification(String relationshipId, String userId, UserBinding binding, String loggedUserName, String loggedUserSurname);

    void sendSuspendedUserNotification(String relationshipId, String userId, UserBinding binding, String loggedUserName, String loggedUserSurname);

    void sendAddedProductRoleNotification(String userId, Institution institution, String productTitle, List<String> roleLabels, String loggedUserName, String loggedUserSurname);
}
