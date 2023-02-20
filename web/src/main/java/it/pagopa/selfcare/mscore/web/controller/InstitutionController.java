package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
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

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Institution")
@Slf4j
public class InstitutionController {

    private static final String ENTRY_LOG = "Creating institution having external id {}";
    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    /**
     * The function persist PA institution
     *
     * @param externalId String
     *
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: TokenId
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PA.create}")
    @PostMapping(value = "/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                             @PathVariable("externalId") String externalId) {

        log.info(ENTRY_LOG, externalId);
        CustomExceptionMessage.setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist institution manually
     *
     * @param externalId  String
     * @param institution InstitutionRequest
     *
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.create}")
    @PostMapping(value = "/insert/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionRaw(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                    @PathVariable("externalId") String externalId,
                                                                    @RequestBody @Valid InstitutionRequest institution) {
        log.info(ENTRY_LOG, externalId);
        CustomExceptionMessage.setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionRaw(InstitutionMapper.toInstitution(institution, externalId), externalId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist PG institution
     *
     * @param externalId String
     * @param existsInRegistry boolean
     *
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: TokenId
     * * Code: 404, Message: Institution data not found on InfoCamere, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PG.create}")
    @PostMapping(value = "/pg/{externalId}")
    public ResponseEntity<InstitutionResponse> createPgInstitution(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                   @PathVariable("externalId") String externalId,
                                                                   @RequestParam(value = "manual") boolean existsInRegistry,
                                                                   Authentication authentication) {

        log.info(ENTRY_LOG, externalId);
        CustomExceptionMessage.setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createPgInstitution(externalId, existsInRegistry, (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function return products related to institution
     *
     * @param institutionId String
     * @param states List<String
     *
     * @return OnboardedProducts
     * * Code: 200, Message: successful operation, DataType: OnboardedProducts
     * * Code: 404, Message: Products not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.products}")
    @GetMapping(value = "/{id}/products")
    public ResponseEntity<OnboardedProducts> retrieveInstitutionProducts(@PathVariable("id") String institutionId,
                                                                         @RequestParam(value = "states", required = false) List<String> states) {

        log.info("Retrieving products for institution {}", institutionId);
        CustomExceptionMessage.setCustomMessage(GET_PRODUCTS_ERROR);
        List<Onboarding> list = institutionService.retrieveInstitutionProducts(institutionId, states);
        return ResponseEntity.ok(InstitutionMapper.toOnboardedProducts(list));
    }
}
