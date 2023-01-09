package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    Institution getBillingByExternalId(String externalId, String productId);

    OnboardedUser getManagerByExternalId(String externalId, String productId);
}
