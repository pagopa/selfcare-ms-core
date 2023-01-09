package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.List;

public interface InstitutionService {

    Institution createInstitutionByExternalId(InstitutionType institutionType, String externalId);

    Institution saveInstitution(Institution institution, String externalId);

    List<GeographicTaxonomies> getGeoTaxonomies(String id);

    Institution createInstitutionRaw(Institution institution, String externalId);

    List<Onboarding> retrieveInstitutionProducts(String id, List<String> states);
}
