package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
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
import it.pagopa.selfcare.mscore.web.model.institution.AttributesRequest;
import it.pagopa.selfcare.mscore.web.model.institution.AttributesResponse;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.DataProtectionOfficerRequest;
import it.pagopa.selfcare.mscore.web.model.institution.DataProtectionOfficerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionBillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionManagerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResource;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.web.model.institution.PaymentServiceProviderRequest;
import it.pagopa.selfcare.mscore.web.model.institution.PaymentServiceProviderResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getIpaCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getIpaCode());
        List<AttributesResponse> attributes1 = actualToInstitutionResponseResult.getAttributes();
        assertEquals(1, attributes1.size());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getIpaCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("42", actualToInstitutionResponseResult.getIpaCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
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
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse5() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getIpaCode());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        DataProtectionOfficerResponse dataProtectionOfficer1 = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
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
    void testToInstitutionResponse6() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        List<AttributesResponse> attributes1 = actualToInstitutionResponseResult.getAttributes();
        assertEquals(1, attributes1.size());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getIpaCode());
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
    void testToInstitutionResponse7() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getIpaCode());
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
    void testToInstitutionResponse8() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getIpaCode());
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
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "42", "42");
        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertEquals("User", actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals(1, product.getRole().size());
        assertEquals("42", product.getId());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse2() {
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
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "42", "42");
        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertEquals("User", actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        List<String> geographicTaxonomyCodes = institutionUpdate.getGeographicTaxonomyCodes();
        assertEquals(1, geographicTaxonomyCodes.size());
        assertEquals("Code", geographicTaxonomyCodes.get(0));
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals(1, product.getRole().size());
        assertEquals("42", product.getId());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse3() {
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

        GeographicTaxonomies geographicTaxonomies1 = new GeographicTaxonomies();
        geographicTaxonomies1.setCode("Code");
        geographicTaxonomies1.setCountry("GB");
        geographicTaxonomies1.setCountryAbbreviation("GB");
        geographicTaxonomies1.setDesc("The characteristics of someone or something");
        geographicTaxonomies1.setEnable(true);
        geographicTaxonomies1.setEndDate("2020-03-01");
        geographicTaxonomies1.setProvince("Province");
        geographicTaxonomies1.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomies1.setRegion("us-east-2");
        geographicTaxonomies1.setStartDate("2020-03-01");

        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        geographicTaxonomiesList.add(geographicTaxonomies1);
        geographicTaxonomiesList.add(geographicTaxonomies);

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
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "42", "42");
        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertEquals("User", actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        List<String> geographicTaxonomyCodes = institutionUpdate.getGeographicTaxonomyCodes();
        assertEquals(2, geographicTaxonomyCodes.size());
        assertEquals("Code", geographicTaxonomyCodes.get(0));
        assertEquals("Code", geographicTaxonomyCodes.get(1));
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals(1, product.getRole().size());
        assertEquals("42", product.getId());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse4() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(onboardingList);
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "42", "42");
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertEquals("User", actualToInstitutionManagerResponseResult.getFrom());
        assertEquals("Pricing Plan", actualToInstitutionManagerResponseResult.getPricingPlan());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals("42", product.getId());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        BillingResponse billingResponse = actualToInstitutionManagerResponseResult.getBillingResponse();
        assertEquals("42", billingResponse.getVatNumber());
        assertEquals("Recipient Code", billingResponse.getRecipientCode());
        assertEquals(1, product.getRole().size());
        assertTrue(billingResponse.isPublicServices());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse5() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(onboardingList);
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "42", "42");
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertEquals("User", actualToInstitutionManagerResponseResult.getFrom());
        assertEquals("Pricing Plan", actualToInstitutionManagerResponseResult.getPricingPlan());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals("42", product.getId());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        BillingResponse billingResponse = actualToInstitutionManagerResponseResult.getBillingResponse();
        assertEquals("42", billingResponse.getVatNumber());
        assertEquals("Recipient Code", billingResponse.getRecipientCode());
        assertEquals(1, product.getRole().size());
        assertFalse(billingResponse.isPublicServices());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse6() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(onboardingList);
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "42", "42");
        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertEquals("User", actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals(1, product.getRole().size());
        assertEquals("42", product.getId());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse7() {
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
        onboarding.setStatus(RelationshipState.ACTIVE);
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(onboardingList);
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "42", "42");
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertEquals("User", actualToInstitutionManagerResponseResult.getFrom());
        assertEquals("Pricing Plan", actualToInstitutionManagerResponseResult.getPricingPlan());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals("42", product.getId());
        assertNull(product.getCreatedAt());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        BillingResponse billingResponse = actualToInstitutionManagerResponseResult.getBillingResponse();
        assertEquals("42", billingResponse.getVatNumber());
        assertEquals("Recipient Code", billingResponse.getRecipientCode());
        assertEquals(1, product.getRole().size());
        assertTrue(billingResponse.isPublicServices());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("42", actualToBillingResponseResult.getIpaCode());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("Address", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("42", actualToBillingResponseResult.getIpaCode());
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
    void testToBillingResponse4() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Ipa Code", actualToBillingResponseResult.getIpaCode());
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
    void testToBillingResponse5() {
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
        Institution toInstitutionResult = InstitutionMapper.toInstitution(new InstitutionRequest(), "42");
        toInstitutionResult.setAddress("42 Main St");
        toInstitutionResult.setAttributes(new ArrayList<>());
        toInstitutionResult.setBilling(billing);
        toInstitutionResult.setDataProtectionOfficer(dataProtectionOfficer);
        toInstitutionResult.setDescription("The characteristics of someone or something");
        toInstitutionResult.setDigitalAddress("42 Main St");
        toInstitutionResult.setExternalId("42");
        toInstitutionResult.setGeographicTaxonomies(new ArrayList<>());
        toInstitutionResult.setId("42");
        toInstitutionResult.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.PA);
        toInstitutionResult.setIpaCode("Ipa Code");
        toInstitutionResult.setOnboarding(new ArrayList<>());
        toInstitutionResult.setPaymentServiceProvider(paymentServiceProvider);
        toInstitutionResult.setTaxCode("Tax Code");
        toInstitutionResult.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper
                .toBillingResponse(toInstitutionResult, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Ipa Code", actualToBillingResponseResult.getIpaCode());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertEquals("42", actualToBillingResponseResult.getIpaCode());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertEquals("42", actualToBillingResponseResult.getIpaCode());
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
        institution.setIpaCode("42");
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("42", actualToBillingResponseResult.getIpaCode());
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
    void testToBillingResponse11() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(onboardingList);
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertEquals("Ipa Code", actualToBillingResponseResult.getIpaCode());
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
    void testToBillingResponse12() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(onboardingList);
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertEquals("Ipa Code", actualToBillingResponseResult.getIpaCode());
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
    void testToBillingResponse13() {
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
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(onboardingList);
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertEquals("42 Main St", actualToBillingResponseResult.getAddress());
        assertEquals("21654", actualToBillingResponseResult.getZipCode());
        assertEquals("Tax Code", actualToBillingResponseResult.getTaxCode());
        assertEquals("Ipa Code", actualToBillingResponseResult.getIpaCode());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.PA,
                actualToBillingResponseResult.getInstitutionType());
        assertEquals("42", actualToBillingResponseResult.getInstitutionId());
        assertEquals("42", actualToBillingResponseResult.getExternalId());
        assertEquals("42 Main St", actualToBillingResponseResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToBillingResponseResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toResource(Institution)}
     */
    @Test
    void testToResource() {
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
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        InstitutionResource actualToResourceResult = InstitutionMapper.toResource(institution);
        assertEquals("42", actualToResourceResult.getExternalId());
        assertEquals("42", actualToResourceResult.getId());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution() {
        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertTrue(paymentServiceProvider.getVatNumberGroup());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution2() {
        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("Code");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("Origin");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        List<Attributes> attributes = actualToInstitutionResult.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("Pec", dataProtectionOfficer.getPec());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertTrue(paymentServiceProvider.getVatNumberGroup());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        Attributes getResult = attributes.get(0);
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution3() {
        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("Code");
        geoTaxonomies.setDesc("The characteristics of someone or something");
        geoTaxonomies.setEnable(true);

        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        geoTaxonomiesList.add(geoTaxonomies);

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals(1, actualToInstitutionResult.getGeographicTaxonomies().size());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertTrue(paymentServiceProvider.getVatNumberGroup());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution4() {
        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(false);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertFalse(paymentServiceProvider.getVatNumberGroup());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
    }
}

