package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;

import java.util.EnumSet;
import java.util.List;

public interface UserService {
    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);
    OnboardedUser findByUserId(String id);
    OnboardedUser createUser(UserToOnboard userToOnboard);
    User getUserFromUserRegistry(String userId, EnumSet<User.Fields> fields);

    boolean checkIfAdmin(EnvEnum env, String userId, String institutionId);

    List<OnboardedUser> retrieveAdminUsers(String externalId, String userId, EnvEnum env);

    List<OnboardedUser> retrieveUsers(String externalId, String personId, EnvEnum env, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);
}
