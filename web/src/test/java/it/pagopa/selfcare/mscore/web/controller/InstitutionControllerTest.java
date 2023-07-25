package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.TestUtils;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.mapper.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ContextConfiguration(classes = {InstitutionController.class})
@ExtendWith(MockitoExtension.class)
class InstitutionControllerTest {

    private static final String BASE_URL = "/institutions";

    @InjectMocks
    private InstitutionController institutionController;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private DelegationService delegationService;

    @Spy
    private OnboardingResourceMapper onboardingResourceMapper = new OnboardingResourceMapperImpl();

    @Spy
    private InstitutionResourceMapper institutionResourceMapper = new InstitutionResourceMapperImpl();

    @Spy
    private DelegationMapper delegationMapper = new DelegationMapperImpl();

    private final ObjectMapper objectMapper = new ObjectMapper();


    private final static Onboarding onboarding;
    private final static Billing billing;

    static {
        billing = new Billing();
        billing.setVatNumber("example");
        billing.setRecipientCode("example");

        onboarding = new Onboarding();
        onboarding.setProductId("example");
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setBilling(billing);
        onboarding.setContract("contract");
        onboarding.setTokenId("tokenId");
        onboarding.setPricingPlan("setPricingPlan");
    }

    @Test
    void shouldGetInstitutionsByTaxCode() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();

        when(institutionService.getInstitutions(any(), any()))
                .thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/?taxCode={taxCode}", "TaxCode");

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutions\":[{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false}]}"));
    }

    @Test
    void shouldGetInstitutionsByTaxCodeAndSubunitCode() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSubunitCode("example");
        institution.setSubunitType(InstitutionPaSubunitType.UO.name());
        institution.setParentDescription("parentDescription");

        when(institutionService.getInstitutions(any(), any()))
                .thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/?taxCode={taxCode}&subunitCode={subunitCode}", "TaxCode", "SubunitCode");

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutions\":[{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"subunitCode\":\"example\",\"subunitType\":\"UO\",\"parentDescription\":\"parentDescription\"}]}"));
    }

    @Test
    void shouldGetOnboardingsInstitutionByProductId() throws Exception {

        when(institutionService.getOnboardingInstitutionByProductId(any(), any()))
                .thenReturn(List.of(onboarding));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/{institutionId}/onboardings?productId{productId}", "42", onboarding.getProductId());

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"onboardings\":[{\"productId\":\"example\",\"tokenId\":\"tokenId\",\"status\":\"ACTIVE\",\"contract\":\"contract\",\"pricingPlan\":\"setPricingPlan\",\"billing\":{\"vatNumber\":\"example\",\"recipientCode\":\"example\",\"publicServices\":false},\"createdAt\":null,\"updatedAt\":null,\"closedAt\":null}]}"));
    }

    @Test
    void getUserInstitutionRelationships() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());
        Institution institution = new Institution();
        institution.setId("id");
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
        when(institutionService.retrieveUserInstitutionRelationships(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = get("/institutions/{id}/relationships", "42")
                .principal(authentication);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void retrieveInstitutionById() throws Exception {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionById("42")).thenReturn(institution);
        institution.setId("id");
        MockHttpServletRequestBuilder requestBuilder = get("/institutions/{id}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionById(String)}
     */
    @Test
    void testRetrieveInstitutionById() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}", "42");
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }


    @Test
    void retrieveInstitutionGeoTaxonomies() throws Exception {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Institution institution = new Institution();
        institution.setId("id");
        GeographicTaxonomyPage page = new GeographicTaxonomyPage();
        page.setData(Collections.emptyList());
        MockHttpServletRequestBuilder requestBuilder = get("/institutions/{id}/geotaxonomies", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionFromIpa(InstitutionFromIpaPost)}}
     */
    @Test
    void shouldCreateInstitutionFromIpa() throws Exception {
        // Given
        InstitutionFromIpaPost institutionFromIpaPost = new InstitutionFromIpaPost();
        institutionFromIpaPost.setTaxCode("123456");
        institutionFromIpaPost.setSubunitType(InstitutionPaSubunitType.AOO);
        institutionFromIpaPost.setSubunitCode("1234");
        String content = objectMapper.writeValueAsString(institutionFromIpaPost);

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSubunitCode(institutionFromIpaPost.getSubunitCode());
        institution.setSubunitType(institutionFromIpaPost.getSubunitType().name());
        institution.setParentDescription("parentDescription");

        when(institutionService.createInstitutionFromIpa(any(), any(), any())).thenReturn(institution);

        //Then
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-ipa/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"subunitCode\":\"1234\",\"subunitType\":\"AOO\",\"parentDescription\":\"parentDescription\"}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionFromIpa(InstitutionFromIpaPost)}}
     */
    @Test
    void shouldThrowValidationExceptionWhenCreateInstitutionFromIpaWithoutTax() throws Exception {

        String content = objectMapper.writeValueAsString(new InstitutionFromIpaPost());

        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-ipa/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void shouldCreateInstitution() throws Exception {

        InstitutionRequest institution = TestUtils.createSimpleInstitutionRequest();
        Institution response = TestUtils.createSimpleInstitutionPA();

        when(institutionService.createInstitution(any())).thenReturn(response);

        String content = objectMapper.writeValueAsString(institution);

        MockHttpServletRequestBuilder requestBuilder = post("/institutions/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

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
        institution.setOriginId("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitutionByExternalId(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId2() throws Exception {
        when(institutionService.createInstitutionByExternalId(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId3() throws Exception {
        when(institutionService.createInstitutionByExternalId(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId4() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();

        when(institutionService.createInstitutionByExternalId(any()))
                .thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\""+institution.getOriginId()+"\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":"
                                        + "\"21654\",\"taxCode\":\""+institution.getTaxCode()+"\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":"
                                        + "\""+institution.getPaymentServiceProvider().getAbiCode()+"\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\"," +
                                        "\"legalRegisterName\":\""+institution.getPaymentServiceProvider().getLegalRegisterName()+"\",\"vatNumberGroup"
                                        + "\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\""+institution.getDataProtectionOfficer().getPec()+"\"},\"rea"
                                        + "\":\""+institution.getRea()+"\",\"shareCapital\":\""+institution.getShareCapital()+"\",\"imported\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId5() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

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
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
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
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }


    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution2() throws Exception {
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
        institution.setOriginId("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitution(any())).thenReturn(institution);

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
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution3() throws Exception {
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
        institution.setOriginId("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitution(any())).thenReturn(institution);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("?");
        geoTaxonomies.setDesc("The characteristics of someone or something");

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
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution4() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

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
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution5() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

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
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution6() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("?");
        geoTaxonomies.setDesc("The characteristics of someone or something");

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
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }


    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution9() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

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
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
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
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution10() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("?");
        geoTaxonomies.setDesc("The characteristics of someone or something");

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
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
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
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionProducts(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsEmpty() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionService.retrieveInstitutionProducts(any(), any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}/products", "42");
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"products\":[]}"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionProducts(String, List)}
     */
    @Test
    void testRetrieveInstitutionProducts() throws Exception {

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionService.retrieveInstitutionProducts(any(), any()))
                .thenReturn(onboardingList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}/products", onboarding.getProductId());
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"products\":[{\"id\":\"example\",\"state\":\"ACTIVE\"}]}"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionGeoTaxonomies(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomies() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionService.retrieveInstitutionGeoTaxonomies(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}/geotaxonomies",
                "42");
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void createPgInstitution() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        CreatePgInstitutionRequest request = new CreatePgInstitutionRequest();
        request.setTaxId("taxId");
        request.setExistsInRegistry(true);
        ObjectMapper mapper = new ObjectMapper();
        when(institutionService.createPgInstitution(any(), any(), anyBoolean(), any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/pg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#updateInstitution(String, InstitutionPut, Authentication)} (String, PgInstitutionPut, Authentication)}
     */
    @Test
    void testUpdateInstitutionDescription() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        InstitutionPut pgInstitutionPut = new InstitutionPut();
        pgInstitutionPut.setDescription("desc");
        pgInstitutionPut.setDigitalAddress("digitalAddress");
        when(institutionService.updateInstitution(any(), any(), any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/institutions/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pgInstitutionPut))
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void testUpdateInstitution() throws Exception {

        InstitutionPut institutionPut = new InstitutionPut();
        institutionPut.setGeographicTaxonomyCodes(new ArrayList<>());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        when(institutionService.updateInstitution(any(), any(), any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/institutions/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(institutionPut))
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void testGetValidInstitutionToOnboard() throws Exception {
        InstitutionToOnboard institution = new InstitutionToOnboard();
        List<InstitutionToOnboard> list = new ArrayList<>();
        list.add(institution);
        List<ValidInstitution> validInstitutions = new ArrayList<>();
        ValidInstitution validInstitution = new ValidInstitution();
        validInstitutions.add(validInstitution);
        when(institutionService.retrieveInstitutionByExternalIds(any(), any())).thenReturn(validInstitutions);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/onboarded/{productId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(list));
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#updateCreatedAt(String, String, java.time.OffsetDateTime)}
     */
    @Test
    void updateCreatedAt() throws Exception {
        // Given
        String institutionIdMock = "institutionId";
        String productIdMock = "productId";
        String createdAtString = "2020-11-01T02:15:30+01:00";
        OffsetDateTime createdAtMock = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL + "/{institutionId}/products/{productId}/createdAt", institutionIdMock, productIdMock)
                .param("createdAt", createdAtString);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Then
        verify(institutionService, times(1))
                .updateCreatedAt(institutionIdMock, productIdMock, createdAtMock);
        verifyNoMoreInteractions(institutionService);
    }

    /**
     * Method under test: {@link InstitutionController#findFromProduct(String, Integer, Integer)}
     */
    @Test
    void findFromProduct() throws Exception {
        // Given
        String productIdMock = "productId";
        Integer pageMock = 0;
        Integer sizeMock = 2;

        Institution institution = new Institution();
        institution.setId("id");

        // When
        when(institutionService.getInstitutionsByProductId(any(), any(), any())).thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/products/{productId}", productIdMock)
                .param("page", pageMock.toString())
                .param("size", sizeMock.toString());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        // Then
        verify(institutionService, times(1))
                .getInstitutionsByProductId(productIdMock, pageMock, sizeMock);
        verifyNoMoreInteractions(institutionService);
    }

    /**
     * Method under test: {@link InstitutionController#findFromProduct(String, Integer, Integer)}
     */
    @Test
    void getDelegations_shouldGetData() throws Exception {
        // Given
        String institutionId = "institutionId";
        Delegation expectedDelegation = dummyDelegation();

        when(delegationService.getDelegations(any())).thenReturn(List.of(expectedDelegation));
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BASE_URL + "/{institutionId}/delegations", institutionId);
        MvcResult result = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        List<DelegationResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        // Then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
        DelegationResponse actual = response.get(0);
        assertThat(actual.getId()).isEqualTo(expectedDelegation.getId());
        assertThat(actual.getInstitutionFromName()).isEqualTo(expectedDelegation.getInstitutionFromName());
        assertThat(actual.getTo()).isEqualTo(expectedDelegation.getTo());
        assertThat(actual.getProductId()).isEqualTo(expectedDelegation.getProductId());
        assertThat(actual.getFrom()).isEqualTo(expectedDelegation.getFrom());
        assertThat(actual.getInstitutionFromRootName()).isEqualTo(expectedDelegation.getInstitutionFromRootName());

        verify(delegationService, times(1))
                .getDelegations(institutionId);
        verifyNoMoreInteractions(institutionService);
    }

    private Delegation dummyDelegation() {
        Delegation delegation = new Delegation();
        delegation.setFrom("from");
        delegation.setTo("to");
        delegation.setId("setId");
        delegation.setProductId("setProductId");
        delegation.setType(DelegationType.PT);
        delegation.setInstitutionFromName("setInstitutionFromName");
        delegation.setInstitutionFromRootName("setInstitutionFromRootName");
        return delegation;
    }
}
