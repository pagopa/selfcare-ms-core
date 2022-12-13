package it.pagopa.selfcare.mscore.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.core.IniPecService;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressIniPECOKDto;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressIniPECRequestBodyDto;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressIniPECRequestBodyFilterDto;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class IniPecControllerTest {
    @InjectMocks
    private IniPecController iniPecController;
    @Mock
    private IniPecService iniPecService;

    @Test
    void testCreateBatchRequest(){
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId("id");
        iniPecBatchRequest.setBatchId("batchId");
        iniPecBatchRequest.setCf("taxId");
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setStatus("status");
        iniPecBatchRequest.setCorrelationId("correlationId");
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequest.setTtl(LocalDateTime.now());

        GetDigitalAddressIniPECRequestBodyDto dto = new GetDigitalAddressIniPECRequestBodyDto();
        GetDigitalAddressIniPECRequestBodyFilterDto filter = new GetDigitalAddressIniPECRequestBodyFilterDto();
        filter.setTaxId("taxId");
        dto.setFilter(filter);

        GetDigitalAddressIniPECOKDto result = new GetDigitalAddressIniPECOKDto();
        result.setCorrelationId("correlationId");

        when(iniPecService.createBatchRequestByCf(any())).thenReturn(iniPecBatchRequest);

        GetDigitalAddressIniPECOKDto getDigitalAddressIniPECOKDto = iniPecController.createBatchRequest(dto).getBody();
        assert getDigitalAddressIniPECOKDto != null;
        assertEquals(getDigitalAddressIniPECOKDto.getCorrelationId(),"correlationId");
    }
}

