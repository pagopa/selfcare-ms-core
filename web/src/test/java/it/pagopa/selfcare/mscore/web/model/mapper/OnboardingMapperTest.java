package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.ContractRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.user.Person;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

class OnboardingMapperTest {

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingRequest(OnboardingInstitutionRequest)}
     */
    @Test
    void testToOnboardingRequest() {
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
        person.setProductRole(List.of("Product Role"));
        person.setRole(PartyRole.MANAGER);
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

        Person person = new Person();
        person.setId("42");
        person.setName("Name");
        person.setProductRole(List.of("Product Role"));
        person.setRole(PartyRole.MANAGER);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBillingRequest(billingRequest);
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
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Person person = new Person();
        person.setId("42");
        person.setName("Name");
        person.setProductRole(List.of("Product Role"));
        person.setRole(PartyRole.DELEGATE);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");

        Person person1 = new Person();
        person1.setId("42");
        person1.setName("Name");
        person1.setProductRole(List.of("Product Role"));
        person1.setRole(PartyRole.MANAGER);
        person1.setSurname("Doe");
        person1.setTaxCode("Tax Code");

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person);

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBillingRequest(billingRequest);
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

        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setBillingRequest(billingRequest);
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
        assertSame(institutionUpdate, actualToOnboardingRequestResult.getInstitutionUpdate());
        assertEquals("Pricing Plan", actualToOnboardingRequestResult.getPricingPlan());
        assertEquals("42", actualToOnboardingRequestResult.getInstitutionExternalId());
        assertEquals("42", actualToOnboardingRequestResult.getProductId());
        assertEquals("Product Name", actualToOnboardingRequestResult.getProductName());
        Billing billingRequest1 = actualToOnboardingRequestResult.getBillingRequest();
        assertEquals("42", billingRequest1.getVatNumber());
        assertFalse(billingRequest1.isPublicServices());
        assertEquals("Recipient Code", billingRequest1.getRecipientCode());
        Contract contract = actualToOnboardingRequestResult.getContract();
        assertEquals("1.0.2", contract.getVersion());
        assertEquals("Path", contract.getPath());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingRequest(OnboardingInstitutionRequest)}
     */
    @Test
    void testToOnboardingRequest5() {
        OnboardingInstitutionRequest onboardingInstitutionRequest = new OnboardingInstitutionRequest();
        onboardingInstitutionRequest.setInstitutionExternalId("42");
        onboardingInstitutionRequest.setPricingPlan("Pricing Plan");
        onboardingInstitutionRequest.setProductId("42");
        onboardingInstitutionRequest.setProductName("Product Name");
        onboardingInstitutionRequest.setSignContract(true);
        onboardingInstitutionRequest.setContract(null);
        onboardingInstitutionRequest.setInstitutionUpdate(null);
        onboardingInstitutionRequest.setBillingRequest(null);
        onboardingInstitutionRequest.setUsers(null);
        OnboardingRequest actualToOnboardingRequestResult = OnboardingMapper
                .toOnboardingRequest(onboardingInstitutionRequest);
        assertTrue(actualToOnboardingRequestResult.isSignContract());
        assertEquals("Product Name", actualToOnboardingRequestResult.getProductName());
        assertEquals("42", actualToOnboardingRequestResult.getProductId());
        assertEquals("Pricing Plan", actualToOnboardingRequestResult.getPricingPlan());
        assertEquals("42", actualToOnboardingRequestResult.getInstitutionExternalId());
    }


    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse() {
        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();

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

        ArrayList<Onboarding> institutionOnboardingList = new ArrayList<>();
        institutionOnboardingList.add(onboarding);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        Attributes attribute = new Attributes();
        attribute.setCode("code");
        attribute.setOrigin("origin");
        attribute.setDescription("description");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attribute);

        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setCode("code");
        geographicTaxonomies.setCountry("country");
        geographicTaxonomies.setDesc("desc");
        geographicTaxonomies.setEnable(true);
        List<GeographicTaxonomies> geographixTaxonomiesList = new ArrayList<>();
        geographixTaxonomiesList.add(geographicTaxonomies);


        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(attributesList);
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(geographixTaxonomiesList);
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        institution.setOnboarding(institutionOnboardingList);


        ArrayList<Onboarding> filteredOnboardingList = new ArrayList<>();
        Onboarding onboarding2 = new Onboarding();
        onboarding2.setBilling(billing);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPremium(premium);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setUpdatedAt(null);
        filteredOnboardingList.add(onboarding);
        institution.setOnboarding(filteredOnboardingList);

        Map<String, OnboardedProduct> filteredProductsMap = new HashMap<>();
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setContract("contract");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setCreatedAt(OffsetDateTime.now());
        filteredProductsMap.put("42", onboardedProduct);

        onboardingInfoList.add(new OnboardingInfo(institution, filteredProductsMap));
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("42",
                onboardingInfoList);
        assertEquals("42", actualToOnboardingInfoResponseResult.getUserId());
        assertEquals("Pricing Plan", actualToOnboardingInfoResponseResult.getInstitutions().get(0).getPricingPlan());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse2() {
        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("42",
                onboardingInfoList);
        assertEquals("42", actualToOnboardingInfoResponseResult.getUserId());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingInfoResponse(String, List)}
     */
    @Test
    void testToOnboardingInfoResponse5() {
        ArrayList<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution onboardedInstitution = new Institution("42", "42", "Ipa Code",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "Tax Code", billing, onboardingList, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null, "Rea", "Share Capital", "Business Register Place",
                "jane.doe@example.org", "4105551212", true);

        onboardingInfoList.add(new OnboardingInfo(onboardedInstitution, new HashMap<>()));
        OnboardingInfoResponse actualToOnboardingInfoResponseResult = OnboardingMapper.toOnboardingInfoResponse("foo",
                onboardingInfoList);
        assertEquals("foo", actualToOnboardingInfoResponseResult.getUserId());
    }
}

