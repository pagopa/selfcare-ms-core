package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.ErrorEnum;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.util.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/onboarding", produces = MediaType.APPLICATION_JSON_VALUE)
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @ExceptionMessage(message = ErrorEnum.ONBOARDING_VERIFICATION_ERROR)
    @RequestMapping(method = {RequestMethod.HEAD}, value = "/institution/{externalId}/products/{productId}")
    public ResponseEntity<Void> verifyOnboardingInfo(@PathVariable(value = "externalId") String externalId,
                                                     @PathVariable(value = "productId") String productId) {
        onboardingService.verifyOnboardingInfo(externalId, productId);
        return ResponseEntity.ok().build();
    }

    @ExceptionMessage(message = ErrorEnum.GETTING_ONBOARDING_INFO_ERROR)
    @GetMapping(value = "/info")
    public ResponseEntity<OnboardingInfoResponse> onboardingInfo(@RequestParam(value = "institutionId") String institutionId,
                                                                 @RequestParam(value = "institutionExternalId") String institutionExternalId,
                                                                 @RequestParam(value = "states") String[] states,
                                                                 Authentication authentication) {
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        String userId = selfCareUser.getId();
        OnboardingInfoResponse onboardingInfo = OnboardingMapper
                .toResource(onboardingService.getOnboardingInfo(institutionId, institutionExternalId, states, userId),userId);
        return ResponseEntity.ok().body(onboardingInfo);
    }
}
