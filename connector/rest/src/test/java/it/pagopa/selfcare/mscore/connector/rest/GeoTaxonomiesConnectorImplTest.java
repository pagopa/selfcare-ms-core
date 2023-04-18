package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.client.GeoTaxonomiesRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class GeoTaxonomiesConnectorImplTest {
    @InjectMocks
    private GeoTaxonomiesConnectorImpl geoTaxonomiesConnectorImpl;

    @Mock
    private GeoTaxonomiesRestClient geoTaxonomiesRestClient;

    @Test
    void testGetExtByCode() {
        GeographicTaxonomiesResponse geographicTaxonomiesResponse = new GeographicTaxonomiesResponse();
        geographicTaxonomiesResponse.setGeotax_id("Code");
        geographicTaxonomiesResponse.setCountry("GB");
        geographicTaxonomiesResponse.setCountry_abbreviation("GB");
        geographicTaxonomiesResponse.setDescription("The characteristics of someone or something");
        geographicTaxonomiesResponse.setEnable(true);
        geographicTaxonomiesResponse.setIstat_code("");
        geographicTaxonomiesResponse.setProvince_id("Province");
        geographicTaxonomiesResponse.setProvince_abbreviation("Province Abbreviation");
        geographicTaxonomiesResponse.setRegion_id("us-east-2");
        when(geoTaxonomiesRestClient.getExtByCode(any())).thenReturn(geographicTaxonomiesResponse);
        GeographicTaxonomies actualExtByCode = geoTaxonomiesConnectorImpl.getExtByCode("Code");
        assertEquals("Code", actualExtByCode.getGeotax_id());
        assertTrue(actualExtByCode.isEnable());
        assertEquals("The characteristics of someone or something", actualExtByCode.getDescription());
        verify(geoTaxonomiesRestClient).getExtByCode(any());
    }

    @Test
    void testGetExtByCode2() {
        GeographicTaxonomiesResponse geographicTaxonomiesResponse = new GeographicTaxonomiesResponse();
        geographicTaxonomiesResponse.setGeotax_id("Code");
        geographicTaxonomiesResponse.setCountry("GB");
        geographicTaxonomiesResponse.setCountry_abbreviation("GB");
        geographicTaxonomiesResponse.setDescription("The characteristics of someone or something");
        geographicTaxonomiesResponse.setEnable(false);
        geographicTaxonomiesResponse.setIstat_code("");
        geographicTaxonomiesResponse.setProvince_id("Province");
        geographicTaxonomiesResponse.setProvince_abbreviation("Province Abbreviation");
        geographicTaxonomiesResponse.setRegion_id("us-east-2");
        when(geoTaxonomiesRestClient.getExtByCode(any())).thenReturn(geographicTaxonomiesResponse);
        GeographicTaxonomies actualExtByCode = geoTaxonomiesConnectorImpl.getExtByCode("Code");
        assertEquals("Code", actualExtByCode.getGeotax_id());
        assertFalse(actualExtByCode.isEnable());
        assertEquals("The characteristics of someone or something", actualExtByCode.getDescription());
        verify(geoTaxonomiesRestClient).getExtByCode(any());
    }

}

