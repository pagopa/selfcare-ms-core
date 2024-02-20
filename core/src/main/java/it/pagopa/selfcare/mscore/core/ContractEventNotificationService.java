package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

public interface ContractEventNotificationService {
    void sendDataLakeNotification(Institution institution, Token token, QueueEvent queueEvent);

    NotificationToSend toNotificationToSend(Institution institution, Token token, QueueEvent queueEvent);

    NotificationToSend toNotificationToSend(NotificationToSend notification, Institution institution, Token token);
}
