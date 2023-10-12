package it.pagopa.selfcare.mscore.connector.rest.mapper;

import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.user_registry.generated.openapi.v1.dto.UserResource;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapperClient {



    User toUser (UserResource userResource);


}
