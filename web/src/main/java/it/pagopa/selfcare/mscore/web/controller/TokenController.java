package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.core.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;
import static it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage.setCustomMessage;

@Slf4j
@RestController
@RequestMapping(value = "/tokens", produces = MediaType.APPLICATION_JSON_VALUE)
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
     *
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     * * Code: 409, Message: Token already consumed, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.external.institution}")
    @PostMapping("/{tokenId}/verify")
    public ResponseEntity<String> verifyToken(@ApiParam("${swagger.mscore.token.tokenId}")
                                              @PathVariable("tokenId") String id) {
        log.info("Verify token identified with {}", id);
        setCustomMessage(VERIFY_TOKEN_FAILED);
        return ResponseEntity.ok().body(tokenService.verifyToken(id).getId());
    }
}
