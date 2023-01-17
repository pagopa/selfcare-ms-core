package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.web.model.institution.AttributesResponse;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.DataProtectionOfficerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionBillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.institution.PaymentServiceProviderResponse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class InstitutionMapperTest {
    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        ArrayList<Attributes> attributesList = new ArrayList<>();
        institution.setAttributes(attributesList);
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getOriginId());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Origin", actualToInstitutionResponseResult.getOrigin());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        DataProtectionOfficerResponse dataProtectionOfficer1 = actualToInstitutionResponseResult.getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer1.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer1.getAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProviderResponse();
        assertTrue(paymentServiceProviderResponse.isVatNumberGroup());
        assertEquals("42", paymentServiceProviderResponse.getLegalRegisterNumber());
        assertEquals("Abi Code", paymentServiceProviderResponse.getAbiCode());
        assertEquals("Legal Register Name", paymentServiceProviderResponse.getLegalRegisterName());
        assertEquals("Pec", dataProtectionOfficer1.getPec());
        assertEquals("42", paymentServiceProviderResponse.getBusinessRegisterNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse2() {
        Attributes attributes = new Attributes();
        attributes.setCode("Code");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Origin");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        institution.setAttributes(attributesList);
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getOriginId());
        List<AttributesResponse> attributes1 = actualToInstitutionResponseResult.getAttributes();
        assertEquals(1, attributes1.size());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Origin", actualToInstitutionResponseResult.getOrigin());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        DataProtectionOfficerResponse dataProtectionOfficer1 = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer1.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer1.getAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProviderResponse();
        assertTrue(paymentServiceProviderResponse.isVatNumberGroup());
        assertEquals("42", paymentServiceProviderResponse.getLegalRegisterNumber());
        assertEquals("Legal Register Name", paymentServiceProviderResponse.getLegalRegisterName());
        assertEquals("42", paymentServiceProviderResponse.getBusinessRegisterNumber());
        assertEquals("Pec", dataProtectionOfficer1.getPec());
        assertEquals("Abi Code", paymentServiceProviderResponse.getAbiCode());
        AttributesResponse getResult = attributes1.get(0);
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse3() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setCountry("GB");
        geographicTaxonomies.setCountryAbbreviation("GB");
        geographicTaxonomies.setDesc("The characteristics of someone or something");
        geographicTaxonomies.setEnable(true);
        geographicTaxonomies.setEndDate("2020-03-01");
        geographicTaxonomies.setProvince("Province");
        geographicTaxonomies.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomies.setRegion("us-east-2");
        geographicTaxonomies.setStartDate("2020-03-01");

        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        geographicTaxonomiesList.add(geographicTaxonomies);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        ArrayList<Attributes> attributesList = new ArrayList<>();
        institution.setAttributes(attributesList);
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getOriginId());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Origin", actualToInstitutionResponseResult.getOrigin());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        List<GeoTaxonomies> geographicTaxonomies1 = actualToInstitutionResponseResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies1.size());
        DataProtectionOfficerResponse dataProtectionOfficer1 = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer1.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer1.getAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProviderResponse();
        assertEquals("42", paymentServiceProviderResponse.getBusinessRegisterNumber());
        assertEquals("Legal Register Name", paymentServiceProviderResponse.getLegalRegisterName());
        assertEquals("Abi Code", paymentServiceProviderResponse.getAbiCode());
        assertTrue(paymentServiceProviderResponse.isVatNumberGroup());
        assertEquals("42", paymentServiceProviderResponse.getLegalRegisterNumber());
        assertEquals("Pec", dataProtectionOfficer1.getPec());
        GeoTaxonomies getResult = geographicTaxonomies1.get(0);
        assertTrue(getResult.isEnable());
        assertEquals("The characteristics of someone or something", getResult.getDesc());
        assertEquals("Code", getResult.getCode());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse4() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setCountry("GB");
        geographicTaxonomies.setCountryAbbreviation("GB");
        geographicTaxonomies.setDesc("The characteristics of someone or something");
        geographicTaxonomies.setEnable(false);
        geographicTaxonomies.setEndDate("2020-03-01");
        geographicTaxonomies.setProvince("Province");
        geographicTaxonomies.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomies.setRegion("us-east-2");
        geographicTaxonomies.setStartDate("2020-03-01");

        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        geographicTaxonomiesList.add(geographicTaxonomies);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        ArrayList<Attributes> attributesList = new ArrayList<>();
        institution.setAttributes(attributesList);
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getOriginId());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Origin", actualToInstitutionResponseResult.getOrigin());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        List<GeoTaxonomies> geographicTaxonomies1 = actualToInstitutionResponseResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies1.size());
        DataProtectionOfficerResponse dataProtectionOfficer1 = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer1.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer1.getAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProviderResponse();
        assertEquals("42", paymentServiceProviderResponse.getBusinessRegisterNumber());
        assertEquals("Legal Register Name", paymentServiceProviderResponse.getLegalRegisterName());
        assertEquals("Abi Code", paymentServiceProviderResponse.getAbiCode());
        assertTrue(paymentServiceProviderResponse.isVatNumberGroup());
        assertEquals("42", paymentServiceProviderResponse.getLegalRegisterNumber());
        assertEquals("Pec", dataProtectionOfficer1.getPec());
        GeoTaxonomies getResult = geographicTaxonomies1.get(0);
        assertFalse(getResult.isEnable());
        assertEquals("The characteristics of someone or something", getResult.getDesc());
        assertEquals("Code", getResult.getCode());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("42", actualToBillingResponseResult.getOriginId());
        assertEquals("Origin", actualToBillingResponseResult.getOrigin());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.PA,
                actualToBillingResponseResult.getInstitutionType());
        assertEquals("42", actualToBillingResponseResult.getInstitutionId());
        assertEquals("42", actualToBillingResponseResult.getExternalId());
        assertEquals("42 Main St", actualToBillingResponseResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToBillingResponseResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse2() {
        assertNull(InstitutionMapper.toBillingResponse(null, "foo"));
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse3() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        institution.setAddress("Address");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("Address", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("42", actualToBillingResponseResult.getOriginId());
        assertEquals("Origin", actualToBillingResponseResult.getOrigin());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.PA,
                actualToBillingResponseResult.getInstitutionType());
        assertEquals("42", actualToBillingResponseResult.getInstitutionId());
        assertEquals("42", actualToBillingResponseResult.getExternalId());
        assertEquals("42 Main St", actualToBillingResponseResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToBillingResponseResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse6() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing1);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.PA);
        institution.setOnboarding(onboardingList);
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertEquals("42", actualToBillingResponseResult.getOriginId());
        assertEquals("Origin", actualToBillingResponseResult.getOrigin());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.PA,
                actualToBillingResponseResult.getInstitutionType());
        assertEquals("42", actualToBillingResponseResult.getInstitutionId());
        assertEquals("42", actualToBillingResponseResult.getExternalId());
        assertEquals("42 Main St", actualToBillingResponseResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToBillingResponseResult.getDescription());
        BillingResponse billing2 = actualToBillingResponseResult.getBilling();
        assertEquals("Recipient Code", billing2.getRecipientCode());
        assertTrue(billing2.isPublicServices());
        assertEquals("42", billing2.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse7() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        Billing billing1 = new Billing();
        billing1.setPublicServices(false);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing1);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.PA);
        institution.setOnboarding(onboardingList);
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertEquals("42", actualToBillingResponseResult.getOriginId());
        assertEquals("Origin", actualToBillingResponseResult.getOrigin());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.PA,
                actualToBillingResponseResult.getInstitutionType());
        assertEquals("42", actualToBillingResponseResult.getInstitutionId());
        assertEquals("42", actualToBillingResponseResult.getExternalId());
        assertEquals("42 Main St", actualToBillingResponseResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToBillingResponseResult.getDescription());
        BillingResponse billing2 = actualToBillingResponseResult.getBilling();
        assertEquals("Recipient Code", billing2.getRecipientCode());
        assertFalse(billing2.isPublicServices());
        assertEquals("42", billing2.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse8() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing1);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("Product Id");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.PA);
        institution.setOnboarding(onboardingList);
        institution.setOrigin("Origin");
        institution.setOriginId("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("42", actualToBillingResponseResult.getOriginId());
        assertEquals("Origin", actualToBillingResponseResult.getOrigin());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.PA,
                actualToBillingResponseResult.getInstitutionType());
        assertEquals("42", actualToBillingResponseResult.getInstitutionId());
        assertEquals("42", actualToBillingResponseResult.getExternalId());
        assertEquals("42 Main St", actualToBillingResponseResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToBillingResponseResult.getDescription());
    }
}

