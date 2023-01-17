package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.Institution;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    Institution getBillingByExternalId(Institution institution, String productId);

    OnboardedUser getInstitutionManager(Institution institution, String productId);

    String getRelationShipToken(String institutionId, String userId, String productId);
}
