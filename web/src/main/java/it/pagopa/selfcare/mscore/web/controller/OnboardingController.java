package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.ResourceResponse;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionLegalsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionOperatorsRequest;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.PRODUCT_RELATIONSHIP_STATES;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

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
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.verify}")
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
     * @param onboardingInstitutionRequest OnboardingInstitutionRequest
     * @return no content
     * <p>
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.institution}", notes = "${swagger.mscore.onboarding.institution}")
    @PostMapping(value = "/institution")
    public ResponseEntity<Void> onboardingInstitution(@RequestBody @Valid OnboardingInstitutionRequest onboardingInstitutionRequest,
                                                      Authentication authentication) {
        log.info("Onboarding institution having externalId {}", onboardingInstitutionRequest.getInstitutionExternalId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_OPERATION_ERROR);
        onboardingService.onboardingInstitution(OnboardingMapper.toOnboardingRequest(onboardingInstitutionRequest), (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function complete onboarding request
     *
     * @param tokenId String
     * @param file    MultipartFile
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.complete}")
    @PostMapping(value = "/complete/{tokenId}")
    public ResponseEntity<Void> completeOnboarding(@PathVariable(value = "tokenId") String tokenId,
                                                   @RequestBody MultipartFile file) {
        log.info("Confirm onboarding of token identified with {}", tokenId);
        CustomExceptionMessage.setCustomMessage(CONFIRM_ONBOARDING_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.completeOboarding(token, file);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function approve onboarding request (review of an operator)
     *
     * @param tokenId String
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.approve}")
    @PostMapping(value = "/approve/{tokenId}")
    public ResponseEntity<Void> approveOnboarding(@PathVariable(value = "tokenId") String tokenId,
                                                  Authentication authentication) {
        log.info("Onboarding Approve having tokenId {}", tokenId);
        CustomExceptionMessage.setCustomMessage(ONBOARDING_OPERATION_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.approveOnboarding(token, (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function invalidate onboarding request
     *
     * @param tokenId String
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.invalidate}")
    @DeleteMapping(value = "/complete/{tokenId}")
    public ResponseEntity<Void> invalidateOnboarding(@PathVariable(value = "tokenId") String tokenId) {
        log.info("Invalidating onboarding for token identified with {}", tokenId);
        CustomExceptionMessage.setCustomMessage(INVALIDATE_ONBOARDING_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.invalidateOnboarding(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function invalidate onboarding request (review of an operator)
     *
     * @param tokenId String
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.reject}")
    @DeleteMapping(value = "/reject/{tokenId}")
    public ResponseEntity<OnboardingInfoResponse> onboardingReject(@PathVariable("tokenId") String tokenId) {
        log.info("Onboarding Reject having tokenId {}", tokenId);
        CustomExceptionMessage.setCustomMessage(ONBOARDING_OPERATION_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.onboardingReject(token);
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
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.operator}")
    @PostMapping(value = "/operators")
    public ResponseEntity<List<RelationshipResult>> onboardingInstitutionOperators(@RequestBody @Valid OnboardingInstitutionOperatorsRequest request) {
        log.info("Onboarding operators on institution {}", request.getInstitutionId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_OPERATORS_ERROR);
        tokenService.verifyOnboarding(request.getInstitutionId(),request.getProductId(), PRODUCT_RELATIONSHIP_STATES);
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
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.subdelegates}")
    @PostMapping(value = "/subdelegates")
    public ResponseEntity<List<RelationshipResult>> onboardingInstitutionSubDelegate(@RequestBody @Valid OnboardingInstitutionOperatorsRequest request) {
        log.info("Onboarding subdelegates on institution {}", request.getInstitutionId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_SUBDELEGATES_ERROR);
        tokenService.verifyOnboarding(request.getInstitutionId(),request.getProductId(), PRODUCT_RELATIONSHIP_STATES);
        List<RelationshipInfo> response = onboardingService.onboardingOperators(OnboardingMapper.toOnboardingOperatorRequest(request), PartyRole.SUB_DELEGATE);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipResultList(response));
    }

    /**
     * The function persist legals for given onboarding
     *
     * @param request OnboardingInstitutionLegalsRequest
     * @return no content
     * * Code: 204, Message: successful operation
     * * Code: 404, Message: Not found, DataType: Problem
     * * Code: 400, Message: Invalid request, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.legals}")
    @PostMapping(value = "/legals")
    public ResponseEntity<Void> onboardingInstitutionLegals(@RequestBody @Valid OnboardingInstitutionLegalsRequest request, Authentication authentication) {
        log.info("Onboarding Legals of institution {} and/or externalId {}", request.getInstitutionId(), request.getInstitutionExternalId());
        CustomExceptionMessage.setCustomMessage(ONBOARDING_LEGALS_ERROR);
        //Token token = tokenService.
        tokenService.verifyOnboarding(request.getInstitutionId(), request.getProductId(), PRODUCT_RELATIONSHIP_STATES);
        onboardingService.onboardingLegals(OnboardingMapper.toOnboardingLegalsRequest(request), (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function retrieve the document of specific onboarding
     *
     * @param relationshipId String
     * @return no content
     * * Code: 200, Message: successful operation, DataType: Resource (signed onboarding document)
     * * Code: 404, Message: Document Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.onboarding.relationship.document}")
    @GetMapping(value = "/relationship/{relationshipId}/document")
    public ResponseEntity<byte[]> getOnboardingDocument(@PathVariable("relationshipId") String relationshipId) {
        log.info("Getting onboarding document of relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(GETTING_ONBOARDING_INFO_ERROR);
        ResourceResponse file = onboardingService.retrieveDocument(relationshipId);
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName());
        log.info("contentType: {}", headers.getContentType());
        if (file.getData() != null) {
            log.info("byteArray size: {}", file.getData().length);
        }
        return ResponseEntity.ok().headers(headers).body(file.getData());
    }
}
