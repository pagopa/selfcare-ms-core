package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface NotificationMapper {

    @Mapping(source = "relationshipInfo.institution.id", target = "institutionId")
    @Mapping(source = "relationshipInfo.onboardedProduct.productId", target = "productId")
    @Mapping(source = "relationshipInfo.onboardedProduct.createdAt", target = "createdAt")
    @Mapping(target = "updatedAt", expression = "java((null == relationshipInfo.getOnboardedProduct().getUpdatedAt()) ? relationshipInfo.getOnboardedProduct().getCreatedAt() : relationshipInfo.getOnboardedProduct().getUpdatedAt())")
    UserNotificationToSend setNotificationDetailsFromRelationship(RelationshipInfo relationshipInfo, UserToNotify user, QueueEvent eventType);

    @Mapping(source = "token.institutionId", target = "institutionId")
    @Mapping(source = "token.productId", target = "productId")
    @Mapping(source  = "token.id", target = "onboardingTokenId")
    @Mapping(source  = "token.createdAt", target = "createdAt")
    @Mapping(source  = "token.updatedAt", target = "updatedAt")
    UserNotificationToSend setNotificationDetailsFromToken(Token token, UserToNotify user, QueueEvent eventType);

    @Mapping(source = "onboardedProduct.createdAt", target = "createdAt")
    @Mapping(target = "updatedAt", expression = "java((null == onboardedProduct.getUpdatedAt()) ? onboardedProduct.getCreatedAt() : onboardedProduct.getUpdatedAt())")
    @Mapping(source = "onboardedProduct.tokenId", target = "onboardingTokenId")
    @Mapping(source = "onboardedProduct.productId", target = "productId")
    UserNotificationToSend setNotificationDetailsFromOnboardedProduct(UserToNotify user, OnboardedProduct onboardedProduct, String institutionId);

    UserNotificationToSend setUpdateUserNotification(String institutionId, UserToNotify user);
}
