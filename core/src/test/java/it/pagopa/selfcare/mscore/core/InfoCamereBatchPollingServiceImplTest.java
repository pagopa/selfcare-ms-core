package it.pagopa.selfcare.mscore.core;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.mscore.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.mscore.api.InfoCamereConnector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.pagopa.selfcare.mscore.model.infocamere.BatchStatus;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchPolling;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePec;
import it.pagopa.selfcare.mscore.model.infocamere.Pec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class InfoCamereBatchPollingServiceImplTest {

    @InjectMocks
    private InfoCamereBatchPollingServiceImpl iniPecBatchPollingService;

    @Mock
    private InfoCamereBatchPollingConnector infoCamereBatchPollingConnector;
    @Mock
    private InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;
    @Mock
    private InfoCamereConnector infoCamereConnector;

    @Test
    void testGetPecList() {
        InfoCamereBatchPolling infoCamereBatchPolling = new InfoCamereBatchPolling();
        infoCamereBatchPolling.setBatchId("batchId");
        infoCamereBatchPolling.setPollingId("pollingId");
        infoCamereBatchPolling.setStatus("status");
        infoCamereBatchPolling.setId("id");
        infoCamereBatchPolling.setTimeStamp(LocalDateTime.now());
        List<InfoCamereBatchPolling> infoCamereBatchPollingList = new ArrayList<>();
        infoCamereBatchPollingList.add(infoCamereBatchPolling);

        when(infoCamereBatchPollingConnector.findByStatus(any())).thenReturn(infoCamereBatchPollingList, new ArrayList<>());

        Pec pec = new Pec();
        pec.setPecImpresa("pecImpresa");
        pec.setPecProfessionistas(new ArrayList<>());
        pec.setCf("cf");
        List<Pec> pecs = new ArrayList<>();
        pecs.add(pec);
        InfoCamerePec infoCamerePec = new InfoCamerePec();
        infoCamerePec.setIdentificativoRichiesta("correlationId");
        infoCamerePec.setDataOraDownload(LocalDateTime.now().toString());
        infoCamerePec.setElencoPec(pecs);

        when(infoCamereConnector.callEServiceRequestPec(any())).thenReturn(infoCamerePec);

        infoCamereBatchPolling.setStatus(BatchStatus.WORKED.getValue());
        when(infoCamereBatchPollingConnector.save(any())).thenReturn(infoCamereBatchPolling);

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

        when(infoCamereBatchRequestConnector.findAllByBatchId(any())).thenReturn(infoCamereBatchRequests);

        infoCamereBatchRequest.setStatus(BatchStatus.WORKED.getValue());

        when(infoCamereBatchRequestConnector.save(any())).thenReturn(infoCamereBatchRequest);

        iniPecBatchPollingService.getPecList();
    }
}

