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
import it.pagopa.selfcare.mscore.web.model.mapper.ProductMapper;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.CREATE_INSTITUTION_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.GET_PRODUCTS_ERROR;
import static it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage.setCustomMessage;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Institution")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PA.create}")
    @PostMapping(value = "/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                             @PathVariable("externalId") String externalId) {
        setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.create}")
    @PostMapping(value = "/insert/{externalId}")
    public ResponseEntity<InstitutionResponse> createInstitutionRaw(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                    @PathVariable("externalId") String externalId,
                                                                    @RequestBody @Valid InstitutionRequest institution) {
        setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createInstitutionRaw(InstitutionMapper.toInstitution(institution, externalId), externalId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionResponse(saved));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PG.create}")
    @PostMapping(value = "/pg/{externalId}")
    public ResponseEntity<InstitutionResponse> createPgInstitution(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                   @PathVariable("externalId") String externalId,
                                                                   Authentication authentication) {

        setCustomMessage(CREATE_INSTITUTION_ERROR);
        Institution saved = institutionService.createPgInstitution(externalId, (SelfCareUser) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toInstitutionResponse(saved));
    }

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
}
