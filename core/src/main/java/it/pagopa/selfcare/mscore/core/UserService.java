package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.UserToOnboard;

import java.util.List;

public interface UserService {
    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);
    OnboardedUser findByUserId(String id);
    List<OnboardedUser> retrieveAdminUsers(String externalId, String userId, EnvEnum env);
    List<OnboardedUser> retrieveUsers(String externalId, String personId, EnvEnum env, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);
    boolean checkIfAdmin(EnvEnum env, String userId, String institutionId);
    OnboardedUser createUser(UserToOnboard fromDto);
    void verifyUser(String userId);
}
