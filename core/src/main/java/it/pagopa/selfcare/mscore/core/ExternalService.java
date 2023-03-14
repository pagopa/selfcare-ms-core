package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.institution.OnboardingPage;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    ProductManagerInfo retrieveInstitutionManager(String externalId, String productId);

    String retrieveRelationship(ProductManagerInfo manager, String productId);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId, Pageable pageable);

    List<RelationshipInfo> getUserInstitutionRelationships(String externalId, String userId, String personId,
                                                           List<PartyRole> roles,
                                                           List<RelationshipState> states,
                                                           List<String> products,
                                                           List<String> productRoles,
                                                           Pageable pageable);

    OnboardingPage retrieveInstitutionProductsByExternalId(String externalId, List<RelationshipState> states, Pageable pageable);

}
