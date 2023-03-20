package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TokenConnector {

    List<Token> findAll();

    Token save(Token token);

    Token findActiveContract(String institutionId, String userId, String productId);

    void deleteById(String id);

    Token save(Token token, List<InstitutionGeographicTaxonomies> geographicTaxonomies);

    Token findById(String tokenId);

    Token findAndUpdateToken(Token token, RelationshipState state, @Nullable String checksum);

    Token findWithFilter(String institutionId, String productId);
}
