package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface InstitutionConnector {

    Institution save(Institution example);

    List<Institution> findAll();

    void deleteById(String id);

    Optional<Institution> findByExternalId(String externalId);

    List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);

    Institution findById(String id);

    Institution findAndUpdateStatus(String id, String tokenId, RelationshipState state);

    Institution findAndUpdate(String id, Onboarding onboarding, List<InstitutionGeographicTaxonomies> geographicTaxonomies, InstitutionUpdate institutionUpdate);

    Institution findInstitutionProduct(String externalId, String productId);

    void findAndRemoveOnboarding(String institutionId, Onboarding onboarding);

    Institution findAndUpdateInstitutionData(String id, Token token, Onboarding onboarding, RelationshipState state);

    List<Institution> findByGeotaxonomies(List<String> geo, SearchMode searchMode);

    List<Institution> findByProductId(String productId);

    List<Institution> findAllByIds(List<String> ids);

    Institution saveOrRetrievePnPg(Institution newInstitution);

    List<String> findByExternalIdAndProductId(List<ValidInstitution> externalIds, String productId);

    Institution updateOnboardedProductCreatedAt(String institutionId, String productId, OffsetDateTime createdAt);
}
