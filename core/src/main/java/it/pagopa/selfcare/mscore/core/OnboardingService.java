package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OnboardingService {

    void verifyOnboardingInfo(String externalId, String productId);

    void verifyOnboardingInfoSubunit(String taxCode, String subunitCode, String productId);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String userId, String[] states);

    void onboardingInstitution(OnboardingRequest request, SelfCareUser principal);

    void onboardingInstitutionComplete(OnboardingRequest request, SelfCareUser principal);

    void completeOnboarding(Token token, MultipartFile contract);

    void invalidateOnboarding(Token token);

    void completeOnboardingWithoutSignatureVerification(Token token, MultipartFile contract);

    Institution persistOnboarding(String institutionId, String productId, String pricingPlan, Billing billing, List<UserToOnboard> users);

    void approveOnboarding(Token token, SelfCareUser selfCareUser);

    void onboardingReject(Token token);

    List<RelationshipInfo> onboardingUsers(OnboardingUsersRequest request, String loggedUserName, String loggedUserSurname);

    List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest toOnboardingOperatorRequest, PartyRole role, String loggedUserName, String loggedUserSurname);

    void onboardingLegals(OnboardingLegalsRequest toOnboardingLegalsRequest, SelfCareUser selfCareUser, Token token);

    ResourceResponse retrieveDocument(String relationshipId);

}
