package it.pagopa.selfcare.mscore.web.model.user;

import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * This objects wrap user's info sent on topic sc-users
 */
@Data
public class UserNotificationResponse {

    private String id;
    private String institutionId;
    private String productId;
    private String onboardingTokenId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private QueueEvent eventType;
    private UserToNotify user;

}
