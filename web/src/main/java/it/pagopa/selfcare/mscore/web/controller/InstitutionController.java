package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.constant.ErrorEnum;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionProduct;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResource;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.util.ExceptionMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @ExceptionMessage(message = ErrorEnum.CREATE_INSTITUTION_ERROR)
    @PostMapping(value = "{institutionType}/{externalId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstitutionResource> createInstitutionByExternalId(@PathVariable("institutionType") InstitutionType institutionType,
                                                                             @PathVariable("externalId") String externalId) {
        Institution institution = institutionService.createInstitutionByExternalId(institutionType, externalId);
        return ResponseEntity.ok(InstitutionMapper.toResource(institution));
    }

    @ExceptionMessage(message = ErrorEnum.CREATE_INSTITUTION_ERROR)
    @PostMapping(value = "/insert/{externalId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstitutionResource> createInstitutionRaw(@PathVariable("externalId") String externalId,
                                                                    @RequestBody Institution institution) {
        Institution saved = institutionService.createInstitutionRaw(institution, externalId);
        return ResponseEntity.ok(InstitutionMapper.toResource(saved));
    }

    @ExceptionMessage(message = ErrorEnum.GET_PRODUCTS_ERROR)
    @GetMapping(value = "/institutions/{id}/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InstitutionProduct>> retrieveInstitutionProducts(@PathVariable("id") String id,
                                                                                @RequestParam(value = "states") String[] states) {
        List<Onboarding> list = institutionService.retrieveInstitutionProducts(id, List.of(states));
        return ResponseEntity.ok(list.stream().map(InstitutionMapper::toResource)
                .collect(Collectors.toList()));
    }
}
