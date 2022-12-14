package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.api.InfoCamereConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.InfoCamerePecResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.InfoCamerePollingResponse;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePec;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePolling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InfoCamereClientImpl implements InfoCamereConnector {

    private final InfoCamereRestClient restClient;

    public InfoCamereClientImpl(InfoCamereRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public InfoCamerePolling callEServiceRequestId(InfoCamereCfRequest infoCamereCfRequest) {
        InfoCamerePollingResponse infoCamerePollingResponse = restClient.callEServiceRequestId(infoCamereCfRequest);
        return convertIniPecPolling(infoCamerePollingResponse);
    }

    @Override
    public InfoCamerePec callEServiceRequestPec(String correlationId) {
        InfoCamerePecResponse infoCamerePecResponse = restClient.callEServiceRequestPec(correlationId);
        return convertIniPecPec(infoCamerePecResponse);
    }

    private InfoCamerePolling convertIniPecPolling(InfoCamerePollingResponse response){
        InfoCamerePolling infoCamerePolling = new InfoCamerePolling();
        infoCamerePolling.setDataOraRichiesta(response.getDataOraRichiesta());
        infoCamerePolling.setIdentificativoRichiesta(response.getIdentificativoRichiesta());
        return infoCamerePolling;
    }

    private InfoCamerePec convertIniPecPec(InfoCamerePecResponse response){
        InfoCamerePec iniPec = new InfoCamerePec();
        iniPec.setElencoPec(response.getElencoPec());
        iniPec.setIdentificativoRichiesta(response.getIdentificativoRichiesta());
        iniPec.setDataOraDownload(response.getDataOraDownload());
        return iniPec;
    }

}
