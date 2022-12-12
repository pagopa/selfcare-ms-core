package it.pagopa.selfcare.mscore.connector.rest.client;

import it.pagopa.selfcare.mscore.connector.rest.model.*;
import it.pagopa.selfcare.mscore.model.inipec.IniPecCfRequest;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface IniPecRestClient {

    @PostMapping(value = "${rest-client.inipec.callEServiceRequestId.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    IniPecPollingResponse callEServiceRequestId(@PathVariable("cf") IniPecCfRequest iniPecCfRequest);

    @PostMapping(value = "${rest-client.inipec.callEServiceRequestPec.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    IniPecPecResponse callEServiceRequestPec(@PathVariable("correlationId") String correlationId);

}
