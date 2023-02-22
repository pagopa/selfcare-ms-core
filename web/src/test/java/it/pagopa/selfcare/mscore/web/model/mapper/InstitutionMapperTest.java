package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionMapperTest {

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse() {
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(new Institution());
        assertNull(actualToInstitutionResponseResult.getAddress());
        assertFalse(actualToInstitutionResponseResult.isImported());
        assertNull(actualToInstitutionResponseResult.getZipCode());
        assertNull(actualToInstitutionResponseResult.getTaxCode());
        assertNull(actualToInstitutionResponseResult.getSupportPhone());
        assertNull(actualToInstitutionResponseResult.getSupportEmail());
        assertNull(actualToInstitutionResponseResult.getShareCapital());
        assertNull(actualToInstitutionResponseResult.getRea());
        assertNull(actualToInstitutionResponseResult.getOriginId());
        assertNull(actualToInstitutionResponseResult.getInstitutionType());
        assertNull(actualToInstitutionResponseResult.getId());
        assertNull(actualToInstitutionResponseResult.getExternalId());
        assertNull(actualToInstitutionResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionResponseResult.getDescription());
        assertNull(actualToInstitutionResponseResult.getBusinessRegisterPlace());
    }


    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse3() {
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper
                .toInstitutionResponse(InstitutionMapper.toInstitution(new InstitutionRequest(), "42"));
        assertNull(actualToInstitutionResponseResult.getAddress());
        assertFalse(actualToInstitutionResponseResult.isImported());
        assertNull(actualToInstitutionResponseResult.getZipCode());
        assertNull(actualToInstitutionResponseResult.getTaxCode());
        assertNull(actualToInstitutionResponseResult.getSupportPhone());
        assertNull(actualToInstitutionResponseResult.getSupportEmail());
        assertNull(actualToInstitutionResponseResult.getShareCapital());
        assertNull(actualToInstitutionResponseResult.getRea());
        assertNull(actualToInstitutionResponseResult.getOriginId());
        assertNull(actualToInstitutionResponseResult.getInstitutionType());
        assertNull(actualToInstitutionResponseResult.getId());
        List<GeoTaxonomies> geographicTaxonomies = actualToInstitutionResponseResult.getGeographicTaxonomies();
        assertTrue(geographicTaxonomies.isEmpty());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        assertNull(actualToInstitutionResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionResponseResult.getDescription());
        assertNull(actualToInstitutionResponseResult.getBusinessRegisterPlace());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse4() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper
                .toInstitutionResponse(new Institution("42", "42", "Ipa Code", "The characteristics of someone or something",
                        InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboardingList,
                        geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "Rea",
                        "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true));
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertTrue(actualToInstitutionResponseResult.isImported());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResponseResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResponseResult.getRea());
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getOriginId());
        assertEquals("Business Register Place", actualToInstitutionResponseResult.getBusinessRegisterPlace());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProvider();
        assertNull(paymentServiceProviderResponse.getLegalRegisterNumber());
        assertFalse(paymentServiceProviderResponse.isVatNumberGroup());
        DataProtectionOfficerResponse dataProtectionOfficer = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertNull(dataProtectionOfficer.getAddress());
        assertNull(dataProtectionOfficer.getEmail());
        assertNull(paymentServiceProviderResponse.getAbiCode());
        assertNull(paymentServiceProviderResponse.getLegalRegisterName());
        assertNull(paymentServiceProviderResponse.getBusinessRegisterNumber());
        assertNull(dataProtectionOfficer.getPec());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse5() {
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
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper
                .toInstitutionResponse(new Institution("42", "42", "Ipa Code", "The characteristics of someone or something",
                        InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboardingList,
                        geographicTaxonomiesList, attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null,
                        "Rea", "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true));
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertTrue(actualToInstitutionResponseResult.isImported());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResponseResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResponseResult.getRea());
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getOriginId());
        assertEquals("Business Register Place", actualToInstitutionResponseResult.getBusinessRegisterPlace());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        List<GeoTaxonomies> geographicTaxonomies1 = actualToInstitutionResponseResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies1.size());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProvider();
        assertNull(paymentServiceProviderResponse.getBusinessRegisterNumber());
        assertNull(paymentServiceProviderResponse.getLegalRegisterNumber());
        assertFalse(paymentServiceProviderResponse.isVatNumberGroup());
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
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper
                .toInstitutionResponse(new Institution("42", "42", "Ipa Code", "The characteristics of someone or something",
                        InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboardingList,
                        geographicTaxonomies, attributesList, paymentServiceProvider, new DataProtectionOfficer(), null, null,
                        "Rea", "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true));
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertTrue(actualToInstitutionResponseResult.isImported());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResponseResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResponseResult.getRea());
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getOriginId());
        List<AttributesResponse> attributes1 = actualToInstitutionResponseResult.getAttributes();
        assertEquals(1, attributes1.size());
        assertEquals("Business Register Place", actualToInstitutionResponseResult.getBusinessRegisterPlace());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProvider();
        assertNull(paymentServiceProviderResponse.getLegalRegisterName());
        assertNull(paymentServiceProviderResponse.getLegalRegisterNumber());
        assertFalse(paymentServiceProviderResponse.isVatNumberGroup());
        DataProtectionOfficerResponse dataProtectionOfficer = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertNull(dataProtectionOfficer.getAddress());


    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(ProductManagerInfo, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse9() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());

        ProductManagerInfo productManagerInfo = new ProductManagerInfo("id", institution, new ArrayList<>());
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(productManagerInfo, "42", "42");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getZipCode());
    }


    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse2() {
        assertNull(InstitutionMapper.toInstitutionBillingResponse(null, "foo"));
    }


    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse6() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toInstitutionBillingResponse(institution, "42");
        assertNull(actualToBillingResponseResult.getAddress());
        assertNull(actualToBillingResponseResult.getZipCode());
        assertNull(actualToBillingResponseResult.getTaxCode());
        assertNull(actualToBillingResponseResult.getOriginId());
        assertNull(actualToBillingResponseResult.getInstitutionId());
        assertNull(actualToBillingResponseResult.getExternalId());
        assertNull(actualToBillingResponseResult.getDigitalAddress());
        assertNull(actualToBillingResponseResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse7() {
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

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toInstitutionBillingResponse(institution, "42");
        assertNull(actualToBillingResponseResult.getAddress());
        assertNull(actualToBillingResponseResult.getZipCode());
        assertNull(actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertNull(actualToBillingResponseResult.getOriginId());
        assertNull(actualToBillingResponseResult.getInstitutionId());
        assertNull(actualToBillingResponseResult.getExternalId());
        assertNull(actualToBillingResponseResult.getDigitalAddress());
        assertNull(actualToBillingResponseResult.getDescription());
        BillingResponse billing1 = actualToBillingResponseResult.getBilling();
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertTrue(billing1.isPublicServices());
        assertEquals("42", billing1.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse8() {
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

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toInstitutionBillingResponse(institution,
                "Product Id");
        assertNull(actualToBillingResponseResult.getAddress());
        assertNull(actualToBillingResponseResult.getZipCode());
        assertNull(actualToBillingResponseResult.getTaxCode());
        assertNull(actualToBillingResponseResult.getOriginId());
        assertNull(actualToBillingResponseResult.getInstitutionId());
        assertNull(actualToBillingResponseResult.getExternalId());
        assertNull(actualToBillingResponseResult.getDigitalAddress());
        assertNull(actualToBillingResponseResult.getDescription());
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
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertTrue(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertTrue(paymentServiceProvider.isVatNumberGroup());
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
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertTrue(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        List<Attributes> attributes = actualToInstitutionResult.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertTrue(paymentServiceProvider.isVatNumberGroup());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
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
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertTrue(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals(1, actualToInstitutionResult.getGeographicTaxonomies().size());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertTrue(paymentServiceProvider.isVatNumberGroup());
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
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertFalse(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        DataProtectionOfficer dataProtectionOfficer = actualToInstitutionResult.getDataProtectionOfficer();
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        PaymentServiceProvider paymentServiceProvider = actualToInstitutionResult.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertTrue(paymentServiceProvider.isVatNumberGroup());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
    }
}

