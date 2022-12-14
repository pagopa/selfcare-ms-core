package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePec;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePolling;

public interface InfoCamereConnector {

    InfoCamerePolling callEServiceRequestId(InfoCamereCfRequest infoCamereCfRequest);

    InfoCamerePec callEServiceRequestPec(String correlationId);
}
