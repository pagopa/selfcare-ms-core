package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionLegalsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionOperatorsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
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
     * @param taxCode String
     * @param subunitCode String
     * @param productId  String
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.verify.subunit}", notes = "${swagger.mscore.onboarding.verify.subunit}")
    @RequestMapping(method = {RequestMethod.HEAD}, value = "/")
    public ResponseEntity<Void> verifyOnboardingInfo(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                     @RequestParam(value = "taxCode") String taxCode,
                                                     @ApiParam("${swagger.mscore.institutions.model.subunitCode}")
                                                     @RequestParam(value = "subunitCode", required = false) String subunitCode,
                                                     @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                     @RequestParam(value = "productId") String productId) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_VERIFICATION_ERROR);
        onboardingService.verifyOnboardingInfoSubunit(taxCode, subunitCode, productId);
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
        CustomExceptionMessage.setCustomMessage(GenericError.GETTING_ONBOARDING_INFO_ERROR);
        String userId = ((SelfCareUser) authentication.getPrincipal()).getId();
        List<OnboardingInfo> onboardingInfoList = onboardingService.getOnboardingInfo(institutionId, institutionExternalId, states, userId);
        OnboardingInfoResponse onboardingInfoResponse = OnboardingMapper.toOnboardingInfoResponse(userId, onboardingInfoList);
        log.debug("onboardingInfo result = {}", onboardingInfoResponse);
        return ResponseEntity.ok().body(onboardingInfoResponse);
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
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_OPERATION_ERROR);
        onboardingService.onboardingInstitution(OnboardingMapper.toOnboardingRequest(request), (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function persist onboarding data without contract
     *
     * @param request OnboardingInstitutionRequest
     * @return no content
     * <p>
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.institution.complete}", notes = "${swagger.mscore.onboarding.institution.complete}")
    @PostMapping(value = "/institution/complete")
    public ResponseEntity<Void> onboardingInstitutionComplete(@RequestBody @Valid OnboardingInstitutionRequest request,
                                                      Authentication authentication) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_OPERATION_ERROR);
        onboardingService.onboardingInstitutionComplete(OnboardingMapper.toOnboardingRequest(request), (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * The function complete onboarding request
     *
     * @param tokenId String
     * @param contract MultipartFile
     * @return no content
     * * Code: 204, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.onboarding.complete}", notes = "${swagger.mscore.onboarding.complete}")
    @PostMapping(value = "/complete/{tokenId}")
    public ResponseEntity<Void> completeOnboarding(@ApiParam("${swagger.mscore.token.tokenId}")
                                                   @PathVariable(value = "tokenId") String tokenId,
                                                   @RequestPart MultipartFile contract) {
        CustomExceptionMessage.setCustomMessage(GenericError.CONFIRM_ONBOARDING_ERROR);
        Token token = tokenService.verifyToken(tokenId);
        onboardingService.completeOboarding(token, contract);
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
    @ApiOperation(value = "${swagger.mscore.onboarding.approve}", notes = "${swagger.mscore.onboarding.approve}")
    @PostMapping(value = "/approve/{tokenId}")
    public ResponseEntity<Void> approveOnboarding(@ApiParam("${swagger.mscore.token.tokenId}")
                                                  @PathVariable(value = "tokenId") String tokenId,
                                                  Authentication authentication) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_OPERATION_ERROR);
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
    @ApiOperation(value = "${swagger.mscore.onboarding.invalidate}", notes = "${swagger.mscore.onboarding.invalidate}")
    @DeleteMapping(value = "/complete/{tokenId}")
    public ResponseEntity<Void> invalidateOnboarding(@ApiParam("${swagger.mscore.token.tokenId}")
                                                     @PathVariable(value = "tokenId") String tokenId) {
        CustomExceptionMessage.setCustomMessage(GenericError.INVALIDATE_ONBOARDING_ERROR);
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
    @ApiOperation(value = "${swagger.mscore.onboarding.reject}", notes = "${swagger.mscore.onboarding.reject}")
    @DeleteMapping(value = "/reject/{tokenId}")
    public ResponseEntity<OnboardingInfoResponse> onboardingReject(@ApiParam("${swagger.mscore.token.tokenId}")
                                                                   @PathVariable("tokenId") String tokenId) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_OPERATION_ERROR);
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
    @ApiOperation(value = "${swagger.mscore.onboarding.operator}", notes = "${swagger.mscore.onboarding.operator}")
    @PostMapping(value = "/operators")
    public ResponseEntity<List<RelationshipResult>> onboardingInstitutionOperators(@RequestBody @Valid OnboardingInstitutionOperatorsRequest request) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_OPERATORS_ERROR);
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
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_SUBDELEGATES_ERROR);
        tokenService.verifyOnboarding(request.getInstitutionId(), request.getProductId());
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
    @ApiOperation(value = "${swagger.mscore.onboarding.legals}", notes = "${swagger.mscore.onboarding.legals}")
    @PostMapping(value = "/legals")
    public ResponseEntity<Void> onboardingInstitutionLegals(@RequestBody @Valid OnboardingInstitutionLegalsRequest request, Authentication authentication) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_LEGALS_ERROR);
        Token token = tokenService.verifyOnboarding(request.getInstitutionId(), request.getProductId());
        onboardingService.onboardingLegals(OnboardingMapper.toOnboardingLegalsRequest(request), (SelfCareUser) authentication.getPrincipal(), token);
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
    @ApiOperation(value = "${swagger.mscore.onboarding.relationship.document}", notes = "${swagger.mscore.onboarding.relationship.document}")
    @GetMapping(value = "/relationship/{relationshipId}/document")
    public ResponseEntity<byte[]> getOnboardingDocument(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                        @PathVariable("relationshipId") String relationshipId) {
        CustomExceptionMessage.setCustomMessage(GenericError.GETTING_ONBOARDING_INFO_ERROR);
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
