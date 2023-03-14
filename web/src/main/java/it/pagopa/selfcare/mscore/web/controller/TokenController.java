package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

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
     * It takes in a String tokenId and returns a Token object.
     *
     * @param tokenId tokenId
     * @return The token tokenId
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     * * Code: 409, Message: Token already consumed, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.token.verify}", notes = "${swagger.mscore.token.verify}")
    @PostMapping("/tokens/{tokenId}/verify")
    public ResponseEntity<TokenResponse> verifyToken(@ApiParam("${swagger.mscore.token.tokenId}")
                                                     @PathVariable("tokenId") String tokenId) {
        log.info("Verify token identified with {}", tokenId);
        CustomExceptionMessage.setCustomMessage(VERIFY_TOKEN_FAILED);
        tokenService.verifyToken(tokenId);
        return ResponseEntity.ok().body(new TokenResponse(tokenId));
    }
}
