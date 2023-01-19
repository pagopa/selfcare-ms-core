package it.pagopa.selfcare.mscore.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import it.pagopa.selfcare.commons.web.security.JwtAuthenticationToken;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.ContractRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

class OnboardingControllerTest {

    /**
     * Method under test: {@link OnboardingController#onboardingInstitution(OnboardingInstitutionRequest, Authentication)}
     */
    @Test
    void testOnboardingInstitution() {

        OnboardingService onboardingService = mock(OnboardingService.class);
        doNothing().when(onboardingService).onboardingInstitution(any(), any());
        OnboardingController onboardingController = new OnboardingController(onboardingService);

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
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBillingRequest(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdate);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setUsers(new ArrayList<>());
        ResponseEntity<Void> actualOnboardingInstitutionResult = onboardingController
                .onboardingInstitution(onboardingInstitutionRequest, new JwtAuthenticationToken("ABC123"));
        assertNull(actualOnboardingInstitutionResult.getBody());
        assertEquals(HttpStatus.NO_CONTENT, actualOnboardingInstitutionResult.getStatusCode());
        assertTrue(actualOnboardingInstitutionResult.getHeaders().isEmpty());
        verify(onboardingService).onboardingInstitution(any(), any());
    }
}

