package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InstitutionConnector {

    Institution save(Institution example);

    void deleteById(String id);

    Optional<Institution> findByExternalId(String externalId);

    List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);

    Institution findById(String id);

    void findAndUpdateStatus(String id, String tokenId, RelationshipState state);

    Institution findAndUpdate(String id, Onboarding onboarding, List<InstitutionGeographicTaxonomies> geographicTaxonomies);

    Institution findInstitutionProduct(String externalId, String productId);

    void findAndRemoveOnboarding(String institutionId, Onboarding onboarding);

    InstitutionGeographicTaxonomyPage findGeographicTaxonomies(String institutionId, Pageable pageable);

    OnboardingPage findOnboarding(String institutionId, List<RelationshipState> states, Pageable pageable);

    void findAndUpdateInstitutionData(String id, Token token, Onboarding onboarding, RelationshipState state);

    List<Institution> findByGeotaxonomies(List<String> geo, String searchMode);

    List<Institution> findByProductId(String productId);
}
