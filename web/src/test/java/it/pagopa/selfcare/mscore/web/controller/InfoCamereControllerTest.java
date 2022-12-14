package it.pagopa.selfcare.mscore.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.core.InfoCamereService;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressInfoCamereOKDto;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressInfoCamereRequestBodyDto;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressInfoCamereRequestBodyFilterDto;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class InfoCamereControllerTest {
    @InjectMocks
    private InfoCamereController infoCamereController;
    @Mock
    private InfoCamereService infoCamereService;

    @Test
    void testCreateBatchRequest(){
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("id");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("taxId");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setCorrelationId("correlationId");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());

        GetDigitalAddressInfoCamereRequestBodyDto dto = new GetDigitalAddressInfoCamereRequestBodyDto();
        GetDigitalAddressInfoCamereRequestBodyFilterDto filter = new GetDigitalAddressInfoCamereRequestBodyFilterDto();
        filter.setTaxId("taxId");
        dto.setFilter(filter);

        GetDigitalAddressInfoCamereOKDto result = new GetDigitalAddressInfoCamereOKDto();
        result.setCorrelationId("correlationId");

        when(infoCamereService.createBatchRequestByCf(any())).thenReturn(infoCamereBatchRequest);

        GetDigitalAddressInfoCamereOKDto getDigitalAddressInfoCamereOKDto = infoCamereController.createBatchRequest(dto).getBody();
        assert getDigitalAddressInfoCamereOKDto != null;
        assertEquals(getDigitalAddressInfoCamereOKDto.getCorrelationId(),"correlationId");
    }
}

