package it.pagopa.selfcare.mscore.core;
import it.pagopa.selfcare.mscore.model.Token;


public interface TokenService {
    Token verifyToken(String id);
    Token getToken(String id);
    Token retrieveToken(String institutionId, String productId);

    String findActiveContract(String institutionId, String userId, String productId);
}
