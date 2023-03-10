package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstitutionService {

    Institution retrieveInstitutionById(String id);

    Institution retrieveInstitutionByExternalId(String institutionExternalId);

    void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);

    Institution createInstitutionByExternalId(String externalId);

    Institution createInstitutionRaw(Institution institution, String externalId);

    Institution createPgInstitution(String taxId, String description, boolean existsInRegistry, SelfCareUser selfCareUser);

    OnboardingPage retrieveInstitutionProducts(Institution institution, List<RelationshipState> states, Pageable pageable);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<RelationshipInfo> getUserInstitutionRelationships(Institution institution,
                                                           String userId,
                                                           String personId,
                                                           List<PartyRole> roles,
                                                           List<RelationshipState> states,
                                                           List<String> products,
                                                           List<String> productRoles,
                                                           Pageable pageable);

    void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);

    GeographicTaxonomyPage retrieveInstitutionGeoTaxonomies(Institution institution, Pageable pageable);

    GeographicTaxonomies retrieveGeoTaxonomies(String code);

    Institution updateInstitution(String institutionId, InstitutionUpdate institutionUpdate, String userId);
}
