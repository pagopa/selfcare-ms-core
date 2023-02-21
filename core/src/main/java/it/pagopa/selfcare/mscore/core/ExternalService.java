package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    ProductManagerInfo retrieveInstitutionManager(String externalId, String productId);

    String retrieveRelationship(ProductManagerInfo manager, String productId);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId);



}
