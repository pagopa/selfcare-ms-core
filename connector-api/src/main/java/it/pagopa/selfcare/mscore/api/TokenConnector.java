package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.Token;

import java.util.List;
import java.util.Optional;

public interface TokenConnector {
    List<Token> findActiveContract(String institutionId, String userId, String productId);
    void deleteById(String id);
    Token save(Token token);
    Optional<Token> findById(String tokenId);
}
