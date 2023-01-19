package it.pagopa.selfcare.mscore.connector.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.rest.client.NationalRegistriesRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries.NationalRegistriesAddressResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries.NationalRegistriesProfessionalResponse;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.NationalRegistriesProfessionalAddress;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class NationalRegistriesConnectorImplTest {
    @InjectMocks
    private NationalRegistriesConnectorImpl nationalRegistriesConnectorImpl;

    @Mock
    private NationalRegistriesRestClient nationalRegistriesRestClient;

    /**
     * Method under test: {@link NationalRegistriesConnectorImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress() {
        NationalRegistriesProfessionalResponse nationalRegistriesProfessionalResponse = new NationalRegistriesProfessionalResponse();
        nationalRegistriesProfessionalResponse.setAddress("42 Main St");
        nationalRegistriesProfessionalResponse.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalResponse.setMunicipality("Municipality");
        nationalRegistriesProfessionalResponse.setProvince("Province");
        nationalRegistriesProfessionalResponse.setZip("21654");

        NationalRegistriesAddressResponse nationalRegistriesAddressResponse = new NationalRegistriesAddressResponse();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        nationalRegistriesAddressResponse
                .setDateTimeExtraction(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        nationalRegistriesAddressResponse.setProfessionalAddress(nationalRegistriesProfessionalResponse);
        nationalRegistriesAddressResponse.setTaxId("42");
        when(nationalRegistriesRestClient.getLegalAddress(any()))
                .thenReturn(nationalRegistriesAddressResponse);
        NationalRegistriesProfessionalAddress actualLegalAddress = nationalRegistriesConnectorImpl
                .getLegalAddress("Tax Code");
        assertEquals("42 Main St", actualLegalAddress.getAddress());
        assertEquals("21654", actualLegalAddress.getZip());
        assertEquals("Province", actualLegalAddress.getProvince());
        assertEquals("Municipality", actualLegalAddress.getMunicipality());
        assertEquals("The characteristics of someone or something", actualLegalAddress.getDescription());
        verify(nationalRegistriesRestClient).getLegalAddress(any());
    }

    /**
     * Method under test: {@link NationalRegistriesConnectorImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress2() {
        when(nationalRegistriesRestClient.getLegalAddress(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> nationalRegistriesConnectorImpl.getLegalAddress("Tax Code"));
        verify(nationalRegistriesRestClient).getLegalAddress(any());
    }
}

