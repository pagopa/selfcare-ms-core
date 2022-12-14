package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.InfoCamereService;

import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressInfoCamereOKDto;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressInfoCamereRequestBodyDto;
import it.pagopa.selfcare.mscore.web.model.mapper.GetDigitalAddressInfoCamereMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/national-registries-private/inipec", produces = MediaType.APPLICATION_JSON_VALUE)
public class InfoCamereController {

    private final InfoCamereService infoCamereService;

    public InfoCamereController(
            InfoCamereService infoCamereService) {
        this.infoCamereService = infoCamereService;
    }


    @PostMapping("/digital-address")
    public ResponseEntity<GetDigitalAddressInfoCamereOKDto> createBatchRequest(@RequestBody @Valid GetDigitalAddressInfoCamereRequestBodyDto dto) {
        String cf = dto.getFilter().getTaxId();
        InfoCamereBatchRequest infoCamereBatchRequest = infoCamereService.createBatchRequestByCf(cf);
        return ResponseEntity.ok(GetDigitalAddressInfoCamereMapper.toResource(infoCamereBatchRequest));
    }

}
