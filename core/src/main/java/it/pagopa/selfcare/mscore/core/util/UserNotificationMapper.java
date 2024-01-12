package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface UserNotificationMapper {

    @Mapping(source = "onboardedProduct.role", target = "role")
    @Mapping(source = "onboardedProduct.status", target = "relationshipStatus")
    @Mapping(source = "onboardedProduct.productRole", target = "productRole")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "email", expression = "java(user.getWorkContacts().containsKey(institutionId) ? user.getWorkContacts().get(institutionId).getEmail() : user.getEmail())")
    UserToNotify toUserNotify(User user, OnboardedProduct onboardedProduct, String institutionId);

}
