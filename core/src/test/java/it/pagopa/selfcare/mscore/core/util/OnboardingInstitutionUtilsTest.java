package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnboardingInstitutionUtilsTest {
    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyPgUsers(List)}
     */
    @Test
    void testVerifyPgUsers() {
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.verifyPgUsers(new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyPgUsers(List)}
     */
    @Test
    void testVerifyPgUsers2() {

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        assertDoesNotThrow(() -> OnboardingInstitutionUtils.verifyPgUsers(userToOnboardList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyPgUsers(List)}
     */
    @Test
    void testVerifyPgUsers3() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.DELEGATE);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.verifyPgUsers(userToOnboardList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers() {
        List<UserToOnboard> user = new ArrayList<>();
        OnboardingInstitutionUtils.verifyUsers(user);
        assertEquals(user.size(), 0);
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers2() {

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        assertDoesNotThrow(() -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers3() {

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setId("42");
        userToOnboard1.setName("Name");
        userToOnboard1.setProductRole(new ArrayList<>());
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);
        OnboardingInstitutionUtils.verifyUsers(userToOnboardList);
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers4() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers5() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setId("42");
        userToOnboard1.setName(", ");
        userToOnboard1.setProductRole(new ArrayList<>());
        userToOnboard1.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode(", ");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, OnboardingRequest)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded() {
        Institution institution = new Institution();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest);
        assertFalse(institution.isImported());
        assertSame(billing, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, OnboardingRequest)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded3() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing,
                onboarding, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "jane.doe@example.org", "4105551212", true);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest);
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertEquals("4105551212", institution.getSupportPhone());
        assertEquals("jane.doe@example.org", institution.getSupportEmail());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getShareCapital());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getRea());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(billing1, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("Product Name", onboardingRequest.getProductName());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, OnboardingRequest)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded4() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing1,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "jane.doe@example.org", "4105551212", true);

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing2);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest);
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertEquals("4105551212", institution.getSupportPhone());
        assertEquals("jane.doe@example.org", institution.getSupportEmail());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getShareCapital());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getRea());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals(1, institution.getOnboarding().size());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing1, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(billing2, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("Product Name", onboardingRequest.getProductName());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, OnboardingRequest)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded5() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing1,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "jane.doe@example.org", "4105551212", true);

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing2);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest);
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertEquals("4105551212", institution.getSupportPhone());
        assertEquals("jane.doe@example.org", institution.getSupportEmail());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getShareCapital());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getRea());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals(1, institution.getOnboarding().size());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing1, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(billing2, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("Product Name", onboardingRequest.getProductName());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, OnboardingRequest)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded7() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing1,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(), null,
                null, "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "jane.doe@example.org", "4105551212", true);

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing2);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, OnboardingRequest)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded8() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing1.setVatNumber("42");

        Premium premium1 = new Premium();
        premium1.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        premium1.setStatus(RelationshipState.PENDING);

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding1.setCreatedAt(null);
        onboarding1.setPremium(premium1);
        onboarding1.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding1.setProductId("Product Id");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        Billing billing2 = new Billing();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing2,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "jane.doe@example.org", "4105551212", true);

        Billing billing3 = new Billing();
        billing3.setPublicServices(true);
        billing3.setRecipientCode("Recipient Code");
        billing3.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing3);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest);
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertEquals("4105551212", institution.getSupportPhone());
        assertEquals("jane.doe@example.org", institution.getSupportEmail());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getShareCapital());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getRea());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals(2, institution.getOnboarding().size());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing2, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(billing3, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("Product Name", onboardingRequest.getProductName());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#validateOverridingData(InstitutionUpdate, Institution)}
     */
    @Test
    void testValidateOverridingData2() {
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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOverridingData(institutionUpdate,
                        new Institution("42", "42", "START - validateOverridingData for institution having externalId: {}",
                                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(), null, null,
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org",
                                "4105551212", true)));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#validateOverridingData(InstitutionUpdate, Institution)}
     */
    @Test
    void testValidateOverridingData5() {
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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("START - validateOverridingData for institution having externalId: {}");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOverridingData(institutionUpdate,
                        new Institution("42", "42", "START - validateOverridingData for institution having externalId: {}",
                                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(), null, null,
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org",
                                "4105551212", true)));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#validateOverridingData(InstitutionUpdate, Institution)}
     */
    @Test
    void testValidateOverridingData6() {
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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42",
                "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - validateOverridingData for institution having externalId: {}", billing, onboardingList,
                geographicTaxonomiesList, attributes, paymentServiceProvider1, dataProtectionOfficer1, null, null,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org", "4105551212",
                true);

        OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution);
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals("4105551212", institutionUpdate.getSupportPhone());
        assertEquals("jane.doe@example.org", institutionUpdate.getSupportEmail());
        assertEquals("Share Capital", institutionUpdate.getShareCapital());
        assertEquals("Rea", institutionUpdate.getRea());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PG, institutionUpdate.getInstitutionType());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getTaxCode());
        assertEquals("4105551212", institution.getSupportPhone());
        assertEquals("jane.doe@example.org", institution.getSupportEmail());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institution.getShareCapital());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getRea());
        assertSame(paymentServiceProvider1, institution.getPaymentServiceProvider());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getIpaCode());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertEquals("42", institution.getExternalId());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(dataProtectionOfficer1, institution.getDataProtectionOfficer());
        assertSame(billing, institution.getBilling());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#validateOverridingData(InstitutionUpdate, Institution)}
     */
    @Test
    void testValidateOverridingData7() {
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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("START - validateOverridingData for institution having externalId: {}");
        institutionUpdate.setZipCode("21654");
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42",
                "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - validateOverridingData for institution having externalId: {}", billing, onboardingList,
                geographicTaxonomiesList, attributes, paymentServiceProvider1, dataProtectionOfficer1, null, null,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org", "4105551212",
                true);

        OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution);
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institutionUpdate.getTaxCode());
        assertEquals("4105551212", institutionUpdate.getSupportPhone());
        assertEquals("jane.doe@example.org", institutionUpdate.getSupportEmail());
        assertEquals("Share Capital", institutionUpdate.getShareCapital());
        assertEquals("Rea", institutionUpdate.getRea());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getTaxCode());
        assertEquals("4105551212", institution.getSupportPhone());
        assertEquals("jane.doe@example.org", institution.getSupportEmail());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institution.getShareCapital());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getRea());
        assertSame(paymentServiceProvider1, institution.getPaymentServiceProvider());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getIpaCode());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertEquals("42", institution.getExternalId());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(dataProtectionOfficer1, institution.getDataProtectionOfficer());
        assertSame(billing, institution.getBilling());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String)}
     */
    @Test
    void testConvertToToken() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest");
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertEquals("Path", actualConvertToTokenResult.getContract());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String)}
     */
    @Test
    void testConvertToToken4() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest");
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        assertEquals(RelationshipState.ACTIVE, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertEquals("Path", actualConvertToTokenResult.getContract());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String)}
     */
    @Test
    void testConvertToToken5() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest");
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertEquals("Path", actualConvertToTokenResult.getContract());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructMap(UserToOnboard, OnboardingRequest, String)}
     */
    @Test
    void testConstructMap() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Map<String, Map<String, OnboardedProduct>> actualConstructMapResult = OnboardingInstitutionUtils
                .constructMap(userToOnboard, onboardingRequest, "42");
        assertEquals(1, actualConstructMapResult.size());
        assertEquals(1, actualConstructMapResult.get("42").size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructMap(UserToOnboard, OnboardingRequest, String)}
     */
    @Test
    void testConstructMap2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Map<String, Map<String, OnboardedProduct>> actualConstructMapResult = OnboardingInstitutionUtils
                .constructMap(userToOnboard, onboardingRequest, "42");
        assertEquals(1, actualConstructMapResult.size());
        assertEquals(1, actualConstructMapResult.get("42").size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructMap(UserToOnboard, OnboardingRequest, String)}
     */
    @Test
    void testConstructMap3() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Map<String, Map<String, OnboardedProduct>> actualConstructMapResult = OnboardingInstitutionUtils
                .constructMap(userToOnboard, onboardingRequest, "42");
        assertEquals(1, actualConstructMapResult.size());
        assertEquals(1, actualConstructMapResult.get("42").size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, InstitutionType)}
     */
    @Test
    void testConstructProduct() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                InstitutionType.PA);
        assertEquals(RelationshipState.PENDING, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, InstitutionType)}
     */
    @Test
    void testConstructProduct2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                InstitutionType.PG);
        assertEquals(RelationshipState.ACTIVE, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, InstitutionType)}
     */
    @Test
    void testConstructProduct3() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                InstitutionType.GSP);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, InstitutionType)}
     */
    @Test
    void testConstructProduct4() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                InstitutionType.PT);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, InstitutionType)}
     */
    @Test
    void testConstructProduct5() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                InstitutionType.SCP);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, InstitutionType)}
     */
    @Test
    void testConstructProduct6() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                InstitutionType.PSP);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, InstitutionType)}
     */
    @Test
    void testConstructProduct7() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                InstitutionType.UNKNOWN);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#retrieveStatusFromInstitutionType(InstitutionType)}
     */
    @Test
    void testRetrieveStatusFromInstitutionType() {
        assertEquals(RelationshipState.PENDING,
                OnboardingInstitutionUtils.retrieveStatusFromInstitutionType(InstitutionType.PA));
        assertEquals(RelationshipState.ACTIVE,
                OnboardingInstitutionUtils.retrieveStatusFromInstitutionType(InstitutionType.PG));
        assertEquals(RelationshipState.TOBEVALIDATED,
                OnboardingInstitutionUtils.retrieveStatusFromInstitutionType(InstitutionType.GSP));
        assertEquals(RelationshipState.TOBEVALIDATED,
                OnboardingInstitutionUtils.retrieveStatusFromInstitutionType(InstitutionType.PT));
        assertEquals(RelationshipState.TOBEVALIDATED,
                OnboardingInstitutionUtils.retrieveStatusFromInstitutionType(InstitutionType.SCP));
        assertEquals(RelationshipState.TOBEVALIDATED,
                OnboardingInstitutionUtils.retrieveStatusFromInstitutionType(InstitutionType.PSP));
        assertEquals(RelationshipState.TOBEVALIDATED,
                OnboardingInstitutionUtils.retrieveStatusFromInstitutionType(InstitutionType.UNKNOWN));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProductMap(OnboardingRequest, UserToOnboard)}
     */
    @Test
    void testConstructProductMap() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        Map<String, OnboardedProduct> actualConstructProductMapResult = OnboardingInstitutionUtils
                .constructProductMap(onboardingRequest, userToOnboard);
        assertEquals(1, actualConstructProductMapResult.size());
        assertTrue(actualConstructProductMapResult.containsKey("42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProductMap(OnboardingRequest, UserToOnboard)}
     */
    @Test
    void testConstructProductMap2() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        Map<String, OnboardedProduct> actualConstructProductMapResult = OnboardingInstitutionUtils
                .constructProductMap(onboardingRequest, userToOnboard);
        assertEquals(1, actualConstructProductMapResult.size());
        assertTrue(actualConstructProductMapResult.containsKey("42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProductMap(OnboardingRequest, UserToOnboard)}
     */
    @Test
    void testConstructProductMap3() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        Map<String, OnboardedProduct> actualConstructProductMapResult = OnboardingInstitutionUtils
                .constructProductMap(onboardingRequest, userToOnboard);
        assertEquals(1, actualConstructProductMapResult.size());
        assertTrue(actualConstructProductMapResult.containsKey("42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboarding(OnboardingRequest)}
     */
    @Test
    void testConstructOnboarding() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.PENDING, actualConstructOnboardingResult.getStatus());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboarding(OnboardingRequest)}
     */
    @Test
    void testConstructOnboarding4() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.ACTIVE, actualConstructOnboardingResult.getStatus());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboarding(OnboardingRequest)}
     */
    @Test
    void testConstructOnboarding5() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructOnboardingResult.getStatus());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardingValidManager(List)}
     */
    @Test
    void testGetOnboardingValidManager() {
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getOnboardingValidManager(new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardingValidManager(List)}
     */
    @Test
    void testGetOnboardingValidManager2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        List<String> actualOnboardingValidManager = OnboardingInstitutionUtils
                .getOnboardingValidManager(userToOnboardList);
        assertEquals(1, actualOnboardingValidManager.size());
        assertEquals("42", actualOnboardingValidManager.get(0));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardingValidManager(List)}
     */
    @Test
    void testGetOnboardingValidManager3() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setId("42");
        userToOnboard1.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard1.setProductRole(new ArrayList<>());
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);
        List<String> actualOnboardingValidManager = OnboardingInstitutionUtils
                .getOnboardingValidManager(userToOnboardList);
        assertEquals(2, actualOnboardingValidManager.size());
        assertEquals("42", actualOnboardingValidManager.get(0));
        assertEquals("42", actualOnboardingValidManager.get(1));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardingValidManager(List)}
     */
    @Test
    void testGetOnboardingValidManager4() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setId("42");
        userToOnboard.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.DELEGATE);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getOnboardingValidManager(userToOnboardList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List, String, String)}
     */
    @Test
    void testGetValidManager() {
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManager(new ArrayList<>(), "42", "42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List, String, String)}
     */
    @Test
    void testGetValidManager2() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(new OnboardedUser());
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManager(onboardedUserList, "42", "42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List, String, String)}
     */
    @Test
    void testGetValidManager4() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManager(onboardedUserList, "42", "42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List, String, String)}
     */
    @Test
    void testGetValidManager5() {
        HashMap<String, Map<String, OnboardedProduct>> stringMapMap = new HashMap<>();
        stringMapMap.put("42", new HashMap<>());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(stringMapMap);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManager(onboardedUserList, "42", "42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboardingRequest(Token, Institution)}
     */
    @Test
    void testConstructOnboardingRequest() {
        Token token = new Token();
        OnboardingRequest actualConstructOnboardingRequestResult = OnboardingInstitutionUtils
                .constructOnboardingRequest(token, new Institution());
        assertTrue(actualConstructOnboardingRequestResult.isSignContract());
        assertNull(actualConstructOnboardingRequestResult.getProductName());
        assertNull(actualConstructOnboardingRequestResult.getProductId());
        assertNull(actualConstructOnboardingRequestResult.getInstitutionUpdate().getDescription());
    }

}
