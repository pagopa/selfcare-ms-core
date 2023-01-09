package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardingData;

public interface OnboardingService {
    void verifyOnboardingInfo(String externalId, String productId);
    OnboardingData getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);
}
