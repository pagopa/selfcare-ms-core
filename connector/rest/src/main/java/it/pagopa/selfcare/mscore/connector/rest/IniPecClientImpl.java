package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.api.IniPecConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.IniPecRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.IniPecPecResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.IniPecPollingResponse;
import it.pagopa.selfcare.mscore.model.inipec.IniPecCfRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPec;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPolling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IniPecClientImpl implements IniPecConnector {

    private final IniPecRestClient restClient;

    public IniPecClientImpl(IniPecRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public IniPecPolling callEServiceRequestId(IniPecCfRequest iniPecCfRequest) {
        IniPecPollingResponse iniPecPollingResponse = restClient.callEServiceRequestId(iniPecCfRequest);
        return convertIniPecPolling(iniPecPollingResponse);
    }

    @Override
    public IniPecPec callEServiceRequestPec(String correlationId) {
        IniPecPecResponse iniPecPecResponse = restClient.callEServiceRequestPec(correlationId);
        return convertIniPecPec(iniPecPecResponse);
    }

    private IniPecPolling convertIniPecPolling(IniPecPollingResponse response){
        IniPecPolling iniPecPolling = new IniPecPolling();
        iniPecPolling.setDataOraRichiesta(response.getDataOraRichiesta());
        iniPecPolling.setIdentificativoRichiesta(response.getIdentificativoRichiesta());
        return iniPecPolling;
    }

    private IniPecPec convertIniPecPec(IniPecPecResponse response){
        IniPecPec iniPec = new IniPecPec();
        iniPec.setElencoPec(response.getElencoPec());
        iniPec.setIdentificativoRichiesta(response.getIdentificativoRichiesta());
        iniPec.setDataOraDownload(response.getDataOraDownload());
        return iniPec;
    }

}
