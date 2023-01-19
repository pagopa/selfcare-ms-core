package it.pagopa.selfcare.mscore.connector.rest.client;

import it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries.NationalRegistriesAddressRequest;
import it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries.NationalRegistriesAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.national-registries.serviceCode}", url = "${rest-client.national-registries.base-url}")
public interface NationalRegistriesRestClient {

    @GetMapping(value = "${rest-client.national-registries.getLegalAddress.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    NationalRegistriesAddressResponse getLegalAddress(@RequestBody NationalRegistriesAddressRequest nationalRegistriesAddressRequest);
}
