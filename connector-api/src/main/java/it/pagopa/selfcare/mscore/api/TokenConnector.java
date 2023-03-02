package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

import java.util.List;

public interface TokenConnector {
    Token findActiveContract(String institutionId, String userId, String productId);
    void deleteById(String id);
    Token save(Token token);
    Token findById(String tokenId);
    Token findAndUpdateToken(String tokenId, RelationshipState state, String checksum);
    List<Token> findWithFilter(String institutionId, String productId, List<RelationshipState> state);
}
