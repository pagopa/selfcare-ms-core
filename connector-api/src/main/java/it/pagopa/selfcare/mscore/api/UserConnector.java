package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;

import java.util.List;

public interface UserConnector {

    List<OnboardedUser> findOnboardedManager(String institutionId, String productId);

}
