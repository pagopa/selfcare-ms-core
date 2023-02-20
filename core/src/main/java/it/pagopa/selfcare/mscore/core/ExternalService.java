package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    OnboardedUser retrieveInstitutionManager(Institution institution, String productId);

    String retrieveRelationship(String institutionId, String userId, String productId);

    List<Onboarding> retrieveInstitutionProductsByExternalId(String externalId, List<RelationshipState> states);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId);

    List<RelationshipInfo> getUserInstitutionRelationships(EnvEnum env, String externalId, String userId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);


}
