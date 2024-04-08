package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.TestUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Contract;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.product.entity.Product;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.pagopa.selfcare.mscore.core.util.TestUtils.dummyInstitutionPa;
import static org.junit.jupiter.api.Assertions.*;

class OnboardingInstitutionUtilsTest {

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List, List)}
     */
    @Test
    void testVerifyUsers2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        List<PartyRole> states = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList, states));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List, List)}
     */
    @Test
    void testVerifyUsers3() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(Env.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName(", ");
        userToOnboard1.setProductRole("");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode(", ");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);
        List<PartyRole> states = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList, states));
    }
    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, String)}
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
        List<InstitutionGeographicTaxonomies> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(stringList);
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
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId());
        assertFalse(institution.isImported());
        assertSame(billing, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.getSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, String)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded3() {

        Institution institution = dummyInstitutionPa();

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

        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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

        assertDoesNotThrow(() -> OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId()));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, String)}
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
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = dummyInstitutionPa();
        institution.setOnboarding(onboardingList);

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
        ArrayList<InstitutionGeographicTaxonomies> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(stringList);
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

        assertDoesNotThrow(() -> OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId()));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, String)}
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
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = dummyInstitutionPa();
        institution.setOnboarding(onboardingList);

        Billing billing1 = new Billing();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();

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
        ArrayList<InstitutionGeographicTaxonomies> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(stringList);
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

        assertDoesNotThrow(() -> OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId()));
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, String)}
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
        onboarding1.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding1.setProductId("Product Id");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        Institution institution = dummyInstitutionPa();
        institution.setOnboarding(onboardingList);

        Billing billing2 = new Billing();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();

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
        ArrayList<InstitutionGeographicTaxonomies> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(stringList);
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

        assertDoesNotThrow(() -> OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId()));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#checkIfProductAlreadyOnboarded(Institution, String)}
     */
    @Test
    void testCheckIfProductAlreadyOnboarded9() {
        Onboarding onboarding = new Onboarding();
        onboarding.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = dummyInstitutionPa();
        institution.setOnboarding(onboardingList);

        Billing billing3 = new Billing();
        billing3.setPublicServices(true);
        billing3.setRecipientCode("Recipient Code");
        billing3.setVatNumber("42");

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<InstitutionGeographicTaxonomies> stringList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(stringList);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(ResourceConflictException.class,
                () -> OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId()),
                "Product 42 already onboarded for institution having externalId 42");
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#validateOnboarding(Billing, boolean)}
     */
    @Test
    void testValidatePaOnboarding() {
        Billing billing = TestUtils.createSimpleBilling();

        Contract contract = TestUtils.createSimpleContract();

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        OnboardingRequest onboardingRequest = TestUtils.dummyOnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        OnboardingInstitutionUtils.validateOnboarding(onboardingRequest.getBillingRequest(), true);
        assertSame(billing, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.getSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(TokenType.INSTITUTION, onboardingRequest.getTokenType());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("prod-io", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#validateOnboarding(Billing, boolean)}
     */
    @Test
    void testValidatePaOnboarding4() {
        Billing billing = TestUtils.createSimpleBilling();
        billing.setRecipientCode(null);

        Contract contract = TestUtils.createSimpleContract();

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        OnboardingRequest onboardingRequest = TestUtils.dummyOnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOnboarding(onboardingRequest.getBillingRequest(), true));
    }

    /**
     * Method under test:
     * {@link OnboardingInstitutionUtils#validateOverridingData(InstitutionUpdate, Institution)}
     */
    @Test
    void testValidateOverridingData() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setCity("Oxford");
        institutionUpdate.setCountry("GB");
        institutionUpdate.setCounty("3");
        institutionUpdate.setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setIvassCode("Ivass Code");
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, new Institution()));
    }

    /**
     * Method under test:
     * {@link OnboardingInstitutionUtils#validateOverridingData(InstitutionUpdate, Institution)}
     */
    @Test
    void testValidateOverridingData2() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setCity("Oxford");
        institutionUpdate.setCountry("GB");
        institutionUpdate.setCounty("3");
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setIvassCode("Ivass Code");
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42", "Legal Register Name",
                "42", true);

        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setZipCode(null);
        institutionUpdate.setAddress("via del corso");

        Institution institution = new Institution();
        institution.setOrigin("IPA");
        institution.setZipCode(null);
        institution.setExternalId("42");
        institution.setAddress("via roma");
        institution.setDigitalAddress(null);
        institution.setDescription(null);
        institution.setTaxCode(null);

        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution));
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOperatorProduct(UserToOnboard, String)}
     */
    @Test
    void testConstructOperatorProduct() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        String productId = "42";

        OnboardedProduct actualConstructOperatorProductResult = OnboardingInstitutionUtils
                .constructOperatorProduct(userToOnboard, productId);
        assertEquals(RelationshipState.ACTIVE, actualConstructOperatorProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructOperatorProductResult.getRole());
        assertEquals(productId, actualConstructOperatorProductResult.getProductId());
        assertEquals(Env.ROOT, actualConstructOperatorProductResult.getEnv());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOperatorProduct(UserToOnboard, String)}
     */
    @Test
    void testConstructOperatorProduct2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(null);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        String productId = "42";

        OnboardedProduct actualConstructOperatorProductResult = OnboardingInstitutionUtils
                .constructOperatorProduct(userToOnboard, productId);
        assertEquals(RelationshipState.ACTIVE, actualConstructOperatorProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructOperatorProductResult.getRole());
        assertEquals("42", actualConstructOperatorProductResult.getProductId());
        assertEquals(Env.ROOT, actualConstructOperatorProductResult.getEnv());
    }

    @Test
    void testConstructProduct() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setContract(new Contract());
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                onboardingRequest, new Institution());
        assertEquals(RelationshipState.ACTIVE, actualConstructProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructProductResult.getRole());
        assertEquals(Env.ROOT, actualConstructProductResult.getEnv());
    }

    @Test
    void testConstructProduct2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setContract(new Contract());

        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PA);
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                onboardingRequest, institution);
        assertEquals(RelationshipState.PENDING, actualConstructProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructProductResult.getRole());
        assertEquals(Env.ROOT, actualConstructProductResult.getEnv());
    }

    @Test
    void testValidateSaOnboarding() {
        Billing billing = TestUtils.createSimpleBilling();
        billing.setRecipientCode(null);

        Contract contract = TestUtils.createSimpleContract();

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdateSA();

        OnboardingRequest onboardingRequest = TestUtils.dummyOnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        OnboardingInstitutionUtils.validateOnboarding(onboardingRequest.getBillingRequest(), false);
        assertSame(billing, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.getSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(TokenType.INSTITUTION, onboardingRequest.getTokenType());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("prod-io", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
    }

    @Test
    void testValidateSaOnboardingNull() {
        Billing billing = TestUtils.createSimpleBilling();
        billing.setVatNumber(null);

        Contract contract = TestUtils.createSimpleContract();

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdateSA();

        OnboardingRequest onboardingRequest = TestUtils.dummyOnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOnboarding(onboardingRequest.getBillingRequest(), false));
    }
}

