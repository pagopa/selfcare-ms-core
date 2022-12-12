package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;

import java.util.List;

public interface IniPecBatchRequestConnector {

    IniPecBatchRequest save(IniPecBatchRequest iniPecBatchRequest);
    List<IniPecBatchRequest> findAllByBatchId(String batchId);
    List<IniPecBatchRequest> findAllByBatchIdNotAndStatusWorking(String batchId);
    List<IniPecBatchRequest> setBatchIdAndStatusWorking(List<IniPecBatchRequest> iniPecBatchRequests, String batchId);
}
