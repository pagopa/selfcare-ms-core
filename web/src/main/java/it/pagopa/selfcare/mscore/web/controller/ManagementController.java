package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapperCustom;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;
import it.pagopa.selfcare.mscore.web.model.user.PersonId;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@Slf4j
@RestController
@Api(tags = "Management")
public class ManagementController {

    private final UserService userService;
    private final InstitutionService institutionService;
    private final TokenService tokenService;
    private final TokenMapper tokenMapper;

    public ManagementController(UserService userService, InstitutionService institutionService, TokenService tokenService, TokenMapper tokenMapper) {
        this.userService = userService;
        this.institutionService = institutionService;
        this.tokenService = tokenService;
        this.tokenMapper = tokenMapper;
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.person.get}", notes = "${swagger.mscore.person.get}")
    @GetMapping(value = "/persons/{id}")
    public ResponseEntity<PersonId> getUser(@ApiParam("${swagger.mscore.institutions.model.personId}")
                                            @PathVariable(value = "id") String userId) {
        CustomExceptionMessage.setCustomMessage(GET_USER_ERROR);
        OnboardedUser user = userService.findByUserId(userId);
        return ResponseEntity.ok().body(new PersonId(user.getId()));
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.institution}", notes = "${swagger.mscore.institution}")
    @GetMapping(value = "/management/institutions/{id}")
    public ResponseEntity<InstitutionManagementResponse> getInstitutionByInternalId(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                                    @PathVariable(value = "id") String institutionId) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_ID_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        return ResponseEntity.ok().body(InstitutionMapperCustom.toInstitutionManagementResponse(institution));
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.external.institution}", notes = "${swagger.mscore.external.institution}")
    @GetMapping(value = "/management/external/institutions/{externalId}")
    public ResponseEntity<InstitutionManagementResponse> getInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                                    @PathVariable(value = "externalId") String externalId) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_EXTERNAL_ID_ERROR);
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return ResponseEntity.ok().body(InstitutionMapperCustom.toInstitutionManagementResponse(institution));
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.institution.attributes}", notes = "${swagger.mscore.institution.attributes}")
    @GetMapping(value = "/institutions/{id}/attributes")
    public ResponseEntity<List<AttributesResponse>> getInstitutionAttributes(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                             @PathVariable(value = "id") String institutionId) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_ATTRIBUTES_ERROR);
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        return ResponseEntity.ok().body(InstitutionMapperCustom.toInstitutionAttributeResponse(institution.getAttributes(), institutionId));
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.relationships.get}", notes = "${swagger.mscore.relationships.get}")
    @GetMapping(value = "/relationships")
    public ResponseEntity<RelationshipsManagement> getInstitutionRelationships(@RequestParam(value = "from", required = false) String userId,
                                                                               @RequestParam(value = "to", required = false) String institutionId,
                                                                               @RequestParam(value = "roles", required = false) List<PartyRole> roles,
                                                                               @RequestParam(value = "states", required = false) List<RelationshipState> states,
                                                                               @RequestParam(value = "products", required = false) List<String> products,
                                                                               @RequestParam(value = "productRoles", required = false) List<String> productRoles) {
        CustomExceptionMessage.setCustomMessage(GET_RELATIONSHIP_ERROR);
        List<RelationshipInfo> response = institutionService.retrieveUserRelationships(userId, institutionId, roles, states, products, productRoles);
        return ResponseEntity.ok().body(new RelationshipsManagement(RelationshipMapper.toRelationshipResultList(response)));
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.institution.bygeotaxonomies}", notes = "${swagger.mscore.institution.bygeotaxonomies}")
    @GetMapping(value = "/institutions/bygeotaxonomies")
    public ResponseEntity<InstitutionListResponse> getInstitutionByGeotaxonomies(@ApiParam("${swagger.mscore.institutions.geotaxonomy}")
                                                                                @RequestParam(value = "geoTaxonomies") String geoTaxonomies,
                                                                                @ApiParam("${swagger.mscore.institutions.geotaxonomy.searchMode}")
                                                                                @RequestParam(value = "searchMode", required = false, defaultValue = "ANY") SearchMode searchMode) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_GEOTAXONOMY_ERROR);
        List<Institution> institutions = institutionService.findInstitutionsByGeoTaxonomies(geoTaxonomies, searchMode);
        return ResponseEntity.ok().body(new InstitutionListResponse(InstitutionMapperCustom.toInstitutionListResponse(institutions)));
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.institution.byproductid}", notes = "${swagger.mscore.institution.byproductid}")
    @GetMapping(value = "/external/institutions/product/{productId}")
    public ResponseEntity<InstitutionListResponse> getInstitutionByProductId(@ApiParam("${swagger.mscore.institutions.model.productId}")
                                                                             @PathVariable(value = "productId") String productId) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_PRODUCTID_ERROR);
        List<Institution> institutions = institutionService.findInstitutionsByProductId(productId);
        return ResponseEntity.ok().body(new InstitutionListResponse(InstitutionMapperCustom.toInstitutionListResponse(institutions)));
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${swagger.mscore.institution.verify}", notes = "${swagger.mscore.institution.verify}")
    @RequestMapping(method = RequestMethod.HEAD, value = "/institutions/{id}")
    public ResponseEntity<Void> verifyInstitution(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                  @PathVariable(value = "id") String institutionId) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_ID_ERROR);
        institutionService.retrieveInstitutionById(institutionId);
        return ResponseEntity.ok().build();
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.person.verify}", notes = "${swagger.mscore.person.verify}")
    @RequestMapping(method = {RequestMethod.HEAD}, value = "/persons/{id}")
    public ResponseEntity<Void> verifyUser(@ApiParam("${swagger.mscore.institutions.model.personId}")
                                           @PathVariable(value = "id") String userId) {
        CustomExceptionMessage.setCustomMessage(VERIFY_USER_ERROR);
        userService.verifyUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * @deprecated
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.token}", notes = "${swagger.mscore.token}")
    @GetMapping("/tokens/{tokenId}")
    public ResponseEntity<TokenResponse> getToken(@ApiParam("${swagger.mscore.token.tokenId}")
                                                  @PathVariable("tokenId") String tokenId) {
        CustomExceptionMessage.setCustomMessage(VERIFY_TOKEN_FAILED);
        TokenRelationships tokenRelationships = tokenService.retrieveToken(tokenId);
        return ResponseEntity.ok().body(tokenMapper.toTokenResponse(tokenRelationships));
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.institution}", notes = "${swagger.mscore.institution}")
    @PostMapping(value = "/bulk/institutions")
    public ResponseEntity<BulkInstitutions> retrieveInstitutionByIds(@ApiParam("${swagger.mscore.institutions.model.internalIds}")
                                                                              @RequestBody @Valid BulkPartiesSeed bulkPartiesSeed) {
        CustomExceptionMessage.setCustomMessage(GET_INSTITUTION_BY_ID_ERROR);
        List<String> ids = new ArrayList<>(bulkPartiesSeed.getPartyIdentifiers());
        List<Institution> institution = institutionService.retrieveInstitutionByIds(ids);
        return ResponseEntity.ok().body(InstitutionMapperCustom.toBulkInstitutions(institution, ids));
    }
}
