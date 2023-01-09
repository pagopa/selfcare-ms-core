package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.constant.ErrorEnum;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResource;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionBillingResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.web.util.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/external/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExternalController {

    private final ExternalService externalService;

    public ExternalController(ExternalService externalService) {
        this.externalService = externalService;
    }

    @ExceptionMessage(message = ErrorEnum.GET_INSTITUTION_BY_EXTERNAL_ID_ERROR)
    @GetMapping("/{externalId}")
    public ResponseEntity<InstitutionResource> getByExternalId(@PathVariable("externalId") String id) {
        Institution institution = externalService.getInstitutionByExternalId(id);
        return ResponseEntity.ok().body(InstitutionMapper.toResource(institution));
    }

    @ExceptionMessage(message = ErrorEnum.GET_INSTITUTION_MANAGER_ERROR)
    @GetMapping(value = "/external/institutions/{externalId}/products/{productId}/manager", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OnboardedUser> getManagerInstitutionByExternalId(@PathVariable("externalId") String externalId,
                                                                           @PathVariable("productId") String productId) {
        OnboardedUser manager = externalService.getManagerByExternalId(externalId, productId);
        return ResponseEntity.ok(manager);
    }

    @ExceptionMessage(message = ErrorEnum.GET_INSTITUTION_BILLING_ERROR)
    @GetMapping(value = "/external/institutions/{externalId}/products/{productId}/billing", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstitutionBillingResponse> getBillingInstitutionByExternalId(@PathVariable("externalId") String externalId,
                                                                                        @PathVariable("productId") String productId) {
        Institution institution = externalService.getBillingByExternalId(externalId, productId);
        return ResponseEntity.ok().body(InstitutionMapper.toResponse(institution));
    }
}
