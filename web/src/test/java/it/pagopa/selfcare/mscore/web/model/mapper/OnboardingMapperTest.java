package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.*;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OnboardingMapperTest {

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingRequest(OnboardingInstitutionRequest)}
     */
    @Test
    void testToOnboardingRequest() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
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
        person.setProductRole("");
        person.setRole(PartyRole.MANAGER);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setUsers(personList);
        onboardingInstitutionRequest.setBilling(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdate);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setUsers(new ArrayList<>());
        OnboardingRequest actualToOnboardingRequestResult = OnboardingMapper
                .toOnboardingRequest(onboardingInstitutionRequest);
        assertSame(institutionUpdate, actualToOnboardingRequestResult.getInstitutionUpdate());
        assertEquals("Pricing Plan", actualToOnboardingRequestResult.getPricingPlan());
        assertEquals("42", actualToOnboardingRequestResult.getInstitutionExternalId());
        assertEquals("42", actualToOnboardingRequestResult.getProductId());
        assertEquals("Product Name", actualToOnboardingRequestResult.getProductName());
        Contract contract = actualToOnboardingRequestResult.getContract();
        assertEquals("1.0.2", contract.getVersion());
        Billing billingRequest1 = actualToOnboardingRequestResult.getBillingRequest();
        assertFalse(billingRequest1.isPublicServices());
        assertEquals("Recipient Code", billingRequest1.getRecipientCode());
        assertEquals("Path", contract.getPath());
        assertEquals("42", billingRequest1.getVatNumber());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingRequest(OnboardingInstitutionRequest)}
     */
    @Test
    void testToOnboardingRequest2() {

        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
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

        Person person = new Person();
        person.setId("42");
        person.setName("Name");
        person.setProductRole("");
        person.setRole(PartyRole.MANAGER);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBilling(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdate);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setUsers(personList);
        OnboardingRequest actualToOnboardingRequestResult = OnboardingMapper.toOnboardingRequest(onboardingInstitutionRequest);

        assertSame(institutionUpdate, actualToOnboardingRequestResult.getInstitutionUpdate());
        assertEquals("Pricing Plan", actualToOnboardingRequestResult.getPricingPlan());
        assertEquals("42", actualToOnboardingRequestResult.getInstitutionExternalId());
        assertEquals("42", actualToOnboardingRequestResult.getProductId());
        assertEquals("Product Name", actualToOnboardingRequestResult.getProductName());

    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingRequest(OnboardingInstitutionRequest)}
     */
    @Test
    void testToOnboardingRequest3() {

        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
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
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Person person = new Person();
        person.setId("42");
        person.setName("Name");
        person.setProductRole("");
        person.setRole(PartyRole.DELEGATE);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        Person person1 = new Person();
        person1.setId("42");
        person1.setName("Name");
        person1.setProductRole("");
        person1.setRole(PartyRole.DELEGATE);
        person1.setSurname("Doe");
        person1.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBilling(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdate);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setUsers(personList);
        OnboardingRequest actualToOnboardingRequestResult = OnboardingMapper.toOnboardingRequest(onboardingInstitutionRequest);

        assertSame(institutionUpdate, actualToOnboardingRequestResult.getInstitutionUpdate());
        assertEquals("Pricing Plan", actualToOnboardingRequestResult.getPricingPlan());
        assertEquals("42", actualToOnboardingRequestResult.getInstitutionExternalId());
        assertEquals("42", actualToOnboardingRequestResult.getProductId());
        assertEquals("Product Name", actualToOnboardingRequestResult.getProductName());

    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingRequest(OnboardingInstitutionRequest)}
     */
    @Test
    void testToOnboardingRequest4() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPath("Path");
        contractRequest.setVersion("1.0.2");

        ContractImportedRequest contractImportedRequest = new ContractImportedRequest();
        contractImportedRequest.setContractType("Contract Type");
        contractImportedRequest.setFileName("foo.txt");
        contractImportedRequest.setFilePath("/directory/foo.txt");

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

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBilling(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdate);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setSignContract(true);
        onboardingInstitutionRequest.setUsers(new ArrayList<>());
        OnboardingRequest actualToOnboardingRequestResult = OnboardingMapper
                .toOnboardingRequest(onboardingInstitutionRequest);
        assertTrue(actualToOnboardingRequestResult.isSignContract());
        assertEquals("42", actualToOnboardingRequestResult.getInstitutionExternalId());
        assertEquals("Product Name", actualToOnboardingRequestResult.getProductName());
        assertSame(institutionUpdate, actualToOnboardingRequestResult.getInstitutionUpdate());
        assertEquals("Pricing Plan", actualToOnboardingRequestResult.getPricingPlan());
        assertEquals("42", actualToOnboardingRequestResult.getProductId());
        Billing billingRequest1 = actualToOnboardingRequestResult.getBillingRequest();
        assertEquals("Recipient Code", billingRequest1.getRecipientCode());
        Contract contract = actualToOnboardingRequestResult.getContract();
        assertEquals("1.0.2", contract.getVersion());
        assertFalse(billingRequest1.isPublicServices());
        assertEquals("Path", contract.getPath());
        assertEquals("42", billingRequest1.getVatNumber());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingRequest(OnboardingInstitutionRequest)}
     */
    @Test
    void testToOnboardingRequest5() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPath("Path");
        contractRequest.setVersion("1.0.2");

        ContractImportedRequest contractImportedRequest = new ContractImportedRequest();
        contractImportedRequest.setContractType("Contract Type");
        contractImportedRequest.setFileName("foo.txt");
        contractImportedRequest.setFilePath("/directory/foo.txt");

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

        Person person = new Person();
        person.setEmail("jane.doe@example.org");
        person.setEnv(EnvEnum.ROOT);
        person.setId("42");
        person.setName("Name");
        person.setProductRole("");
        person.setRole(PartyRole.MANAGER);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBilling(billingRequest);
        onboardingInstitutionRequest.setContract(contractRequest);
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setInstitutionUpdate(institutionUpdate);
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setSignContract(true);
        onboardingInstitutionRequest.setUsers(personList);
        OnboardingRequest actualToOnboardingRequestResult = OnboardingMapper
                .toOnboardingRequest(onboardingInstitutionRequest);
        assertTrue(actualToOnboardingRequestResult.isSignContract());
        assertEquals("42", actualToOnboardingRequestResult.getInstitutionExternalId());
        assertEquals("Product Name", actualToOnboardingRequestResult.getProductName());
        assertEquals(1, actualToOnboardingRequestResult.getUsers().size());
        assertSame(institutionUpdate, actualToOnboardingRequestResult.getInstitutionUpdate());
        assertEquals("Pricing Plan", actualToOnboardingRequestResult.getPricingPlan());
        assertEquals("42", actualToOnboardingRequestResult.getProductId());
        Billing billingRequest1 = actualToOnboardingRequestResult.getBillingRequest();
        assertEquals("Recipient Code", billingRequest1.getRecipientCode());
        Contract contract = actualToOnboardingRequestResult.getContract();
        assertEquals("1.0.2", contract.getVersion());
        assertFalse(billingRequest1.isPublicServices());
        assertEquals("Path", contract.getPath());
        assertEquals("42", billingRequest1.getVatNumber());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse() {
        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("42",
                onboardingInfoList);
        assertEquals("42", actualToOnboardingInfoResponseResult.getUserId());
    }


    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse3() {
        Institution institution = new Institution();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        institution.setOnboarding(onboardingList);
        List<Attributes> attributes = new ArrayList<>();
        attributes.add(new Attributes());
        institution.setAttributes(attributes);
        OnboardingInfo e = new OnboardingInfo(institution, new HashMap<>());

        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        onboardingInfoList.add(e);
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("foo",
                onboardingInfoList);
        assertEquals("foo", actualToOnboardingInfoResponseResult.getUserId());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse6() {
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
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        OnboardingInfo e = new OnboardingInfo(institution, new HashMap<>());

        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        onboardingInfoList.add(e);
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("foo",
                onboardingInfoList);
        assertTrue(actualToOnboardingInfoResponseResult.getInstitutions().isEmpty());
        assertEquals("foo", actualToOnboardingInfoResponseResult.getUserId());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse7() {
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
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        List<Attributes> attributes = new ArrayList<>();
        attributes.add(new Attributes());
        institution.setAttributes(attributes);
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        geographicTaxonomies.add(new GeographicTaxonomies());
        institution.setGeographicTaxonomies(geographicTaxonomies);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        HashMap<String, OnboardedProduct> stringOnboardedProductMap = new HashMap<>();
        stringOnboardedProductMap.put("42", onboardedProduct);
        OnboardingInfo e = new OnboardingInfo(institution, stringOnboardedProductMap);

        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        onboardingInfoList.add(e);
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("foo",
                onboardingInfoList);
        assertEquals(1, actualToOnboardingInfoResponseResult.getInstitutions().size());
        assertEquals("foo", actualToOnboardingInfoResponseResult.getUserId());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse8() {
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
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution toInstitutionResult = InstitutionMapper.toInstitution(new InstitutionRequest(), "42");
        toInstitutionResult.setOnboarding(onboardingList);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        HashMap<String, OnboardedProduct> stringOnboardedProductMap = new HashMap<>();
        stringOnboardedProductMap.put("42", onboardedProduct);
        OnboardingInfo e = new OnboardingInfo(toInstitutionResult, stringOnboardedProductMap);

        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        onboardingInfoList.add(e);
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("foo",
                onboardingInfoList);
        assertEquals(1, actualToOnboardingInfoResponseResult.getInstitutions().size());
        assertEquals("foo", actualToOnboardingInfoResponseResult.getUserId());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingOperatorRequest(OnboardingInstitutionOperatorsRequest)}
     */
    @Test
    void testToOnboardingOperatorRequest() {
        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        ArrayList<Person> personList = new ArrayList<>();
        onboardingInstitutionOperatorsRequest.setUsers(personList);
        OnboardingOperatorsRequest actualToOnboardingOperatorRequestResult = OnboardingMapper
                .toOnboardingOperatorRequest(onboardingInstitutionOperatorsRequest);
        assertEquals("42", actualToOnboardingOperatorRequestResult.getInstitutionId());
        assertEquals("42", actualToOnboardingOperatorRequestResult.getProductId());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingOperatorRequest(OnboardingInstitutionOperatorsRequest)}
     */
    @Test
    void testToOnboardingOperatorRequest2() {
        Person person = new Person();
        person.setEmail("jane.doe@example.org");
        person.setEnv(EnvEnum.ROOT);
        person.setId("42");
        person.setName("Name");
        person.setProductRole("");
        person.setRole(PartyRole.MANAGER);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person);

        OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest = new OnboardingInstitutionOperatorsRequest();
        onboardingInstitutionOperatorsRequest.setInstitutionId("42");
        onboardingInstitutionOperatorsRequest.setProductId("42");
        onboardingInstitutionOperatorsRequest.setUsers(personList);
        OnboardingOperatorsRequest actualToOnboardingOperatorRequestResult = OnboardingMapper
                .toOnboardingOperatorRequest(onboardingInstitutionOperatorsRequest);
        assertEquals("42", actualToOnboardingOperatorRequestResult.getInstitutionId());
        assertEquals(1, actualToOnboardingOperatorRequestResult.getUsers().size());
        assertEquals("42", actualToOnboardingOperatorRequestResult.getProductId());
    }

}

