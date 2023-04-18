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
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.CreatePnPgInstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionBillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionManagerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionPnPgResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.GET_INSTITUTION_BY_EXTERNAL_ID_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.GET_INSTITUTION_BY_ID_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.GET_PRODUCTS_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.INSTITUTION_BILLING_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.INSTITUTION_MANAGER_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.RETRIEVE_GEO_TAXONOMIES_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.RETRIEVING_USER_RELATIONSHIP_ERROR;

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
     * * Code: 200, Message: successful operation, DataType: InstitutionManagerResponse
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
     * * Code: 200, Message: successful operation, DataType: InstitutionBillingResponse
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
     * * Code: 200, Message: successful operation, DataType: OnboardedProducts
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution Billing not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.external.institution.products}", notes = "${swagger.mscore.external.institution.products}")
    @GetMapping(value = "/{externalId}/products")
    public ResponseEntity<OnboardedProducts> retrieveInstitutionProductsByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                     @PathVariable("externalId") String externalId,
                                                                                     @RequestParam(value = "states", required = false) List<RelationshipState> states) {
        CustomExceptionMessage.setCustomMessage(GET_PRODUCTS_ERROR);
        List<Onboarding> page = externalService.retrieveInstitutionProductsByExternalId(externalId, states);
        return ResponseEntity.ok(InstitutionMapper.toOnboardedProducts(page));
    }

    /**
     * The function returns geographic taxonomies related to institution
     *
     * @param externalId String
     *
     * @return List
     * * Code: 200, Message: successful operation, DataType: List<GeographicTaxonomies>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.external.geotaxonomies}", notes = "${swagger.mscore.external.geotaxonomies}")
    @GetMapping(value = "/{externalId}/geotaxonomies")
    public ResponseEntity<List<GeographicTaxonomies>> retrieveInstitutionGeoTaxonomiesByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                                   @PathVariable("externalId") String externalId) {
        CustomExceptionMessage.setCustomMessage(RETRIEVE_GEO_TAXONOMIES_ERROR);
        List<GeographicTaxonomies> list = externalService.retrieveInstitutionGeoTaxonomiesByExternalId(externalId);
        return ResponseEntity.ok(list);
    }

    /**
     * The function returns the relationships related to the institution
     *
     * @param externalId   String
     * @param personId     String
     * @param roles        List
     * @param states       List
     * @param products     List
     * @param productRoles List
     *
     * @return List
     * * Code: 200, Message: successful operation, DataType: List<RelationshipResult>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.external.institution.relationships}", notes = "${swagger.mscore.external.institution.relationships}")
    @GetMapping(value = "/{externalId}/relationships")
    public ResponseEntity<List<RelationshipResult>> getUserInstitutionRelationshipsByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}") @PathVariable("externalId") String externalId,
                                                                                                @RequestParam(value = "personId", required = false) String personId,
                                                                                                @RequestParam(value = "roles", required = false) List<PartyRole> roles,
                                                                                                @RequestParam(value = "states", required = false) List<RelationshipState> states,
                                                                                                @RequestParam(value = "products", required = false) List<String> products,
                                                                                                @RequestParam(value = "productRoles", required = false) List<String> productRoles,
                                                                                                Authentication authentication) {
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
     *
     * * Code: 201, Message: successful operation, DataType: InstitutionPnPgResponse
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

    /**
     * The function return an institution given institution internal id
     *
     * @param ids List
     * @return List
     *
     * * Code: 200, Message: successful operation, DataType: List<InstitutionResponse>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution}", notes = "${swagger.mscore.institution}")
    @GetMapping(value = "")
    public ResponseEntity<List<InstitutionResponse>> retrieveInstitutionByIds(@ApiParam("${swagger.mscore.institutions.model.internalIds}")
                                                                                  @RequestParam("ids") List<String> ids) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_ID_ERROR);
        List<Institution> institution = externalService.retrieveInstitutionByIds(ids);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponseList(institution));
    }
}
