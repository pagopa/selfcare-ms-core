package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.mscore.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.mscore.api.InfoCamereConnector;
import it.pagopa.selfcare.mscore.model.infocamere.BatchStatus;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchPolling;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePolling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InfoCamereBatchRequestServiceImpl implements InfoCamereBatchRequestService {

    private final InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;
    private final InfoCamereBatchPollingConnector infoCamereBatchPollingConnector;
    private final InfoCamereConnector infoCamereConnector;

    public InfoCamereBatchRequestServiceImpl(
            InfoCamereBatchRequestConnector infoCamereBatchRequestConnector,
            InfoCamereBatchPollingConnector infoCamereBatchPollingConnector,
            InfoCamereConnector infoCamereConnector) {
        this.infoCamereBatchRequestConnector = infoCamereBatchRequestConnector;
        this.infoCamereBatchPollingConnector = infoCamereBatchPollingConnector;
        this.infoCamereConnector = infoCamereConnector;
    }

    @Override
    public void batchPecListRequest() {
        boolean hasNext = true;
        while(hasNext){
            hasNext = false;
            List<InfoCamereBatchRequest> infoCamereBatchRequests = infoCamereBatchRequestConnector.findAllByBatchId(BatchStatus.NO_BATCH_ID.getValue());
            if(infoCamereBatchRequests != null && !infoCamereBatchRequests.isEmpty()){
                hasNext = true;
                setNewBatchId(infoCamereBatchRequests);
            }
        }
    }

    private void setNewBatchId(List<InfoCamereBatchRequest> infoCamereBatchRequests){
        String batchId = UUID.randomUUID().toString();
        List<InfoCamereBatchRequest> infoCamereBatchRequestsWithBatchId = infoCamereBatchRequestConnector.setBatchIdAndStatusWorking(infoCamereBatchRequests,batchId);
        List<String> requestCfIniPec = infoCamereBatchRequestsWithBatchId.stream()
                .filter(iniPecBatchRequest -> iniPecBatchRequest.getRetry() <= 3)
                .map(InfoCamereBatchRequest::getCf)
                .collect(Collectors.toList());
        callService(requestCfIniPec,batchId);
    }

    private void callService(List<String> requestCfIniPec, String batchId){
        InfoCamereCfRequest infoCamereCfRequest = new InfoCamereCfRequest();
        infoCamereCfRequest.setDataOraRichiesta(LocalDateTime.now().toString());
        infoCamereCfRequest.setElencoCf(requestCfIniPec);
        InfoCamerePolling infoCamerePolling = infoCamereConnector.callEServiceRequestId(infoCamereCfRequest);
        infoCamereBatchPollingConnector.save(createIniPecBatchPolling(batchId, infoCamerePolling.getIdentificativoRichiesta()));
        log.info("Calling ini pec with cf size: {} and batchId: {} and pollingId: {}",requestCfIniPec.size(),batchId, infoCamerePolling.getIdentificativoRichiesta());

    }

    private InfoCamereBatchPolling createIniPecBatchPolling(String batchId, String pollingId){
        InfoCamereBatchPolling infoCamereBatchPolling = new InfoCamereBatchPolling();
        infoCamereBatchPolling.setPollingId(pollingId);
        infoCamereBatchPolling.setBatchId(batchId);
        infoCamereBatchPolling.setStatus(BatchStatus.NOT_WORKED.getValue());
        infoCamereBatchPolling.setTimeStamp(LocalDateTime.now());
        return infoCamereBatchPolling;
    }

    public void recovery(){
        boolean hasNext = true;
        while(hasNext) {
            hasNext = false;
            List<InfoCamereBatchRequest> infoCamereBatchRequests = infoCamereBatchRequestConnector.findAllByBatchIdNotAndStatusWorking(BatchStatus.NO_BATCH_ID.getValue());
            if(infoCamereBatchRequests !=null && !infoCamereBatchRequests.isEmpty()){
                hasNext = true;
                infoCamereBatchRequests.forEach(iniPecBatchRequest -> {
                    iniPecBatchRequest.setBatchId(BatchStatus.NO_BATCH_ID.getValue());
                    iniPecBatchRequest.setStatus(BatchStatus.NOT_WORKED.getValue());
                    infoCamereBatchRequestConnector.save(iniPecBatchRequest);
                });
            }
        }
        batchPecListRequest();
    }
}
