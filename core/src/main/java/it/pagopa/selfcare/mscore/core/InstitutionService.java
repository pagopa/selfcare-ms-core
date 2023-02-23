package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.List;

public interface InstitutionService {

    Institution retrieveInstitutionById(String id);
    Institution retrieveInstitutionByExternalId(String institutionExternalId);
    Institution createInstitutionByExternalId(String externalId);
    Institution createInstitutionRaw(Institution institution, String externalId);
    Institution createPgInstitution(String taxId, boolean existsInRegistry, SelfCareUser selfCareUser);
    List<Onboarding> retrieveInstitutionProducts(Institution institution, List<RelationshipState> states);
    Institution getInstitutionProduct(String externalId, String productId);
    GeographicTaxonomies getGeoTaxonomies(String code);
    List<RelationshipInfo> getUserInstitutionRelationships(Institution institution, String userId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);
    void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);
    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomies(Institution institution);
    Institution updateInstitution(EnvEnum env, String institutionId, InstitutionUpdate institutionUpdate, String userId);
}
