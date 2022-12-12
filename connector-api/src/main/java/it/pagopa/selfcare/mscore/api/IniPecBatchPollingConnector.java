package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchPolling;

import java.util.List;


public interface IniPecBatchPollingConnector {

    IniPecBatchPolling save(IniPecBatchPolling iniPecBatchPolling);
    List<IniPecBatchPolling> findByStatus(String status);

}
