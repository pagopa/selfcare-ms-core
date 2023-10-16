package it.pagopa.selfcare.mscore.connector.rest.mapper;

import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.user_registry.generated.openapi.v1.dto.UserId;
import it.pagopa.selfcare.user_registry.generated.openapi.v1.dto.UserResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;




@Mapper(componentModel = "spring")
public interface UserMapperClient {

    User toUser (UserResource userResource);

    @Mapping(target = "id", source = "userId.id")
    User fromUserId (UserId userId);

}
