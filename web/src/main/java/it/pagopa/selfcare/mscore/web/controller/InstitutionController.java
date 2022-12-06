package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.Institution;
import it.pagopa.selfcare.mscore.web.model.InstitutionResource;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping(value = "/{externalId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstitutionResource> createInstitutionByExternalId(@PathVariable("externalId") String externalId) {
        Institution institution = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.ok(InstitutionMapper.toResource(institution));
    }
}
