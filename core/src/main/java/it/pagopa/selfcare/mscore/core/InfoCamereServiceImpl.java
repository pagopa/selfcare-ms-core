package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.mscore.model.infocamere.BatchStatus;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class InfoCamereServiceImpl implements InfoCamereService {

    private final InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;

    public InfoCamereServiceImpl(
            InfoCamereBatchRequestConnector infoCamereBatchRequestConnector) {
        this.infoCamereBatchRequestConnector = infoCamereBatchRequestConnector;
    }

    @Override
    public InfoCamereBatchRequest createBatchRequestByCf(String cf){
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setCf(cf);
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setBatchId(BatchStatus.NO_BATCH_ID.getValue());
        infoCamereBatchRequest.setStatus(BatchStatus.NOT_WORKED.getValue());
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        log.info("Created Batch Request for taxId: {}",cf);
        return infoCamereBatchRequestConnector.save(infoCamereBatchRequest);
    }
}
