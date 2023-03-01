package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;

import java.util.List;

public interface TokenService {

    Token verifyToken(String id);

    void verifyOnboarding(String institutionId, String productId, List<RelationshipState> states);

    String findActiveContract(String institutionId, String userId, String productId);
}
