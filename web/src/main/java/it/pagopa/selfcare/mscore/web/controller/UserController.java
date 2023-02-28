package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.model.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.ResourceResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;
import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.GET_RELATIONSHIP_ERROR;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@Slf4j
@RestController
@Api(tags = "Persons")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The function verify is user existes
     *
     * @param userId String
     *
     * @return void
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: Person not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.person.verify}", notes = "${swagger.mscore.person.verify}")
    @RequestMapping(method = { RequestMethod.HEAD}, value = "/persons/{id}")
    public ResponseEntity<Void> verifyUser(@PathVariable(value = "id") String userId) {
        userService.verifyUser(userId);
        return ResponseEntity.ok().build();
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
    @ApiOperation(value = "", notes = "${swagger.mscore.relationship.activate}")
    @PostMapping("/relationships/{relationshipId}/activate")
    public ResponseEntity<Void> activateRelationship(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                     @PathVariable("relationshipId") String relationshipId) {
        log.info("Activating relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(ACTIVATE_RELATIONSHIP_ERROR);
        userService.activateRelationship(relationshipId);
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
    @ApiOperation(value = "", notes = "${swagger.mscore.relationship.suspend}")
    @PostMapping("/relationships/{relationshipId}/suspend")
    public ResponseEntity<Void> suspendRelationship(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                    @PathVariable("tokenId") String relationshipId) {
        log.info("Suspending relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(SUSPEND_RELATIONSHIP_ERROR);
        userService.suspendRelationship(relationshipId);
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
    @ApiOperation(value = "", notes = "${swagger.mscore.relationship.delete}")
    @DeleteMapping("/relationships/{relationshipId}")
    public ResponseEntity<Void> deleteRelationship(@ApiParam("${swagger.mscore.relationship.relationshipId}")
                                                   @PathVariable("relationshipId") String relationshipId) {
        log.info("Getting relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(GET_RELATIONSHIP_ERROR);
        userService.deleteRelationship(relationshipId);
        return ResponseEntity.ok().build();
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
    @ApiOperation(value = "", notes = "${swagger.mscore.token.get}")
    @GetMapping("/relationships/{relationshipId}")
    public ResponseEntity<RelationshipResult> getRelationship(@ApiParam("${swagger.mscore.token.relationshipId}")
                                                              @PathVariable("relationshipId") String relationshipId) {
        log.info("Getting relationship {}", relationshipId);
        CustomExceptionMessage.setCustomMessage(GET_RELATIONSHIP_ERROR);
        RelationshipInfo relationship = userService.retrieveRelationship(relationshipId);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipResult(relationship));
    }
}
