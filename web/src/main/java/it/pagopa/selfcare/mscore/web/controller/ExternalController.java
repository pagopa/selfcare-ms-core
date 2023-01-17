package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.constant.ErrorEnum;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionBillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionManagerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
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
    public ResponseEntity<InstitutionResponse> getByExternalId(@PathVariable("externalId") String id) {
        Institution institution = externalService.getInstitutionByExternalId(id);
        return ResponseEntity.ok().body(InstitutionMapper.toInstitutionResponse(institution));
    }

    @ExceptionMessage(message = ErrorEnum.GET_INSTITUTION_MANAGER_ERROR)
    @GetMapping(value = "/{externalId}/products/{productId}/manager")
    public ResponseEntity<InstitutionManagerResponse> getManagerInstitutionByExternalId(@PathVariable("externalId") String externalId,
                                                                                        @PathVariable("productId") String productId) {
        log.info("Getting manager for institution having externalId {}",externalId);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        OnboardedUser manager = externalService.getInstitutionManager(institution, productId);
        String contractId = externalService.getRelationShipToken(institution.getId(),manager.getUser(), productId);
        return ResponseEntity.ok(InstitutionMapper.toInstitutionManagerResponse(institution, manager, productId, contractId));
    }

    @ExceptionMessage(message = ErrorEnum.GET_INSTITUTION_BILLING_ERROR)
    @GetMapping(value = "/{externalId}/products/{productId}/billing")
    public ResponseEntity<InstitutionBillingResponse> getBillingInstitutionByExternalId(@PathVariable("externalId") String externalId,
                                                                                        @PathVariable("productId") String productId) {
        log.info("Retrieving billing data for institution having externalId {} and productId {}", externalId, productId);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        institution = externalService.getBillingByExternalId(institution, productId);
        return ResponseEntity.ok().body(InstitutionMapper.toBillingResponse(institution, productId));
    }
}
