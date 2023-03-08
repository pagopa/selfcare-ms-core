package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import it.pagopa.selfcare.mscore.web.util.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Institution")
@Slf4j
public class InstitutionController {

    private static final String ENTRY_LOG = "Creating institution given externalId";
    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    /**
     * The function persist PA institution
     *
     * @param externalId String
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: TokenId
     * * Code: 404, Message: Institution data not found on Ipa, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.PA.create}", notes = "${swagger.mscore.institution.PA.create}")
    @PostMapping(value = "/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                             @PathVariable("externalId") String externalId) {

        log.info(ENTRY_LOG);
        CustomExceptionMessage.setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist institution manually
     *
     * @param externalId  String
     * @param institution InstitutionRequest
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.create}", notes = "${swagger.mscore.institution.create}")
    @PostMapping(value = "/insert/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionRaw(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                    @PathVariable("externalId") String externalId,
                                                                    @RequestBody @Valid InstitutionRequest institution) {
        log.info(ENTRY_LOG);
        CustomExceptionMessage.setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionRaw(InstitutionMapper.toInstitution(institution, externalId), externalId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function persist PG institution
     *
     * @param request CreatePgInstitutionRequest
     * @param existsInRegistry boolean
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: TokenId
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 404, Message: Institution data not found on InfoCamere, DataType: Problem
     * * Code: 409, Message: Institution conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.institution.PG.create}", notes = "${swagger.mscore.institution.PG.create}")
    @PostMapping(value = "/pg")
    public ResponseEntity<InstitutionResponse> createPgInstitution(@RequestBody @Valid CreatePgInstitutionRequest request,
                                                                   @ApiParam("${swagger.mscore.institutions.existsInRegistry}")
                                                                   @RequestParam(value = "existsInRegistry") boolean existsInRegistry,
                                                                   Authentication authentication) {
        CustomExceptionMessage.setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createPgInstitution(request.getTaxId(), request.getDescription(), existsInRegistry, (SelfCareUser) authentication.getPrincipal());
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

        log.info("Retrieving products for institution {}", institutionId);
        CustomExceptionMessage.setCustomMessage(GET_PRODUCTS_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        List<Onboarding> list = institutionService.retrieveInstitutionProducts(institution, states);
        return ResponseEntity.ok(InstitutionMapper.toOnboardedProducts(list));
    }


    /**
     * The function Update the corresponding institution given internal institution id
     *
     * @param institutionId String
     * @param institutionPut InstitutionPut
     *
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: OnboardedProducts
     * * Code: 400, Message: bad request, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.update}", notes = "${swagger.mscore.institution.update}")
    @PutMapping(value = "/{id}")
    public ResponseEntity<InstitutionResponse> updateInstitution(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                 @PathVariable("id") String institutionId,
                                                                 @RequestBody InstitutionPut institutionPut,
                                                                 Authentication authentication) {

        log.info("Updating institution {} with {}", institutionId, institutionPut);
        CustomExceptionMessage.setCustomMessage(PUT_INSTITUTION_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        Institution saved = institutionService.updateInstitution(institutionId, InstitutionMapper.toInstitutionUpdate(institutionPut), selfCareUser.getId());
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(saved));
    }

    /**
     * The function return geographic taxonomies related to institution
     *
     * @param id String
     *
     * @return GeographicTaxonomies
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.geotaxonomies}", notes = "${swagger.mscore.institution.geotaxonomies}")
    @GetMapping(value = "/{id}/geotaxonomies")
    public ResponseEntity<List<GeographicTaxonomies>> retrieveInstitutionGeoTaxonomies(@PathVariable("id") String id,
                                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "0") Integer pageSize,
                                                                                       @RequestParam(value = "pageNumber", required = false, defaultValue = "10") Integer pageNumber) {

        CustomExceptionMessage.setCustomMessage(RETRIEVE_GEO_TAXONOMIES_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(id);
        List<GeographicTaxonomies> list = institutionService.retrieveInstitutionGeoTaxonomies(institution);
        return ResponseEntity.ok(PaginationUtils.paginate(list,pageSize,pageNumber));
    }

    /**
     * The function return an institution given institution internal id
     *
     * @param id String
     *
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution}", notes = "${swagger.mscore.institution}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InstitutionResponse> retrieveInstitutionById(@PathVariable("id") String id) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_ID_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(id);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(institution));
    }


    /**
     * The function return user institution relationships
     *
     * @param institutionId String
     * @param personId String
     * @param roles String[]
     * @param states String[]
     * @param products String[]
     * @param productRoles String[]
     *
     * @return GeographicTaxonomies
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies or Institution not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution.relationships}", notes = "${swagger.mscore.institution.relationships}")
    @GetMapping(value = "/{id}/relationships")
    public ResponseEntity<List<RelationshipResult>> getUserInstitutionRelationships(@PathVariable("id") String institutionId,
                                                                                    @RequestParam(value = "personId", required = false) String personId,
                                                                                    @RequestParam(value = "roles", required = false) List<PartyRole> roles,
                                                                                    @RequestParam(value = "states", required = false) List<RelationshipState> states,
                                                                                    @RequestParam(value = "products", required = false) List<String> products,
                                                                                    @RequestParam(value = "productRoles", required = false) List<String> productRoles,
                                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "0") Integer pageSize,
                                                                                    @RequestParam(value = "pageNumber", required = false, defaultValue = "10") Integer pageNumber,
                                                                                    Authentication authentication) {
        log.info("Getting relationship for institution {} and current user", institutionId);
        CustomExceptionMessage.setCustomMessage(GET_USER_INSTITUTION_RELATIONSHIP_ERROR);
        SelfCareUser selfCareUser = (SelfCareUser) authentication.getPrincipal();
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        List<RelationshipInfo> relationshipInfoList = institutionService.getUserInstitutionRelationships(institution, selfCareUser.getId(), personId, roles, states, products, productRoles);
        return ResponseEntity.ok().body(PaginationUtils.paginate(RelationshipMapper.toRelationshipResultList(relationshipInfoList),pageSize,pageNumber));
    }


}
