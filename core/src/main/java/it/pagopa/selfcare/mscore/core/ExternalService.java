package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.List;

public interface ExternalService {

    Institution getInstitutionByExternalId(String externalId);

    Institution getBillingByExternalId(Institution institution, String productId);

    OnboardedUser getInstitutionManager(Institution institution, String productId);

    String getRelationShipToken(String institutionId, String userId, String productId);

    void getInstitutionWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates);

    List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId);
    List<Onboarding> retrieveInstitutionProductsByExternalId(String externalId, List<String> states);


}
