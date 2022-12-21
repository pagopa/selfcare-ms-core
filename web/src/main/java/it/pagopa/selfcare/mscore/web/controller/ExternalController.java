package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.CreateInstitutionDto;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResource;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/external/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExternalController {

    private final ExternalService externalService;

    public ExternalController(ExternalService externalService) {
        this.externalService = externalService;
    }

    @GetMapping
    public ResponseEntity<List<InstitutionResource>> getAll() {
        List<InstitutionResource> resources = externalService.getAllInstitution().stream()
                .map(InstitutionMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}") //party-process
    public ResponseEntity<InstitutionResource> getById(@PathVariable("id") String id) {
        Institution institution = externalService.getInstitutionById(id);
        return ResponseEntity.ok().body(InstitutionMapper.toResource(institution));
    }

    @GetMapping("/external/{id}")
    public ResponseEntity<InstitutionResource> getByExternalId(@PathVariable("id") String id) {
        Institution institution = externalService.getInstitutionByExternalId(id);
        return ResponseEntity.ok().body(InstitutionMapper.toResource(institution));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstitutionResource> createInstitution(@RequestBody @Valid CreateInstitutionDto dto) {
        Institution institution = externalService.createInstitution(InstitutionMapper.fromDto(dto));
        return ResponseEntity.ok(InstitutionMapper.toResource(institution));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable("id") String id) {
        externalService.deleteInstitution(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/institutions/{externalId}/geotaxonomies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GeoTaxonomies>> retrieveInstitutionGeoTaxonomiesByExternalId(@PathVariable("externalId") String externalId){
        List<GeographicTaxonomies> list = externalService.getGeoTaxonomies(externalId);
        return ResponseEntity.ok(list.stream().map(InstitutionMapper::toResource)
                .collect(Collectors.toList()));
    }
}
