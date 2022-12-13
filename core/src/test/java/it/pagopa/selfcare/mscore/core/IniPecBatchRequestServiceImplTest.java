package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.IniPecBatchPollingConnector;
import it.pagopa.selfcare.mscore.api.IniPecBatchRequestConnector;
import it.pagopa.selfcare.mscore.api.IniPecConnector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchPolling;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPolling;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class IniPecBatchRequestServiceImplTest {

    @InjectMocks
    private IniPecBatchRequestServiceImpl iniPecBatchRequestServiceImpl;

    @Mock
    private IniPecBatchPollingConnector iniPecBatchPollingConnector;
    @Mock
    private IniPecBatchRequestConnector iniPecBatchRequestConnector;
    @Mock
    private IniPecConnector iniPecConnector;

    @Test
    void testBatchPecListRequest() {
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId("id");
        iniPecBatchRequest.setBatchId("batchId");
        iniPecBatchRequest.setCf("cf");
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setStatus("status");
        iniPecBatchRequest.setCorrelationId("correlationId");
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequest.setTtl(LocalDateTime.now());
        List<IniPecBatchRequest> iniPecBatchRequests = new ArrayList<>();
        iniPecBatchRequests.add(iniPecBatchRequest);

        when(iniPecBatchRequestConnector.findAllByBatchId(any())).thenReturn(iniPecBatchRequests,new ArrayList<>());

        when(iniPecBatchRequestConnector.setBatchIdAndStatusWorking(anyList(),any())).thenReturn(iniPecBatchRequests);

        IniPecPolling iniPecPolling = new IniPecPolling();
        iniPecPolling.setDataOraRichiesta("Data Ora Richiesta");
        iniPecPolling.setIdentificativoRichiesta("correlationId");
        when(iniPecConnector.callEServiceRequestId(any())).thenReturn(iniPecPolling);
        assertEquals(iniPecPolling.getIdentificativoRichiesta(),"correlationId");
        assertEquals(iniPecPolling.getDataOraRichiesta(),"Data Ora Richiesta");

        IniPecBatchPolling iniPecBatchPolling = new IniPecBatchPolling();
        iniPecBatchPolling.setBatchId("batchId");
        iniPecBatchPolling.setPollingId("pollingId");
        iniPecBatchPolling.setStatus("status");
        iniPecBatchPolling.setId("id");
        iniPecBatchPolling.setTimeStamp(LocalDateTime.now());
        when(iniPecBatchPollingConnector.save(any())).thenReturn(iniPecBatchPolling);

        iniPecBatchRequestServiceImpl.batchPecListRequest();
    }

    @Test
    void testRecovery(){
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId("id");
        iniPecBatchRequest.setBatchId("batchId");
        iniPecBatchRequest.setCf("cf");
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setStatus("status");
        iniPecBatchRequest.setCorrelationId("correlationId");
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequest.setTtl(LocalDateTime.now());
        List<IniPecBatchRequest> iniPecBatchRequests = new ArrayList<>();
        iniPecBatchRequests.add(iniPecBatchRequest);

        when(iniPecBatchRequestConnector.findAllByBatchIdNotAndStatusWorking(any())).thenReturn(iniPecBatchRequests,new ArrayList<>());
        when(iniPecBatchRequestConnector.save(any())).thenReturn(iniPecBatchRequest);
        testBatchPecListRequest();
        iniPecBatchRequestServiceImpl.recovery();
    }
}

