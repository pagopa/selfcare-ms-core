package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static Person toResource(OnboardedUser user) {
        Person person = new Person();
        person.setId(user.getUser());
        return person;
    }

    public static OnboardedUser fromDto(Person dto) {
        OnboardedUser user = new OnboardedUser();
        user.setUser(dto.getId());
        return user;
    }
}
