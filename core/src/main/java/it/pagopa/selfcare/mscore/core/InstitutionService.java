package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;

import java.time.OffsetDateTime;
import java.util.List;

public interface InstitutionService {

    Institution retrieveInstitutionById(String id);

    Institution retrieveInstitutionByExternalId(String institutionExternalId);

    Institution createInstitutionByExternalId(String externalId);

    Institution createInstitutionRaw(Institution institution, String externalId);

    Institution createPgInstitution(String taxId, String description, boolean existsInRegistry, SelfCareUser selfCareUser);

    List<Onboarding> retrieveInstitutionProducts(Institution institution, List<RelationshipState> states);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<RelationshipInfo> retrieveUserRelationships(String userId, String institutionId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    List<RelationshipInfo> retrieveUserInstitutionRelationships(Institution institution,
                                                           String userId,
                                                           String personId,
                                                           List<PartyRole> roles,
                                                           List<RelationshipState> states,
                                                           List<String> products,
                                                           List<String> productRoles);

    void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomies(Institution institution);

    GeographicTaxonomies retrieveGeoTaxonomies(String code);

    List<Institution> findInstitutionsByGeoTaxonomies(String geoTaxonomies, SearchMode searchMode);

    List<Institution> findInstitutionsByProductId(String productId);

    List<Institution> retrieveInstitutionByIds(List<String> ids);

    Institution createPnPgInstitution(String taxId, String description);

    Institution updateInstitution(String institutionId, InstitutionUpdate institutionUpdate, String userId);

    List<ValidInstitution> retrieveInstitutionByExternalIds(List<ValidInstitution> validInstitutionList, String productId);

    void updateCreatedAt(String institutionId, String productId, OffsetDateTime createdAt);
}
