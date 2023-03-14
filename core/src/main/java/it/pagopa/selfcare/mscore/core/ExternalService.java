package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    ProductManagerInfo retrieveInstitutionManager(String externalId, String productId);

    Institution retrieveInstitutionProduct(String externalId, String productId);

    List<RelationshipInfo> getUserInstitutionRelationships(String externalId, String userId, String personId,
                                                           List<PartyRole> roles,
                                                           List<RelationshipState> states,
                                                           List<String> products,
                                                           List<String> productRoles);

    List<Onboarding> retrieveInstitutionProductsByExternalId(String externalId, List<RelationshipState> states);

}
