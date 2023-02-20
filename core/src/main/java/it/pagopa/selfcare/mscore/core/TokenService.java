package it.pagopa.selfcare.mscore.core;

public interface TokenService {
    String findActiveContract(String institutionId, String userId, String productId);

}
