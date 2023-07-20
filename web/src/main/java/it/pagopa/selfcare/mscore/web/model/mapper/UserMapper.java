package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static List<UserToOnboard> toUserToOnboard(List<Person> persons) {
        List<UserToOnboard> users = new ArrayList<>();
        for (Person p : persons) {
            users.add(toUserToOnboard(p));
        }
        return users;
    }

    public static UserToOnboard toUserToOnboard(Person p) {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setId(p.getId());
        userToOnboard.setName(p.getName());
        userToOnboard.setSurname(p.getSurname());
        userToOnboard.setTaxCode(p.getTaxCode());
        userToOnboard.setEmail(p.getEmail());
        userToOnboard.setRole(p.getRole());
        userToOnboard.setProductRole(p.getProductRole());
        userToOnboard.setRoleLabel(p.getRoleLabel());
        userToOnboard.setEnv(p.getEnv());
        return userToOnboard;
    }
}
