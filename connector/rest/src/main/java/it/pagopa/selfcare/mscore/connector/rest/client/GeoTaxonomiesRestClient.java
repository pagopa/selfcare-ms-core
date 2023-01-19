package it.pagopa.selfcare.mscore.connector.rest.client;

import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface GeoTaxonomiesRestClient {

    //TODO: CAPIRE LA STRUTTURA DEL CLIENT E APPORTARE LE OPPORTUNE MODIFICHE
    @GetMapping(value = "${rest-client.geo-taxonomies.getExtByCode.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    GeographicTaxonomies getExtByCode(@RequestParam (value = "code") String code);
}
