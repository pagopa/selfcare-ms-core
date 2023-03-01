package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

import java.util.List;

public interface TokenService {

    Token verifyToken(String id);

    void verifyOnboarding(String institutionId, String productId, List<RelationshipState> states);

    String findActiveContract(String institutionId, String userId, String productId);
    TokenRelationships getToken(String tokenId);
}
