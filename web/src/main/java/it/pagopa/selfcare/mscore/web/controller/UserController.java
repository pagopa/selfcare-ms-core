package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.core.UserRelationshipService;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;
import static it.pagopa.selfcare.mscore.constant.GenericError.GET_RELATIONSHIP_ERROR;

@Slf4j
@RestController
@Api(tags = "Persons")
public class UserController {

    private final UserRelationshipService userRelationshipService;

    public UserController(UserRelationshipService userRelationshipService) {
        this.userRelationshipService = userRelationshipService;
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
}
