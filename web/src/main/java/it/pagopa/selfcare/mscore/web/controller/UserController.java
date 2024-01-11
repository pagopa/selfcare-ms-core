package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.UserEventService;
import it.pagopa.selfcare.mscore.core.UserRelationshipService;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.user.UserProductsResponse;
import it.pagopa.selfcare.mscore.web.model.user.UserResponse;
import it.pagopa.selfcare.mscore.web.model.user.UsersNotificationResponse;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@Slf4j
@RestController
@Api(tags = "Persons")
public class UserController {

    private final UserRelationshipService userRelationshipService;
    private final UserService userService;
    private final UserEventService userEventService;
    private final UserMapper userMapper;

    public UserController(UserRelationshipService userRelationshipService,
                          UserService userService,
                          UserEventService userEventService,
                          UserMapper userMapper) {
        this.userRelationshipService = userRelationshipService;
        this.userService = userService;
        this.userEventService = userEventService;
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
                                                     @PathVariable("relationshipId") String relationshipId,
                                                     Authentication authentication) {
        log.info("Activating relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(ACTIVATE_RELATIONSHIP_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        userRelationshipService.activateRelationship(relationshipId, selfCareUser.getUserName(), selfCareUser.getSurname());
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
                                                    @PathVariable("relationshipId") String relationshipId,
                                                    Authentication authentication) {
        log.info("Suspending relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(SUSPEND_RELATIONSHIP_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        userRelationshipService.suspendRelationship(relationshipId, selfCareUser.getUserName(), selfCareUser.getSurname());
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
                                                   @PathVariable("relationshipId") String relationshipId,
                                                   Authentication authentication) {
        log.info("Getting relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(GET_RELATIONSHIP_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        userRelationshipService.deleteRelationship(relationshipId, selfCareUser.getUserName(), selfCareUser.getSurname());
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
     * @param userId        String
     * @param institutionId String
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
                                                                             @RequestParam(value = "institutionId", required = false) String institutionId,
                                                                             @ApiParam("${swagger.mscore.institutions.model.relationshipState}")
                                                                             @RequestParam(value = "states", required = false) String[] states) {
        CustomExceptionMessage.setCustomMessage(GenericError.GETTING_ONBOARDING_INFO_ERROR);
        List<OnboardingInfo> onboardingInfoList = userService.getUserInfo(userId, institutionId, states);
        OnboardingInfoResponse onboardingInfoResponse = OnboardingMapper.toUserInfoResponse(userId, onboardingInfoList);
        log.debug("getInstitutionProductsInfo result = {}", onboardingInfoResponse);
        return ResponseEntity.ok().body(onboardingInfoResponse);
    }

    /**
     * The function retrieves products info and role which the user is enabled
     *
     * @param userId        String
     * @param institutionId String
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
                                                                    @RequestParam(value = "institutionId", required = false) String institutionId,
                                                                    @ApiParam("${swagger.mscore.institutions.model.relationshipState}")
                                                                    @RequestParam(value = "states", required = false) String[] states) {

        List<UserBinding> userBindings = userService.retrieveBindings(institutionId, userId, states, null);
        if (Objects.isNull(userBindings) || userBindings.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(UserProductsResponse.builder()
                .id(userId)
                .bindings(userBindings.stream()
                        .map(userMapper::toInstitutionProducts)
                        .collect(Collectors.toList()))
                .build());
    }


    /**
     * The function delete logically the association institution and product
     *
     * @param userId        String
     * @param institutionId String
     * @param productId     String
     * @return onboardingInfoResponse
     * <p>
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.users.delete.products}", notes = "${swagger.mscore.users.delete.products}")
    @DeleteMapping(value = "/users/{userId}/institutions/{institutionId}/products/{productId}")
    public ResponseEntity<Void> deleteProducts(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                               @PathVariable("userId") String userId,
                                               @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                               @PathVariable(value = "institutionId") String institutionId,
                                               @ApiParam("${swagger.mscore.institutions.model.productId}")
                                               @PathVariable(value = "productId") String productId) {

        userService.findAndUpdateStateByInstitutionAndProduct(userId, institutionId, productId, RelationshipState.DELETED);
        return ResponseEntity.ok().build();
    }

    /**
     * The function retrieves userInfo given UserId and optional productId
     *
     * @param userId        String
     * @param productId     String
     * @param institutionId String
     * @return onboardingInfoResponse
     * <p>
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @Tags({@Tag(name = "support"), @Tag(name = "external-v2"), @Tag(name = "Persons")})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.users}", notes = "${swagger.mscore.users}")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@ApiParam("${swagger.mscore.users.userId}")
                                                    @PathVariable("id") String userId,
                                                    @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                    @RequestParam(value = "productId", required = false) String productId,
                                                    @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                    @RequestParam(value = "institutionId", required = false) String institutionId) {

        User user = userService.retrievePerson(userId, productId, institutionId);
        return ResponseEntity.ok().body(userMapper.toUserResponse(user, institutionId));
    }

    /**
     * The function sends a notification to DL when a user's information are updated
     *
     * @param userId        String
     * @param institutionId String
     * @return onboardingInfoResponse
     * <p>
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.api.users.updateUser}", notes = "${swagger.mscore.api.users.updateUser}")
    @PostMapping(value = "/users/{id}/update")
    public ResponseEntity<Void> updateUser(@ApiParam("${swagger.mscore.users.userId}")
                                           @PathVariable("id") String userId,
                                           @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                           @RequestParam(value = "institutionId") String institutionId) {
        userEventService.sendUpdateUserNotificationToQueue(userId, institutionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * The function return onboardingInfo
     *
     * @param size          Integer
     * @param page          Integer
     * @param productId     String
     * @return {@link UsersNotificationResponse}
     * <p>
     * * Code: 200, Message: successful operation
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Not found, DataType: Problem
     */
    @ApiOperation(value = "", notes = "${swagger.mscore.api.users.findAll}")
    @GetMapping(value = "/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsersNotificationResponse> getUsers(@RequestParam(name = "size", required = false) Optional<Integer> size,
                                                              @RequestParam(name = "page", required = false) Optional<Integer> page,
                                                              @RequestParam(name = "productId", required = false) String productId) {
        List<UserNotificationToSend> users = userService.findAll(size, page, productId);
        UsersNotificationResponse userNotificationResponse = new UsersNotificationResponse();
        userNotificationResponse.setUsers(users.stream()
                .map(userMapper::toUserNotification)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(userNotificationResponse);
    }
}
