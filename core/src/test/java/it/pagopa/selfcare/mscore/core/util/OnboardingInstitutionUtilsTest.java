package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.*;
import it.pagopa.selfcare.mscore.core.TestUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.OffsetDateTime;
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
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List, Token)}
     */
    @Test
    void testGetValidManagerToOnboard() {
        ArrayList<UserToOnboard> users = new ArrayList<>();

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
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManagerToOnboard(users, token));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List, Token)}
     */
    @Test
    void testGetValidManagerToOnboard6() {
        ArrayList<UserToOnboard> users = new ArrayList<>();

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

        ArrayList<TokenUser> tokenUserList = new ArrayList<>();
        tokenUserList.add(new TokenUser("42", PartyRole.MANAGER));

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
        token.setUsers(tokenUserList);
        List<String> actualValidManagerToOnboard = OnboardingInstitutionUtils.getValidManagerToOnboard(users, token);
        assertEquals(1, actualValidManagerToOnboard.size());
        assertEquals("42", actualValidManagerToOnboard.get(0));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List, Token)}
     */
    @Test
    void testGetValidManagerToOnboard3() {
        ArrayList<UserToOnboard> users = new ArrayList<>();

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

        ArrayList<TokenUser> tokenUserList = new ArrayList<>();
        tokenUserList.add(new TokenUser("42", PartyRole.MANAGER));
        tokenUserList.add(new TokenUser("42", PartyRole.MANAGER));

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
        token.setUsers(tokenUserList);
        List<String> actualValidManagerToOnboard = OnboardingInstitutionUtils.getValidManagerToOnboard(users, token);
        assertEquals(2, actualValidManagerToOnboard.size());
        assertEquals("42", actualValidManagerToOnboard.get(0));
        assertEquals("42", actualValidManagerToOnboard.get(1));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List, Token)}
     */
    @Test
    void testGetValidManagerToOnboard4() {
        ArrayList<UserToOnboard> users = new ArrayList<>();

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

        ArrayList<TokenUser> tokenUserList = new ArrayList<>();
        tokenUserList.add(new TokenUser("42", null));

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
        token.setUsers(tokenUserList);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManagerToOnboard(users, token));
    }

    @Test
    void testGetValidManagerToOnboard2() {
        ArrayList<UserToOnboard> users = new ArrayList<>();

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

        assertThrows(InvalidRequestException.class, () -> OnboardingInstitutionUtils.getValidManagerToOnboard(users, null));

    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardedValidManager(Token)}
     */
    @Test
    void testGetOnboardedValidManager() {
        Token token = new Token();
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getOnboardedValidManager(token));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboardingRequest(OnboardingLegalsRequest)}
     */
    @Test
    void testConstructOnboardingRequest() {
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
        onboardingLegalsRequest.setTokenType(TokenType.INSTITUTION);
        onboardingLegalsRequest.setUsers(new ArrayList<>());
        OnboardingRequest actualConstructOnboardingRequestResult = OnboardingInstitutionUtils
                .constructOnboardingRequest(onboardingLegalsRequest);
        assertTrue(actualConstructOnboardingRequestResult.isSignContract());
        assertTrue(actualConstructOnboardingRequestResult.getUsers().isEmpty());
        assertEquals(TokenType.LEGALS, actualConstructOnboardingRequestResult.getTokenType());
        assertEquals("Product Name", actualConstructOnboardingRequestResult.getProductName());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductId());
        assertEquals("42", actualConstructOnboardingRequestResult.getInstitutionExternalId());
        assertSame(contract, actualConstructOnboardingRequestResult.getContract());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboardingRequest(Token, Institution)}
     */
    @Test
    void testConstructOnboardingRequest2() {
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
        Institution institution = new Institution();
        institution.setBilling(new Billing());
        List<Onboarding> onboardingList = new ArrayList<>();
        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(new Billing());
        onboarding.setTokenId("42");
        onboarding.setPricingPlan("C3");
        onboarding.setProductId("42");
        onboardingList.add(onboarding);
        institution.setOnboarding(onboardingList);
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
        OnboardingRequest actualConstructOnboardingRequestResult = OnboardingInstitutionUtils
                .constructOnboardingRequest(token, institution);
        assertTrue(actualConstructOnboardingRequestResult.isSignContract());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductName());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductId());
        assertEquals("Contract Template", actualConstructOnboardingRequestResult.getContract().getPath());
        assertNull(actualConstructOnboardingRequestResult.getInstitutionUpdate().getDescription());
    }

    @Test
    void testConstructOnboardingRequest3() {
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
        Institution institution = new Institution();
        institution.setBilling(new Billing());
        List<Onboarding> onboardingList = new ArrayList<>();
        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(new Billing());
        onboarding.setTokenId("43");
        onboarding.setPricingPlan("C3");
        onboarding.setProductId("42");
        onboardingList.add(onboarding);
        institution.setOnboarding(onboardingList);
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
        OnboardingRequest actualConstructOnboardingRequestResult = OnboardingInstitutionUtils
                .constructOnboardingRequest(token, institution);
        assertTrue(actualConstructOnboardingRequestResult.isSignContract());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductName());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductId());
        assertEquals("Contract Template", actualConstructOnboardingRequestResult.getContract().getPath());
        assertNull(actualConstructOnboardingRequestResult.getInstitutionUpdate().getDescription());
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
        assertTrue(onboardingRequest.isSignContract());
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
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42", Origin.SELC.name(),
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing,
                onboarding, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                true, OffsetDateTime.now(), OffsetDateTime.now(), null, null);

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
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId());
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertTrue(onboardingRequest.isSignContract());
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
        Billing billing1 = new Billing();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42", Origin.SELC.name(),
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing1,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                true, OffsetDateTime.now(), OffsetDateTime.now(), null, null);

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
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId());
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertEquals(1, institution.getOnboarding().size());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getOriginId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing1, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
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
        Billing billing1 = new Billing();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42", Origin.SELC.name(),
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing1,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                true, OffsetDateTime.now(), OffsetDateTime.now(), null, null);

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
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId());
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertEquals(1, institution.getOnboarding().size());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getOriginId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing1, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
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
        Billing billing2 = new Billing();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42", Origin.SELC.name(),
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing2,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                true, OffsetDateTime.now(), OffsetDateTime.now(), null, null);

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
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest.getProductId());
        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getTaxCode());
        assertEquals(2, institution.getOnboarding().size());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getOriginId());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing2, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
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
        Billing billing2 = new Billing();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42", Origin.SELC.name(),
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing2,
                onboardingList, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer, null, null,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                true, OffsetDateTime.now(), OffsetDateTime.now(), null, null);

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
     * Method under test: {@link OnboardingInstitutionUtils#validatePaOnboarding(Billing)}
     */
    @Test
    void testValidatePaOnboarding() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
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

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.validatePaOnboarding(onboardingRequest.getBillingRequest());
        assertSame(billing, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals(TokenType.INSTITUTION, onboardingRequest.getTokenType());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Product Name", onboardingRequest.getProductName());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#validatePaOnboarding(Billing)}
     */
    @Test
    void testValidatePaOnboarding4() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber(null);

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

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

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validatePaOnboarding(onboardingRequest.getBillingRequest()));
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", Origin.SELC.name(), "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(), null, null,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", true,
                OffsetDateTime.now(), OffsetDateTime.now(), null, null);
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", Origin.SELC.name(), "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St",
                "21654", "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(), null, null,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", true,
                OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#validateOverridingData(InstitutionUpdate, Institution)}
     */
    @Test
    void testValidateOverridingData6() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("pec@pec.it");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42", Origin.SELC.name(),
                "START - validateOverridingData for institution having externalId: {}",
                "The characteristics of someone or something", InstitutionType.PA, "pec@pec.it", "42 Main St", "21654",
                "taxCode", billing, onboardingList,
                geographicTaxonomiesList, attributes, paymentServiceProvider1, dataProtectionOfficer1, null, null,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", true, OffsetDateTime.now(),
                OffsetDateTime.now(), null, null);

        OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution);
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PA);
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest, institution);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.PENDING, actualConstructOnboardingResult.getStatus());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
    }

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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest, new Institution());
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.ACTIVE, actualConstructOnboardingResult.getStatus());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
    }

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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest, new Institution());
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructOnboardingResult.getStatus());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
    }
    @Test
    void testConstructOnboarding6() {
        Billing billing = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();
        DataProtectionOfficer dataProtectionOfficer = TestUtils.createSimpleDataProtectionOfficer();
        PaymentServiceProvider paymentServiceProvider = TestUtils.createSimplePaymentServiceProvider();

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Institution institution = new Institution();
        institution.setOrigin("IPA");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("prod-interop");
        onboardingRequest.setProductName("prod-interop");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest, institution);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.PENDING, actualConstructOnboardingResult.getStatus());
        assertEquals("prod-interop", actualConstructOnboardingResult.getProductId());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "PSP,SELC,prod-io",
            "PSP,SELC,prod-interop",
            "PSP,IPA,prod-io",
            "PSP,IPA,prod-interop",
            "GSP,SELC,prod-io",
            "GSP,SELC,prod-interop",
            "GSP,IPA,prod-io"
    })
    void testConstructOnboarding_GSP_prodInterop_originIPA_failingConditions(InstitutionType institutionType, String origin, String productId) {
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setInstitutionType(institutionType);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Institution institution = new Institution();
        institution.setOrigin(origin);

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId(productId);
        onboardingRequest.setProductName("prod-interop");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest, institution);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructOnboardingResult.getStatus());
        assertEquals(productId, actualConstructOnboardingResult.getProductId());
    }

}

