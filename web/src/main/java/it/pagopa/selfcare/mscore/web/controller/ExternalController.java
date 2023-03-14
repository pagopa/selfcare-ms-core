package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@Slf4j
@RestController
@RequestMapping(value = "/external/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "External")
public class ExternalController {

    private final ExternalService externalService;

    public ExternalController(ExternalService externalService) {
        this.externalService = externalService;
    }

    /**
     * The function returns institution Data from its externalId
     *
     * @param  externalId externalId
     *
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: InstitutionResponse
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.external.institution}", notes = "${swagger.mscore.external.institution}")
    @GetMapping("/{externalId}")
    public ResponseEntity<InstitutionResponse> getByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                               @PathVariable("externalId") String externalId) {
        log.info("Retrieving institution for externalId {}", externalId);
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_EXTERNAL_ID_ERROR);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(institution));
    }


    /**
     * The function returns Manager institution Data from its externalId
     *
     * @param externalId String
     * @param productId  String
     *
     * @return InstitutionManagerResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution Manager not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.external.institution.manager}", notes = "${swagger.mscore.external.institution.manager}")
    @GetMapping(value = "/{externalId}/products/{productId}/manager")
    public ResponseEntity<InstitutionManagerResponse> getManagerInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                        @PathVariable("externalId") String externalId,
                                                                                        @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                                        @PathVariable("productId") String productId) {
        log.info("Getting manager for institution having externalId {}", externalId);
        CustomExceptionMessage.setCustomMessage(INSTITUTION_MANAGER_ERROR);
        ProductManagerInfo manager = externalService.retrieveInstitutionManager(externalId, productId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionManagerResponse(manager, productId));
    }


    /**
     * The function returns billing institution Data from its externalId
     *
     * @param externalId String
     * @param productId  String
     *
     * @return InstitutionBillingResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution Billing not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.external.institution.billing}", notes = "${swagger.mscore.external.institution.billing}")
    @GetMapping(value = "/{externalId}/products/{productId}/billing")
    public ResponseEntity<InstitutionBillingResponse> getBillingInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                        @PathVariable("externalId") String externalId,
                                                                                        @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                                        @PathVariable("productId") String productId) {
        log.info("Retrieving billing data for institution having externalId {} and productId {}", externalId, productId);
        CustomExceptionMessage.setCustomMessage(INSTITUTION_BILLING_ERROR);
        Institution institution = externalService.retrieveInstitutionProduct(externalId, productId);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionBillingResponse(institution, productId));
    }


    /**
     * The function returns institution related products given externalId
     *
     * @param externalId String
     * @param states List<String>
     *
     * @return OnboardedProducts
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution Billing not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.external.institution.products}", notes = "${swagger.mscore.external.institution.products}")
    @GetMapping(value = "/{externalId}/products")
    public ResponseEntity<OnboardedProducts> retrieveInstitutionProductsByExternalId(@PathVariable("externalId") String externalId,
                                                                                     @RequestParam(value = "states", required = false) List<RelationshipState> states) {
        log.info("Retrieving products for institution having externalId {}", externalId);
        CustomExceptionMessage.setCustomMessage(GET_PRODUCTS_ERROR);
        List<Onboarding> onboardings = externalService.retrieveInstitutionProductsByExternalId(externalId, states);
        return ResponseEntity.ok(InstitutionMapper.toOnboardedProducts(onboardings));
    }

    /**
     * The function returns the relationships related to the institution
     *
     * @param externalId String
     *
     * @return GeographicTaxonomies
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.external.institution.relationships}", notes = "${swagger.mscore.external.institution.relationships}")
    @GetMapping(value = "/{externalId}/relationships")
    public ResponseEntity<List<RelationshipResult>> getUserInstitutionRelationshipsByExternalId(@PathVariable("externalId") String externalId,
                                                                                                @RequestParam(value = "personId", required = false) String personId,
                                                                                                @RequestParam(value = "roles", required = false) List<PartyRole> roles,
                                                                                                @RequestParam(value = "states", required = false) List<RelationshipState> states,
                                                                                                @RequestParam(value = "products", required = false) List<String> products,
                                                                                                @RequestParam(value = "productRoles", required = false) List<String> productRoles,
                                                                                                Authentication authentication) {
        log.info("Getting relationship for institution {} and current user", externalId);
        CustomExceptionMessage.setCustomMessage(RETRIEVING_USER_RELATIONSHIP_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        List<RelationshipInfo> response = externalService.getUserInstitutionRelationships(externalId, selfCareUser.getId(), personId, roles, states, products, productRoles);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipResultList(response));
    }

    /**
     * The function persist PG institution
     *
     * @param request CreatePnPgInstitutionRequest
     * @return InstitutionPnPgResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionId
     * * Code: 400, Message: Bad Request, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.PG.create}", notes = "${swagger.mscore.institution.PG.create}")
    @PostMapping(value = "/pn-pg")
    public ResponseEntity<InstitutionPnPgResponse> createPnPgInstitution(@RequestBody @Valid CreatePnPgInstitutionRequest request) {
        CustomExceptionMessage.setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = externalService.createPnPgInstitution(request.getTaxId(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(new InstitutionPnPgResponse(saved.getId()));
    }
}
