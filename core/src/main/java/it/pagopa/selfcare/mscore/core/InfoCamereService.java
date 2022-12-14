package it.pagopa.selfcare.mscore.core;


import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;

public interface InfoCamereService {

    InfoCamereBatchRequest createBatchRequestByCf(String cf);

}
