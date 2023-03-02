package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OnboardingInstitutionUtilsTest {


    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List, List)}
     */
    @Test
    void testVerifyUsers2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
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
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(EnvEnum.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName(", ");
        userToOnboard1.setProductRole(new ArrayList<>());
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

    @Test
    void getOnboardingValidManager() {
        List<UserToOnboard> userToOnboardList = new ArrayList<>();
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        userToOnboardList.add(userToOnboard);

        assertNotNull(OnboardingInstitutionUtils.getOnboardingValidManager(userToOnboardList));
    }

    @Test
    void getOnboardingValidManager2() {
        List<UserToOnboard> userToOnboardList = new ArrayList<>();

        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.getOnboardingValidManager(userToOnboardList));
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
    void testGetValidManager3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());

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
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManager(onboardedUserList, "42", "42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManager(List, String, String)}
     */
    @Test
    void testGetValidManager6() {
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

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

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboardingRequest(OnboardingLegalsRequest, Institution)}
     */
    @Test
    void testConstructOnboardingRequest4() {
        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        OnboardingLegalsRequest onboardingLegalsRequest = new OnboardingLegalsRequest();
        onboardingLegalsRequest.setContract(contract);
        onboardingLegalsRequest.setInstitutionExternalId("42");
        onboardingLegalsRequest.setInstitutionId("42");
        onboardingLegalsRequest.setProductId("42");
        onboardingLegalsRequest.setProductName("Product Name");
        onboardingLegalsRequest.setSignContract(true);
        onboardingLegalsRequest.setUsers(new ArrayList<>());
        OnboardingRequest actualConstructOnboardingRequestResult = OnboardingInstitutionUtils
                .constructOnboardingRequest(onboardingLegalsRequest, new Institution());
        assertTrue(actualConstructOnboardingRequestResult.isSignContract());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductName());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductId());
        assertEquals("Path", actualConstructOnboardingRequestResult.getContract().getPath());
        assertNull(actualConstructOnboardingRequestResult.getInstitutionUpdate().getDescription());
    }

    @Test
    void constructProductMap() {
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
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        assertNotNull(OnboardingInstitutionUtils.constructProductMap(onboardingRequest, userToOnboard));
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
        Institution institution = new Institution("42", "42", "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(), null, null,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org",
                "4105551212", true);
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
        Institution institution = new Institution("42", "42", "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(), null, null,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org",
                "4105551212", true);
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
     * Method under test: {@link OnboardingInstitutionUtils#constructOperatorProduct(UserToOnboard, OnboardingOperatorsRequest)}
     */
    @Test
    void testConstructOperatorProduct() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        OnboardedProduct actualConstructOperatorProductResult = OnboardingInstitutionUtils
                .constructOperatorProduct(userToOnboard, onboardingOperatorsRequest);
        assertEquals(RelationshipState.ACTIVE, actualConstructOperatorProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructOperatorProductResult.getRole());
        assertEquals("42", actualConstructOperatorProductResult.getProductId());
        assertEquals(EnvEnum.ROOT, actualConstructOperatorProductResult.getEnv());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOperatorProduct(UserToOnboard, OnboardingOperatorsRequest)}
     */
    @Test
    void testConstructOperatorProduct2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(null);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        OnboardedProduct actualConstructOperatorProductResult = OnboardingInstitutionUtils
                .constructOperatorProduct(userToOnboard, onboardingOperatorsRequest);
        assertEquals(RelationshipState.ACTIVE, actualConstructOperatorProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructOperatorProductResult.getRole());
        assertEquals("42", actualConstructOperatorProductResult.getProductId());
        assertEquals(EnvEnum.ROOT, actualConstructOperatorProductResult.getEnv());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, OnboardingRequest)}
     */
    @Test
    void testConstructProduct() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setContract(new Contract());
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                onboardingRequest);
        assertEquals(RelationshipState.ACTIVE, actualConstructProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructProductResult.getRole());
        assertTrue(actualConstructProductResult.getProductRoles().isEmpty());
        assertEquals(EnvEnum.ROOT, actualConstructProductResult.getEnv());
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

}

