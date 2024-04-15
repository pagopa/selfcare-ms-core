package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingOperatorsRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingUsersRequest;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;

import java.util.List;

public interface OnboardingService {

    void verifyOnboardingInfo(String externalId, String productId);

    void verifyOnboardingInfoSubunit(String taxCode, String subunitCode, String productId);

    void verifyOnboardingInfoOrigin(String productId, String origin, String originId);

    void verifyOnboardingInfoByFilters(String productId, String externalId, String taxCode, String origin, String originId, String subunitCode);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);

    Institution persistOnboarding(String institutionId, String productId, List<UserToOnboard> users, Onboarding onboarding);

    List<RelationshipInfo> onboardingUsers(OnboardingUsersRequest request, String loggedUserName, String loggedUserSurname);

    List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest toOnboardingOperatorRequest, PartyRole role, String loggedUserName, String loggedUserSurname);

    ResourceResponse retrieveDocument(String relationshipId);


}
