package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;

import java.util.List;

public interface OnboardingService {

    void verifyOnboardingInfo(String externalId, String productId);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);

    void onboardingInstitution(OnboardingRequest request, SelfCareUser principal);

    List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest toOnboardingOperatorRequest, PartyRole role);
}
