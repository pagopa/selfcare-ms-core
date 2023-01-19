package it.pagopa.selfcare.mscore.web.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.web.model.institution.AttributesRequest;
import it.pagopa.selfcare.mscore.web.model.institution.DataProtectionOfficerRequest;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.institution.PaymentServiceProviderRequest;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {InstitutionController.class})
@ExtendWith(SpringExtension.class)
class InstitutionControllerTest {
    @Autowired
    private InstitutionController institutionController;

    @MockBean
    private InstitutionService institutionService;

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitutionByExternalId((String) any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\"}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionRaw(String, InstitutionRequest)}
     */
    @Test
    void testCreateInstitutionRaw() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitutionRaw((Institution) any(), (String) any())).thenReturn(institution);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(new ArrayList<>());
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\"}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionRaw(String, InstitutionRequest)}
     */
    @Test
    void testCreateInstitutionRaw2() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitutionRaw((Institution) any(), (String) any())).thenReturn(institution);

        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("?");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("?");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\"}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionRaw(String, InstitutionRequest)}
     */
    @Test
    void testCreateInstitutionRaw3() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitutionRaw((Institution) any(), (String) any())).thenReturn(institution);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("?");
        geoTaxonomies.setDesc("The characteristics of someone or something");
        geoTaxonomies.setEnable(true);

        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        geoTaxonomiesList.add(geoTaxonomies);

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(new ArrayList<>());
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\"}"));
    }

    /**
     * Method under test: {@link InstitutionController#createPgInstitution(String)}
     */
    @Test
    void testCreatePgInstitution() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createPgInstitution((String) any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/pg/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\"}"));
    }
}

