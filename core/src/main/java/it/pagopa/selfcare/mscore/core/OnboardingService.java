package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;

import java.util.List;

public interface OnboardingService {

    void onboardingInstitution(OnboardingRequest request, SelfCareUser principal);

    void verifyOnboardingInfo(String externalId, String productId);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);

}
