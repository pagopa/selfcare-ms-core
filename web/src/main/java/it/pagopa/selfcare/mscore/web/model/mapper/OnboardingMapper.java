package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.OnboardingData;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    public static OnboardingResponse toResource(OnboardingData onboardingData) {
       OnboardingResponse onboardingResponse = new OnboardingResponse();

       return onboardingResponse;
    }

    public static OnboardingData fromDto(OnboardingRequest dto) {
       OnboardingData onboardingData = new OnboardingData();

       return onboardingData;
    }

}
