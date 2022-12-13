package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.IniPecBatchRequestConnector;
import it.pagopa.selfcare.mscore.model.inipec.BatchStatus;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class IniPecServiceImpl implements IniPecService {

    private final IniPecBatchRequestConnector iniPecBatchRequestConnector;

    public IniPecServiceImpl(
            IniPecBatchRequestConnector iniPecBatchRequestConnector) {
        this.iniPecBatchRequestConnector = iniPecBatchRequestConnector;
    }

    @Override
    public IniPecBatchRequest createBatchRequestByCf(String cf){
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setCf(cf);
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setBatchId(BatchStatus.NO_BATCH_ID.getValue());
        iniPecBatchRequest.setStatus(BatchStatus.NOT_WORKED.getValue());
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        log.info("Created Batch Request for taxId: {}",cf);
        return iniPecBatchRequestConnector.save(iniPecBatchRequest);
    }
}
