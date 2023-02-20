package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    OnboardedUser retrieveInstitutionManager(Institution institution, String productId);

    String retrieveRelationship(String institutionId, String userId, String productId);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId);



}
