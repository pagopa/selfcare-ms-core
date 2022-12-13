package it.pagopa.selfcare.mscore.core;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.IniPecBatchPollingConnector;
import it.pagopa.selfcare.mscore.api.IniPecBatchRequestConnector;
import it.pagopa.selfcare.mscore.api.IniPecConnector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.pagopa.selfcare.mscore.model.inipec.BatchStatus;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchPolling;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPec;
import it.pagopa.selfcare.mscore.model.inipec.Pec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class IniPecBatchPollingServiceImplTest {

    @InjectMocks
    private IniPecBatchPollingServiceImpl iniPecBatchPollingService;

    @Mock
    private IniPecBatchPollingConnector iniPecBatchPollingConnector;
    @Mock
    private IniPecBatchRequestConnector iniPecBatchRequestConnector;
    @Mock
    private IniPecConnector iniPecConnector;

    @Test
    void testGetPecList() {
        IniPecBatchPolling iniPecBatchPolling = new IniPecBatchPolling();
        iniPecBatchPolling.setBatchId("batchId");
        iniPecBatchPolling.setPollingId("pollingId");
        iniPecBatchPolling.setStatus("status");
        iniPecBatchPolling.setId("id");
        iniPecBatchPolling.setTimeStamp(LocalDateTime.now());
        List<IniPecBatchPolling> iniPecBatchPollingList = new ArrayList<>();
        iniPecBatchPollingList.add(iniPecBatchPolling);

        when(iniPecBatchPollingConnector.findByStatus(any())).thenReturn(iniPecBatchPollingList, new ArrayList<>());

        Pec pec = new Pec();
        pec.setPecImpresa("pecImpresa");
        pec.setPecProfessionistas(new ArrayList<>());
        pec.setCf("cf");
        List<Pec> pecs = new ArrayList<>();
        pecs.add(pec);
        IniPecPec iniPecPec = new IniPecPec();
        iniPecPec.setIdentificativoRichiesta("correlationId");
        iniPecPec.setDataOraDownload(LocalDateTime.now().toString());
        iniPecPec.setElencoPec(pecs);

        when(iniPecConnector.callEServiceRequestPec(any())).thenReturn(iniPecPec);

        iniPecBatchPolling.setStatus(BatchStatus.WORKED.getValue());
        when(iniPecBatchPollingConnector.save(any())).thenReturn(iniPecBatchPolling);

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

        when(iniPecBatchRequestConnector.findAllByBatchId(any())).thenReturn(iniPecBatchRequests);

        iniPecBatchRequest.setStatus(BatchStatus.WORKED.getValue());

        when(iniPecBatchRequestConnector.save(any())).thenReturn(iniPecBatchRequest);

        iniPecBatchPollingService.getPecList();
    }
}

