package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.constant.ErrorEnum;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResource;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.util.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Institution")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ExceptionMessage(message = ErrorEnum.CREATE_INSTITUTION_ERROR)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PA.create}")
    @PostMapping(value = "/{externalId}")
    public ResponseEntity<InstitutionResource> createInstitutionByExternalId(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                             @PathVariable("externalId") String externalId) {
        Institution saved = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toResource(saved));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionMessage(message = ErrorEnum.CREATE_INSTITUTION_ERROR)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.create}")
    @PostMapping(value = "/insert/{externalId}")
    public ResponseEntity<InstitutionResource> createInstitutionRaw(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                    @PathVariable("externalId") String externalId,
                                                                    @RequestBody InstitutionRequest institution) {
        Institution saved = institutionService.createInstitutionRaw(InstitutionMapper.toInstitution(institution, externalId), externalId);
        return ResponseEntity.ok(InstitutionMapper.toResource(saved));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ExceptionMessage(message = ErrorEnum.CREATE_INSTITUTION_ERROR)
    @ApiOperation(value = "", notes = "${swagger.mscore.institution.PG.create}")
    @PostMapping(value = "/pg/{externalId}")
    public ResponseEntity<InstitutionResource> createPgInstitution(@ApiParam("${swagger.mscore.institutions.model.externalId}")
                                                                   @PathVariable("externalId") String externalId) {
        Institution saved = institutionService.createPgInstitution(externalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstitutionMapper.toResource(saved));
    }
}
