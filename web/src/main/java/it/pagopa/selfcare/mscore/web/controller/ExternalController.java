package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionBillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionManagerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;
import static it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage.setCustomMessage;

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
     * The function return institutionData from its externalId
     *
     * @param  id externalId
     *
     * @return InstitutionResponse
     *
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution not found, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.external.institution}")
    @GetMapping("/{externalId}")
    public ResponseEntity<InstitutionResponse> getByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                               @PathVariable("externalId") String id) {
        setCustomMessage(GET_INSTITUTION_BY_EXTERNAL_ID_ERROR);
        Institution institution = externalService.getInstitutionByExternalId(id);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(institution));
    }


    /**
     * The function return institutionData from its externalId
     *
     * @param externalId String
     * @param productId String
     *
     * @return InstitutionManagerResponse
     *
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution Manager not found, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.external.institution.manager}")
    @GetMapping(value = "/{externalId}/products/{productId}/manager")
    public ResponseEntity<InstitutionManagerResponse> getManagerInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                        @PathVariable("externalId") String externalId,
                                                                                        @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                                        @PathVariable("productId") String productId) {
        log.info("Getting manager for institution having externalId {}", externalId);
        setCustomMessage(GET_INSTITUTION_MANAGER_ERROR);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        OnboardedUser manager = externalService.getInstitutionManager(institution, productId);
        String contractId = externalService.getRelationShipToken(institution.getId(), manager.getUser(), productId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionManagerResponse(institution, manager, productId, contractId));
    }


    /**
     * The function return institutionData from its externalId
     *
     * @param externalId String
     * @param productId String
     *
     * @return InstitutionBillingResponse
     *
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution Billing not found, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.external.institution.billing}")
    @GetMapping(value = "/{externalId}/products/{productId}/billing")
    public ResponseEntity<InstitutionBillingResponse> getBillingInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                        @PathVariable("externalId") String externalId,
                                                                                        @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                                        @PathVariable("productId") String productId) {
        log.info("Retrieving billing data for institution having externalId {} and productId {}", externalId, productId);
        setCustomMessage(GET_INSTITUTION_BILLING_ERROR);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        institution = externalService.getBillingByExternalId(institution, productId);
        return ResponseEntity.ok().body(InstitutionMapper.toBillingResponse(institution, productId));
    }

    /**
     * The function return geographic taxonomies related to institution
     *
     * @param externalId String
     *
     * @return GeographicTaxonomies
     *
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.external.geotaxonomies}")
    @GetMapping(value = "/institutions/{externalId}/geotaxonomies")
    public ResponseEntity<List<GeographicTaxonomies>> retrieveInstitutionGeoTaxonomiesByExternalId(@PathVariable("externalId") String externalId) {

        setCustomMessage(RETRIEVE_GEO_TAXONOMIES_ERROR);
        List<GeographicTaxonomies> list = externalService.retrieveInstitutionGeoTaxonomiesByExternalId(externalId);
        return ResponseEntity.ok(list);
    }
}
