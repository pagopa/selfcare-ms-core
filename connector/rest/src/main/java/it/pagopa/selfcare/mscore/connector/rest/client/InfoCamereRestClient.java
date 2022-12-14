package it.pagopa.selfcare.mscore.connector.rest.client;

import it.pagopa.selfcare.mscore.connector.rest.model.InfoCamerePecResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.InfoCamerePollingResponse;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereCfRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@FeignClient(name = "${rest-client.info-camere.serviceCode}", url = "${rest-client.inipec.base-url}")
public interface InfoCamereRestClient {

    @PostMapping(value = "${rest-client.info-camere.callEServiceRequestId.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InfoCamerePollingResponse callEServiceRequestId(@PathVariable("cf") InfoCamereCfRequest infoCamereCfRequest);

    @PostMapping(value = "${rest-client.info-camere.callEServiceRequestPec.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InfoCamerePecResponse callEServiceRequestPec(@PathVariable("correlationId") String correlationId);

}
