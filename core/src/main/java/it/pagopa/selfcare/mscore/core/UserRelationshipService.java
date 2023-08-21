package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;

public interface UserRelationshipService {

    OnboardedUser findByRelationshipId(String relationshipId);

    void activateRelationship(String relationshipId, String loggedUserName, String loggedUserSurname);

    void suspendRelationship(String relationshipId, String loggedUserName, String loggedUserSurname);

    void deleteRelationship(String relationshipId, String loggedUserName, String loggedUserSurname);

    RelationshipInfo retrieveRelationship(String relationshipId);

}
