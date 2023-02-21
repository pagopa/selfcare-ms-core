package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.List;

public interface InstitutionService {

    Institution retrieveInstitutionById(String id);
    Institution retrieveInstitutionByExternalId(String institutionExternalId);
    Institution createInstitutionByExternalId(String externalId);
    Institution createInstitutionRaw(Institution institution, String externalId);
    Institution createPgInstitution(String taxId, boolean existsInRegistry, SelfCareUser selfCareUser);
    List<Onboarding> retrieveInstitutionProducts(String institutionId, List<String> states);
    Institution getInstitutionProduct(String externalId, String productId);
    GeographicTaxonomies getGeoTaxonomies(String code);
    void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);
}
