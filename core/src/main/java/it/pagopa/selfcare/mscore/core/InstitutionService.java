package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserInfo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface InstitutionService {

    List<Onboarding> getOnboardingInstitutionByProductId(String institutionId, String productId);

    Institution retrieveInstitutionById(String id);

    Institution retrieveInstitutionByExternalId(String institutionExternalId);

    List<Institution> getInstitutions(String taxCode, String subunitCode, String origin, String originId);

    List<Institution> getInstitutions(String taxCode, String subunitCode);

    Institution createInstitutionFromIpa(String taxCode, InstitutionPaSubunitType subunitType, String subunitCode, List<InstitutionGeographicTaxonomies> geographicTaxonomies, InstitutionType institutionType);

    Institution createInstitutionFromPda(Institution institution, String injectionInstitutionType);

    Institution createInstitutionByExternalId(String externalId);

    Institution createInstitutionFromAnac(Institution institution);

    Institution createInstitutionFromIvass(Institution institution);

    Institution createInstitutionFromInfocamere(Institution institution);

    Institution createPgInstitution(String taxId, String description, boolean existsInRegistry, SelfCareUser selfCareUser);

    Institution createInstitution(Institution institution);

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

    Optional<GeographicTaxonomies> retrieveGeoTaxonomies(String code);

    List<Institution> findInstitutionsByGeoTaxonomies(String geoTaxonomies, SearchMode searchMode);

    List<Institution> findInstitutionsByProductId(String productId);

    List<Institution> retrieveInstitutionByIds(List<String> ids);

    Institution createPnPgInstitution(String taxId, String description);

    Institution updateInstitution(String institutionId, InstitutionUpdate institutionUpdate, String userId);

    void updateInstitutionDelegation(String institutionId, boolean delegation);

    List<ValidInstitution> retrieveInstitutionByExternalIds(List<ValidInstitution> validInstitutionList, String productId);

    void updateCreatedAt(String institutionId, String productId, OffsetDateTime createdAt, OffsetDateTime activatedAt);

    List<RelationshipInfo> retrieveAllProduct(String userId, UserBinding binding, Institution institution, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    List<Institution> getInstitutionsByProductId(String productId, Integer page, Integer size);

    List<Institution> getInstitutionBrokers(String productId, InstitutionType type);

    List<UserInfo> getInstitutionUsers(String institutionId);

}
