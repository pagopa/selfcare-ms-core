package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.GeoTaxonomiesRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class GeoTaxonomiesConnectorImpl implements GeoTaxonomiesConnector {

    private final GeoTaxonomiesRestClient restClient;

    @Autowired
    public GeoTaxonomiesConnectorImpl(GeoTaxonomiesRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public GeographicTaxonomies getExtByCode(String code) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getExtByCode code = {}", code);
        Assert.hasText(code, "Code is required");
        GeographicTaxonomiesResponse result = restClient.getExtByCode(code);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getExtByCode result = {}", result);
        return toGeoTaxonomies(result);
    }

    private GeographicTaxonomies toGeoTaxonomies(GeographicTaxonomiesResponse result) {
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setDescription(result.getDescription());
        geographicTaxonomies.setGeotax_id(result.getGeotax_id());
        geographicTaxonomies.setEnable(result.isEnable());
        geographicTaxonomies.setRegion_id(result.getRegion_id());
        geographicTaxonomies.setProvince_id(result.getProvince_id());
        geographicTaxonomies.setProvince_abbreviation(result.getProvince_abbreviation());
        geographicTaxonomies.setCountry(result.getCountry());
        geographicTaxonomies.setCountry_abbreviation(result.getCountry_abbreviation());
        geographicTaxonomies.setIstat_code(result.getIstat_code());
        return geographicTaxonomies;
    }
}
