package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "relationshipInfo.institution.id", target = "institutionId")
    @Mapping(source = "relationshipInfo.onboardedProduct.productId", target = "productId")
    @Mapping(source = "relationshipInfo.onboardedProduct.createdAt", target = "createdAt")
    @Mapping(source = "relationshipInfo.onboardedProduct.updatedAt", target = "updatedAt")
    UserNotificationToSend setNotificationDetailsFromRelationship(RelationshipInfo relationshipInfo, UserToNotify user);

    @Mapping(source = "token.institutionId", target = "institutionId")
    @Mapping(source = "token.productId", target = "productId")
    @Mapping(source  = "token.id", target = "onboardingTokenId")
    @Mapping(source  = "token.createdAt", target = "createdAt")
    @Mapping(source  = "token.updatedAt", target = "updatedAt")
    UserNotificationToSend setNotificationDetailsFromToken(Token token, UserToNotify user);
}
