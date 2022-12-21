package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.OnboardingData;

public interface OnboardingService {
    void verifyOnboardingInfo(String externalId, String productId);

    void onboard(OnboardingData fromDto, SelfCareUser selfCareUser);
}
