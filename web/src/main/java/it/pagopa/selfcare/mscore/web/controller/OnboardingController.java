package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.model.OnboardingInfo;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;

@Slf4j
@RestController
@RequestMapping(value = "/onboarding")
@Api(tags = "Onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    /**
     * The function verify onboarding status of given product and institution
     *
     * @param externalId String
     * @param productId String
     *
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.verify}")
    @RequestMapping(method = {RequestMethod.HEAD}, value = "/institution/{externalId}/products/{productId}")
    public ResponseEntity<Void> verifyOnboardingInfo(@PathVariable(value = "externalId") String externalId,
                                                     @PathVariable(value = "productId") String productId) {

        log.info("Verifying onboarding for institution having externalId {} on product {}", externalId, productId);
        CustomExceptionMessage.setCustomMessage(ONBOARDING_VERIFICATION_ERROR);
        onboardingService.verifyOnboardingInfo(externalId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function return onboardingInfo
     *
     * @param institutionId String
     * @param institutionExternalId String
     * @param states String[]
     *
     * @return onboardingInfoResponse
     *
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.info}")
    @GetMapping(value = "/info")
    public ResponseEntity<OnboardingInfoResponse> onboardingInfo(@RequestParam(value = "institutionId", required = false) String institutionId,
                                                                 @RequestParam(value = "institutionExternalId", required = false) String institutionExternalId,
                                                                 @RequestParam(value = "states", required = false) String[] states,
                                                                 Authentication authentication) {
        log.info("Getting onboarding info for institution having institutionId {} institutionExternalId {} and states {}", institutionId, institutionExternalId, states);
        CustomExceptionMessage.setCustomMessage(GETTING_ONBOARDING_INFO_ERROR);
        String userId = ((SelfCareUser) authentication.getPrincipal()).getId();
        List<OnboardingInfo> onboardingInfoList = onboardingService.getOnboardingInfo(institutionId, institutionExternalId, states, userId);
        return ResponseEntity.ok().body(OnboardingMapper.toOnboardingInfoResponse(userId, onboardingInfoList));
    }


    /**
     * The function persist onboarding data
     *
     * @param onboardingInstitutionRequest OnboardingInstitutionRequest
     *
     * @return no content

     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.institution}")
    @PostMapping(value = "/institution")
    public ResponseEntity<Void> onboardingInstitution(@RequestBody @Valid OnboardingInstitutionRequest onboardingInstitutionRequest,
                                                      Authentication authentication) {
        log.info("Onboarding institution having externalId {}", onboardingInstitutionRequest.getInstitutionExternalId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_OPERATION_ERROR);
        onboardingService.onboardingInstitution(OnboardingMapper.toOnboardingRequest(onboardingInstitutionRequest),
                (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
