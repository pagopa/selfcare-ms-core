package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.web.model.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenListResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;
import it.pagopa.selfcare.mscore.web.model.token.TokenResource;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        CustomExceptionMessage.setCustomMessage(GenericError.VERIFY_TOKEN_FAILED);
        tokenService.verifyToken(tokenId);
        return ResponseEntity.ok().body(new TokenResponse(tokenId));
    }

    /**
     * Retrieves the onboarding token for a given institution and product
     *
     * @param institutionId institution's unique identifier
     * @param productId product's unique identifier
     * @return The token
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Token not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.token.api.getToken}")
    @GetMapping(value = "/tokens/token")
    public TokenResource getToken(@ApiParam("${swagger.mscore.institution.model.id}")@RequestParam(value = "institutionId")String institutionId,
                                  @ApiParam("${swagger.mscore.product.model.id}")@RequestParam(value = "productId")String productId){
        log.trace("getToken start");
        log.debug("getToken institutionId = {}, productId = {}", institutionId, productId);
        Token token = tokenService.getToken(institutionId, productId);
        TokenResource result = TokenMapper.toResource(token);
        log.debug("getToken result = {}", result);
        log.trace("getToken end");
        return result;
    }

    /**
     * Retrieve institutions with productId onboarded
     *
     * @param productId String
     * @param page      Integer
     * @param size      Integer
     * @return List
     * * Code: 200, Message: successful operation
     * * Code: 404, Message: product not found
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.tokens.findFromProduct}", notes = "${swagger.mscore.tokens.findFromProduct}")
    @GetMapping(value = "/tokens/products/{productId}")
    public ResponseEntity<TokenListResponse> findFromProduct(@ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                             @PathVariable(value = "productId") String productId,
                                                             @ApiParam("${swagger.mscore.page.number}")
                                                                             @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                             @ApiParam("${swagger.mscore.page.size}")
                                                                             @RequestParam(name = "size", defaultValue = "100") Integer size) {
        log.trace("findFromProduct start");
        log.debug("findFromProduct productId = {}", productId);
        List<Token> tokens = tokenService.getTokensByProductId(productId, page, size);

        TokenListResponse tokenListResponse = new TokenListResponse(
                tokens.stream()
                        .map(TokenMapper::toTokenResponse)
                        .collect(Collectors.toList()));

        log.trace("findFromProduct end");
        return ResponseEntity.ok().body(tokenListResponse);
    }
}
