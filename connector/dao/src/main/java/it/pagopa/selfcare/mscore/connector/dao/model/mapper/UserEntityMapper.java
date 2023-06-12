package it.pagopa.selfcare.mscore.connector.dao.model.mapper;


import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface UserEntityMapper {

    OnboardedUser toOnboardedUser(UserEntity entity);


    @Mapping(target = "id", defaultExpression = "java(UUID.randomUUID().toString())")
    UserEntity toUserEntity(OnboardedUser user);
}
