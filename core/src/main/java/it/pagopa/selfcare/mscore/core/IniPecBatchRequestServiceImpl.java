package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.IniPecBatchPollingConnector;
import it.pagopa.selfcare.mscore.api.IniPecBatchRequestConnector;
import it.pagopa.selfcare.mscore.api.IniPecConnector;
import it.pagopa.selfcare.mscore.model.BatchStatus;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchPolling;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecCfRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPolling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IniPecBatchRequestServiceImpl implements IniPecBatchRequestService {

    private final IniPecBatchRequestConnector iniPecBatchRequestConnector;
    private final IniPecBatchPollingConnector iniPecBatchPollingConnector;
    private final IniPecConnector iniPecConnector;

    public IniPecBatchRequestServiceImpl(
            IniPecBatchRequestConnector iniPecBatchRequestConnector,
            IniPecBatchPollingConnector iniPecBatchPollingConnector,
            IniPecConnector iniPecConnector) {
        this.iniPecBatchRequestConnector = iniPecBatchRequestConnector;
        this.iniPecBatchPollingConnector = iniPecBatchPollingConnector;
        this.iniPecConnector = iniPecConnector;
    }

    @Override
    public void batchPecListRequest() {
        boolean hasNext = true;
        while(hasNext){
            hasNext = false;
            List<IniPecBatchRequest> iniPecBatchRequests = iniPecBatchRequestConnector.findAllByBatchId(BatchStatus.NO_BATCH_ID.getValue());
            if(iniPecBatchRequests != null && !iniPecBatchRequests.isEmpty()){
                hasNext = true;
                setNewBatchId(iniPecBatchRequests);
            }
        }
    }

    private void setNewBatchId(List<IniPecBatchRequest> iniPecBatchRequests){
        String batchId = UUID.randomUUID().toString();
        List<IniPecBatchRequest> iniPecBatchRequestsWithBatchId = iniPecBatchRequestConnector.setBatchIdAndStatusWorking(iniPecBatchRequests,batchId);
        List<String> requestCfIniPec = iniPecBatchRequestsWithBatchId.stream()
                .filter(iniPecBatchRequest -> iniPecBatchRequest.getRetry() <= 3)
                .map(IniPecBatchRequest::getCf)
                .collect(Collectors.toList());
        callService(requestCfIniPec,batchId);
    }

    private void callService(List<String> requestCfIniPec, String batchId){
        IniPecCfRequest iniPecCfRequest = new IniPecCfRequest();
        iniPecCfRequest.setDataOraRichiesta(LocalDateTime.now().toString());
        iniPecCfRequest.setElencoCf(requestCfIniPec);
        IniPecPolling iniPecPolling = iniPecConnector.callEServiceRequestId(iniPecCfRequest);
        iniPecBatchPollingConnector.save(createIniPecBatchPolling(batchId,iniPecPolling.getIdentificativoRichiesta()));
        log.info("Calling ini pec with cf size: {} and batchId: {} and pollingId: {}",requestCfIniPec.size(),batchId,iniPecPolling.getIdentificativoRichiesta());

    }

    private IniPecBatchPolling createIniPecBatchPolling(String batchId, String pollingId){
        IniPecBatchPolling iniPecBatchPolling = new IniPecBatchPolling();
        iniPecBatchPolling.setPollingId(pollingId);
        iniPecBatchPolling.setBatchId(batchId);
        iniPecBatchPolling.setStatus(BatchStatus.NOT_WORKED.getValue());
        iniPecBatchPolling.setTimeStamp(LocalDateTime.now());
        return iniPecBatchPolling;
    }

    public void recovery(){
        boolean hasNext = true;
        while(hasNext) {
            hasNext = false;
            List<IniPecBatchRequest> iniPecBatchRequests = iniPecBatchRequestConnector.findAllByBatchIdNotAndStatusWorking(BatchStatus.NO_BATCH_ID.getValue());
            if(iniPecBatchRequests!=null && !iniPecBatchRequests.isEmpty()){
                hasNext = true;
                iniPecBatchRequests.forEach(iniPecBatchRequest -> {
                    iniPecBatchRequest.setBatchId(BatchStatus.NO_BATCH_ID.getValue());
                    iniPecBatchRequest.setStatus(BatchStatus.NOT_WORKED.getValue());
                    iniPecBatchRequestConnector.save(iniPecBatchRequest);
                });
            }
        }
        batchPecListRequest();
    }
}
