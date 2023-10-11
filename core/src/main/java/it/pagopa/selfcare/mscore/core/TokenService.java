package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;

import java.util.List;

public interface TokenService {

    Token verifyToken(String id);

    Token verifyOnboarding(String institutionId, String productId);

    TokenRelationships retrieveToken(String tokenId);

    Token getToken(String institutionId, String productId);

    List<TokenRelationships> getTokensByProductId(String productId, Integer page, Integer size);

}
