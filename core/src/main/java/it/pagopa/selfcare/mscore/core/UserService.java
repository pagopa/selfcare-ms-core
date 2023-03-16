package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import java.util.List;

public interface UserService {

    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);

    OnboardedUser findByUserId(String id);

    List<OnboardedUser> retrieveUsers(String externalId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    boolean checkIfAdmin(String userId, String institutionId);

    void verifyUser(String userId);
}
