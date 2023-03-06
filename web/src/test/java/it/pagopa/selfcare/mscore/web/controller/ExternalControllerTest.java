package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.ExternalService;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.utils.OriginEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
        billing1.setPublicServices(true);
        billing1.setRecipientCode("?");
        billing1.setVatNumber("42");

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setClosedAt(null);
        onboarding1.setContract("?");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("?");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
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
                        .string("{\"products\":[{\"id\":\"42\",\"state\":\"PENDING\"},{\"id\":\"42\",\"state\":\"PENDING\"}]}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId4() throws Exception {
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution());
        when(externalService.retrieveInstitutionProductsByExternalId(any(), any()))
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
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution("42", "42",
                OriginEnum.MOCK, "42", "The characteristics of someone or something", InstitutionType.PA, "42 Main St",
                "42 Main St", "21654", "?", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), "?", "?", "?", "jane.doe@example.org", "4105551212", true, null, null));
        when(externalService.retrieveInstitutionProductsByExternalId(any(), any()))
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
                                "{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"42\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":"
                                        + "\"21654\",\"taxCode\":\"?\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\""
                                        + ":null,\"businessRegisterNumber\":null,\"legalRegisterNumber\":null,\"legalRegisterName\":null,\"vatNumberGroup"
                                        + "\":false},\"dataProtectionOfficer\":{\"address\":null,\"email\":null,\"pec\":null},\"rea\":\"?\",\"shareCapital\":"
                                        + "\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"4105551212\","
                                        + "\"imported\":true}"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId() throws Exception {
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
                                "{\"institutionId\":null,\"externalId\":null,\"originId\":null,\"description\":null,\"institutionType\":null,"
                                        + "\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"pricingPlan\":null,\"billing\":null"
                                        + "}"));
    }

    /**
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String, Integer, Integer)}
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
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String, Integer, Integer)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId2() throws Exception {
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
        when(externalService.retrieveInstitutionGeoTaxonomiesByExternalId(any()))
                .thenReturn(geographicTaxonomiesList);
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
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String, Integer, Integer)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId3() throws Exception {
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution());
        when(externalService.retrieveInstitutionGeoTaxonomiesByExternalId(any())).thenReturn(new ArrayList<>());
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
     * Method under test: {@link ExternalController#retrieveInstitutionGeoTaxonomiesByExternalId(String, Integer, Integer)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId4() throws Exception {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution("42", "42",
                OriginEnum.MOCK, "42", "The characteristics of someone or something", InstitutionType.PA, "42 Main St",
                "42 Main St", "21654", "?", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), "?", "?", "?", "jane.doe@example.org", "4105551212", true, null, null));
        when(externalService.retrieveInstitutionGeoTaxonomiesByExternalId(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/geotaxonomies", "", "Uri Vars");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"42\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":"
                                        + "\"21654\",\"taxCode\":\"?\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\""
                                        + ":null,\"businessRegisterNumber\":null,\"legalRegisterNumber\":null,\"legalRegisterName\":null,\"vatNumberGroup"
                                        + "\":false},\"dataProtectionOfficer\":{\"address\":null,\"email\":null,\"pec\":null},\"rea\":\"?\",\"shareCapital\":"
                                        + "\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"4105551212\","
                                        + "\"imported\":true}"));
    }

    /**
     * Method under test: {@link ExternalController#getBillingInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetBillingInstitutionByExternalId2() throws Exception {
        Institution institution = new Institution();
        institution.setTaxCode("U");
        institution.setZipCode("OX1 1PT");
        institution.setAddress("?");
        institution.setDigitalAddress("?");
        institution.setInstitutionType(InstitutionType.PG);
        institution.setDescription("?");
        institution.setOriginId("?");
        institution.setExternalId("?");
        institution.setId("?");
        when(externalService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
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
    void testGetByExternalId2() throws Exception {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(externalService.getInstitutionByExternalId(any())).thenReturn(new Institution("42", "42",
                OriginEnum.MOCK, "42", "The characteristics of someone or something", InstitutionType.PA, "42 Main St",
                "42 Main St", "21654", "?", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), "?", "?", "?", "jane.doe@example.org", "4105551212", true, null, null));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/external/institutions/{externalId}",
                "42");
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"42\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":"
                                        + "\"21654\",\"taxCode\":\"?\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\""
                                        + ":null,\"businessRegisterNumber\":null,\"legalRegisterNumber\":null,\"legalRegisterName\":null,\"vatNumberGroup"
                                        + "\":false},\"dataProtectionOfficer\":{\"address\":null,\"email\":null,\"pec\":null},\"rea\":\"?\",\"shareCapital\":"
                                        + "\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"4105551212\","
                                        + "\"imported\":true}"));
    }

    /**
     * Method under test: {@link ExternalController#getManagerInstitutionByExternalId(String, String)}
     */
    @Test
    void testGetManagerInstitutionByExternalId() throws Exception {
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
                                        + "\":{\"imported\":false},\"billing\":{\"vatNumber\":\"42\",\"recipientCode\":\"?\",\"publicServices\":true},\"createdAt"
                                        + "\":null,\"updatedAt\":null}"));
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
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();

        Institution institution = new Institution("42", "42", OriginEnum.MOCK, "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?",
                billing1, onboarding1, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                "?", "?", "?", "jane.doe@example.org", "4105551212", true, null, null);
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
                                "{\"id\":null,\"from\":\"?\",\"to\":\"?\",\"role\":null,\"product\":null,\"state\":null,\"pricingPlan\":\"?\",\"institutionUpdate"
                                        + "\":{\"institutionType\":\"PA\",\"description\":\"The characteristics of someone or something\",\"digitalAddress\":\"42"
                                        + " Main St\",\"address\":\"42 Main St\",\"taxCode\":\"?\",\"zipCode\":\"21654\",\"paymentServiceProvider\":{\"abiCode\""
                                        + ":null,\"businessRegisterNumber\":null,\"legalRegisterName\":null,\"legalRegisterNumber\":null,\"vatNumberGroup"
                                        + "\":false},\"dataProtectionOfficer\":{\"address\":null,\"email\":null,\"pec\":null},\"geographicTaxonomyCodes\":"
                                        + "[],\"rea\":\"?\",\"shareCapital\":\"?\",\"businessRegisterPlace\":\"?\",\"supportEmail\":\"jane.doe@example.org\","
                                        + "\"supportPhone\":\"4105551212\",\"imported\":true},\"billing\":{\"vatNumber\":\"42\",\"recipientCode\":\"?\","
                                        + "\"publicServices\":true},\"createdAt\":null,\"updatedAt\":null}"));
    }

    /**
     * Method under test: {@link ExternalController#getUserInstitutionRelationshipsByExternalId(String, String, List, List, List, List, Integer, Integer, Authentication)}
     */
    @Test
    void testGetUserInstitutionRelationshipsByExternalId() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getUserInstitutionRelationshipsByExternalId() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        when(externalService.getUserInstitutionRelationships(any(),any(),any(),any(),any(),any(),any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/{externalId}/relationships", "42")
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(externalController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

