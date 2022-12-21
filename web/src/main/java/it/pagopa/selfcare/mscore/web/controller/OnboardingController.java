package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
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

    @RequestMapping(method = {RequestMethod.HEAD}, value = "/institution/{externalId}/products/{productId}")
    public ResponseEntity<Void> verifyOnboardingInfo(@PathVariable(value = "externalId") String externalId,
                                                     @PathVariable(value = "productId") String productId) {
        onboardingService.verifyOnboardingInfo(externalId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/institution")
    public ResponseEntity<Void> onboard(@RequestBody OnboardingRequest onboardingRequest,
                                        Authentication authentication) {
        onboardingService.onboard(OnboardingMapper.fromDto(onboardingRequest), (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.ok().build();
    }
}
