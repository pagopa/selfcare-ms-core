package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingLegalsRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingOperatorsRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OnboardingService {

    void verifyOnboardingInfo(String externalId, String productId);

    void verifyOnboardingInfoSubunit(String taxCode, String subunitCode, String productId);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);

    void onboardingInstitution(OnboardingRequest request, SelfCareUser principal);

    void onboardingInstitutionComplete(OnboardingRequest request, SelfCareUser principal);

    void completeOboarding(Token token, MultipartFile contract);

    void invalidateOnboarding(Token token);

    void approveOnboarding(Token token, SelfCareUser selfCareUser);

    void onboardingReject(Token token);

    List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest toOnboardingOperatorRequest, PartyRole role);

    void onboardingLegals(OnboardingLegalsRequest toOnboardingLegalsRequest, SelfCareUser selfCareUser, Token token);

    ResourceResponse retrieveDocument(String relationshipId);

}
