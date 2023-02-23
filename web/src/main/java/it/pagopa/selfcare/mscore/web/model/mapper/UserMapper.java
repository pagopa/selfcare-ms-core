package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static Person toResource(OnboardedUser user) {
        Person person = new Person();
        person.setId(user.getId());
        return person;
    }

    public static UserToOnboard fromDto(Person dto) {
        UserToOnboard user = new UserToOnboard();
        user.setId(dto.getId());
        return user;
    }
}
