package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;

import java.util.List;

public interface InfoCamereBatchRequestConnector {

    InfoCamereBatchRequest save(InfoCamereBatchRequest infoCamereBatchRequest);
    List<InfoCamereBatchRequest> findAllByBatchId(String batchId);
    List<InfoCamereBatchRequest> findAllByBatchIdNotAndStatusWorking(String batchId);
    List<InfoCamereBatchRequest> setBatchIdAndStatusWorking(List<InfoCamereBatchRequest> infoCamereBatchRequests, String batchId);
}
