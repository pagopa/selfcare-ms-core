package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionOperatorsRequest;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

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
        log.info("Verifying onboarding for institution having externalId {} on product {}", externalId, productId);
        CustomExceptionMessage.setCustomMessage(ONBOARDING_VERIFICATION_ERROR);
        onboardingService.verifyOnboardingInfo(externalId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function return onboardingInfo
     *
     * @param institutionId         String
     * @param institutionExternalId String
     * @param states                String[]
     * @return onboardingInfoResponse
     * <p>
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.onboarding.info}", notes = "${swagger.mscore.onboarding.info}")
    @GetMapping(value = "/info")
    public ResponseEntity<OnboardingInfoResponse> onboardingInfo(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                 @RequestParam(value = "institutionId", required = false) String institutionId,
                                                                 @ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                 @RequestParam(value = "institutionExternalId", required = false) String institutionExternalId,
                                                                 @ApiParam("${swagger.mscore.institutions.model.relationshipState}")
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
     * @param request OnboardingInstitutionRequest
     * @return no content
     * <p>
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.institution}", notes = "${swagger.mscore.onboarding.institution}")
    @PostMapping(value = "/institution")
    public ResponseEntity<Void> onboardingInstitution(@RequestBody @Valid OnboardingInstitutionRequest request,
                                                      Authentication authentication) {
        log.info("Onboarding institution having externalId {}", request.getInstitutionExternalId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_OPERATION_ERROR);
        onboardingService.onboardingInstitution(OnboardingMapper.toOnboardingRequest(request), (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function persist operators for given onboarding
     *
     * @param request OnboardingInstitutionOperatorsRequest
     * @return no content
     * * Code: 204, Message: successful operation
     * * Code: 404, Message: Not found, DataType: Problem
     * * Code: 400, Message: Invalid request, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.onboarding.operator}", notes = "${swagger.mscore.onboarding.operator}")
    @PostMapping(value = "/operators")
    public ResponseEntity<List<RelationshipResult>> onboardingInstitutionOperators(@RequestBody @Valid OnboardingInstitutionOperatorsRequest request) {
        log.info("Onboarding operators on institution {}", request.getInstitutionId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_OPERATORS_ERROR);
        tokenService.verifyOnboarding(request.getInstitutionId(), request.getProductId());
        List<RelationshipInfo> response = onboardingService.onboardingOperators(OnboardingMapper.toOnboardingOperatorRequest(request), PartyRole.OPERATOR);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipResultList(response));
    }

    /**
     * The function persist subDelegates for given onboarding
     *
     * @param request OnboardingInstitutionOperatorRequest
     * @return no content
     * * Code: 204, Message: successful operation
     * * Code: 404, Message: Not found, DataType: Problem
     * * Code: 400, Message: Invalid request, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.onboarding.subdelegates}", notes = "${swagger.mscore.onboarding.subdelegates}")
    @PostMapping(value = "/subdelegates")
    public ResponseEntity<List<RelationshipResult>> onboardingInstitutionSubDelegate(@RequestBody @Valid OnboardingInstitutionOperatorsRequest request) {
        log.info("Onboarding subdelegates on institution {}", request.getInstitutionId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_SUBDELEGATES_ERROR);
        tokenService.verifyOnboarding(request.getInstitutionId(), request.getProductId());
        List<RelationshipInfo> response = onboardingService.onboardingOperators(OnboardingMapper.toOnboardingOperatorRequest(request), PartyRole.SUB_DELEGATE);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipResultList(response));
    }
}
