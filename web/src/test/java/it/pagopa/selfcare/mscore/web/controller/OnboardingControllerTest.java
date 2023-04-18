package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.DataProtectionOfficerRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateRequest;
import it.pagopa.selfcare.mscore.web.model.institution.PaymentServiceProviderRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.ContractRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionLegalsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionOperatorsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {OnboardingController.class})
@ExtendWith(MockitoExtension.class)
class OnboardingControllerTest {
    @InjectMocks
    private OnboardingController onboardingController;

    @Mock
    private OnboardingService onboardingService;

    @Mock
    TokenService tokenService;

    @Test
    void onboardingInstitutionOperators() throws Exception {
        OnboardingInstitutionOperatorsRequest request = new OnboardingInstitutionOperatorsRequest();
        request.setInstitutionId("id");
        request.setProductId("id");
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());
        request.setUsers(personList);
        String content = (new ObjectMapper()).writeValueAsString(request);

        when(onboardingService.onboardingOperators(any(), any())).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/onboarding/operators")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void onboardingInstitutionSubDelegate() throws Exception {
        OnboardingInstitutionOperatorsRequest request = new OnboardingInstitutionOperatorsRequest();
        request.setInstitutionId("id");
        request.setProductId("id");
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());
        request.setUsers(personList);
        String content = (new ObjectMapper()).writeValueAsString(request);
        when(onboardingService.onboardingOperators(any(), any())).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/onboarding/subdelegates")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link OnboardingController#approveOnboarding(String, Authentication)}
     */
    @Test
    void testApproveOnboarding() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenService.verifyToken(any())).thenReturn(token);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/onboarding/approve/{tokenId}", "42")
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    /**
     * Method under test: {@link OnboardingController#getOnboardingDocument(String)}
     */
    @Test
    void testGetOnboardingDocument() throws Exception {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        when(onboardingService.retrieveDocument(org.mockito.Mockito.any())).thenReturn(resourceResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/onboarding/relationship/{relationshipId}/document", "42");
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"))
                .andExpect(MockMvcResultMatchers.content().string("AXAXAXAX"));
    }

    /**
     * Method under test: {@link OnboardingController#getOnboardingDocument(String)}
     */
    @Test
    void testGetOnboardingDocument2() throws Exception {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData(null);
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        when(onboardingService.retrieveDocument(org.mockito.Mockito.any())).thenReturn(resourceResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/onboarding/relationship/{relationshipId}/document", "42");
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitution(OnboardingInstitutionRequest, Authentication)}
     */
    @Test
    void testOnboardingInstitution() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPath("Path");
        contractRequest.setVersion("1.0.2");

        InstitutionUpdateRequest institutionUpdate = new InstitutionUpdateRequest();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        List<String> geographicTaxonomiesList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(geographicTaxonomiesList);
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        institutionUpdate.setPaymentServiceProvider(new PaymentServiceProviderRequest());
        institutionUpdate.setDataProtectionOfficer(new DataProtectionOfficerRequest());

        Person person = new Person();
        person.setId("42");
        person.setName("Name");
        person.setProductRole("Product Role");
        person.setRole(PartyRole.MANAGER);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setUsers(personList);
        onboardingInstitutionRequest.setBilling(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdate);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");

        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/onboarding/institution")
                .principal(authentication)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitution(OnboardingInstitutionRequest, Authentication)}
     */
    @Test
    void testOnboardingInstitution2() throws Exception {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPath("Path");
        contractRequest.setVersion("1.0.2");

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

        InstitutionUpdateRequest institutionUpdateRequest = new InstitutionUpdateRequest();
        institutionUpdateRequest.setAddress("42 Main St");
        institutionUpdateRequest.setBusinessRegisterPlace("Business Register Place");
        institutionUpdateRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionUpdateRequest.setDescription("The characteristics of someone or something");
        institutionUpdateRequest.setDigitalAddress("42 Main St");
        institutionUpdateRequest.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdateRequest.setImported(true);
        institutionUpdateRequest.setInstitutionType(InstitutionType.PA);
        institutionUpdateRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionUpdateRequest.setRea("Rea");
        institutionUpdateRequest.setShareCapital("Share Capital");
        institutionUpdateRequest.setSupportEmail("jane.doe@example.org");
        institutionUpdateRequest.setSupportPhone("6625550144");
        institutionUpdateRequest.setTaxCode("Tax Code");
        institutionUpdateRequest.setZipCode("21654");

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBilling(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdateRequest);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setSignContract(true);
        onboardingInstitutionRequest.setUsers(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/onboarding/institution")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitutionLegals(OnboardingInstitutionLegalsRequest, Authentication)}
     */
    @Test
    void testOnboardingInstitutionLegals() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPath("Path");
        contractRequest.setVersion("1.0.2");

        OnboardingInstitutionLegalsRequest onboardingInstitutionLegalsRequest = new OnboardingInstitutionLegalsRequest();
        onboardingInstitutionLegalsRequest.setContract(contractRequest);
        onboardingInstitutionLegalsRequest.setInstitutionExternalId("42");
        onboardingInstitutionLegalsRequest.setInstitutionId("42");
        onboardingInstitutionLegalsRequest.setProductId("42");
        onboardingInstitutionLegalsRequest.setProductName("Product Name");
        onboardingInstitutionLegalsRequest.setSignContract(true);
        Person person = new Person();
        person.setId("id");
        onboardingInstitutionLegalsRequest.setUsers(List.of(person));

        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionLegalsRequest);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/onboarding/legals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .principal(authentication);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitutionOperators(OnboardingInstitutionOperatorsRequest)}
     */
    @Test
    void testOnboardingInstitutionOperators() throws Exception {
        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        onboardingInstitutionOperatorsRequest.setUsers(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionOperatorsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/onboarding/operators")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitutionOperators(OnboardingInstitutionOperatorsRequest)}
     */
    @Test
    void testOnboardingInstitutionOperators2() throws Exception {
        when(onboardingService.onboardingOperators(org.mockito.Mockito.any(),
                org.mockito.Mockito.any())).thenReturn(new ArrayList<>());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenService.verifyOnboarding(org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                .thenReturn(token);

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());

        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        onboardingInstitutionOperatorsRequest.setUsers(personList);
        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionOperatorsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/onboarding/operators")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitutionSubDelegate(OnboardingInstitutionOperatorsRequest)}
     */
    @Test
    void testOnboardingInstitutionSubDelegate() throws Exception {
        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        onboardingInstitutionOperatorsRequest.setUsers(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionOperatorsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/onboarding/subdelegates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitutionSubDelegate(OnboardingInstitutionOperatorsRequest)}
     */
    @Test
    void testOnboardingInstitutionSubDelegate2() throws Exception {
        when(onboardingService.onboardingOperators(org.mockito.Mockito.any(),
                org.mockito.Mockito.any())).thenReturn(new ArrayList<>());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenService.verifyOnboarding(org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                .thenReturn(token);

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());

        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        onboardingInstitutionOperatorsRequest.setUsers(personList);
        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionOperatorsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/onboarding/subdelegates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfo(any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/institution/{externalId}/products/{productId}", "42", "42");
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo2() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo3() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfo(any(), any());
        MockHttpServletRequestBuilder headResult = MockMvcRequestBuilders
                .head("/onboarding/institution/{externalId}/products/{productId}", "42", "42");
        headResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(headResult)
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo4() throws Exception {
        doNothing().when(onboardingService)
                .verifyOnboardingInfo(org.mockito.Mockito.any(), org.mockito.Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/institution/{externalId}/products/{productId}", "42", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo5() throws Exception {
        doNothing().when(onboardingService)
                .verifyOnboardingInfo(org.mockito.Mockito.any(), org.mockito.Mockito.any());
        MockHttpServletRequestBuilder headResult = MockMvcRequestBuilders
                .head("/onboarding/institution/{externalId}/products/{productId}", "42", "42");
        headResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(headResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#completeOnboarding(String, MultipartFile)}
     */
    @Test
    void testCompleteOnboarding() throws Exception {
        doNothing().when(onboardingService).completeOboarding(any(), any());
        MockMultipartFile file = new MockMultipartFile("contract", "".getBytes());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/onboarding/complete/{tokenId}",
                "42")
                .file(file);
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    /**
     * Method under test: {@link OnboardingController#invalidateOnboarding(String)}
     */
    @Test
    void testInvalidateOnboarding() throws Exception {
        doNothing().when(onboardingService).invalidateOnboarding(org.mockito.Mockito.any());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenService.verifyToken(org.mockito.Mockito.any())).thenReturn(token);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/onboarding/complete/{tokenId}",
                "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#onboardingReject(String)}
     */
    @Test
    void testOnboardingReject() throws Exception {
        doNothing().when(onboardingService).onboardingReject(org.mockito.Mockito.any());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenService.verifyToken(org.mockito.Mockito.any())).thenReturn(token);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/onboarding/reject/{tokenId}",
                "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    /**
     * Method under test: {@link OnboardingController#onboardingInfo(String, String, String[], Authentication)}
     */
    @Test
    void testOnboardingInfo() throws Exception {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/onboarding/info")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

