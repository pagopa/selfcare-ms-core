package it.pagopa.selfcare.mscore.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserNotificationToSend {

    private String id;
    private String institutionId;
    private String productId;
    private String onboardingTokenId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private QueueEvent eventType;
    private UserToNotify user;

}
