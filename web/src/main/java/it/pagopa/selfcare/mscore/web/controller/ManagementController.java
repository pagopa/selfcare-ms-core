package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.core.UserRelationshipService;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.model.institution.AttributesResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionListResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionManagementResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipsManagement;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.RelationshipMapper;
import it.pagopa.selfcare.mscore.web.model.user.PersonId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "Management")
public class ManagementController {

    private final UserService userService;
    private final InstitutionService institutionService;
    private final UserRelationshipService userRelationshipService;

    public ManagementController(UserService userService, InstitutionService institutionService, UserRelationshipService userRelationshipService) {
        this.userService = userService;
        this.institutionService = institutionService;
        this.userRelationshipService = userRelationshipService;
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${}", notes = "${}")
    @GetMapping(value = "/persons/{id}")
    public ResponseEntity<PersonId> getUser(@PathVariable(value = "id") String userId) {
        OnboardedUser user = userService.findByUserId(userId);
        return ResponseEntity.ok().body(new PersonId(user.getId()));
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${}", notes = "${}")
    @GetMapping(value = "/institutions/{id}")
    public ResponseEntity<InstitutionManagementResponse> getInstitutionByInternalId(@PathVariable(value = "id") String institutionId) {
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionManagementResponse(institution));
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${}", notes = "${}")
    @GetMapping(value = "/external/institutions/{externalId}")
    public ResponseEntity<InstitutionManagementResponse> getInstitutionByExternalId(@PathVariable(value = "externalId") String externalId) {
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionManagementResponse(institution));
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${}", notes = "${}")
    @GetMapping(value = "/institutions/{id}/attributes")
    public ResponseEntity<List<AttributesResponse>> getInstitutionAttributes(@PathVariable(value = "id") String institutionId) {
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        return ResponseEntity.ok().body(InstitutionMapper.toAttributeResponse(institution.getAttributes()));
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${}", notes = "${}")
    @GetMapping(value = "/relationships")
    public ResponseEntity<RelationshipsManagement> getInstitutionRelationships(@RequestParam(value = "from", required = false) String userId,
                                                                               @RequestParam(value = "to", required = false) String institutionId,
                                                                               @RequestParam(value = "roles", required = false) List<PartyRole> roles,
                                                                               @RequestParam(value = "states", required = false) List<RelationshipState> states,
                                                                               @RequestParam(value = "products", required = false) List<String> products,
                                                                               @RequestParam(value = "productRoles", required = false) List<String> productRoles) {
        List<RelationshipInfo> response = institutionService.retrieveUserRelationships(userId, institutionId, roles, states, products, productRoles);
        return ResponseEntity.ok().body(new RelationshipsManagement(RelationshipMapper.toRelationshipResultList(response)));
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${}", notes = "${}")
    @GetMapping(value = "/institutions/bygeotaxonomies")
    public ResponseEntity<InstitutionListResponse> getInstitutionByGeotaxnomies(@RequestParam(value = "geoTaxonomies") String geoTaxonomies,
                                                                                @RequestParam(value = "searchMode", required = false) String searchMode) {

        List<Institution> institutions = institutionService.findInstitutionsByGeoTaxonomies(geoTaxonomies, searchMode);
        return ResponseEntity.ok().body(new InstitutionListResponse(InstitutionMapper.toInstitutionListResponse(institutions)));
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${}", notes = "${}")
    @GetMapping(value = "/external/institutions/product/{productId}")
    public ResponseEntity<InstitutionListResponse> getInstitutionByProductId(@PathVariable(value = "productId") String productId) {
        List<Institution> institutions = institutionService.findInstitutionsByProductId(productId);
        return ResponseEntity.ok().body(new InstitutionListResponse(InstitutionMapper.toInstitutionListResponse(institutions)));
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "${}", notes = "${}")
    @RequestMapping(method = RequestMethod.HEAD, value = "/institutions/{id}")
    public ResponseEntity<Void> verifyInstitution(@RequestParam(value = "id") String institutionId) {
        institutionService.retrieveInstitutionById(institutionId);
        return ResponseEntity.ok().build();
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since = "1.0-SNAPSHOT")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.person.verify}", notes = "${swagger.mscore.person.verify}")
    @RequestMapping(method = { RequestMethod.HEAD}, value = "/persons/{id}")
    public ResponseEntity<Void> verifyUser(@PathVariable(value = "id") String userId) {
        userService.verifyUser(userId);
        return ResponseEntity.ok().build();
    }
}
