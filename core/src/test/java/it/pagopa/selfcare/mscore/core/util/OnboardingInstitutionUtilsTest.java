package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.utils.OriginEnum;
import it.pagopa.selfcare.mscore.utils.TokenTypeEnum;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OnboardingInstitutionUtilsTest {


    @Test
    void testVerifyUsers0() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");

        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        List<PartyRole> states = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList, states));
    }

    @Test
    void testVerifyUsers1() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");

        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(EnvEnum.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName(", ");

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
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List, List)}
     */
    @Test
    void testVerifyUsers2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("Product Role");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList, new ArrayList<>()));
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
        userToOnboard.setProductRole("Product Role");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(EnvEnum.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName(", ");
        userToOnboard1.setProductRole(", ");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode(", ");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.verifyUsers(userToOnboardList, new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#verifyUsers(List, List)}
     */
    @Test
    void testVerifyUsers4() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("Product Role");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        ArrayList<PartyRole> partyRoleList = new ArrayList<>();
        partyRoleList.add(PartyRole.MANAGER);
        OnboardingInstitutionUtils.verifyUsers(userToOnboardList, partyRoleList);
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, onboardingRequest);
        assertFalse(institution.isImported());
        assertSame(billing, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertSame(contract, onboardingRequest.getContract());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertEquals("Product Name", onboardingRequest.getProductName());
        assertEquals(TokenTypeEnum.INSTITUTION, onboardingRequest.getTokenType());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertSame(contractImported, onboardingRequest.getContractImported());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertEquals("42", onboardingRequest.getProductId());
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
        Institution institution = new Institution("42", "42", OriginEnum.MOCK, "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", billing,
                onboarding, geographicTaxonomies, attributes, paymentServiceProvider, dataProtectionOfficer,
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                "jane.doe@example.org", "4105551212", true, null, null);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
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
        assertEquals("42", institution.getOriginId());
        assertEquals("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals(OriginEnum.MOCK, institution.getOrigin());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(billing1, onboardingRequest.getBillingRequest());
        assertTrue(onboardingRequest.isSignContract());
        assertEquals(TokenTypeEnum.INSTITUTION, onboardingRequest.getTokenType());
        assertEquals("Product Name", onboardingRequest.getProductName());
        assertEquals("42", onboardingRequest.getProductId());
        assertEquals("Pricing Plan", onboardingRequest.getPricingPlan());
        assertSame(institutionUpdate, onboardingRequest.getInstitutionUpdate());
        assertEquals("42", onboardingRequest.getInstitutionExternalId());
        assertSame(contractImported, onboardingRequest.getContractImported());
        assertSame(contract, onboardingRequest.getContract());
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
        institutionUpdate.setImported(true);
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
                        new Institution("42", "42", OriginEnum.MOCK, "42", "The characteristics of someone or something",
                                InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                                "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(),
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org",
                                "4105551212", true, null, null)));
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
        institutionUpdate.setImported(true);
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
                        new Institution("42", "42", OriginEnum.MOCK, "42", "The characteristics of someone or something",
                                InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                                "START - validateOverridingData for institution having externalId: {}", billing, onboarding,
                                geographicTaxonomies, attributes, paymentServiceProvider1, new DataProtectionOfficer(),
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}",
                                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org",
                                "4105551212", true, null, null)));
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
        institutionUpdate.setImported(true);
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
        Institution institution = new Institution("42", "42", OriginEnum.MOCK, "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - validateOverridingData for institution having externalId: {}", billing, onboardingList,
                geographicTaxonomiesList, attributes, paymentServiceProvider1, dataProtectionOfficer1,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org", "4105551212",
                true, null, null);

        OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution);
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertTrue(institutionUpdate.isImported());
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
        assertEquals("42", institution.getOriginId());
        assertEquals(OriginEnum.MOCK, institution.getOrigin());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(dataProtectionOfficer1, institution.getDataProtectionOfficer());
        assertEquals("42 Main St", institution.getDigitalAddress());
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
        institutionUpdate.setImported(true);
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
        Institution institution = new Institution("42", "42", OriginEnum.MOCK, "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - validateOverridingData for institution having externalId: {}", billing, onboardingList,
                geographicTaxonomiesList, attributes, paymentServiceProvider1, dataProtectionOfficer1,
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}",
                "START - validateOverridingData for institution having externalId: {}", "jane.doe@example.org", "4105551212",
                true, null, null);

        OnboardingInstitutionUtils.validateOverridingData(institutionUpdate, institution);
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertTrue(institutionUpdate.isImported());
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
        assertEquals("42", institution.getOriginId());
        assertEquals(OriginEnum.MOCK, institution.getOrigin());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertEquals("42", institution.getExternalId());
        assertEquals("START - validateOverridingData for institution having externalId: {}",
                institution.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institution.getDescription());
        assertSame(dataProtectionOfficer1, institution.getDataProtectionOfficer());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertSame(billing, institution.getBilling());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String, OffsetDateTime)}
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest", null);
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        assertEquals(TokenTypeEnum.INSTITUTION, actualConvertToTokenResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertEquals("Path", actualConvertToTokenResult.getContractTemplate());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String, OffsetDateTime)}
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest", null);
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        assertEquals(TokenTypeEnum.INSTITUTION, actualConvertToTokenResult.getType());
        assertEquals(RelationshipState.ACTIVE, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertEquals("Path", actualConvertToTokenResult.getContractTemplate());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String, OffsetDateTime)}
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest", null);
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        assertEquals(TokenTypeEnum.INSTITUTION, actualConvertToTokenResult.getType());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertEquals("Path", actualConvertToTokenResult.getContractTemplate());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String, OffsetDateTime)}
     */
    @Test
    void testConvertToToken6() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("START - convertToToken for institution having externalId: {} and digest: {}");
        userToOnboard.setProductRole("START - convertToToken for institution having externalId: {} and digest: {}");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - convertToToken for institution having externalId: {} and digest: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(userToOnboardList);
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest", null);
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        List<TokenUser> users = actualConvertToTokenResult.getUsers();
        assertEquals(1, users.size());
        assertEquals(TokenTypeEnum.INSTITUTION, actualConvertToTokenResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertEquals("Path", actualConvertToTokenResult.getContractTemplate());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        TokenUser getResult = users.get(0);
        assertEquals(PartyRole.MANAGER, getResult.getRole());
        assertEquals("42", getResult.getUserId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#convertToToken(OnboardingRequest, Institution, String, OffsetDateTime)}
     */
    @Test
    void testConvertToToken7() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("START - convertToToken for institution having externalId: {} and digest: {}");
        userToOnboard.setProductRole("START - convertToToken for institution having externalId: {} and digest: {}");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - convertToToken for institution having externalId: {} and digest: {}");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(EnvEnum.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName("START - convertToToken for institution having externalId: {} and digest: {}");
        userToOnboard1.setProductRole("START - convertToToken for institution having externalId: {} and digest: {}");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("START - convertToToken for institution having externalId: {} and digest: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(userToOnboardList);
        Token actualConvertToTokenResult = OnboardingInstitutionUtils.convertToToken(onboardingRequest, new Institution(),
                "Digest", null);
        assertEquals("Digest", actualConvertToTokenResult.getChecksum());
        List<TokenUser> users = actualConvertToTokenResult.getUsers();
        assertEquals(2, users.size());
        assertEquals("Path", actualConvertToTokenResult.getContractTemplate());
        assertEquals(TokenTypeEnum.INSTITUTION, actualConvertToTokenResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertNull(actualConvertToTokenResult.getInstitutionId());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        TokenUser getResult = users.get(0);
        assertEquals(PartyRole.MANAGER, getResult.getRole());
        TokenUser getResult1 = users.get(1);
        assertEquals(PartyRole.MANAGER, getResult1.getRole());
        assertEquals("42", getResult1.getUserId());
        assertEquals("42", getResult.getUserId());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#toTokenUser(UserToOnboard)}
     */
    @Test
    void testToTokenUser() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("Product Role");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        TokenUser actualToTokenUserResult = OnboardingInstitutionUtils.toTokenUser(userToOnboard);
        assertEquals(PartyRole.MANAGER, actualToTokenUserResult.getRole());
        assertEquals("42", actualToTokenUserResult.getUserId());
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
        userToOnboard.setProductRole("Product Role");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        OnboardedProduct actualConstructOperatorProductResult = OnboardingInstitutionUtils
                .constructOperatorProduct(userToOnboard, onboardingOperatorsRequest);
        assertEquals(RelationshipState.ACTIVE, actualConstructOperatorProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructOperatorProductResult.getRole());
        assertEquals("Product Role", actualConstructOperatorProductResult.getProductRole());
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
        userToOnboard.setProductRole("Product Role");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        OnboardedProduct actualConstructOperatorProductResult = OnboardingInstitutionUtils
                .constructOperatorProduct(userToOnboard, onboardingOperatorsRequest);
        assertEquals(RelationshipState.ACTIVE, actualConstructOperatorProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructOperatorProductResult.getRole());
        assertEquals("Product Role", actualConstructOperatorProductResult.getProductRole());
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
        userToOnboard.setProductRole("Product Role");
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                onboardingRequest);
        assertEquals("Path", actualConstructProductResult.getContract());
        assertEquals(RelationshipState.PENDING, actualConstructProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructProductResult.getRole());
        assertEquals("Product Role", actualConstructProductResult.getProductRole());
        assertEquals("42", actualConstructProductResult.getProductId());
        assertEquals(EnvEnum.ROOT, actualConstructProductResult.getEnv());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, OnboardingRequest)}
     */
    @Test
    void testConstructProduct2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(null);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("Product Role");
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                onboardingRequest);
        assertEquals("Path", actualConstructProductResult.getContract());
        assertEquals(RelationshipState.PENDING, actualConstructProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructProductResult.getRole());
        assertEquals("Product Role", actualConstructProductResult.getProductRole());
        assertEquals("42", actualConstructProductResult.getProductId());
        assertEquals(EnvEnum.ROOT, actualConstructProductResult.getEnv());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, OnboardingRequest)}
     */
    @Test
    void testConstructProduct5() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("Product Role");
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                onboardingRequest);
        assertEquals("Path", actualConstructProductResult.getContract());
        assertEquals(RelationshipState.ACTIVE, actualConstructProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructProductResult.getRole());
        assertEquals("Product Role", actualConstructProductResult.getProductRole());
        assertEquals("42", actualConstructProductResult.getProductId());
        assertEquals(EnvEnum.ROOT, actualConstructProductResult.getEnv());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructProduct(UserToOnboard, OnboardingRequest)}
     */
    @Test
    void testConstructProduct6() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("Product Role");
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardedProduct actualConstructProductResult = OnboardingInstitutionUtils.constructProduct(userToOnboard,
                onboardingRequest);
        assertEquals("Path", actualConstructProductResult.getContract());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructProductResult.getStatus());
        assertEquals(PartyRole.MANAGER, actualConstructProductResult.getRole());
        assertEquals("Product Role", actualConstructProductResult.getProductRole());
        assertEquals("42", actualConstructProductResult.getProductId());
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
        assertEquals(RelationshipState.PENDING, actualConstructOnboardingResult.getStatus());
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
        assertEquals(RelationshipState.ACTIVE, actualConstructOnboardingResult.getStatus());
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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setImported(true);
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
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenTypeEnum.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        Onboarding actualConstructOnboardingResult = OnboardingInstitutionUtils.constructOnboarding(onboardingRequest);
        assertSame(billing, actualConstructOnboardingResult.getBilling());
        assertEquals("Pricing Plan", actualConstructOnboardingResult.getPricingPlan());
        assertEquals("Path", actualConstructOnboardingResult.getContract());
        assertEquals("42", actualConstructOnboardingResult.getProductId());
        assertEquals(RelationshipState.TOBEVALIDATED, actualConstructOnboardingResult.getStatus());
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List)}
     */
    @Test
    void testGetValidManagerToOnboard() {
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManagerToOnboard(new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List)}
     */
    @Test
    void testGetValidManagerToOnboard2() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setProductRole("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        List<String> actualValidManagerToOnboard = OnboardingInstitutionUtils.getValidManagerToOnboard(userToOnboardList);
        assertEquals(1, actualValidManagerToOnboard.size());
        assertEquals("42", actualValidManagerToOnboard.get(0));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List)}
     */
    @Test
    void testGetValidManagerToOnboard3() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setProductRole("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(EnvEnum.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard1.setProductRole("START - getOnboardingValidManager for users list size: {}");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);
        List<String> actualValidManagerToOnboard = OnboardingInstitutionUtils.getValidManagerToOnboard(userToOnboardList);
        assertEquals(2, actualValidManagerToOnboard.size());
        assertEquals("42", actualValidManagerToOnboard.get(0));
        assertEquals("42", actualValidManagerToOnboard.get(1));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getValidManagerToOnboard(List)}
     */
    @Test
    void testGetValidManagerToOnboard4() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setProductRole("START - getOnboardingValidManager for users list size: {}");
        userToOnboard.setRole(PartyRole.DELEGATE);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("START - getOnboardingValidManager for users list size: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getValidManagerToOnboard(userToOnboardList));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardedValidManager(List, String, String)}
     */
    @Test
    void testGetOnboardedValidManager() {
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getOnboardedValidManager(new ArrayList<>(), "42", "42"));
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardedValidManager(List, String, String)}
     */
    @Test
    void testGetOnboardedValidManager3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getOnboardedValidManager(onboardedUserList, "42", "42"));
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardedValidManager(List, String, String)}
     */
    @Test
    void testGetOnboardedValidManager5() {
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getOnboardedValidManager(onboardedUserList, "42", "42"));
    }

    /**
     * Method under test: {@link OnboardingInstitutionUtils#getOnboardedValidManager(List, String, String)}
     */
    @Test
    void testGetOnboardedValidManager6() {
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> OnboardingInstitutionUtils.getOnboardedValidManager(onboardedUserList, "42", "42"));
    }


    /**
     * Method under test: {@link OnboardingInstitutionUtils#constructOnboardingRequest(Token, Institution)}
     */
    @Test
    void testConstructOnboardingRequest() {
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
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
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
        token.setType(TokenTypeEnum.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        OnboardingRequest actualConstructOnboardingRequestResult = OnboardingInstitutionUtils
                .constructOnboardingRequest(token, new Institution());
        assertTrue(actualConstructOnboardingRequestResult.isSignContract());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductName());
        assertEquals("42", actualConstructOnboardingRequestResult.getProductId());
        assertEquals("Contract Template", actualConstructOnboardingRequestResult.getContract().getPath());
        assertNull(actualConstructOnboardingRequestResult.getInstitutionUpdate().getDescription());
    }


}

