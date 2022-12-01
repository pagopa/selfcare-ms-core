package it.pagopa.selfcare.mscore.connector.rest.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${rest-client.service-name.serviceCode}", url = "${rest-client.service-name.base-url}")
public interface ServiceNameRestClient {//TODO change Name
}
