package it.pagopa.selfcare.mscore.connector.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.rest.client.GeoTaxonomiesRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class GeoTaxonomiesConnectorImplTest {
    @InjectMocks
    private GeoTaxonomiesConnectorImpl geoTaxonomiesConnectorImpl;

    @Mock
    private GeoTaxonomiesRestClient geoTaxonomiesRestClient;

    @Test
    void testGetExtByCode() {
        GeographicTaxonomiesResponse geographicTaxonomiesResponse = new GeographicTaxonomiesResponse();
        geographicTaxonomiesResponse.setCode("Code");
        geographicTaxonomiesResponse.setCountry("GB");
        geographicTaxonomiesResponse.setCountryAbbreviation("GB");
        geographicTaxonomiesResponse.setDesc("The characteristics of someone or something");
        geographicTaxonomiesResponse.setEnable(true);
        geographicTaxonomiesResponse.setEndDate("2020-03-01");
        geographicTaxonomiesResponse.setProvince("Province");
        geographicTaxonomiesResponse.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomiesResponse.setRegion("us-east-2");
        geographicTaxonomiesResponse.setStartDate("2020-03-01");
        when(geoTaxonomiesRestClient.getExtByCode(any())).thenReturn(geographicTaxonomiesResponse);
        GeographicTaxonomies actualExtByCode = geoTaxonomiesConnectorImpl.getExtByCode("Code");
        assertEquals("Code", actualExtByCode.getCode());
        assertTrue(actualExtByCode.isEnable());
        assertEquals("The characteristics of someone or something", actualExtByCode.getDesc());
        verify(geoTaxonomiesRestClient).getExtByCode(any());
    }

    @Test
    void testGetExtByCode2() {
        GeographicTaxonomiesResponse geographicTaxonomiesResponse = new GeographicTaxonomiesResponse();
        geographicTaxonomiesResponse.setCode("Code");
        geographicTaxonomiesResponse.setCountry("GB");
        geographicTaxonomiesResponse.setCountryAbbreviation("GB");
        geographicTaxonomiesResponse.setDesc("The characteristics of someone or something");
        geographicTaxonomiesResponse.setEnable(false);
        geographicTaxonomiesResponse.setEndDate("2020-03-01");
        geographicTaxonomiesResponse.setProvince("Province");
        geographicTaxonomiesResponse.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomiesResponse.setRegion("us-east-2");
        geographicTaxonomiesResponse.setStartDate("2020-03-01");
        when(geoTaxonomiesRestClient.getExtByCode(any())).thenReturn(geographicTaxonomiesResponse);
        GeographicTaxonomies actualExtByCode = geoTaxonomiesConnectorImpl.getExtByCode("Code");
        assertEquals("Code", actualExtByCode.getCode());
        assertFalse(actualExtByCode.isEnable());
        assertEquals("The characteristics of someone or something", actualExtByCode.getDesc());
        verify(geoTaxonomiesRestClient).getExtByCode(any());
    }

}

