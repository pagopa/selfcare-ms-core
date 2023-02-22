package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.ProductRelationship;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;

@Slf4j
@RestController
@Api(tags = "Token")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    /**
     * The verifyToken function is used to verify the token that was created by the onboarding service.
     * It takes in a String id and returns an Institution object.
     *
     * @param  id tokenId
     *
     * @return The token id
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     * * Code: 409, Message: Token already consumed, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.token.verify}")
    @PostMapping("/tokens/{tokenId}/verify")
    public ResponseEntity<String> verifyToken(@ApiParam("${swagger.mscore.token.tokenId}")
                                              @PathVariable("tokenId") String id) {
        log.info("Verify token identified with {}", id);
        CustomExceptionMessage.setCustomMessage(ACTIVATE_RELATIONSHIP_ERROR);
        String tokenId = tokenService.verifyToken(id).getId();
        return ResponseEntity.ok().body(tokenId);
    }

    /**
     * The function activate a suspended relationship
     *
     * @param  id tokenId
     *
     * @return no content
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid starter token status, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.token.activate}")
    @PostMapping("/relationships/{tokenId}/activate")
    public ResponseEntity<String> activateRelationship(@ApiParam("${swagger.mscore.token.tokenId}")
                                              @PathVariable("tokenId") String id) {
        log.info("Activating relationship {}", id);
        CustomExceptionMessage.setCustomMessage(SUSPEND_RELATIONSHIP_ERROR);
        tokenService.activateRelationship(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * The function suspend an active relationship
     *
     * @param  id tokenId
     *
     * @return no content
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid starter token status, DataType: Problem
     * * Code: 404, Messa
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.token.suspend}")
    @PostMapping("/relationships/{tokenId}/suspend")
    public ResponseEntity<String> suspendRelationship(@ApiParam("${swagger.mscore.token.tokenId}")
                                              @PathVariable("tokenId") String id) {
        log.info("Suspending relationship {}", id);
        CustomExceptionMessage.setCustomMessage(VERIFY_TOKEN_FAILED);
        tokenService.suspendRelationship(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * The function Gets the corresponding relationship
     *
     * @param  tokenId tokenId
     *
     * @return RelationshipResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid starter token status, DataType: Problem
     * * Code: 404, Messa
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", notes = "${swagger.mscore.token.suspend}")
    @PostMapping("/relationships/{id}")
    public ResponseEntity<RelationshipResult> getRelationship(@ApiParam("${swagger.mscore.token.tokenId}")
                                                      @PathVariable("id") String tokenId) {
        log.info("Getting relationship {}", tokenId);
        CustomExceptionMessage.setCustomMessage(GET_RELATIONSHIP_ERROR);
        ProductRelationship relationship = tokenService.retrieveRelationship(tokenId);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipInfo(relationship));
    }
}
