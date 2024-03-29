package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;

public interface TokenConnector {

    Token findById(String tokenId);
    Token findWithFilter(String institutionId, String productId);

    Token updateTokenCreatedAt(String tokenId, OffsetDateTime createdAt, OffsetDateTime activatedAt);

    List<Token> findByStatusAndProductId(EnumSet<RelationshipState> statuses, String productId, Integer nextPage, Integer size);
}
