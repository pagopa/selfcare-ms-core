package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;

public interface UserRelationshipService {

    OnboardedUser findByRelationshipId(String relationshipId);

    RelationshipInfo retrieveRelationship(String relationshipId);

}
