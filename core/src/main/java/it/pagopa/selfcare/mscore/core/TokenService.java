package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.Token;

public interface TokenService {
    Token verifyToken(String id);
    Token getToken(String id);
}
