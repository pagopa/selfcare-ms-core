package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.*;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;
import static it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage.setCustomMessage;

@Slf4j
@RestController
@RequestMapping(value = "/onboarding")
@Api(tags = "Onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;
    private final TokenService tokenService;

    public OnboardingController(OnboardingService onboardingService, TokenService tokenService) {
        this.onboardingService = onboardingService;
        this.tokenService = tokenService;
    }


    /**
     * Code: 204, Message: successful operation
     * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.verify}")
    @RequestMapping(method = {RequestMethod.HEAD}, value = "/institution/{externalId}/products/{productId}")
    public ResponseEntity<Void> verifyOnboardingInfo(@PathVariable(value = "externalId") String externalId,
                                                     @PathVariable(value = "productId") String productId) {

        log.info("Verifying onboarding for institution having externalId {} on product {}", externalId, productId);
        setCustomMessage(ONBOARDING_VERIFICATION_ERROR);
        onboardingService.verifyOnboardingInfo(externalId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Code: 200, Message: successful operation, DataType: OnboardingInfo
     * Code: 404, Message: Not found, DataType: Problem
     * Code: 400, Message: Invalid ID supplied, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.info}")
    @GetMapping(value = "/info")
    public ResponseEntity<OnboardingInfoResponse> onboardingInfo(@RequestParam(value = "institutionId", required = false) String institutionId,
                                                                 @RequestParam(value = "institutionExternalId", required = false) String institutionExternalId,
                                                                 @RequestParam(value = "states", required = false) String[] states,
                                                                 Authentication authentication) {
        log.info("Getting onboarding info for institution having institutionId {} institutionExternalId {} and states {}", institutionId, institutionExternalId, states);
        setCustomMessage(GETTING_ONBOARDING_INFO_ERROR);
        String userId = ((SelfCareUser) authentication.getPrincipal()).getId();
        List<OnboardingInfo> onboardingInfoList = onboardingService.getOnboardingInfo(institutionId, institutionExternalId, states, userId);
        return ResponseEntity.ok().body(OnboardingMapper.toOnboardingInfoResponse(userId, onboardingInfoList));
    }


    /**
     * Code: 204, Message: successful operation
     * Code: 404, Message: Not found, DataType: Problem
     * Code: 400, Message: Invalid ID supplied, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.institution}")
    @PostMapping(value = "/institution")
    public ResponseEntity<Void> onboardingInstitution(@RequestBody @Valid OnboardingInstitutionRequest onboardingInstitutionRequest
                                                      /*Authentication authentication*/) {
        SelfCareUser selfCareUser = SelfCareUser.builder("3fa85f64-5717-4562-b3fc-2c963f66afa6").build();
        log.info("Onboarding institution having externalId {}", onboardingInstitutionRequest.getInstitutionExternalId());
        setCustomMessage(ONBOARDING_OPERATION_ERROR);
        onboardingService.onboardingInstitution(OnboardingMapper.toOnboardingRequest(onboardingInstitutionRequest), selfCareUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.complete}")
    @PostMapping(value = "/complete/{tokenId}")
    public ResponseEntity<Void> completeOnboarding(@PathVariable(value = "tokenId") String tokenId,
                                                  @RequestParam("contract") MultipartFile file) {
        log.info("Confirm onboarding of token identified with {}", tokenId);
        setCustomMessage(CONFIRM_ONBOARDING_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.completeOboarding(token, file);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.complete}")
    @PostMapping(value = "/approve/{tokenId}")
    public ResponseEntity<Void> approveOnboarding(@PathVariable(value = "tokenId") String tokenId/*,
                                                  Authentication authentication*/) {
        setCustomMessage(CONFIRM_ONBOARDING_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        SelfCareUser selfCareUser = SelfCareUser.builder("3fa85f64-5717-4562-b3fc-2c963f66afa6").build();
        onboardingService.approveOnboarding(token, selfCareUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.invalidate}")
    @DeleteMapping(value = "/complete/{tokenId}")
    public ResponseEntity<Void> invalidateOnboarding(@PathVariable(value = "tokenId") String tokenId) {
        log.info("Invalidating onboarding for token identified with {}", tokenId);
        setCustomMessage(INVALIDATE_ONBOARDING_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.invalidateOnboarding(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.reject}")
    @DeleteMapping(value = "/reject/{tokenId}")
    public ResponseEntity<OnboardingInfoResponse> onboardingReject(@PathVariable("tokenId") String tokenId) {
        log.info("Onboarding Reject having tokenId {}", tokenId);
        setCustomMessage(ONBOARDING_OPERATION_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.onboardingReject(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
