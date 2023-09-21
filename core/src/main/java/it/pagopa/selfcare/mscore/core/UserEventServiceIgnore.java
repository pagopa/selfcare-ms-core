package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(
        value="core.user-event-service.type",
        havingValue = "ignore",
        matchIfMissing = true)
public class UserEventServiceIgnore implements UserEventService{
    @Override
    public void sendLegalTokenUserNotification(Token token) {

    }

    @Override
    public void sendOperatorUserNotification(RelationshipInfo relationshipInfo, QueueEvent eventType) {

    }

    @Override
    public void sendUpdateUserNotification(String userId, String institutionId) {

    }
}
