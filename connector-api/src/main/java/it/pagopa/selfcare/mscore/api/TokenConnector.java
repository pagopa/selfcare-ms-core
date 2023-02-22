package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;

import java.util.List;

public interface TokenConnector {
    Token findActiveContract(String institutionId, String userId, String productId);
    void deleteById(String id);
    Token save(Token token);
    Token findById(String tokenId);

    Token findAndUpdateTokenState(String tokenId, RelationshipState state);

    void findAndUpdateTokenUser(String id, List<String> usersId);

    List<Token> findWithFilter(String institutionId, String productId, List<RelationshipState> state);
}
