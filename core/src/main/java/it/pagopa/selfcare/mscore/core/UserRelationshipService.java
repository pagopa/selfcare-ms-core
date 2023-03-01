package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipInfo;

public interface UserRelationshipService {

    OnboardedUser findByRelationshipId(String relationshipId);

    void activateRelationship(String relationshipId);

    void suspendRelationship(String relationshipId);

    void deleteRelationship(String relationshipId);

    RelationshipInfo retrieveRelationship(String relationshipId);
}
