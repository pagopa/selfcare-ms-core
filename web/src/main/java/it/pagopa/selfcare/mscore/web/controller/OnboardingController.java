package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.ONBOARDING_OPERATION_ERROR;
import static it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage.setCustomMessage;

@Slf4j
@RestController
@RequestMapping(value = "/onboarding")
@Api(tags = "Onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping(value = "/institution")
    public ResponseEntity<Void> onboardingInstitution(@RequestBody @Valid OnboardingInstitutionRequest onboardingInstitutionRequest,
                                                      Authentication authentication) {
            setCustomMessage(ONBOARDING_OPERATION_ERROR);
            onboardingService.onboardingInstitution(OnboardingMapper.toOnboardingRequest(onboardingInstitutionRequest), (SelfCareUser) authentication.getPrincipal());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
