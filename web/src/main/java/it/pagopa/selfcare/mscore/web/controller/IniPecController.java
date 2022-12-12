package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.IniPecService;

import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressIniPECOKDto;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressIniPECRequestBodyDto;
import it.pagopa.selfcare.mscore.web.model.mapper.GetDigitalAddressIniPecMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/national-registries-private/inipec", produces = MediaType.APPLICATION_JSON_VALUE)
public class IniPecController {

    private final IniPecService iniPecService;

    public IniPecController(
            IniPecService iniPecService) {
        this.iniPecService = iniPecService;
    }


    @PostMapping("/digital-address")
    public ResponseEntity<GetDigitalAddressIniPECOKDto> createBatchRequest(@RequestBody @Valid GetDigitalAddressIniPECRequestBodyDto dto) {
        String cf = dto.getFilter().getTaxId();
        String correlationId = dto.getFilter().getCorrelationId();
        IniPecBatchRequest iniPecBatchRequest = iniPecService.createBatchRequestByCfAndCorrelationId(cf,correlationId);
        return ResponseEntity.ok(GetDigitalAddressIniPecMapper.toResource(iniPecBatchRequest));
    }

}
