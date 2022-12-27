package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface ExternalService {

    Institution createInstitution(Institution institution);

    List<Institution> getAllInstitution();

    Institution getInstitutionById(String id);

    Institution getInstitutionByExternalId(String externalId);

    void deleteInstitution(String id);

    List<GeographicTaxonomies> getGeoTaxonomies(String externalId);

    Institution getBillingByExternalId(String externalId, String productId);

    OnboardedUser getManagerByExternalId(String externalId, String productId);
}
