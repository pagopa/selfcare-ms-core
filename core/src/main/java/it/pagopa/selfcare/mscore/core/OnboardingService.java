package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;

public interface OnboardingService {

    void onboardingInstitution(OnboardingRequest request, SelfCareUser principal);

    void verifyOnboardingInfo(String externalId, String productId);

}
