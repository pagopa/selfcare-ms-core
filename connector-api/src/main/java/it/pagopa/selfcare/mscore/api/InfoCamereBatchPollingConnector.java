package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchPolling;

import java.util.List;


public interface InfoCamereBatchPollingConnector {

    InfoCamereBatchPolling save(InfoCamereBatchPolling infoCamereBatchPolling);
    List<InfoCamereBatchPolling> findByStatus(String status);

}
