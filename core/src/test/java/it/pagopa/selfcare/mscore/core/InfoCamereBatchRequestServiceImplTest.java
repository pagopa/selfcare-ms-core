package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.mscore.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.mscore.api.InfoCamereConnector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchPolling;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePolling;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class InfoCamereBatchRequestServiceImplTest {

    @InjectMocks
    private InfoCamereBatchRequestServiceImpl iniPecBatchRequestServiceImpl;

    @Mock
    private InfoCamereBatchPollingConnector infoCamereBatchPollingConnector;
    @Mock
    private InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;
    @Mock
    private InfoCamereConnector infoCamereConnector;

    @Test
    void testBatchPecListRequest() {
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
        List<InfoCamereBatchRequest> infoCamereBatchRequests = new ArrayList<>();
        infoCamereBatchRequests.add(infoCamereBatchRequest);

        when(infoCamereBatchRequestConnector.findAllByBatchId(any())).thenReturn(infoCamereBatchRequests,new ArrayList<>());

        when(infoCamereBatchRequestConnector.setBatchIdAndStatusWorking(anyList(),any())).thenReturn(infoCamereBatchRequests);

        InfoCamerePolling infoCamerePolling = new InfoCamerePolling();
        infoCamerePolling.setDataOraRichiesta("Data Ora Richiesta");
        infoCamerePolling.setIdentificativoRichiesta("correlationId");
        when(infoCamereConnector.callEServiceRequestId(any())).thenReturn(infoCamerePolling);
        assertEquals(infoCamerePolling.getIdentificativoRichiesta(),"correlationId");
        assertEquals(infoCamerePolling.getDataOraRichiesta(),"Data Ora Richiesta");

        InfoCamereBatchPolling infoCamereBatchPolling = new InfoCamereBatchPolling();
        infoCamereBatchPolling.setBatchId("batchId");
        infoCamereBatchPolling.setPollingId("pollingId");
        infoCamereBatchPolling.setStatus("status");
        infoCamereBatchPolling.setId("id");
        infoCamereBatchPolling.setTimeStamp(LocalDateTime.now());
        when(infoCamereBatchPollingConnector.save(any())).thenReturn(infoCamereBatchPolling);

        iniPecBatchRequestServiceImpl.batchPecListRequest();
    }

    @Test
    void testRecovery(){
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
        List<InfoCamereBatchRequest> infoCamereBatchRequests = new ArrayList<>();
        infoCamereBatchRequests.add(infoCamereBatchRequest);

        when(infoCamereBatchRequestConnector.findAllByBatchIdNotAndStatusWorking(any())).thenReturn(infoCamereBatchRequests,new ArrayList<>());
        when(infoCamereBatchRequestConnector.save(any())).thenReturn(infoCamereBatchRequest);
        testBatchPecListRequest();
        iniPecBatchRequestServiceImpl.recovery();
    }
}

