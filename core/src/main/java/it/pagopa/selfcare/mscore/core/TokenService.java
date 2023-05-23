package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;

public interface TokenService {

    Token verifyToken(String id);

    Token verifyOnboarding(String institutionId, String productId);

    TokenRelationships retrieveToken(String tokenId);

    Token getToken(String institutionId, String productId);

}
