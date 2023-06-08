package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.ValidInstitution;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingResourceMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Institution")
@Slf4j
public class InstitutionController {
    private final InstitutionService institutionService;
    private final OnboardingResourceMapper onboardingResourceMapper;

    public InstitutionController(InstitutionService institutionService,
                                 OnboardingResourceMapper onboardingResourceMapper) {
        this.institutionService = institutionService;
        this.onboardingResourceMapper = onboardingResourceMapper;
    }

    /**
     * Gets institutions filtering by taxCode and/or subunitCode
     *
     * @param taxCode String
     * @param subunitCode String
     * @return OnboardedProducts
     * * Code: 200, Message: successful operation, DataType: OnboardedProducts
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 404, Message: Products not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institutions}", notes = "${swagger.mscore.institutions}")
    @GetMapping(value = "/")
    public ResponseEntity<InstitutionsResponse> getInstitutions(@ApiParam("${swagger.mscore.institutions.model.taxCode}")
                                                             @RequestParam(value = "taxCode") String taxCode,
                                                             @ApiParam("${swagger.mscore.institutions.model.subunitCode}")
                                                             @RequestParam(value = "subunitCode", required = false) String subunitCode) {

        CustomExceptionMessage.setCustomMessage(GenericError.GET_INSTITUTION_BY_ID_ERROR);
        List<Institution> institutions = institutionService.getInstitutions(taxCode, subunitCode);
        InstitutionsResponse institutionsResponse = new InstitutionsResponse();
        institutionsResponse.setInstitutions(institutions.stream()
                        .map(InstitutionMapper::toInstitutionResponse)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(institutionsResponse);
    }

    /**
     * The function create an institution retriving values from IPA
     *
     * @param institutionFromIpaPost InstitutionPost
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionResponse
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.create.from-ipa}", notes = "${swagger.mscore.institution.create.from-ipa}")
    @PostMapping(value = "/from-ipa/")
    public ResponseEntity<InstitutionResponse> createInstitutionFromIpa( @RequestBody @Valid InstitutionFromIpaPost institutionFromIpaPost) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);

        if (Objects.isNull(institutionFromIpaPost.getSubunitType()) && Objects.nonNull(institutionFromIpaPost.getSubunitCode())) {
            throw new ValidationException("subunitCode and subunitType must both be evaluated.");
        }

        Institution saved = institutionService.createInstitutionFromIpa(institutionFromIpaPost.getTaxCode(),
                institutionFromIpaPost.getSubunitType(), institutionFromIpaPost.getSubunitCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist PA institution
     *
     * @param externalId String
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionResponse
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.PA.create}", notes = "${swagger.mscore.institution.PA.create}")
    @PostMapping(value = "/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                             @PathVariable("externalId") String externalId) {

        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist institution manually
     *
     * @param externalId  String
     * @param institution InstitutionRequest
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: InstitutionResponse
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.create}", notes = "${swagger.mscore.institution.create}")
    @PostMapping(value = "/insert/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionRaw(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                    @PathVariable("externalId") String externalId,
                                                                    @RequestBody @Valid InstitutionRequest institution) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionRaw(InstitutionMapper.toInstitution(institution, externalId), externalId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist PG institution
     *
     * @param request CreatePgInstitutionRequest
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionResponse
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 404, Message: Institution data not found on InfoCamere, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.PG.create}", notes = "${swagger.mscore.institution.PG.create}")
    @PostMapping(value = "/pg")
    public ResponseEntity<InstitutionResponse> createPgInstitution(@RequestBody @Valid CreatePgInstitutionRequest request,
                                                                   Authentication authentication) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createPgInstitution(request.getTaxId(), request.getDescription(), request.isExistsInRegistry(), (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function return products related to institution
     *
     * @param institutionId String
     * @param states        List<String>
     * @return OnboardedProducts
     * * Code: 200, Message: successful operation, DataType: OnboardedProducts
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 404, Message: Products not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.products}", notes = "${swagger.mscore.institution.products}")
    @GetMapping(value = "/{id}/products")
    public ResponseEntity<OnboardedProducts> retrieveInstitutionProducts(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                         @PathVariable("id") String institutionId,
                                                                         @ApiParam("${swagger.mscore.institutions.model.relationshipState}")
                                                                         @RequestParam(value = "states", required = false) List<RelationshipState> states) {

        CustomExceptionMessage.setCustomMessage(GenericError.GET_PRODUCTS_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        List<Onboarding> page = institutionService.retrieveInstitutionProducts(institution, states);
        return ResponseEntity.ok(InstitutionMapper.toOnboardedProducts(page));
    }

    /**
     * The function Update the corresponding institution given internal institution id
     *
     * @param institutionId  String
     * @param institutionPut InstitutionPut
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: InstitutionResponse
     * * Code: 400, Message: bad request, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.update}", notes = "${swagger.mscore.institution.update}")
    @PutMapping(value = "/{id}")
    public ResponseEntity<InstitutionResponse> updateInstitution(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                 @PathVariable("id") String institutionId,
                                                                 @RequestBody InstitutionPut institutionPut,
                                                                 Authentication authentication
                                                                 ) {

        CustomExceptionMessage.setCustomMessage(GenericError.PUT_INSTITUTION_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        Institution saved = institutionService.updateInstitution(institutionId, InstitutionMapper.toInstitutionUpdate(institutionPut), selfCareUser.getId());
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function return geographic taxonomies related to institution
     *
     * @param id String
     * @return List
     * * Code: 200, Message: successful operation, DataType: List<GeographicTaxonomies></GeographicTaxonomies>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.geotaxonomies}", notes = "${swagger.mscore.institution.geotaxonomies}")
    @GetMapping(value = "/{id}/geotaxonomies")
    public ResponseEntity<List<GeographicTaxonomies>> retrieveInstitutionGeoTaxonomies(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                                       @PathVariable("id") String id) {

        CustomExceptionMessage.setCustomMessage(GenericError.RETRIEVE_GEO_TAXONOMIES_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(id);
        List<GeographicTaxonomies> geo = institutionService.retrieveInstitutionGeoTaxonomies(institution);
        return ResponseEntity.ok(geo);
    }

    /**
     * The function return an institution given institution internal id
     *
     * @param id String
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: InstitutionResponse
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution}", notes = "${swagger.mscore.institution}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstitutionResponse> retrieveInstitutionById(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                       @PathVariable("id") String id) {
        CustomExceptionMessage.setCustomMessage(GenericError.GET_INSTITUTION_BY_ID_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(id);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(institution));
    }

    /**
     * The function return user institution relationships
     *
     * @param institutionId String
     * @param personId      String
     * @param roles         String[]
     * @param states        String[]
     * @param products      String[]
     * @param productRoles  String[]
     * @return List
     * * Code: 200, Message: successful operation, DataType: List<RelationshipResult>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.relationships}", notes = "${swagger.mscore.institution.relationships}")
    @GetMapping(value = "/{id}/relationships")
    public ResponseEntity<List<RelationshipResult>> getUserInstitutionRelationships(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                                    @PathVariable("id") String institutionId,
                                                                                    @RequestParam(value = "personId", required = false) String personId,
                                                                                    @RequestParam(value = "roles", required = false) List<PartyRole> roles,
                                                                                    @RequestParam(value = "states", required = false) List<RelationshipState> states,
                                                                                    @RequestParam(value = "products", required = false) List<String> products,
                                                                                    @RequestParam(value = "productRoles", required = false) List<String> productRoles,
                                                                                    Authentication authentication) {
        CustomExceptionMessage.setCustomMessage(GenericError.GET_USER_INSTITUTION_RELATIONSHIP_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        List<RelationshipInfo> relationshipInfoList = institutionService.retrieveUserInstitutionRelationships(institution, selfCareUser.getId(), personId, roles, states, products, productRoles);
        return ResponseEntity.ok().body(RelationshipMapper.toRelationshipResultList(relationshipInfoList));
    }



    /**
     * Get list of onboarding for a certain productId
     *
     * @param institutionId String
     * @param productId      String
     * @return List
     * * Code: 200, Message: successful operation, DataType: List<RelationshipResult>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.info}", notes = "${swagger.mscore.institution.info}")
    @GetMapping(value = "/{institutionId}/onboardings")
    public ResponseEntity<OnboardingsResponse> getOnboardingsInstitution(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                                    @PathVariable("institutionId") String institutionId,
                                                                                    @RequestParam(value = "productId", required = false) String productId) {
        CustomExceptionMessage.setCustomMessage(GenericError.GETTING_ONBOARDING_INFO_ERROR);
        List<Onboarding> onboardings = institutionService.getOnboardingInstitutionByProductId(institutionId, productId);
        OnboardingsResponse onboardingsResponse = new OnboardingsResponse();
        onboardingsResponse.setOnboardings(onboardings.stream()
                .map(onboardingResourceMapper::toResponse)
                .collect(Collectors.toList()));
        return ResponseEntity.ok().body(onboardingsResponse);
    }

    /**
     * The function return a List of Institution that user can onboard
     *
     * @param institutions List<CreatePnPgInstitutionRequest>
     * @return List
     * * Code: 200, Message: successful operation, DataType: List<RelationshipResult>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institutions.valid}", notes = "${swagger.mscore.institutions.valid}")
    @PostMapping(value = "/onboarded/{productId}")
    public ResponseEntity<List<InstitutionToOnboard>> getValidInstitutionToOnboard(@RequestBody List<InstitutionToOnboard> institutions,
                                                                                   @PathVariable(value = "productId") String productId) {
        List<ValidInstitution> validInstitutions = institutionService.retrieveInstitutionByExternalIds(InstitutionMapper.toValidInstitutions(institutions), productId);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionToOnboardList(validInstitutions));
    }

    /**
     * The function updates the field createdAt of the OnboardedProduct, the related Token and UserBindings for the given institution-product pair
     *
     * @param institutionId String
     * @param productId     String
     * @param createdAt     OffsetDateTime
     * @return no content
     * * Code: 200, Message: successful operation
     * * Code: 404, Message: Institution or Token or UserBinding not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institutions.updateCreatedAt}", notes = "${swagger.mscore.institutions.updateCreatedAt}")
    @PutMapping(value = "/{institutionId}/products/{productId}/createdAt")
    public ResponseEntity<Void> updateCreatedAt(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                @PathVariable("institutionId") String institutionId,
                                                @ApiParam("${swagger.mscore.product.model.id}")
                                                @PathVariable("productId") String productId,
                                                @ApiParam("${swagger.mscore.institutions.model.createdAt}")
                                                @RequestParam(value = "createdAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime createdAt) {
        log.trace("updateCreatedAt start");
        log.debug("updateCreatedAt institutionId = {}, productId = {}, createdAt = {}", institutionId, productId, createdAt);
        if (createdAt.compareTo(OffsetDateTime.now()) > 0) {
            throw new ValidationException("Invalid createdAt date: the createdAt date must be prior to the current date.");
        }
        institutionService.updateCreatedAt(institutionId, productId, createdAt);
        log.trace("updateCreatedAt end");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
