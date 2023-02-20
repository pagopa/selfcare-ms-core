package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionBillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionManagerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;

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
     * The function returns institutionData from its externalId
     *
     * @param  externalId externalId
     *
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Invalid ID supplied, DataType: Problem
     * * Code: 404, Message: Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.external.institution}")
    @GetMapping("/{externalId}")
    public ResponseEntity<InstitutionResponse> getByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                               @PathVariable("externalId") String externalId) {
        log.info("Retrieving institution for externalId {}", externalId);
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_EXTERNAL_ID_ERROR);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(institution));
    }


    /**
     * The function returns institutionData from its externalId
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
    @ApiOperation(value = "", notes = "${swagger.mscore.external.institution.manager}")
    @GetMapping(value = "/{externalId}/products/{productId}/manager")
    public ResponseEntity<InstitutionManagerResponse> getManagerInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                        @PathVariable("externalId") String externalId,
                                                                                        @ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                                        @PathVariable("productId") String productId) {
        log.info("Getting manager for institution having externalId {}", externalId);
        CustomExceptionMessage.setCustomMessage(INSTITUTION_MANAGER_ERROR);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        OnboardedUser manager = externalService.retrieveInstitutionManager(institution, productId);
        String contractId = externalService.retrieveRelationship(institution.getId(), manager.getId(), productId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionManagerResponse(institution, manager, productId, contractId));
    }


    /**
     * The function returns institutionData from its externalId
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
    @ApiOperation(value = "", notes = "${swagger.mscore.external.institution.billing}")
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
}
