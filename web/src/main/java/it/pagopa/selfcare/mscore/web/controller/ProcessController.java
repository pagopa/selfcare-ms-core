package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.ProcessService;
import it.pagopa.selfcare.mscore.core.model.Institution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }


    @RequestMapping(method = {RequestMethod.GET}, value = "/external/institutions/{externalId}")
    public ResponseEntity<Institution> getInstitutionByExternalId(@PathVariable(value = "externalId") String externalId) {
        return ResponseEntity.ok().body(processService.getInstitutionByExternalId(externalId));
    }
}
