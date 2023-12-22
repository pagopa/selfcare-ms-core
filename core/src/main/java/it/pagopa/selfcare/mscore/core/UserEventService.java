package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;

public interface UserEventService {

    void sendLegalTokenUserNotification(Token token);

    void sendOperatorUserNotification(RelationshipInfo relationshipInfo, QueueEvent eventType);
    void sendUpdateUserNotificationToQueue(String userId, String institutionId);

    void sendOnboardedUserNotification(OnboardedUser onboardedUser, String productId);

}
