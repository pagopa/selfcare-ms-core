package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

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

    void findAndUpdateInstitutionData(String id, Token token, Onboarding onboarding, RelationshipState state);
}
