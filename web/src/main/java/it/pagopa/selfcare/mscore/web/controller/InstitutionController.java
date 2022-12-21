package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResource;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
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

    @PostMapping(value = "/{externalId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstitutionResource> createInstitutionByExternalId(@PathVariable("externalId") String externalId) {
        Institution institution = institutionService.createInstitutionByExternalId(externalId);
        return ResponseEntity.ok(InstitutionMapper.toResource(institution));
    }

    @PostMapping(value = "/insert/{externalId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstitutionResource> createInstitutionRaw(@PathVariable("externalId") String externalId,
                                                                    @RequestBody Institution institution) {
        Institution savedInstitution = institutionService.saveInstitution(institution, externalId);
        return ResponseEntity.ok(InstitutionMapper.toResource(savedInstitution));
    }

    @GetMapping(value = "/institutions/{id}/geotaxonomies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GeoTaxonomies>> retrieveInstitutionGeoTaxonomies(@PathVariable("id") String id){
        List<GeographicTaxonomies> list = institutionService.getGeoTaxonomies(id);
        return ResponseEntity.ok(list.stream().map(InstitutionMapper::toResource)
                .collect(Collectors.toList()));
    }
}
