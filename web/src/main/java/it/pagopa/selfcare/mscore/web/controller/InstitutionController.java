package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.UserInfo;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.mapper.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Institution")
@Slf4j
public class InstitutionController {

    private final InstitutionService institutionService;
    private final OnboardingService onboardingService;
    private final OnboardingResourceMapper onboardingResourceMapper;
    private final InstitutionResourceMapper institutionResourceMapper;
    private final BrokerMapper brokerMapper;
    private final UserMapper userMapper;

    public InstitutionController(InstitutionService institutionService,
                                 OnboardingService onboardingService, OnboardingResourceMapper onboardingResourceMapper,
                                 InstitutionResourceMapper institutionResourceMapper,
                                 BrokerMapper brokerMapper,
                                 UserMapper userMapper) {
        this.institutionService = institutionService;
        this.onboardingService = onboardingService;
        this.onboardingResourceMapper = onboardingResourceMapper;
        this.institutionResourceMapper = institutionResourceMapper;
        this.userMapper = userMapper;
        this.brokerMapper = brokerMapper;
    }

    /**
     * Gets institutions filtering by taxCode and/or subunitCode and/or origin and/or originId
     *
     * @param taxCode     String
     * @param subunitCode String
     * @param origin      String
     * @param originId    originId
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: OnboardedProducts
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 404, Message: Products not found, DataType: Problem
     */
    @Tags({@Tag(name = "support"), @Tag(name = "external-v2"), @Tag(name = "Institution")})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institutions}", notes = "${swagger.mscore.institutions}")
    @GetMapping
    public ResponseEntity<InstitutionsResponse> getInstitutions(@ApiParam("${swagger.mscore.institutions.model.taxCode}")
                                                                @RequestParam(value = "taxCode", required = false) String taxCode,
                                                                @ApiParam("${swagger.mscore.institutions.model.subunitCode}")
                                                                @RequestParam(value = "subunitCode", required = false) String subunitCode,
                                                                @RequestParam(value = "origin", required = false) String origin,
                                                                @RequestParam(value = "originId", required = false) String originId) {


        if (!StringUtils.hasText(taxCode) && !StringUtils.hasText(originId) && !StringUtils.hasText(origin)) {
            throw new ValidationException("At least one of taxCode, origin or originId must be present");
        } else if (StringUtils.hasText(subunitCode) && !StringUtils.hasText(taxCode)) {
            throw new ValidationException("TaxCode is required if subunitCode is present");
        }

        CustomExceptionMessage.setCustomMessage(GenericError.GET_INSTITUTION_BY_ID_ERROR);

        List<Institution> institutions = institutionService.getInstitutions(taxCode, subunitCode, origin, originId);
        InstitutionsResponse institutionsResponse = new InstitutionsResponse();
        institutionsResponse.setInstitutions(institutions.stream()
                .map(institutionResourceMapper::toInstitutionResponse)
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
    @PostMapping(value = "/from-ipa")
    public ResponseEntity<InstitutionResponse> createInstitutionFromIpa(@RequestBody @Valid InstitutionFromIpaPost institutionFromIpaPost) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);

        if (Objects.isNull(institutionFromIpaPost.getSubunitType()) && Objects.nonNull(institutionFromIpaPost.getSubunitCode())) {
            throw new ValidationException("subunitCode and subunitType must both be evaluated.");
        }

        List<InstitutionGeographicTaxonomies> geographicTaxonomies = Optional.ofNullable(institutionFromIpaPost.getGeographicTaxonomies())
                .map(geoTaxonomies -> geoTaxonomies.stream().map(institutionResourceMapper::toInstitutionGeographicTaxonomies).toList())
                .orElse(List.of());

        Institution saved = institutionService.createInstitutionFromIpa(institutionFromIpaPost.getTaxCode(),
                institutionFromIpaPost.getSubunitType(), institutionFromIpaPost.getSubunitCode(), geographicTaxonomies, institutionFromIpaPost.getInstitutionType());
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
    }

    /**
     * The function create an institution retriving values from ANAC
     *
     * @param institution InstitutionRequest
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionResponse
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.create.from-anac}", notes = "${swagger.mscore.institution.create.from-anac}")
    @PostMapping(value = "/from-anac")
    public ResponseEntity<InstitutionResponse> createInstitutionFromAnac(@RequestBody @Valid InstitutionRequest institution) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionFromAnac(InstitutionMapperCustom.toInstitution(institution, null));
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
    }

    /**
     * The function create an institution retriving values from IVASS
     *
     * @param institution InstitutionRequest
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionResponse
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.create.from-ivass}", notes = "${swagger.mscore.institution.create.from-ivass}")
    @PostMapping(value = "/from-ivass")
    public ResponseEntity<InstitutionResponse> createInstitutionFromIvass(@RequestBody @Valid InstitutionRequest institution) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionFromIvass(InstitutionMapperCustom.toInstitution(institution, null));
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
    }

    /**
     * The function create an institution retriving values from IPA
     *
     * @param institutionRequest InstitutionRequest
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionResponse
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.create.from-pda}", notes = "${swagger.mscore.institution.create.from-ipa}")
    @PostMapping(value = "/from-pda")
    public ResponseEntity<InstitutionResponse> createInstitutionFromPda(@RequestBody @Valid PdaInstitutionRequest institutionRequest) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);

        Institution saved = institutionService.createInstitutionFromPda(InstitutionMapperCustom.toInstitution(institutionRequest, null), institutionRequest.getInjectionInstitutionType());
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
    }

    /**
     * The function create an institution retriving values from INFOCAMERE
     *
     * @param institutionRequest InstitutionRequest
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: InstitutionResponse
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.create.from-infocamere}", notes = "${swagger.mscore.institution.create.from-infocamere}")
    @PostMapping(value = "/from-infocamere")
    public ResponseEntity<InstitutionResponse> createInstitutionFromInfocamere(@RequestBody @Valid InstitutionRequest institutionRequest) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);

        Institution saved = institutionService.createInstitutionFromInfocamere(InstitutionMapperCustom.toInstitution(institutionRequest, null));
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
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
    @Deprecated
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.PA.create}", notes = "${swagger.mscore.institution.PA.create}")
    @PostMapping(value = "/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                             @PathVariable("externalId") String externalId) {

        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist institution
     *
     * @param institution InstitutionRequest
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: InstitutionResponse
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.create}", notes = "${swagger.mscore.institution.create}")
    @PostMapping
    public ResponseEntity<InstitutionResponse> createInstitution(@RequestBody @Valid InstitutionRequest institution) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitution(InstitutionMapperCustom.toInstitution(institution, null));
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
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
    @Deprecated
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.create}", notes = "${swagger.mscore.institution.create}")
    @PostMapping(value = "/insert/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionRaw(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                    @PathVariable("externalId") String externalId,
                                                                    @RequestBody @Valid InstitutionRequest institution) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitution(InstitutionMapperCustom.toInstitution(institution, null));
        return ResponseEntity.ok(institutionResourceMapper.toInstitutionResponse(saved));
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
        return ResponseEntity.status(HttpStatus.CREATED).body(institutionResourceMapper.toInstitutionResponse(saved));
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
        return ResponseEntity.ok(InstitutionMapperCustom.toOnboardedProducts(page));
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
        Institution saved = institutionService.updateInstitution(institutionId, InstitutionMapperCustom.toInstitutionUpdate(institutionPut), selfCareUser.getId());
        return ResponseEntity.ok().body(institutionResourceMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist user on registry if not exists and add relation with institution-product
     *
     * @param request OnboardingInstitutionUsersRequest
     * @return no content
     * * Code: 204, Message: successful operation
     * * Code: 404, Message: Not found, DataType: Problem
     * * Code: 400, Message: Invalid request, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.onboarding.users}", notes = "${swagger.mscore.onboarding.users}")
    @PostMapping(value = "/{id}/onboarding")
    public ResponseEntity<InstitutionResponse> onboardingInstitution(@RequestBody @Valid InstitutionOnboardingRequest request,
                                                                     @PathVariable("id") String id) {
        CustomExceptionMessage.setCustomMessage(GenericError.ONBOARDING_OPERATION_ERROR);
        List<UserToOnboard> usersToOnboard = Optional.ofNullable(request.getUsers())
                .map(users -> users.stream().map(userMapper::toUserToOnboard).toList())
                .orElse(List.of());
        Institution institution = onboardingService.persistOnboarding(id, request.getProductId(), usersToOnboard, onboardingResourceMapper.toOnboarding(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(institutionResourceMapper.toInstitutionResponse(institution));
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
    @Tags({@Tag(name = "external-v2"), @Tag(name = "Institution")})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution}", notes = "${swagger.mscore.institution}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstitutionResponse> retrieveInstitutionById(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                       @PathVariable("id") String id) {
        CustomExceptionMessage.setCustomMessage(GenericError.GET_INSTITUTION_BY_ID_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(id);
        return ResponseEntity.ok().body(institutionResourceMapper.toInstitutionResponse(institution));
    }


    /**
     * Get list of onboarding for a certain productId
     *
     * @param institutionId String
     * @param productId     String
     * @return List
     * * Code: 200, Message: successful operation, DataType: List<RelationshipResult>
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @Tags({@Tag(name = "external-v2"), @Tag(name = "Institution")})
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
        List<ValidInstitution> validInstitutions = institutionService.retrieveInstitutionByExternalIds(InstitutionMapperCustom.toValidInstitutions(institutions), productId);
        return ResponseEntity.ok().body(InstitutionMapperCustom.toInstitutionToOnboardList(validInstitutions));
    }

    /**
     * The function updates the field createdAt of the OnboardedProduct, the related Token and UserBindings for the given institution-product pair
     *
     * @param institutionId String
     * @param createdAtRequest     CreatedAtRequest
     * @return no content
     * * Code: 200, Message: successful operation
     * * Code: 404, Message: Institution or Token or UserBinding not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institutions.updateCreatedAt}", notes = "${swagger.mscore.institutions.updateCreatedAt}")
    @PutMapping(value = "/{institutionId}/createdAt")
    public ResponseEntity<Void> updateCreatedAt(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                @PathVariable("institutionId") String institutionId,
                                                @Valid @RequestBody CreatedAtRequest createdAtRequest) {
        log.trace("updateCreatedAt start");
        log.debug("updateCreatedAt institutionId = {}, productId = {}, createdAt = {}", institutionId, createdAtRequest.getProductId(), createdAtRequest.getCreatedAt());
        if (createdAtRequest.getCreatedAt().isAfter(OffsetDateTime.now())) {
            throw new ValidationException("Invalid createdAt date: the createdAt date must be prior to the current date.");
        }
        institutionService.updateCreatedAt(institutionId, createdAtRequest.getProductId(), createdAtRequest.getCreatedAt(), createdAtRequest.getActivatedAt());
        log.trace("updateCreatedAt end");
        return ResponseEntity.status(HttpStatus.OK).build();
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
    @ApiOperation(value = "${swagger.mscore.institutions.findFromProduct}", notes = "${swagger.mscore.institutions.findFromProduct}")
    @GetMapping(value = "/products/{productId}")
    public ResponseEntity<InstitutionOnboardingListResponse> findFromProduct(@ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                             @PathVariable(value = "productId") String productId,
                                                                             @ApiParam("${swagger.mscore.page.number}")
                                                                             @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                             @ApiParam("${swagger.mscore.page.size}")
                                                                             @RequestParam(name = "size", defaultValue = "100") Integer size) {
        log.trace("findFromProduct start");
        log.debug("findFromProduct productId = {}", productId);
        List<Institution> institutions = institutionService.getInstitutionsByProductId(productId, page, size);

        InstitutionOnboardingListResponse institutionListResponse = new InstitutionOnboardingListResponse(
                institutions.stream()
                        .map(InstitutionMapperCustom::toInstitutionOnboardingResponse)
                        .collect(Collectors.toList()));

        log.trace("findFromProduct end");
        return ResponseEntity.ok().body(institutionListResponse);
    }

    @GetMapping(value = "/{productId}/brokers/{institutionType}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institutions.brokers}", notes = "${swagger.mscore.institutions.getInstitutionBrokers}")
    public Collection<BrokerResponse> getInstitutionBrokers(@ApiParam("${swagger.mscore.institutions.model.productId}")
                                                            @PathVariable("productId")
                                                            String productId,
                                                            @ApiParam("${swagger.mscore.institutions.model.type}")
                                                            @PathVariable("institutionType")
                                                            InstitutionType institutionType) {
        log.trace("getInstitutionBrokers start");
        log.debug("productId = {}, institutionType = {}", productId, institutionType);
        List<Institution> institutions = institutionService.getInstitutionBrokers(productId, institutionType);
        List<BrokerResponse> result = brokerMapper.toBrokers(institutions);
        log.debug("getInstitutionBrokers result = {}", result);
        log.trace("getInstitutionBrokers end");
        return result;
    }

    @Tags({@Tag(name = "support"), @Tag(name = "Institution")})
    @GetMapping(value = "/{institutionId}/users")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institutions.api.getInstitutionUsers}")
    public List<UserInfoResponse> getInstitutionUsers(@ApiParam("${swagger.mscore.institutions.model.id}")
                                                      @PathVariable("institutionId")
                                                      String institutionId) {

        log.trace("getInstitutionUsers start");
        log.debug("getInstitutionUsers institutionId = {}, role = {}", institutionId);
        List<UserInfo> userInfos = institutionService.getInstitutionUsers(institutionId);
        List<UserInfoResponse> result = userInfos.stream()
                .map(userInfo -> userMapper.toUserInfoResponse(userInfo, institutionId))
                .collect(Collectors.toList());
        log.debug("getInstitutionUsers result = {}", result);
        log.trace("getInstitutionUsers end");

        return result;
    }
}
