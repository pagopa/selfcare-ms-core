package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.user.UserBinding;

import java.util.List;
public interface UserNotificationService {

    void sendActivatedUserNotification(String relationshipId, String userId, UserBinding binding);

    void sendDeletedUserNotification(String relationshipId, String userId, UserBinding binding);

    void sendSuspendedUserNotification(String relationshipId, String userId, UserBinding binding);

    void sendAddedProductRoleNotification(String userId, Institution institution, String productTitle, List<String> roleLabels);
}
