package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.web.model.institution.CreatePnPgInstitutionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        when(externalService.retrieveInstitutionProductsByExternalId(any(), any()))
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

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        when(externalService.retrieveInstitutionProductsByExternalId(any(), any()))
                .thenReturn(onboardingList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"products\":[{\"id\":\"42\",\"state\":\"PENDING\"}]}"));
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

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        Billing billing1 = new Billing();
        billing1.setPublicServices(false);
        billing1.setRecipientCode("U");
        billing1.setVatNumber("?");

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setClosedAt(null);
        onboarding1.setContract("U");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("U");
        onboarding1.setProductId("?");
        onboarding1.setStatus(RelationshipState.ACTIVE);
        onboarding1.setTokenId("ABC123");
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        when(externalService.retrieveInstitutionProductsByExternalId(any(), any()))
                .thenReturn(onboardingList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"products\":[{\"id\":\"?\",\"state\":\"ACTIVE\"},{\"id\":\"42\",\"state\":\"PENDING\"}]}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId() throws Exception {
        when(externalService.retrieveInstitutionGeoTaxonomiesByExternalId(any())).thenReturn(new ArrayList<>());
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
     * Method under test: {@link ExternalController#createPnPgInstitution(CreatePnPgInstitutionRequest)}
     */
    @Test
    void testCreatePnPgInstitution() throws Exception {
        when(externalService.createPnPgInstitution(any(), any())).thenReturn(new Institution());

        CreatePnPgInstitutionRequest createPnPgInstitutionRequest = new CreatePnPgInstitutionRequest();
        createPnPgInstitutionRequest.setDescription("The characteristics of someone or something");
        createPnPgInstitutionRequest.setTaxId("42");
        String content = (new ObjectMapper()).writeValueAsString(createPnPgInstitutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/external/institutions/pn-pg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{}"));
    }

    @Test
    void getUserInstitutionRelationshipsByExternalId() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        when(externalService.getUserInstitutionRelationships(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/relationships", "42")
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(externalController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId3() throws Exception {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        when(externalService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/billing", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId5() throws Exception {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        when(externalService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/billing", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutionId\":null,\"externalId\":null,\"origin\":null,\"originId\":null,\"description\":null,\"institutionType\":null,\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"pricingPlan\":null,\"billing\":null,\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}"));
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
        institution.setOriginId("42");
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
                        .string("{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"42\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false}"));
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
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(externalService.getInstitutionByExternalId("42")).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"42\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[{\"origin\":\"?\",\"code\":\"?\",\"description\":\"The characteristics of someone or something\"}],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false}"));

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

        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("?");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
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
        institution.setOriginId("42");
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
                        .string("{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"42\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[{\"code\":\"?\",\"desc\":\"The characteristics of someone or something\"}],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false}"));
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId5() throws Exception {
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution());
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
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution("42", "42", Origin.SELC.name(), "?",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                null, null, "?", "?", "?", true, OffsetDateTime.now(), OffsetDateTime.now(), "BB123", "UO","AA123"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link ExternalController#getByExternalId(String)}
     */
    @Test
    void testGetByExternalId7() throws Exception {
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution());
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
    void testGetByExternalId8() throws Exception {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("?", "42", "?", "42", true);

        when(externalService.getInstitutionByExternalId(any()))
                .thenReturn(new Institution("42", "42", Origin.MOCK.name(), "42", "The characteristics of someone or something",
                        InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?", billing, onboarding, geographicTaxonomies,
                        attributes, paymentServiceProvider, new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "?"),
                        "?", "?", "?", "jane.doe@example.org", "6625550144", true, null, null, "BB123", "UO","AA123"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"42\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"?\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":\"?\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"?\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"?\"},\"rea\":\"?\",\"shareCapital\":\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"6625550144\",\"imported\":true,\"subunitCode\":\"BB123\",\"subunitType\":\"UO\",\"aooParentCode\":\"AA123\"}"));
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
        when(externalService.retrieveInstitutionManager(any(), any())).thenReturn(productManagerInfo);
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
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<Onboarding> onboarding1 = new ArrayList<>();
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();

        Institution institution = new Institution("42", "42", Origin.SELC.name(), "?", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?", billing1, onboarding1, geographicTaxonomies,
                attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "?", "?", "?",
                true, OffsetDateTime.now(), OffsetDateTime.now(), "BB123", "UO","AA123");
        institution.setId("?");
        institution.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setUserId("?");
        productManagerInfo.setInstitution(institution);
        when(externalService.retrieveInstitutionManager(any(), any())).thenReturn(productManagerInfo);
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
    void testGetManagerInstitutionByExternalId4() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("?");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setInstitution(institution);
        when(externalService.retrieveInstitutionManager(any(), any())).thenReturn(productManagerInfo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/manager", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"from\":null,\"to\":null,\"role\":null,\"product\":null,\"state\":null,\"pricingPlan\":\"?\",\"institutionUpdate"
                                        + "\":{\"imported\":false},\"billing\":{\"vatNumber\":\"42\",\"recipientCode\":\"?\",\"publicServices\":true"
                                        + "},\"createdAt\":null,\"updatedAt\":null}"));
    }

    /**
     * Method under test: {@link ExternalController#getManagerInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetManagerInstitutionByExternalId5() throws Exception {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("?");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("?");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("?");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<Onboarding> onboarding1 = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("?", "42", "?", "42", true);

        Institution institution = new Institution("42", "42", Origin.MOCK.name(), "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?",
                billing1, onboarding1, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "?"), "?", "?", "?", "jane.doe@example.org",
                "6625550144", true, null, null, "BB123", "UO","AA123");
        institution.setId("?");
        institution.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setUserId("?");
        productManagerInfo.setInstitution(institution);
        when(externalService.retrieveInstitutionManager(any(), any())).thenReturn(productManagerInfo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/products/{productId}/manager", "42", "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"from\":\"?\",\"to\":\"?\",\"role\":null,\"product\":null,\"state\":null,\"pricingPlan\":\"?\",\"institutionUpdate\":{\"institutionType\":\"PA\",\"description\":\"The characteristics of someone or something\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"taxCode\":\"?\",\"zipCode\":\"21654\",\"paymentServiceProvider\":{\"abiCode\":\"?\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"?\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"?\"},\"geographicTaxonomyCodes\":[],\"rea\":\"?\",\"shareCapital\":\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"6625550144\",\"imported\":true,\"subunitCode\":\"BB123\",\"subunitType\":\"UO\",\"aooParentCode\":\"AA123\"},\"billing\":{\"vatNumber\":\"42\",\"recipientCode\":\"?\",\"publicServices\":true},\"createdAt\":null,\"updatedAt\":null}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionByIds(List)}
     */
    @Test
    void testRetrieveInstitutionByIds() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions")
                .param("ids","[code1, code2]");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(200));
    }
}


