package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.model.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.ContractRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ContextConfiguration(classes = {OnboardingController.class})
@ExtendWith(SpringExtension.class)
class OnboardingControllerTest {
    @Autowired
    private OnboardingController onboardingController;

    @MockBean
    private OnboardingService onboardingService;

    /**
     * Method under test: {@link OnboardingController#onboardingInstitution(OnboardingInstitutionRequest, Authentication)}
     */
    @Test
    void testOnboardingInstitution() throws Exception {

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServer("Public Server");
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPath("Path");
        contractRequest.setVersion("1.0.2");

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

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Person person = new Person();
        person.setId("42");
        person.setName("Name");
        person.setProductRole("Product Role");
        person.setRole("MANAGER");
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setUsers(personList);
        onboardingInstitutionRequest.setBillingRequest(billingRequest);
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
        doNothing().when(onboardingService).verifyOnboardingInfo(any(), any());
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
     * Method under test: {@link OnboardingController#onboardingInfo(String, String, String[], Authentication)}
     */
    @Test
    void testOnboardingInfo() throws Exception {
        Product product = new Product();
        product.setContract("contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        Map<String, Product> productMap = new HashMap<>();
        productMap.put("product42", product);

        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("product42");
        onboarding.setStatus(RelationshipState.PENDING);
        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution onboardedInstitution = new Institution();
        onboardedInstitution.setId("institution1");
        onboardedInstitution.setOnboarding(onboardingList);

        OnboardingInfo onboardingInfo = new OnboardingInfo(onboardedInstitution, productMap);
        List<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        onboardingInfoList.add(onboardingInfo);

        when(onboardingService.getOnboardingInfo(any(), any(), any(), any())).thenReturn(onboardingInfoList);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        SelfCareUser user = SelfCareUser.builder("id").build();
        when(authentication.getPrincipal()).thenReturn(user);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/onboarding/info")
                .param("institutionId", "42")
                .param("institutionExternalId", "44")
                .param("states", "PENDING")
                .principal(authentication);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.institutions").isArray());
    //.andExpect(MockMvcResultMatchers.content().string("{\"userId\":\"id\",\"institutions\":[{\"id\":\"institution1\",\"state\":\"PENDING\",\"productInfo\":{\"id\":\"product42\",\"role\":[]}}]}"));

    }
}

