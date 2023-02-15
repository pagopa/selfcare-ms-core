package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipsResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.ProductMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.*;
import static it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage.setCustomMessage;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Institution")
@Slf4j
public class InstitutionController {

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
     *
     * * Code: 201, Message: successful operation, DataType: TokenId
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PA.create}")
    @PostMapping(value = "/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                             @PathVariable("externalId") String externalId) {

        log.info("Creating institution having external id {}", externalId);
        setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist institution manually
     *
     * @param externalId String
     * @param institution InstitutionRequest
     *
     * @return InstitutionResponse
     *
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 409, Message: Institution conflict, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.create}")
    @PostMapping(value = "/insert/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionRaw(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                    @PathVariable("externalId") String externalId,
                                                                    @RequestBody @Valid InstitutionRequest institution) {
        log.info("Creating institution having external id {}", externalId);
        setCustomMessage(CREATE_INSTITUTION_ERROR);
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
     *
     * * Code: 201, Message: successful operation, DataType: TokenId
     * * Code: 404, Message: Institution data not found on InfoCamere, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PG.create}")
    @PostMapping(value = "/pg/{externalId}")
    public ResponseEntity<InstitutionResponse> createPgInstitution(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                   @PathVariable("externalId") String externalId,
                                                                   @RequestParam(value = "manual") boolean existsInRegistry,
                                                                   Authentication authentication) {

        log.info("Creating institution having external id {}", externalId);
        setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createPgInstitution(externalId, existsInRegistry, (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function return products related to institution
     *
     * @param id String
     * @param states List<String
     *
     * @return OnboardedProducts
     *
     * * Code: 200, Message: successful operation, DataType: OnboardedProducts
     * * Code: 404, Message: Products not found, DataType: Problem
     *
     * @docauthor Trelent
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.products}")
    @GetMapping(value = "/{id}/products")
    public ResponseEntity<OnboardedProducts> retrieveInstitutionProducts(@PathVariable("id") String id,
                                                                         @RequestParam(value = "states", required = false) List<String> states) {

        setCustomMessage(GET_PRODUCTS_ERROR);
        List<Onboarding> list = institutionService.retrieveInstitutionProducts(id, states);
        return ResponseEntity.ok(ProductMapper.toOnboardedProducts(list.stream()
                .map(ProductMapper::toResource)
                .collect(Collectors.toList())));
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.find}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstitutionResponse> retrieveInstitutionById(@PathVariable("id") String id) {
        setCustomMessage(GET_INSTITUTION_BY_ID_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(id);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(institution));
    }


    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.relationships}")
    @GetMapping(value = "/{id}/relationships")
    public ResponseEntity<RelationshipsResponse> getUserInstitutionRelationships(@PathVariable("id") String institutionId,
                                                                                 @RequestParam(value = "personId", required = false) String uuid,
                                                                                 @RequestParam(value = "roles", required = false) List<String> roles,
                                                                                 @RequestParam(value = "states", required = false) List<String> states,
                                                                                 @RequestParam(value = "products", required = false) List<String> products,
                                                                                 @RequestParam(value = "productRoles", required = false) List<String> productRoles) {
        log.info("Getting relationship for institution {} and current user", institutionId);
        setCustomMessage(GET_INSTITUTION_BY_ID_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        List<OnboardedUser> onboardedUsers = institutionService.getUserInstitutionRelationships(institution, uuid, roles, states);
        return ResponseEntity.ok().body(InstitutionMapper.toRelationshipResponse(institution, onboardedUsers, products, productRoles));
    }
}
