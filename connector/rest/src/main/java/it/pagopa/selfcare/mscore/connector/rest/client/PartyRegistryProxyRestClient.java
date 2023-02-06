package it.pagopa.selfcare.mscore.connector.rest.client;

import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.InstitutionsByLegalRequest;
import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.InstitutionsByLegalResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.ProxyCategoryResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.ProxyInstitutionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.party-registry-proxy.serviceCode}", url = "${rest-client.party-registry-proxy.base-url}")
public interface PartyRegistryProxyRestClient {

    @GetMapping(value = "${rest-client.party-registry-proxy.getInstitutionById.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    ProxyInstitutionResponse getInstitutionById(@PathVariable("institutionId") String id);

    @GetMapping(value = "${rest-client.party-registry-proxy.getCategory.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    ProxyCategoryResponse getCategory(@PathVariable("origin") String origin, @PathVariable("code") String code);

    @PostMapping(value = "${rest-client.party-registry-proxy.getInstitutionsByLegal.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InstitutionsByLegalResponse getInstitutionsByLegal(@RequestBody InstitutionsByLegalRequest institutions);
}
