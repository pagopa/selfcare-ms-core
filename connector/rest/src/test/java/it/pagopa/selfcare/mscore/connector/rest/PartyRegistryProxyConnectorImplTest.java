package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.client.PartyRegistryProxyRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.Institutions;
import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.InstitutionsByLegalResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.ProxyCategoryResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.ProxyInstitutionResponse;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.InstitutionByLegal;
import it.pagopa.selfcare.mscore.model.NationalRegistriesProfessionalAddress;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {PartyRegistryProxyConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class PartyRegistryProxyConnectorImplTest {
    @Autowired
    private PartyRegistryProxyConnectorImpl partyRegistryProxyConnectorImpl;

    @MockBean
    private PartyRegistryProxyRestClient partyRegistryProxyRestClient;

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionById(String)}
     */
    @Test
    void testGetInstitutionById() {
        ProxyInstitutionResponse proxyInstitutionResponse = new ProxyInstitutionResponse();
        proxyInstitutionResponse.setAddress("42 Main St");
        proxyInstitutionResponse.setAoo("Aoo");
        proxyInstitutionResponse.setCategory("Category");
        proxyInstitutionResponse.setDescription("The characteristics of someone or something");
        proxyInstitutionResponse.setDigitalAddress("42 Main St");
        proxyInstitutionResponse.setId("42");
        proxyInstitutionResponse.setO("foo");
        proxyInstitutionResponse.setOrigin("Origin");
        proxyInstitutionResponse.setOriginId("42");
        proxyInstitutionResponse.setOu("Ou");
        proxyInstitutionResponse.setTaxCode("Tax Code");
        proxyInstitutionResponse.setZipCode("21654");
        when(partyRegistryProxyRestClient.getInstitutionById(any())).thenReturn(proxyInstitutionResponse);
        InstitutionProxyInfo actualInstitutionById = partyRegistryProxyConnectorImpl.getInstitutionById("42");
        assertEquals("42 Main St", actualInstitutionById.getAddress());
        assertEquals("21654", actualInstitutionById.getZipCode());
        assertEquals("Tax Code", actualInstitutionById.getTaxCode());
        assertEquals("Ou", actualInstitutionById.getOu());
        assertEquals("42", actualInstitutionById.getOriginId());
        assertEquals("Origin", actualInstitutionById.getOrigin());
        assertEquals("foo", actualInstitutionById.getO());
        assertEquals("42", actualInstitutionById.getId());
        assertEquals("42 Main St", actualInstitutionById.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualInstitutionById.getDescription());
        assertEquals("Category", actualInstitutionById.getCategory());
        assertEquals("Aoo", actualInstitutionById.getAoo());
        verify(partyRegistryProxyRestClient).getInstitutionById(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionById(String)}
     */
    @Test
    void testGetInstitutionById2() {
        when(partyRegistryProxyRestClient.getInstitutionById(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> partyRegistryProxyConnectorImpl.getInstitutionById("42"));
        verify(partyRegistryProxyRestClient).getInstitutionById(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionById(String)}
     */
    @Test
    void testGetInstitutionById3() {
        ProxyInstitutionResponse proxyInstitutionResponse = new ProxyInstitutionResponse();
        proxyInstitutionResponse.setAddress("42 Main St");
        proxyInstitutionResponse.setAoo("Aoo");
        proxyInstitutionResponse.setCategory("Category");
        proxyInstitutionResponse.setDescription("The characteristics of someone or something");
        proxyInstitutionResponse.setDigitalAddress("42 Main St");
        proxyInstitutionResponse.setId("42");
        proxyInstitutionResponse.setO("foo");
        proxyInstitutionResponse.setOrigin("Origin");
        proxyInstitutionResponse.setOriginId("42");
        proxyInstitutionResponse.setOu("Ou");
        proxyInstitutionResponse.setTaxCode("Tax Code");
        proxyInstitutionResponse.setZipCode("21654");
        when(partyRegistryProxyRestClient.getInstitutionById(any())).thenReturn(proxyInstitutionResponse);
        InstitutionProxyInfo actualInstitutionById = partyRegistryProxyConnectorImpl.getInstitutionById("42");
        assertEquals("42 Main St", actualInstitutionById.getAddress());
        assertEquals("21654", actualInstitutionById.getZipCode());
        assertEquals("Tax Code", actualInstitutionById.getTaxCode());
        assertEquals("Ou", actualInstitutionById.getOu());
        assertEquals("42", actualInstitutionById.getOriginId());
        assertEquals("Origin", actualInstitutionById.getOrigin());
        assertEquals("foo", actualInstitutionById.getO());
        assertEquals("42", actualInstitutionById.getId());
        assertEquals("42 Main St", actualInstitutionById.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualInstitutionById.getDescription());
        assertEquals("Category", actualInstitutionById.getCategory());
        assertEquals("Aoo", actualInstitutionById.getAoo());
        verify(partyRegistryProxyRestClient).getInstitutionById(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionById(String)}
     */
    @Test
    void testGetInstitutionById4() {
        when(partyRegistryProxyRestClient.getInstitutionById(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> partyRegistryProxyConnectorImpl.getInstitutionById("42"));
        verify(partyRegistryProxyRestClient).getInstitutionById(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getCategory(String, String)}
     */
    @Test
    void testGetCategory() {
        ProxyCategoryResponse proxyCategoryResponse = new ProxyCategoryResponse();
        proxyCategoryResponse.setCode("Code");
        proxyCategoryResponse.setKind("Kind");
        proxyCategoryResponse.setName("Name");
        proxyCategoryResponse.setOrigin("Origin");
        when(partyRegistryProxyRestClient.getCategory(any(), any())).thenReturn(proxyCategoryResponse);
        CategoryProxyInfo actualCategory = partyRegistryProxyConnectorImpl.getCategory("Origin", "Code");
        assertEquals("Code", actualCategory.getCode());
        assertEquals("Origin", actualCategory.getOrigin());
        assertEquals("Name", actualCategory.getName());
        assertEquals("Kind", actualCategory.getKind());
        verify(partyRegistryProxyRestClient).getCategory(any(), any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getCategory(String, String)}
     */
    @Test
    void testGetCategory2() {
        when(partyRegistryProxyRestClient.getCategory(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class,
                () -> partyRegistryProxyConnectorImpl.getCategory("Origin", "Code"));
        verify(partyRegistryProxyRestClient).getCategory(any(), any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getCategory(String, String)}
     */
    @Test
    void testGetCategory3() {
        ProxyCategoryResponse proxyCategoryResponse = new ProxyCategoryResponse();
        proxyCategoryResponse.setCode("Code");
        proxyCategoryResponse.setKind("Kind");
        proxyCategoryResponse.setName("Name");
        proxyCategoryResponse.setOrigin("Origin");
        when(partyRegistryProxyRestClient.getCategory(any(), any())).thenReturn(proxyCategoryResponse);
        CategoryProxyInfo actualCategory = partyRegistryProxyConnectorImpl.getCategory("Origin", "Code");
        assertEquals("Code", actualCategory.getCode());
        assertEquals("Origin", actualCategory.getOrigin());
        assertEquals("Name", actualCategory.getName());
        assertEquals("Kind", actualCategory.getKind());
        verify(partyRegistryProxyRestClient).getCategory(any(), any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getCategory(String, String)}
     */
    @Test
    void testGetCategory4() {
        when(partyRegistryProxyRestClient.getCategory(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class,
                () -> partyRegistryProxyConnectorImpl.getCategory("Origin", "Code"));
        verify(partyRegistryProxyRestClient).getCategory(any(), any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal() {
        InstitutionsByLegalResponse institutionsByLegalResponse = new InstitutionsByLegalResponse();
        institutionsByLegalResponse.setBusinesses(new ArrayList<>());
        institutionsByLegalResponse.setLegalTaxId("42");
        institutionsByLegalResponse.setRequestDateTime("2020-03-01");
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenReturn(institutionsByLegalResponse);
        assertTrue(partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42").isEmpty());
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal2() {
        Institutions institutions = new Institutions();
        institutions.setBusinessName("Business Name");
        institutions.setBusinessTaxId("42");

        ArrayList<Institutions> institutionsList = new ArrayList<>();
        institutionsList.add(institutions);

        InstitutionsByLegalResponse institutionsByLegalResponse = new InstitutionsByLegalResponse();
        institutionsByLegalResponse.setBusinesses(institutionsList);
        institutionsByLegalResponse.setLegalTaxId("42");
        institutionsByLegalResponse.setRequestDateTime("2020-03-01");
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenReturn(institutionsByLegalResponse);
        List<InstitutionByLegal> actualInstitutionsByLegal = partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42");
        assertEquals(1, actualInstitutionsByLegal.size());
        InstitutionByLegal getResult = actualInstitutionsByLegal.get(0);
        assertEquals("Business Name", getResult.getBusinessName());
        assertEquals("42", getResult.getBusinessTaxId());
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal3() {
        Institutions institutions = new Institutions();
        institutions.setBusinessName("Business Name");
        institutions.setBusinessTaxId("42");

        Institutions institutions1 = new Institutions();
        institutions1.setBusinessName("Business Name");
        institutions1.setBusinessTaxId("42");

        ArrayList<Institutions> institutionsList = new ArrayList<>();
        institutionsList.add(institutions1);
        institutionsList.add(institutions);

        InstitutionsByLegalResponse institutionsByLegalResponse = new InstitutionsByLegalResponse();
        institutionsByLegalResponse.setBusinesses(institutionsList);
        institutionsByLegalResponse.setLegalTaxId("42");
        institutionsByLegalResponse.setRequestDateTime("2020-03-01");
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenReturn(institutionsByLegalResponse);
        List<InstitutionByLegal> actualInstitutionsByLegal = partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42");
        assertEquals(2, actualInstitutionsByLegal.size());
        InstitutionByLegal getResult = actualInstitutionsByLegal.get(0);
        assertEquals("42", getResult.getBusinessTaxId());
        InstitutionByLegal getResult1 = actualInstitutionsByLegal.get(1);
        assertEquals("42", getResult1.getBusinessTaxId());
        assertEquals("Business Name", getResult1.getBusinessName());
        assertEquals("Business Name", getResult.getBusinessName());
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal4() {
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42"));
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal5() {
        InstitutionsByLegalResponse institutionsByLegalResponse = new InstitutionsByLegalResponse();
        institutionsByLegalResponse.setBusinesses(new ArrayList<>());
        institutionsByLegalResponse.setLegalTaxId("42");
        institutionsByLegalResponse.setRequestDateTime("2020-03-01");
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenReturn(institutionsByLegalResponse);
        assertTrue(partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42").isEmpty());
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal6() {
        Institutions institutions = new Institutions();
        institutions.setBusinessName("Business Name");
        institutions.setBusinessTaxId("42");

        ArrayList<Institutions> institutionsList = new ArrayList<>();
        institutionsList.add(institutions);

        InstitutionsByLegalResponse institutionsByLegalResponse = new InstitutionsByLegalResponse();
        institutionsByLegalResponse.setBusinesses(institutionsList);
        institutionsByLegalResponse.setLegalTaxId("42");
        institutionsByLegalResponse.setRequestDateTime("2020-03-01");
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenReturn(institutionsByLegalResponse);
        List<InstitutionByLegal> actualInstitutionsByLegal = partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42");
        assertEquals(1, actualInstitutionsByLegal.size());
        InstitutionByLegal getResult = actualInstitutionsByLegal.get(0);
        assertEquals("Business Name", getResult.getBusinessName());
        assertEquals("42", getResult.getBusinessTaxId());
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal7() {
        Institutions institutions = new Institutions();
        institutions.setBusinessName("Business Name");
        institutions.setBusinessTaxId("42");

        Institutions institutions1 = new Institutions();
        institutions1.setBusinessName("Business Name");
        institutions1.setBusinessTaxId("42");

        ArrayList<Institutions> institutionsList = new ArrayList<>();
        institutionsList.add(institutions1);
        institutionsList.add(institutions);

        InstitutionsByLegalResponse institutionsByLegalResponse = new InstitutionsByLegalResponse();
        institutionsByLegalResponse.setBusinesses(institutionsList);
        institutionsByLegalResponse.setLegalTaxId("42");
        institutionsByLegalResponse.setRequestDateTime("2020-03-01");
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenReturn(institutionsByLegalResponse);
        List<InstitutionByLegal> actualInstitutionsByLegal = partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42");
        assertEquals(2, actualInstitutionsByLegal.size());
        InstitutionByLegal getResult = actualInstitutionsByLegal.get(0);
        assertEquals("42", getResult.getBusinessTaxId());
        InstitutionByLegal getResult1 = actualInstitutionsByLegal.get(1);
        assertEquals("42", getResult1.getBusinessTaxId());
        assertEquals("Business Name", getResult1.getBusinessName());
        assertEquals("Business Name", getResult.getBusinessName());
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getInstitutionsByLegal(String)}
     */
    @Test
    void testGetInstitutionsByLegal8() {
        when(partyRegistryProxyRestClient.getInstitutionsByLegal(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> partyRegistryProxyConnectorImpl.getInstitutionsByLegal("42"));
        verify(partyRegistryProxyRestClient).getInstitutionsByLegal(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress() {
        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setAddress("42 Main St");
        nationalRegistriesProfessionalAddress.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalAddress.setMunicipality("Municipality");
        nationalRegistriesProfessionalAddress.setProvince("Province");
        nationalRegistriesProfessionalAddress.setZip("21654");
        when(partyRegistryProxyRestClient.getLegalAddress(any()))
                .thenReturn(nationalRegistriesProfessionalAddress);
        assertSame(nationalRegistriesProfessionalAddress, partyRegistryProxyConnectorImpl.getLegalAddress("42"));
        verify(partyRegistryProxyRestClient).getLegalAddress(any());
    }

    /**
     * Method under test: {@link PartyRegistryProxyConnectorImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress2() {
        when(partyRegistryProxyRestClient.getLegalAddress(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> partyRegistryProxyConnectorImpl.getLegalAddress("42"));
        verify(partyRegistryProxyRestClient).getLegalAddress(any());
    }

    @Test
    void testGetInstitutionByIdThrow() {
        when(partyRegistryProxyRestClient.getInstitutionById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> partyRegistryProxyConnectorImpl.getInstitutionById("id"));
        verify(partyRegistryProxyRestClient).getInstitutionById(any());
    }
}

