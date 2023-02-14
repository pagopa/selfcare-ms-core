package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ExternalServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ExternalServiceImplTest {
    @Autowired
    private ExternalServiceImpl externalServiceImpl;

    @MockBean
    private InstitutionConnector institutionConnector;

    @MockBean
    private UserConnector userConnector;

    @MockBean
    private TokenConnector tokenConnector;

    /**
     * Method under test: {@link ExternalServiceImpl#getBillingByExternalId(Institution, String)}
     */
    @Test
    void testGetBillingByExternalId() {
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
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getBillingByExternalId(institution, "42"));
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getBillingByExternalId(Institution, String)}
     */
    @Test
    void testGetBillingByExternalId2() {
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

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("The institution having externalId %s has not billing data for product %s");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("The institution having externalId %s has not billing data for product %s");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("The institution having externalId %s has not billing data for product %s");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("The institution having externalId %s has not billing data for product %s");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = mock(Institution.class);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getOnboarding()).thenReturn(onboardingList);
        doNothing().when(institution).setAddress(any());
        doNothing().when(institution).setAttributes(any());
        doNothing().when(institution).setDataProtectionOfficer(any());
        doNothing().when(institution).setDescription(any());
        doNothing().when(institution).setDigitalAddress(any());
        doNothing().when(institution).setExternalId(any());
        doNothing().when(institution).setGeographicTaxonomies(any());
        doNothing().when(institution).setId(any());
        doNothing().when(institution).setInstitutionType(any());
        doNothing().when(institution).setOnboarding(any());
        doNothing().when(institution).setIpaCode(any());
        doNothing().when(institution).setPaymentServiceProvider(any());
        doNothing().when(institution).setTaxCode(any());
        doNothing().when(institution).setZipCode(any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
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
        externalServiceImpl.getBillingByExternalId(institution, "42");
        verify(institution).getOnboarding();
        verify(institution).setAddress(any());
        verify(institution).setAttributes(any());
        verify(institution).setDataProtectionOfficer(any());
        verify(institution).setDescription(any());
        verify(institution).setDigitalAddress(any());
        verify(institution).setExternalId(any());
        verify(institution).setGeographicTaxonomies(any());
        verify(institution).setId(any());
        verify(institution).setInstitutionType(any());
        verify(institution).setOnboarding(any());
        verify(institution).setIpaCode(any());
        verify(institution).setPaymentServiceProvider(any());
        verify(institution).setTaxCode(any());
        verify(institution).setZipCode(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getBillingByExternalId(Institution, String)}
     */
    @Test
    void testGetBillingByExternalId3() {
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getBillingByExternalId(new Institution(), "42"));
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getBillingByExternalId(Institution, String)}
     */
    @Test
    void testGetBillingByExternalId5() {
        Institution institution = mock(Institution.class);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getBillingByExternalId(institution, "42"));
        verify(institution).getExternalId();
        verify(institution).getOnboarding();
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getBillingByExternalId(Institution, String)}
     */
    @Test
    void testGetBillingByExternalId6() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = mock(Institution.class);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getOnboarding()).thenReturn(onboardingList);
        externalServiceImpl.getBillingByExternalId(institution, "42");
        verify(institution).getOnboarding();
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionManager(Institution, String)}
     */
    @Test
    void testGetInstitutionManager() {
        when(userConnector.findOnboardedManager(any(), any()))
                .thenReturn(new ArrayList<>());

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
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getInstitutionManager(institution, "42"));
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionManager(Institution, String)}
     */
    @Test
    void testGetInstitutionManager2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.findOnboardedManager(any(), any()))
                .thenReturn(onboardedUserList);

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
        assertSame(onboardedUser, externalServiceImpl.getInstitutionManager(institution, "42"));
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionManager(Institution, String)}
     */
    @Test
    void testGetInstitutionManager3() {
        when(userConnector.findOnboardedManager(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));

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
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getInstitutionManager(institution, "42"));
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionManager(Institution, String)}
     */
    @Test
    void testGetInstitutionManager4() {
        when(userConnector.findOnboardedManager(any(), any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getInstitutionManager(new Institution(), "42"));
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionManager(Institution, String)}
     */
    @Test
    void testGetInstitutionManager5() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUserList.add(onboardedUser);
        when(userConnector.findOnboardedManager(any(), any())).thenReturn(onboardedUserList);
        assertSame(onboardedUser, externalServiceImpl.getInstitutionManager(new Institution(), "42"));
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionManager(Institution, String)}
     */
    @Test
    void testGetInstitutionManager7() {
        when(userConnector.findOnboardedManager(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getInstitutionManager(new Institution(), "42"));
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getRelationShipToken(String, String, String)}
     */
    @Test
    void testGetRelationShipToken() {
        when(tokenConnector.findActiveContract(any(), any(), any()))
                .thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getRelationShipToken("42", "42", "42"));
        verify(tokenConnector).findActiveContract(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getRelationShipToken(String, String, String)}
     */
    @Test
    void testGetRelationShipToken2() {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContract("Contract");
        token.setExpiringDate("2020-03-01");
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUsers(new ArrayList<>());

        ArrayList<Token> tokenList = new ArrayList<>();
        tokenList.add(token);
        when(tokenConnector.findActiveContract(any(), any(), any())).thenReturn(tokenList);
        assertEquals("42", externalServiceImpl.getRelationShipToken("42", "42", "42"));
        verify(tokenConnector).findActiveContract(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getRelationShipToken(String, String, String)}
     */
    @Test
    void testGetRelationShipToken3() {
        when(tokenConnector.findActiveContract(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getRelationShipToken("42", "42", "42"));
        verify(tokenConnector).findActiveContract(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getRelationShipToken(String, String, String)}
     */
    @Test
    void testGetRelationShipToken4() {
        when(tokenConnector.findActiveContract(any(), any(), any()))
                .thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getRelationShipToken("42", "42", "42"));
        verify(tokenConnector).findActiveContract(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getRelationShipToken(String, String, String)}
     */
    @Test
    void testGetRelationShipToken5() {
        ArrayList<Token> tokenList = new ArrayList<>();
        tokenList.add(new Token());
        when(tokenConnector.findActiveContract(any(), any(), any())).thenReturn(tokenList);
        assertNull(externalServiceImpl.getRelationShipToken("42", "42", "42"));
        verify(tokenConnector).findActiveContract(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getRelationShipToken(String, String, String)}
     */
    @Test
    void testGetRelationShipToken6() {
        when(tokenConnector.findActiveContract(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getRelationShipToken("42", "42", "42"));
        verify(tokenConnector).findActiveContract(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testGetInstitutionWithFilter() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(new ArrayList<>());
        List<RelationshipState> list = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getInstitutionWithFilter("42", "42", list));
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testGetInstitutionWithFilter2() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(institutionList);
        externalServiceImpl.getInstitutionWithFilter("42", "42", new ArrayList<>());
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testGetInstitutionWithFilter3() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred",
                        "Verifying onboarding for institution having externalId {} on product {}"));
        List<RelationshipState> list = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getInstitutionWithFilter("42", "42", list));
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testGetInstitutionWithFilter4() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getInstitutionWithFilter("42", "42", new ArrayList<>()));
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testGetInstitutionWithFilter5() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(institutionList);
        externalServiceImpl.getInstitutionWithFilter("42", "42", new ArrayList<>());
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testGetInstitutionWithFilter6() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred",
                        "Verifying onboarding for institution having externalId {} on product {}"));
        assertThrows(ResourceNotFoundException.class,
                () -> externalServiceImpl.getInstitutionWithFilter("42", "42", new ArrayList<>()));
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId() {
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
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);
        assertSame(institution, externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId2() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId3() {
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Retrieving institution for externalId {}"));
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId4() {
        Institution institution = new Institution();
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(institution));
        assertSame(institution, externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId5() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId6() {
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Retrieving institution for externalId {}"));
        assertThrows(ResourceNotFoundException.class, () -> externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }
}

