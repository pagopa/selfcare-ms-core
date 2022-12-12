package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.inipec.IniPecCfRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPec;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPolling;

public interface IniPecConnector {

    IniPecPolling callEServiceRequestId(IniPecCfRequest iniPecCfRequest);

    IniPecPec callEServiceRequestPec(String correlationId);
}
