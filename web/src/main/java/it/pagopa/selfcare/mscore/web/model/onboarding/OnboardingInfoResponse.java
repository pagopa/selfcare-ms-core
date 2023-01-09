package it.pagopa.selfcare.mscore.web.model.onboarding;

import lombok.Data;

import java.util.List;

@Data
public class OnboardingInfoResponse {
    private String userId;
    private List<OnboardingDataResponse> institutions;
}
