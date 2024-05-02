package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.VerifyOnboardingFilters;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;

import java.util.List;

public interface OnboardingService {

    void verifyOnboardingInfo(String externalId, String productId);

    void verifyOnboardingInfoSubunit(String taxCode, String subunitCode, String productId);

    void verifyOnboardingInfoByFilters(VerifyOnboardingFilters filters);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);

    Institution persistOnboarding(String institutionId, String productId, List<UserToOnboard> users, Onboarding onboarding);

    ResourceResponse retrieveDocument(String relationshipId);


}
