package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.Token;

public interface TokenService {
    String findActiveContract(String institutionId, String userId, String productId);
    Token retrieveToken(String institutionId, String productId);
}
