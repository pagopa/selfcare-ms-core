package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    ProductManagerInfo retrieveInstitutionManager(String externalId, String productId);

    String retrieveRelationship(ProductManagerInfo manager, String productId);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId);

    List<RelationshipInfo> getUserInstitutionRelationships(String externalId, String userId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    List<Onboarding> retrieveInstitutionProductsByExternalId(String externalId, List<RelationshipState> states);

}
