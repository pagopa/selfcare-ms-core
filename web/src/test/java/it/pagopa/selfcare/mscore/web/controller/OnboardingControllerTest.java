package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionOperatorsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionUsersRequest;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OnboardingController.class})
@ExtendWith(MockitoExtension.class)
class OnboardingControllerTest {
    @InjectMocks
    private OnboardingController onboardingController;

    @InjectMocks
    private TokenController tokenController;

    @Mock
    private OnboardingService onboardingService;

    @Mock
    TokenService tokenService;

    @Spy
    @InjectMocks
    OnboardingResourceMapper onboardingResourceMapper = new OnboardingResourceMapperImpl();

    @Spy
    InstitutionUpdateMapper institutionUpdateMapper = new InstitutionUpdateMapperImpl();

    @Spy
    InstitutionResourceMapper institutionResourceMapper = new InstitutionResourceMapperImpl();

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void resetContext() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    void onboardingInstitutionOperators() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        OnboardingInstitutionOperatorsRequest request = new OnboardingInstitutionOperatorsRequest();
        request.setInstitutionId("id");
        request.setProductId("id");
        request.setProductTitle("productTitle");
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());
        request.setUsers(personList);
        String content = (new ObjectMapper()).writeValueAsString(request);

        when(onboardingService.onboardingOperators(any(), any(), any(), any())).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/onboarding/operators")
                .principal(authentication)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void onboardingInstitutionSubDelegate() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        OnboardingInstitutionOperatorsRequest request = new OnboardingInstitutionOperatorsRequest();
        request.setInstitutionId("id");
        request.setProductId("id");
        request.setProductTitle("productTitle");
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());
        request.setUsers(personList);
        String content = (new ObjectMapper()).writeValueAsString(request);
        when(onboardingService.onboardingOperators(any(), any(), any(), any())).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/onboarding/subdelegates")
                .principal(authentication)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInstitutionUsers(OnboardingInstitutionUsersRequest, Authentication)}
     */
    @Test
    void onboardingInstitutionUsers() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);

        OnboardingInstitutionUsersRequest request = new OnboardingInstitutionUsersRequest();
        request.setInstitutionTaxCode("taxCode");
        request.setProductId("id");
        request.setUsers(List.of(new Person()));

        when(onboardingService.onboardingUsers(any(), any(), any())).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/onboarding/users")
                .principal(authentication)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
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
     * Method under test: {@link OnboardingController#onboardingInstitutionOperators(OnboardingInstitutionOperatorsRequest, Authentication)}
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
     * Method under test: {@link OnboardingController#onboardingInstitutionOperators(OnboardingInstitutionOperatorsRequest, Authentication)}
     */
    @Test
    void testOnboardingInstitutionOperators2() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        when(onboardingService.onboardingOperators(org.mockito.Mockito.any(),
                org.mockito.Mockito.any(), any(), any())).thenReturn(new ArrayList<>());

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

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());

        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        onboardingInstitutionOperatorsRequest.setUsers(personList);
        onboardingInstitutionOperatorsRequest.setProductTitle("productTitle");
        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionOperatorsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/onboarding/operators")
                .principal(authentication)
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
     * Method under test: {@link OnboardingController#onboardingInstitutionSubDelegate(OnboardingInstitutionOperatorsRequest, Authentication)}
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
     * Method under test: {@link OnboardingController#onboardingInstitutionSubDelegate(OnboardingInstitutionOperatorsRequest, Authentication)}
     */
    @Test
    void testOnboardingInstitutionSubDelegate2() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        when(onboardingService.onboardingOperators(org.mockito.Mockito.any(),
                org.mockito.Mockito.any(), any(), any())).thenReturn(new ArrayList<>());

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

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());

        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        onboardingInstitutionOperatorsRequest.setUsers(personList);
        onboardingInstitutionOperatorsRequest.setProductTitle("productTitle");
        String content = (new ObjectMapper()).writeValueAsString(onboardingInstitutionOperatorsRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/onboarding/subdelegates")
                .principal(authentication)
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
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String, String)}
     */
    @Test
    void shouldVerifyOnboardingInfoBySubunit() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfoSubunit(any(), any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/")
                .queryParam("taxCode", "42")
                .queryParam("productId", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfoByFilters(String, String, String, String, String, String)}
     */
    @Test
    void shouldVerifyOnboardingInfoByFiltersSuccess() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfoByFilters(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/verify")
                .queryParam("origin", "42")
                .queryParam("originId", "42")
                .queryParam("productId", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    @Test
    void shouldVerifyOnboardingInfoByExternalId() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfo(any(), any());
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
    //@Test
    void shouldNotFoundVerifyOnboardingInfoByExternalId() throws Exception {
        doThrow(new ResourceNotFoundException("","")).when(onboardingService).verifyOnboardingInfo(any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/institution/{externalId}/products/{productId}", "42", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
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

