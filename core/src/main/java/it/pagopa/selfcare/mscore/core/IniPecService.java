package it.pagopa.selfcare.mscore.core;


import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;

public interface IniPecService {

    IniPecBatchRequest createBatchRequestByCf(String cf);

}
