package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.model.onboarding.VerifyOnboardingFilters;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param productId  String
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.verify}", notes = "${swagger.mscore.onboarding.verify}")
    @RequestMapping(method = {RequestMethod.HEAD}, value = "/institution/{externalId}/products/{productId}")
    public ResponseEntity<Void> verifyOnboardingInfo(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                     @PathVariable(value = "externalId") String externalId,
                                                     @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                     @PathVariable(value = "productId") String productId) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_VERIFICATION_ERROR);
        onboardingService.verifyOnboardingInfo(externalId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * The function verify onboarding status of given product and subunit of institution
     *
     * @param taxCode     String
     * @param subunitCode String
     * @param productId   String
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.verify}", notes = "${swagger.mscore.onboarding.verify}")
    @RequestMapping(method = {RequestMethod.HEAD}, value = "")
    public ResponseEntity<Void> verifyOnboardingInfo(@ApiParam("${swagger.mscore.institutions.model.taxCode}")
                                                     @RequestParam(value = "taxCode") String taxCode,
                                                     @ApiParam("${swagger.mscore.institutions.model.subunitCode}")
                                                     @RequestParam(value = "subunitCode", required = false) String subunitCode,
                                                     @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                     @RequestParam(value = "productId") String productId) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_VERIFICATION_ERROR);
        onboardingService.verifyOnboardingInfoSubunit(taxCode, subunitCode, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.verify}", notes = "${swagger.mscore.onboarding.verify}")
    @RequestMapping(method = {RequestMethod.HEAD}, value = "/verify")
    public ResponseEntity<Void> verifyOnboardingInfoByFilters(@ApiParam("${swagger.mscore.institutions.model.productId}")
                                                       @RequestParam(value = "productId") String productId,
                                                       @ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                       @RequestParam(value = "externalId", required = false) String externalId,
                                                       @ApiParam("${swagger.mscore.institutions.model.taxCode}")
                                                       @RequestParam(value = "taxCode", required = false) String taxCode,
                                                       @ApiParam("${swagger.mscore.institutions.model.origin}")
                                                       @RequestParam(value = "origin", required = false) String origin,
                                                       @ApiParam("${swagger.mscore.institutions.model.originId}")
                                                       @RequestParam(value = "originId", required = false) String originId,
                                                       @ApiParam("${swagger.mscore.institutions.model.subunitCode}")
                                                       @RequestParam(value = "subunitCode", required = false) String subunitCode) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_VERIFICATION_ERROR);
        onboardingService.verifyOnboardingInfoByFilters(new VerifyOnboardingFilters(productId, externalId, taxCode, origin, originId, subunitCode));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
