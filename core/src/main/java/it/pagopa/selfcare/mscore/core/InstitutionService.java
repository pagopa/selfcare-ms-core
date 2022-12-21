package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface InstitutionService {

    Institution createInstitutionByExternalId(String externalId);

    Institution saveInstitution(Institution institution, String externalId);

    List<GeographicTaxonomies> getGeoTaxonomies(String id);
}
