package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.ContractRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;

import java.util.ArrayList;

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
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
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
}

