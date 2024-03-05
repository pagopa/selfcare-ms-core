package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.onboarding.PaginatedToken;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.web.model.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenListResponse;
import it.pagopa.selfcare.mscore.web.model.token.PaginatedTokenResponse;
import it.pagopa.selfcare.mscore.web.model.token.TokenResource;
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
    private final TokenMapper tokenMapper;

    public TokenController(TokenService tokenService, TokenMapper tokenMapper) {
        this.tokenService = tokenService;
        this.tokenMapper = tokenMapper;
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
        TokenResource result = tokenMapper.toTokenResponse(token);
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
    @Tags({@Tag(name = "external-v2"), @Tag(name = "Token")})
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
        List<TokenRelationships> tokens = tokenService.getTokensByProductId(productId, page, size);

        TokenListResponse tokenListResponse = new TokenListResponse(
                tokens.stream()
                        .map(tokenMapper::toTokenResponse)
                        .collect(Collectors.toList()));

        log.trace("findFromProduct end");
        return ResponseEntity.ok().body(tokenListResponse);
    }

    /**
     * Retrieve Contract filter by Status
     *
     * @param states List<RelationshipState>
     * @param page   Integer
     * @param size   Integer
     * @return List
     * * Code: 200, Message: successful operation
     * * Code: 404, Message: product not found
     */
    @Tag(name = "Token")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.tokens.getAll}", notes = "${swagger.mscore.tokens.getAll}")
    @GetMapping(value = "/tokens")
    public ResponseEntity<PaginatedTokenResponse> getAllTokens(@ApiParam("${swagger.mscore.token.model.states}")
                                                               @RequestParam(value = "states", defaultValue = "ACTIVE,DELETED") List<RelationshipState> states,
                                                               @ApiParam("${swagger.mscore.page.number}")
                                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @ApiParam("${swagger.mscore.page.size}")
                                                               @RequestParam(name = "size", defaultValue = "100") Integer size,
                                                               @RequestParam(name = "productId", required = false) String productId) {
        log.trace("getAllToken start");
        log.debug("getAllToken page = {}", page);
        PaginatedToken tokens = tokenService.retrieveContractsFilterByStatus(states, page, size, productId);

        PaginatedTokenResponse tokenListResponse = new PaginatedTokenResponse(
                tokens.getItems().stream()
                        .map(tokenMapper::toScContractResponse)
                        .toList());

        log.trace("getAllToken end");
        return ResponseEntity.ok().body(tokenListResponse);
    }
}
