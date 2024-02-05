package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(
        value="core.user-event-service.type",
        havingValue = "ignore",
        matchIfMissing = true)
public class ContractEventNotificationServiceIgnore implements ContractEventNotificationService {
    @Override
    public void sendDataLakeNotification(Institution institution, Token token, QueueEvent queueEvent) {

    }

    @Override
    public NotificationToSend toNotificationToSend(Institution institution, Token token, QueueEvent queueEvent) {
        return null;
    }

    @Override
    public NotificationToSend toNotificationToSend(NotificationToSend notification, Institution institution, Token token) {
        return null;
    }
}
