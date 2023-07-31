package it.pagopa.selfcare.mscore.api;


import it.pagopa.selfcare.mscore.model.notification.MessageRequest;

public interface NotificationServiceConnector {
    void sendNotificationToUser(MessageRequest messageRequest);

}
