package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.onboarding.Token;

public interface TokenService {

    Token verifyToken(String id);

    Token verifyOnboarding(String institutionId, String productId);

}
