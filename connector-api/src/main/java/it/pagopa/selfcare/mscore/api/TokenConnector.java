package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TokenConnector {

    Token findActiveContract(String institutionId, String userId, String productId);

    void deleteById(String id);

    Token save(Token token, List<GeographicTaxonomies> geographicTaxonomies);

    Token findById(String tokenId);

    Token findAndUpdateToken(String tokenId, RelationshipState state, @Nullable String checksum);

    Token findWithFilter(String institutionId, String productId);
}
