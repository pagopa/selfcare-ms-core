package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
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

    Institution findAndUpdate(String id, Onboarding onboarding, List<GeographicTaxonomies> geographicTaxonomies);

    Institution findInstitutionProduct(String externalId, String productId);

    void findAndRemoveOnboarding(String institutionId, Onboarding onboarding);

    GeographicTaxonomyPage findGeographicTaxonomies(String institutionId, Pageable pageable);

    OnboardingPage findOnboarding(String institutionId, List<RelationshipState> states, Pageable pageable);
}
