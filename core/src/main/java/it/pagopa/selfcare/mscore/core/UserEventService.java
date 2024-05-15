package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;

public interface UserEventService {

    void sendOnboardedUserNotification(OnboardedUser onboardedUser, String productId);

}
