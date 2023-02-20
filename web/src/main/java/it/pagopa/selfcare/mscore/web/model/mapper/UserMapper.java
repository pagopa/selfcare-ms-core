package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import it.pagopa.selfcare.mscore.web.model.user.PersonResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.USER_NOT_FOUND_ERROR;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static PersonResponse toPersonResponse(OnboardedUser user, String userId){
        if(user!=null) {
            PersonResponse personResponse = new PersonResponse();
            personResponse.setUserId(user.getId());
            return personResponse;
        }
        throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(),userId), USER_NOT_FOUND_ERROR.getCode());
    }

    public static Person toResource(OnboardedUser user) {
        Person person = new Person();
        person.setId(user.getId());
        return person;
    }

    public static UserToOnboard fromDto(Person dto) {
        UserToOnboard user = new UserToOnboard();
        user.setId(dto.getId());
        user.setEnv(dto.getEnv());
        user.setRole(dto.getRole());
        user.setProductRole(dto.getProductRole());
        return user;
    }
}
