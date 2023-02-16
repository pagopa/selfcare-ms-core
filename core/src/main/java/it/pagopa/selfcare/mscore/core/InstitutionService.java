package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.List;

public interface InstitutionService {

    Institution retrieveInstitutionById(String id);
    Institution createInstitutionByExternalId(String externalId);

    Institution createInstitutionRaw(Institution institution, String externalId);

    Institution createPgInstitution(String taxId, boolean existsInRegistry, SelfCareUser selfCareUser);

    List<Onboarding> retrieveInstitutionProducts(String id, List<String> states);
    List<OnboardedUser> getUserInstitutionRelationships(Institution institution, String uuid, List<String> roles, List<String> states);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomies(String id);

}
