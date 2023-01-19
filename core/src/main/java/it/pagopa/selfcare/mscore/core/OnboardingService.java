package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;

public interface OnboardingService {

    void onboardingInstitution(OnboardingRequest request, SelfCareUser principal);
}
