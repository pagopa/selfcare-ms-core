package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;

import java.util.List;

public interface InstitutionService {

    Institution retrieveInstitutionById(String id);
    Institution retrieveInstitutionByExternalId(String institutionExternalId);
    Institution createInstitutionByExternalId(String externalId);
    Institution createInstitutionRaw(Institution institution, String externalId);
    Institution createPgInstitution(String taxId, boolean existsInRegistry, SelfCareUser selfCareUser);
    List<Onboarding> retrieveInstitutionProducts(String institutionId, List<String> states);
    List<OnboardedUser> getUserInstitutionRelationships(Institution institution, String uuid, List<String> roles, List<String> states);
    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomies(String id);
    Institution updateInstitution(EnvEnum env, String institutionId, InstitutionUpdate institutionUpdate, String userId);
    Institution getInstitutionProduct(String externalId, String productId);
    GeographicTaxonomies getGeoTaxonomies(String code);
    void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);
}
