package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;

import java.util.EnumSet;
import java.util.List;

public interface UserService {
    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);
    OnboardedUser findByUserId(String id);
    List<OnboardedUser> retrieveAdminUsers(String externalId, String userId);
    List<OnboardedUser> retrieveUsers(String externalId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);
    boolean checkIfAdmin(String userId, String institutionId);
    OnboardedUser findByRelationshipId(String relationshipId);
    void verifyUser(String userId);
    void activateRelationship(String relationshipId);
    void suspendRelationship(String relationshipId);
    RelationshipInfo retrieveRelationship(String relationshipId);
    void deleteRelationship(String relationshipId);
    User getUserFromUserRegistry(String userId, EnumSet<User.Fields> fields);

}
