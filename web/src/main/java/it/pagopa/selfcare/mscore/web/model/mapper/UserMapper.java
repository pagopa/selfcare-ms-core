package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static List<UserToOnboard> toUserToOnboard(List<Person> personList) {
        List<UserToOnboard> users = new ArrayList<>();
        if (!personList.isEmpty()) {
            for (Person p : personList) {
                UserToOnboard userToOnboard = new UserToOnboard();
                userToOnboard.setId(p.getId());
                userToOnboard.setName(p.getName());
                userToOnboard.setSurname(p.getSurname());
                userToOnboard.setTaxCode(p.getTaxCode());
                userToOnboard.setEmail(p.getEmail());
                userToOnboard.setRole(p.getRole());
                userToOnboard.setProductRole(p.getProductRole());
                userToOnboard.setEnv(p.getEnv());
                users.add(userToOnboard);
            }
        }
        return users;
    }

    public static Person toPerson(OnboardedUser user) {
        return new Person(user.getId());
    }
}
