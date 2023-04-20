package it.pagopa.selfcare.mscore.connector.rest.client;

import it.pagopa.selfcare.mscore.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.geo-taxonomies.serviceCode}", url = "${rest-client.geo-taxonomies.base-url}")
public interface GeoTaxonomiesRestClient {

    @GetMapping(value = "${rest-client.geo-taxonomies.getByCode.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    GeographicTaxonomiesResponse getExtByCode(@PathVariable(value = "geotax_id") String code);
}
