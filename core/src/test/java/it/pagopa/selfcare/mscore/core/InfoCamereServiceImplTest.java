package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class InfoCamereServiceImplTest {
    @Mock
    private InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;

    @InjectMocks
    private InfoCamereServiceImpl iniPecServiceImpl;

    @Test
    void testCreateBatchRequestByCfAndCorrelationId() {
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("id");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("cf");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setCorrelationId("correlationId");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());

        when(infoCamereBatchRequestConnector.save(any())).thenReturn(infoCamereBatchRequest);

        assertNotNull(iniPecServiceImpl.createBatchRequestByCf("cf"));
    }
}

