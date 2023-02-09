package it.pagopa.selfcare.mscore.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
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

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

class OnboardingInstitutionUtilsTest {

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyPgUsers(List)}
     */
    @Test
    void testVerifyPgUsers2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.verifyPgUsers(onboardedUserList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyPgUsers(List)}
     */
    @Test
    void testVerifyPgUsers3() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

        OnboardedUser onboardedUser1 = new OnboardedUser();
        onboardedUser1.setBindings(new HashMap<>());
        onboardedUser1.setCreatedAt(null);
        onboardedUser1.setEmail("jane.doe@example.org");
        onboardedUser1.setId("42");
        onboardedUser1.setName("Name");
        onboardedUser1.setProductRole(new ArrayList<>());
        onboardedUser1.setRole(PartyRole.MANAGER);
        onboardedUser1.setSurname("Doe");
        onboardedUser1.setTaxCode("Tax Code");
        onboardedUser1.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser1);
        onboardedUserList.add(onboardedUser);
        OnboardingInstitutionUtils.verifyPgUsers(onboardedUserList);
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyPgUsers(List)}
     */
    @Test
    void testVerifyPgUsers4() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

        OnboardedUser onboardedUser1 = new OnboardedUser();
        onboardedUser1.setBindings(new HashMap<>());
        onboardedUser1.setCreatedAt(null);
        onboardedUser1.setEmail("jane.doe@example.org");
        onboardedUser1.setId("42");
        onboardedUser1.setName("Name");
        onboardedUser1.setProductRole(new ArrayList<>());
        onboardedUser1.setRole(PartyRole.DELEGATE);
        onboardedUser1.setSurname("Doe");
        onboardedUser1.setTaxCode("Tax Code");
        onboardedUser1.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser1);
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.verifyPgUsers(onboardedUserList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        OnboardingInstitutionUtils.verifyUsers(new ArrayList<>());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers2() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        OnboardingInstitutionUtils.verifyUsers(onboardedUserList);
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers3() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

        OnboardedUser onboardedUser1 = new OnboardedUser();
        onboardedUser1.setBindings(new HashMap<>());
        onboardedUser1.setCreatedAt(null);
        onboardedUser1.setEmail("jane.doe@example.org");
        onboardedUser1.setId("42");
        onboardedUser1.setName("Name");
        onboardedUser1.setProductRole(new ArrayList<>());
        onboardedUser1.setRole(PartyRole.MANAGER);
        onboardedUser1.setSurname("Doe");
        onboardedUser1.setTaxCode("Tax Code");
        onboardedUser1.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser1);
        onboardedUserList.add(onboardedUser);
        OnboardingInstitutionUtils.verifyUsers(onboardedUserList);
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List)}
     */
    @Test
    void testVerifyUsers4() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.SUB_DELEGATE);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.verifyUsers(onboardedUserList));
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
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
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
        assertSame(billing, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(stringList, onboardingRequest.getUsers());
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
                onboarding, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null);

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
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals(stringList, institution.getOnboarding());
        assertEquals(stringList, institution.getAttributes());
        assertEquals("42", institution.getExternalId());
        assertEquals(stringList, institution.getGeographicTaxonomies());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals("42", institution.getId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertSame(billing, institution.getBilling());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertSame(billing1, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(stringList, onboardingRequest.getUsers());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
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
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null);

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
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals(1, institution.getOnboarding().size());
        assertEquals(stringList, institution.getAttributes());
        assertEquals("42", institution.getExternalId());
        assertEquals(stringList, institution.getGeographicTaxonomies());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals("42", institution.getId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertSame(billing1, institution.getBilling());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertSame(billing2, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(stringList, onboardingRequest.getUsers());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
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
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null);

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
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals(1, institution.getOnboarding().size());
        assertEquals(stringList, institution.getAttributes());
        assertEquals("42", institution.getExternalId());
        assertEquals(stringList, institution.getGeographicTaxonomies());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals("42", institution.getId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertSame(billing1, institution.getBilling());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertSame(billing2, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(stringList, onboardingRequest.getUsers());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
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
                null);

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
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null);

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
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals(2, institution.getOnboarding().size());
        assertEquals(stringList, institution.getAttributes());
        assertEquals("42", institution.getExternalId());
        assertEquals(stringList, institution.getGeographicTaxonomies());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getIpaCode());
        assertEquals("42", institution.getId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertSame(billing2, institution.getBilling());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertSame(billing3, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(stringList, onboardingRequest.getUsers());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
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
        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        Institution institution =  new Institution("42", "42", "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                geographicTaxonomies, attributes, paymentServiceProvider1, dataProtectionOfficer1, null, null);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution));
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
        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        Institution institution =  new Institution("42", "42", "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                geographicTaxonomies, attributes, paymentServiceProvider1, dataProtectionOfficer1, null, null);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution));
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
                geographicTaxonomiesList, attributes, paymentServiceProvider1, dataProtectionOfficer1, null, null);

        OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution);
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PG, institutionUpdate.getInstitutionType());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        List<String> geographicTaxonomyCodes = institutionUpdate.getGeographicTaxonomyCodes();
        assertEquals(onboardingList, geographicTaxonomyCodes);
        assertEquals("42 Main St", institution.getAddress());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getTaxCode());
        assertSame(paymentServiceProvider1, institution.getPaymentServiceProvider());
        assertEquals(geographicTaxonomiesList, institution.getOnboarding());
        assertEquals(geographicTaxonomiesList, institution.getAttributes());
        assertEquals("42", institution.getExternalId());
        assertEquals(geographicTaxonomyCodes, institution.getGeographicTaxonomies());
        assertSame(dataProtectionOfficer1, institution.getDataProtectionOfficer());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getIpaCode());
        assertEquals("42", institution.getId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertSame(billing, institution.getBilling());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertEquals("42 Main St", institution.getDigitalAddress());
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
                geographicTaxonomiesList, attributes, paymentServiceProvider1, dataProtectionOfficer1, null, null);

        OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution);
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institutionUpdate.getTaxCode());
        List<String> geographicTaxonomyCodes = institutionUpdate.getGeographicTaxonomyCodes();
        assertEquals(onboardingList, geographicTaxonomyCodes);
        assertEquals("42 Main St", institution.getAddress());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getTaxCode());
        assertSame(paymentServiceProvider1, institution.getPaymentServiceProvider());
        assertEquals(geographicTaxonomiesList, institution.getOnboarding());
        assertEquals(geographicTaxonomiesList, institution.getAttributes());
        assertEquals("42", institution.getExternalId());
        assertEquals(geographicTaxonomyCodes, institution.getGeographicTaxonomies());
        assertSame(dataProtectionOfficer1, institution.getDataProtectionOfficer());
        assertEquals("START - validateOverridingData for institution having externalId: {}", institution.getIpaCode());
        assertEquals("42", institution.getId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertSame(billing, institution.getBilling());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertEquals("42 Main St", institution.getDigitalAddress());
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
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
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
        assertEquals(stringList, actualConvertToTokenResult.getUsers());
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
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
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
        assertEquals(stringList, actualConvertToTokenResult.getUsers());
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
        ArrayList<String> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomyCodes(stringList);
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
        assertEquals(stringList, actualConvertToTokenResult.getUsers());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertEquals("Path", actualConvertToTokenResult.getContract());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructMap(OnboardedUser, OnboardingRequest, String)}
     */
    @Test
    void testConstructMap() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

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
        Map<String, Map<String, Product>> actualConstructMapResult = OnboardingInstitutionUtils
                .constructMap(onboardedUser, onboardingRequest, "42");
        assertEquals(1, actualConstructMapResult.size());
        Map<String, Product> getResult = actualConstructMapResult.get("42");
        assertEquals(1, getResult.size());
        Product getResult1 = getResult.get("42");
        assertEquals(RelationshipState.PENDING, getResult1.getStatus());
        assertEquals(1, getResult1.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructMap(OnboardedUser, OnboardingRequest, String)}
     */
    @Test
    void testConstructMap2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

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
        Map<String, Map<String, Product>> actualConstructMapResult = OnboardingInstitutionUtils
                .constructMap(onboardedUser, onboardingRequest, "42");
        assertEquals(1, actualConstructMapResult.size());
        Map<String, Product> getResult = actualConstructMapResult.get("42");
        assertEquals(1, getResult.size());
        Product getResult1 = getResult.get("42");
        assertEquals(RelationshipState.ACTIVE, getResult1.getStatus());
        assertEquals(1, getResult1.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructMap(OnboardedUser, OnboardingRequest, String)}
     */
    @Test
    void testConstructMap3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");

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
        Map<String, Map<String, Product>> actualConstructMapResult = OnboardingInstitutionUtils
                .constructMap(onboardedUser, onboardingRequest, "42");
        assertEquals(1, actualConstructMapResult.size());
        Map<String, Product> getResult = actualConstructMapResult.get("42");
        assertEquals(1, getResult.size());
        Product getResult1 = getResult.get("42");
        assertEquals(RelationshipState.TOBEVALIDATED, getResult1.getStatus());
        assertEquals(1, getResult1.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(OnboardedUser, InstitutionType)}
     */
    @Test
    void testConstructProduct() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Product actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(onboardedUser,
                InstitutionType.PA);
        assertEquals(RelationshipState.PENDING, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(OnboardedUser, InstitutionType)}
     */
    @Test
    void testConstructProduct2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Product actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(onboardedUser,
                InstitutionType.PG);
        assertEquals(RelationshipState.ACTIVE, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(OnboardedUser, InstitutionType)}
     */
    @Test
    void testConstructProduct3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Product actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(onboardedUser,
                InstitutionType.GSP);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(OnboardedUser, InstitutionType)}
     */
    @Test
    void testConstructProduct4() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Product actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(onboardedUser,
                InstitutionType.PT);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(OnboardedUser, InstitutionType)}
     */
    @Test
    void testConstructProduct5() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Product actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(onboardedUser,
                InstitutionType.SCP);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(OnboardedUser, InstitutionType)}
     */
    @Test
    void testConstructProduct6() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Product actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(onboardedUser,
                InstitutionType.PSP);
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(1, actualConstructProductResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(OnboardedUser, InstitutionType)}
     */
    @Test
    void testConstructProduct7() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Product actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(onboardedUser,
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
     * Method under test: {@link OnboardingInstitutionUtils#constructProductMap(OnboardingRequest, OnboardedUser)}
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

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Map<String, Product> actualConstructProductMapResult = OnboardingInstitutionUtils
                .constructProductMap(onboardingRequest, onboardedUser);
        assertEquals(1, actualConstructProductMapResult.size());
        Product getResult = actualConstructProductMapResult.get("42");
        assertEquals(RelationshipState.PENDING, getResult.getStatus());
        assertEquals(1, getResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProductMap(OnboardingRequest, OnboardedUser)}
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

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Map<String, Product> actualConstructProductMapResult = OnboardingInstitutionUtils
                .constructProductMap(onboardingRequest, onboardedUser);
        assertEquals(1, actualConstructProductMapResult.size());
        Product getResult = actualConstructProductMapResult.get("42");
        assertEquals(RelationshipState.ACTIVE, getResult.getStatus());
        assertEquals(1, getResult.getRoles().size());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProductMap(OnboardingRequest, OnboardedUser)}
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

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("Name");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("Tax Code");
        onboardedUser.setUser("User");
        Map<String, Product> actualConstructProductMapResult = OnboardingInstitutionUtils
                .constructProductMap(onboardingRequest, onboardedUser);
        assertEquals(1, actualConstructProductMapResult.size());
        Product getResult = actualConstructProductMapResult.get("42");
        assertEquals(RelationshipState.TOBEVALIDATED, getResult.getStatus());
        assertEquals(1, getResult.getRoles().size());
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
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List)}
     */
    @Test
    void testGetValidManager() {
        List<OnboardedUser> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.getValidManager(list));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List)}
     */
    @Test
    void testGetValidManager2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("START - getValidManager for users list size: {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("START - getValidManager for users list size: {}");
        onboardedUser.setUser("START - getValidManager for users list size: {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        Assertions.assertDoesNotThrow(() -> OnboardingInstitutionUtils.getValidManager(onboardedUserList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List)}
     */
    @Test
    void testGetValidManager3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser.setName("START - getValidManager for users list size: {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.DELEGATE);
        onboardedUser.setSurname("Doe");
        onboardedUser.setTaxCode("START - getValidManager for users list size: {}");
        onboardedUser.setUser("START - getValidManager for users list size: {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.getValidManager(onboardedUserList));
    }
}

