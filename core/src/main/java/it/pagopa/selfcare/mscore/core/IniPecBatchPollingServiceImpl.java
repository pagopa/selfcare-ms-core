package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.IniPecBatchPollingConnector;
import it.pagopa.selfcare.mscore.api.IniPecBatchRequestConnector;
import it.pagopa.selfcare.mscore.api.IniPecConnector;
import it.pagopa.selfcare.mscore.model.BatchStatus;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchPolling;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IniPecBatchPollingServiceImpl implements IniPecBatchPollingService {

    private final IniPecBatchRequestConnector iniPecBatchRequestConnector;
    private final IniPecBatchPollingConnector iniPecBatchPollingConnector;
    private final IniPecConnector iniPecConnector;

    public IniPecBatchPollingServiceImpl(
            IniPecBatchRequestConnector iniPecBatchRequestConnector,
            IniPecBatchPollingConnector iniPecBatchPollingConnector,
            IniPecConnector iniPecConnector) {
        this.iniPecBatchRequestConnector = iniPecBatchRequestConnector;
        this.iniPecBatchPollingConnector = iniPecBatchPollingConnector;
        this.iniPecConnector = iniPecConnector;
    }

    @Override
    public void getPecList() {
        boolean hasNext = true;

        while(hasNext){
            hasNext = false;
            List<IniPecBatchPolling> iniPecBatchPollings = iniPecBatchPollingConnector.findByStatus(BatchStatus.NOT_WORKED.getValue());
            if(iniPecBatchPollings!=null && !iniPecBatchPollings.isEmpty()){
                hasNext = true;
                IniPecBatchPolling iniPecBatchPolling = iniPecBatchPollings.get(0);
                callService(iniPecBatchPolling);
            }
        }
    }

    private void callService(IniPecBatchPolling iniPecBatchPolling){
        IniPecPec iniPecPec = iniPecConnector.callEServiceRequestPec(iniPecBatchPolling.getPollingId());
        //Coda sqs?
        setWorkedBatchPolling(iniPecBatchPolling);
    }

    private void setWorkedBatchPolling(IniPecBatchPolling iniPecBatchPolling){
        iniPecBatchPolling.setStatus(BatchStatus.WORKED.getValue());
        iniPecBatchPollingConnector.save(iniPecBatchPolling);
        setWorkedBatchRequestByBatchId(iniPecBatchPolling.getBatchId());
    }

    private void setWorkedBatchRequestByBatchId(String batchId){
        List<IniPecBatchRequest> iniPecBatchRequests = iniPecBatchRequestConnector.findAllByBatchId(batchId);
        iniPecBatchRequests.forEach(iniPecBatchRequest -> {
            iniPecBatchRequest.setStatus(BatchStatus.WORKED.getValue());
            iniPecBatchRequestConnector.save(iniPecBatchRequest);
        });
    }
}
