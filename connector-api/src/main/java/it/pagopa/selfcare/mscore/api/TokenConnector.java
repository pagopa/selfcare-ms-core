package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.Token;

import java.util.List;

public interface TokenConnector {
    List<Token> findActiveContract(String institutionId, String userId, String productId);
}
