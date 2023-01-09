package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.OnboardingData;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    public static OnboardingInfoResponse toResource(OnboardingData onboardingData, String userId){
        OnboardingInfoResponse onboardingInfoResponse = new OnboardingInfoResponse();
        onboardingInfoResponse.setUserId(userId);

        return onboardingInfoResponse;
    }
}
