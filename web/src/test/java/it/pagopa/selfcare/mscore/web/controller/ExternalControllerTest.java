package it.pagopa.selfcare.mscore.web.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ExternalController.class})
@ExtendWith(SpringExtension.class)
class ExternalControllerTest {
    @InjectMocks
    private ExternalController externalController;

    @Mock
    private ExternalService externalService;

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId() throws Exception {
        when(externalService.retrieveInstitutionProductsByExternalId((String) any(), (List<RelationshipState>) any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"products\":[]}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId2() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("?");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("?");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        when(externalService.retrieveInstitutionProductsByExternalId((String) any(), (List<RelationshipState>) any()))
                .thenReturn(onboardingList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"products\":[{\"id\":\"42\",\"state\":\"PENDING\"}]}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId3() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("?");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("?");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        Billing billing1 = new Billing();
        billing1.setPublicServices(false);
        billing1.setRecipientCode("U");
        billing1.setVatNumber("?");

        Premium premium1 = new Premium();
        premium1.setContract("U");
        premium1.setStatus(RelationshipState.ACTIVE);

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setContract("U");
        onboarding1.setCreatedAt(null);
        onboarding1.setPremium(premium1);
        onboarding1.setPricingPlan("U");
        onboarding1.setProductId("?");
        onboarding1.setStatus(RelationshipState.ACTIVE);
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        when(externalService.retrieveInstitutionProductsByExternalId((String) any(), (List<RelationshipState>) any()))
                .thenReturn(onboardingList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"products\":[{\"id\":\"?\",\"state\":\"ACTIVE\"},{\"id\":\"42\",\"state\":\"PENDING\"}]}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId4() throws Exception {
        when(externalService.getInstitutionByExternalId((String) any())).thenReturn(new Institution());
        when(externalService.retrieveInstitutionProductsByExternalId((String) any(), (List<RelationshipState>) any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products", "", "Uri Vars");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId5() throws Exception {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(externalService.getInstitutionByExternalId((String) any())).thenReturn(new Institution("42", "42", "?",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                null, null, "?", "?", "?", "jane.doe@example.org", "6625550144", true));
        when(externalService.retrieveInstitutionProductsByExternalId((String) any(), (List<RelationshipState>) any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products", "", "Uri Vars");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"?\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":"
                                        + "\"21654\",\"taxCode\":\"?\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\""
                                        + ":null,\"businessRegisterNumber\":null,\"legalRegisterNumber\":null,\"legalRegisterName\":null,\"vatNumberGroup"
                                        + "\":false},\"dataProtectionOfficer\":{\"address\":null,\"email\":null,\"pec\":null},\"rea\":\"?\",\"shareCapital\":"
                                        + "\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"6625550144\","
                                        + "\"imported\":true}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId() throws Exception {
        when(externalService.retrieveInstitutionGeoTaxonomiesByExternalId((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/geotaxonomies", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId2() throws Exception {
        when(externalService.getInstitutionByExternalId((String) any())).thenReturn(new Institution());
        when(externalService.retrieveInstitutionGeoTaxonomiesByExternalId((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/geotaxonomies", "", "Uri Vars");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId3() throws Exception {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(externalService.getInstitutionByExternalId((String) any())).thenReturn(new Institution("42", "42", "?",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                null, null, "?", "?", "?", "jane.doe@example.org", "6625550144", true));
        when(externalService.retrieveInstitutionGeoTaxonomiesByExternalId((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/geotaxonomies", "", "Uri Vars");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"?\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":"
                                        + "\"21654\",\"taxCode\":\"?\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\""
                                        + ":null,\"businessRegisterNumber\":null,\"legalRegisterNumber\":null,\"legalRegisterName\":null,\"vatNumberGroup"
                                        + "\":false},\"dataProtectionOfficer\":{\"address\":null,\"email\":null,\"pec\":null},\"rea\":\"?\",\"shareCapital\":"
                                        + "\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"6625550144\","
                                        + "\"imported\":true}"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId() throws Exception {
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
        institution.setOnboarding(new ArrayList<>());
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        Institution institution1 = new Institution();
        institution1.setAddress("42 Main St");
        institution1.setAttributes(new ArrayList<>());
        institution1.setBilling(billing1);
        institution1.setDataProtectionOfficer(dataProtectionOfficer1);
        institution1.setDescription("The characteristics of someone or something");
        institution1.setDigitalAddress("42 Main St");
        institution1.setExternalId("42");
        institution1.setGeographicTaxonomies(new ArrayList<>());
        institution1.setId("42");
        institution1.setInstitutionType(InstitutionType.PA);
        institution1.setOnboarding(new ArrayList<>());
        institution1.setIpaCode("42");
        institution1.setPaymentServiceProvider(paymentServiceProvider1);
        institution1.setTaxCode("Tax Code");
        institution1.setZipCode("21654");
        when(externalService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
        when(externalService.getInstitutionByExternalId(any())).thenReturn(institution1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/billing", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutionId\":\"42\",\"externalId\":\"42\",\"ipaCode\":\"42\",\"description\":\"The characteristics"
                                        + " of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main"
                                        + " St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"pricingPlan\":null,\"billing\":null}"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId2() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("?");
        billing1.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("?");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing1);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

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
        institution.setOnboarding(onboardingList);
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        Institution institution1 = new Institution();
        institution1.setAddress("42 Main St");
        institution1.setAttributes(new ArrayList<>());
        institution1.setBilling(billing2);
        institution1.setDataProtectionOfficer(dataProtectionOfficer1);
        institution1.setDescription("The characteristics of someone or something");
        institution1.setDigitalAddress("42 Main St");
        institution1.setExternalId("42");
        institution1.setGeographicTaxonomies(new ArrayList<>());
        institution1.setId("42");
        institution1.setInstitutionType(InstitutionType.PA);
        institution1.setOnboarding(new ArrayList<>());
        institution1.setIpaCode("42");
        institution1.setPaymentServiceProvider(paymentServiceProvider1);
        institution1.setTaxCode("Tax Code");
        institution1.setZipCode("21654");
        when(externalService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
        when(externalService.getInstitutionByExternalId(any())).thenReturn(institution1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/billing", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutionId\":\"42\",\"externalId\":\"42\",\"ipaCode\":\"42\",\"description\":\"The characteristics"
                                        + " of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main"
                                        + " St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"pricingPlan\":\"?\",\"billing\":{\"vatNumber\":\"42\",\"recipientCode"
                                        + "\":\"?\",\"publicServices\":true}}"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId3() throws Exception {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        when(externalService.retrieveInstitutionProduct((String) any(), (String) any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/billing", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutionId\":null,\"externalId\":null,\"originId\":null,\"description\":null,\"institutionType\":null,"
                                        + "\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"pricingPlan\":null,\"billing\":null"
                                        + "}"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId4() throws Exception {
        Institution institution = new Institution();
        institution.setTaxCode("U");
        institution.setZipCode("OX1 1PT");
        institution.setAddress("17 High St");
        institution.setDigitalAddress("17 High St");
        institution.setInstitutionType(InstitutionType.PG);
        institution.setDescription("?");
        institution.setIpaCode("U");
        institution.setExternalId("?");
        institution.setId("?");
        when(externalService.retrieveInstitutionProduct((String) any(), (String) any())).thenReturn(institution);
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId() throws Exception {
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
        institution.setOnboarding(new ArrayList<>());
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(externalService.getInstitutionByExternalId(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"ipaCode\":\"42\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProviderResponse\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false}"));
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId2() throws Exception {
        Attributes attributes = new Attributes();
        attributes.setCode("?");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("?");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);

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
        institution.setAttributes(attributesList);
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(externalService.getInstitutionByExternalId(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"ipaCode\":\"42\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[{\"origin\":\"?\",\"code\":\"?\",\"description\":\"The characteristics of someone or something\"}],\"paymentServiceProviderResponse\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false}"));
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId3() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setCode("?");
        geographicTaxonomies.setCountry("GB");
        geographicTaxonomies.setCountryAbbreviation("GB");
        geographicTaxonomies.setDesc("The characteristics of someone or something");
        geographicTaxonomies.setEnable(true);
        geographicTaxonomies.setEndDate("2020-03-01");
        geographicTaxonomies.setProvince("?");
        geographicTaxonomies.setProvinceAbbreviation("?");
        geographicTaxonomies.setRegion("us-east-2");
        geographicTaxonomies.setStartDate("2020-03-01");

        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        geographicTaxonomiesList.add(geographicTaxonomies);

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
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(externalService.getInstitutionByExternalId(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":\"42\",\"externalId\":\"42\",\"ipaCode\":\"42\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[{\"code\":\"?\",\"desc\":\"The characteristics of someone or something\",\"enable\":true}],\"attributes\":[],\"paymentServiceProviderResponse\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false}"));
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId4() throws Exception {
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
        institution.setOnboarding(new ArrayList<>());
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(externalService.getInstitutionByExternalId(any())).thenReturn(institution);
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId5() throws Exception {
        when(externalService.getInstitutionByExternalId((String) any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId6() throws Exception {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(externalService.getInstitutionByExternalId((String) any())).thenReturn(new Institution("42", "42", "?",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                null, null, "?", "?", "?", "jane.doe@example.org", "6625550144", true));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"?\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":"
                                        + "\"21654\",\"taxCode\":\"?\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\""
                                        + ":null,\"businessRegisterNumber\":null,\"legalRegisterNumber\":null,\"legalRegisterName\":null,\"vatNumberGroup"
                                        + "\":false},\"dataProtectionOfficer\":{\"address\":null,\"email\":null,\"pec\":null},\"rea\":\"?\",\"shareCapital\":"
                                        + "\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"6625550144\","
                                        + "\"imported\":true}"));
    }

    /**
     * Method under test: {@link ExternalController#getManagerInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetManagerInstitutionByExternalId() throws Exception {
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
        institution.setOnboarding(new ArrayList<>());
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setUserId("User");
        productManagerInfo.setInstitution(institution);

        when(externalService.retrieveRelationship(any(), any())).thenReturn("ABC123");
        when(externalService.retrieveInstitutionManager(any(), any())).thenReturn(productManagerInfo);
        when(externalService.getInstitutionByExternalId(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/manager", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link ExternalController#getManagerInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetManagerInstitutionByExternalId2() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("?");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("?");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setInstitution(institution);
        when(externalService.retrieveInstitutionManager((String) any(), (String) any())).thenReturn(productManagerInfo);
        when(externalService.retrieveRelationship((ProductManagerInfo) any(), (String) any())).thenReturn("127.0.0.1");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/manager", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link ExternalController#getManagerInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetManagerInstitutionByExternalId3() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("?");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("?");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<Onboarding> onboarding1 = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();

        Institution institution = new Institution("42", "42", "?", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?", billing1, onboarding1, geographicTaxonomies,
                attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "?", "?", "?",
                "jane.doe@example.org", "6625550144", true);
        institution.setId("?");
        institution.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setUserId("?");
        productManagerInfo.setInstitution(institution);
        when(externalService.retrieveInstitutionManager((String) any(), (String) any())).thenReturn(productManagerInfo);
        when(externalService.retrieveRelationship((ProductManagerInfo) any(), (String) any())).thenReturn("127.0.0.1");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/manager", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }
}


