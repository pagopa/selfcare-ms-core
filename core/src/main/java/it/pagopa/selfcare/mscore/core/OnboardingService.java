package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OnboardingService {

    void onboardingInstitution(OnboardingRequest request, SelfCareUser principal);

    void verifyOnboardingInfo(String externalId, String productId);

    List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId);

    void completeOboarding(Token token, MultipartFile contract);

    void invalidateOnboarding(Token token);

    void approveOnboarding(Token token, SelfCareUser selfCareUser);

    void onboardingReject(Token token);

    List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest toOnboardingOperatorRequest, PartyRole role);

    void onboardingLegals(OnboardingLegalsRequest toOnboardingLegalsRequest, SelfCareUser selfCareUser, Token token);

    ResourceResponse retrieveDocument(String relationshipId);

}
