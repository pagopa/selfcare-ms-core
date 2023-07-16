package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
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
    private RelationshipState relationshipStatus;
    private UserToNotify user;

}
