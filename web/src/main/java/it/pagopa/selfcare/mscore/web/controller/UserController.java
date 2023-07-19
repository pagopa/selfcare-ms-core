package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.UserRelationshipService;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.user.UserProductsResponse;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@Slf4j
@RestController
@Api(tags = "Persons")
public class UserController {

    private final UserRelationshipService userRelationshipService;
    private final OnboardingService onboardingService;
    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserRelationshipService userRelationshipService,
                          OnboardingService onboardingService, UserService userService, UserMapper userMapper) {
        this.userRelationshipService = userRelationshipService;
        this.onboardingService = onboardingService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * The function activate a suspended relationship
     *
     * @param relationshipId relationshipId
     * @return no content
     * * Code: 204
     * * Code: 400, Message: Invalid starter token status, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.relationship.activate}", notes = "${swagger.mscore.relationship.activate}")
    @PostMapping("/relationships/{relationshipId}/activate")
    public ResponseEntity<Void> activateRelationship(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                     @PathVariable("relationshipId") String relationshipId) {
        log.info("Activating relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(ACTIVATE_RELATIONSHIP_ERROR);
        userRelationshipService.activateRelationship(relationshipId);
        return ResponseEntity.noContent().build();
    }

    /**
     * The function suspend an active relationship
     *
     * @param relationshipId tokenId
     * @return no content
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid starter token status, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.relationship.suspend}", notes = "${swagger.mscore.relationship.suspend}")
    @PostMapping("/relationships/{relationshipId}/suspend")
    public ResponseEntity<Void> suspendRelationship(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                    @PathVariable("relationshipId") String relationshipId) {
        log.info("Suspending relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(SUSPEND_RELATIONSHIP_ERROR);
        userRelationshipService.suspendRelationship(relationshipId);
        return ResponseEntity.noContent().build();
    }

    /**
     * The function deleted the corresponding relationship
     *
     * @param relationshipId relationshipId
     * @return RelationshipResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid starter token status, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.relationship.delete}", notes = "${swagger.mscore.relationship.delete}")
    @DeleteMapping("/relationships/{relationshipId}")
    public ResponseEntity<Void> deleteRelationship(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                   @PathVariable("relationshipId") String relationshipId) {
        log.info("Getting relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(GET_RELATIONSHIP_ERROR);
        userRelationshipService.deleteRelationship(relationshipId);
        return ResponseEntity.noContent().build();
    }

    /**
     * The function Gets the corresponding relationship
     *
     * @param relationshipId relationshipId
     * @return RelationshipResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid starter token status, DataType: Problem
     * * Code: 404, Messa
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.relationships}", notes = "${swagger.mscore.relationships}")
    @GetMapping("/relationships/{relationshipId}")
    public ResponseEntity<RelationshipResult> getRelationship(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                              @PathVariable("relationshipId") String relationshipId) {
        log.info("Getting relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(GET_RELATIONSHIP_ERROR);
        RelationshipInfo relationship = userRelationshipService.retrieveRelationship(relationshipId);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipResult(relationship));
    }

    /**
     * The function return onboardingInfo
     *
     * @param userId                String
     * @param institutionId         String
     * @return onboardingInfoResponse
     * <p>
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.onboarding.info}", notes = "${swagger.mscore.onboarding.info}")
    @GetMapping(value = "/users/{userId}/institution-products")
    public ResponseEntity<OnboardingInfoResponse> getInstitutionProductsInfo(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                                             @PathVariable("userId") String userId,
                                                                             @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                             @RequestParam(value = "institutionId", required = false) String institutionId) {
        CustomExceptionMessage.setCustomMessage(GenericError.GETTING_ONBOARDING_INFO_ERROR);
        List<OnboardingInfo> onboardingInfoList = onboardingService.getOnboardingInfo(institutionId, userId);
        OnboardingInfoResponse onboardingInfoResponse = OnboardingMapper.toOnboardingInfoResponse(userId, onboardingInfoList);
        log.debug("onboardingInfo result = {}", onboardingInfoResponse);
        return ResponseEntity.ok().body(onboardingInfoResponse);
    }

    /**
     * The function retrieves products info and role which the user is enabled
     *
     * @param userId                String
     * @param institutionId         String
     * @return onboardingInfoResponse
     * <p>
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.users.products}", notes = "${swagger.mscore.users.products}")
    @GetMapping(value = "/users/{userId}/products")
    public ResponseEntity<UserProductsResponse> getUserProductsInfo(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                                             @PathVariable("userId") String userId,
                                                                    @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                             @RequestParam(value = "institutionId", required = false) String institutionId) {
        List<OnboardedUser> onboardingInfoList = userService.retrieveUsers(institutionId, userId, null, null, null, null);
        if(Objects.isNull(onboardingInfoList) || onboardingInfoList.isEmpty())
            return ResponseEntity.notFound().build();
        UserProductsResponse userProductsResponse = userMapper.toEntity(onboardingInfoList.get(0));
        log.debug("onboardingInfo result = {}", userProductsResponse);
        return ResponseEntity.ok().body(userProductsResponse);
    }
}
