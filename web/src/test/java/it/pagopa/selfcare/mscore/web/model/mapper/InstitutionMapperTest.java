package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.core.util.COWArrayList;
import com.fasterxml.jackson.databind.MappingIterator;
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
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

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
        assertNull(actualToInstitutionResponseResult.getIpaCode());
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
    void testToInstitutionResponse2() {
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

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        institution.setAttributes(null);
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertNull(actualToInstitutionResponseResult.getAddress());
        assertFalse(actualToInstitutionResponseResult.isImported());
        assertNull(actualToInstitutionResponseResult.getZipCode());
        assertNull(actualToInstitutionResponseResult.getTaxCode());
        assertNull(actualToInstitutionResponseResult.getSupportPhone());
        assertNull(actualToInstitutionResponseResult.getSupportEmail());
        assertNull(actualToInstitutionResponseResult.getShareCapital());
        assertNull(actualToInstitutionResponseResult.getRea());
        assertNull(actualToInstitutionResponseResult.getIpaCode());
        assertNull(actualToInstitutionResponseResult.getInstitutionType());
        assertNull(actualToInstitutionResponseResult.getId());
        List<GeoTaxonomies> geographicTaxonomies1 = actualToInstitutionResponseResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies1.size());
        assertNull(actualToInstitutionResponseResult.getExternalId());
        assertNull(actualToInstitutionResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionResponseResult.getDescription());
        assertNull(actualToInstitutionResponseResult.getBusinessRegisterPlace());
        GeoTaxonomies getResult = geographicTaxonomies1.get(0);
        assertEquals("Code", getResult.getCode());
        assertTrue(getResult.isEnable());
        assertEquals("The characteristics of someone or something", getResult.getDesc());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse3() {
        Attributes attributes = new Attributes();
        attributes.setCode("Code");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Origin");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        institution.setAttributes(attributesList);
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(institution);
        assertNull(actualToInstitutionResponseResult.getAddress());
        assertFalse(actualToInstitutionResponseResult.isImported());
        assertNull(actualToInstitutionResponseResult.getZipCode());
        assertNull(actualToInstitutionResponseResult.getTaxCode());
        assertNull(actualToInstitutionResponseResult.getSupportPhone());
        assertNull(actualToInstitutionResponseResult.getSupportEmail());
        assertNull(actualToInstitutionResponseResult.getShareCapital());
        assertNull(actualToInstitutionResponseResult.getRea());
        assertNull(actualToInstitutionResponseResult.getIpaCode());
        assertNull(actualToInstitutionResponseResult.getInstitutionType());
        assertNull(actualToInstitutionResponseResult.getId());
        assertNull(actualToInstitutionResponseResult.getExternalId());
        assertNull(actualToInstitutionResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionResponseResult.getDescription());
        assertNull(actualToInstitutionResponseResult.getBusinessRegisterPlace());
        List<AttributesResponse> attributes1 = actualToInstitutionResponseResult.getAttributes();
        assertEquals(1, attributes1.size());
        AttributesResponse getResult = attributes1.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }


    /**
     * Method under test: {@link InstitutionMapper#toInstitutionResponse(Institution)}
     */
    @Test
    void testToInstitutionResponse5() {
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
        assertEquals("Ipa Code", actualToInstitutionResponseResult.getIpaCode());
        assertEquals("Business Register Place", actualToInstitutionResponseResult.getBusinessRegisterPlace());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        PaymentServiceProviderResponse paymentServiceProviderResponse = actualToInstitutionResponseResult
                .getPaymentServiceProviderResponse();
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
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse2() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setOnboarding(null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "foo", "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertEquals("foo", actualToInstitutionManagerResponseResult.getId());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getZipCode());
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

        Premium premium1 = new Premium();
        premium1.setContract("Contract");
        premium1.setStatus(RelationshipState.PENDING);

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPremium(premium1);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Premium premium2 = new Premium();
        premium2.setContract("Contract");
        premium2.setStatus(RelationshipState.PENDING);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPremium(premium2);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setOnboarding(onboardingList);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "foo", "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertEquals("foo", actualToInstitutionManagerResponseResult.getId());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getAddress());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals(1, product.getRole().size());
        assertEquals("foo", product.getId());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(Institution, OnboardedUser, String, String)}
     */
    @Test
    void testToInstitutionManagerResponse7() {
        COWArrayList<GeographicTaxonomies> geographicTaxonomiesList = (COWArrayList<GeographicTaxonomies>) mock(
                COWArrayList.class);
        doNothing().when(geographicTaxonomiesList).forEach(any());

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        institution.setOnboarding(null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(institution, onboardedUser, "foo", "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertEquals("foo", actualToInstitutionManagerResponseResult.getId());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdate institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getInstitutionType());
        assertTrue(institutionUpdate.getGeographicTaxonomyCodes().isEmpty());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getZipCode());
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
    void testToBillingResponse6() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        institution.setInstitutionType(it.pagopa.selfcare.mscore.model.institution.InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertNull(actualToBillingResponseResult.getAddress());
        assertNull(actualToBillingResponseResult.getZipCode());
        assertNull(actualToBillingResponseResult.getTaxCode());
        assertNull(actualToBillingResponseResult.getIpaCode());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.GSP,
                actualToBillingResponseResult.getInstitutionType());
        assertNull(actualToBillingResponseResult.getInstitutionId());
        assertNull(actualToBillingResponseResult.getExternalId());
        assertNull(actualToBillingResponseResult.getDigitalAddress());
        assertNull(actualToBillingResponseResult.getDescription());
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
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution, "42");
        assertNull(actualToBillingResponseResult.getAddress());
        assertNull(actualToBillingResponseResult.getZipCode());
        assertNull(actualToBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToBillingResponseResult.getPricingPlan());
        assertNull(actualToBillingResponseResult.getIpaCode());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.GSP,
                actualToBillingResponseResult.getInstitutionType());
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
     * Method under test: {@link InstitutionMapper#toBillingResponse(Institution, String)}
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
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(institution,
                "Product Id");
        assertNull(actualToBillingResponseResult.getAddress());
        assertNull(actualToBillingResponseResult.getZipCode());
        assertNull(actualToBillingResponseResult.getTaxCode());
        assertNull(actualToBillingResponseResult.getIpaCode());
        assertEquals(it.pagopa.selfcare.mscore.web.model.institution.InstitutionType.GSP,
                actualToBillingResponseResult.getInstitutionType());
        assertNull(actualToBillingResponseResult.getInstitutionId());
        assertNull(actualToBillingResponseResult.getExternalId());
        assertNull(actualToBillingResponseResult.getDigitalAddress());
        assertNull(actualToBillingResponseResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toResource(Institution)}
     */
    @Test
    void testToResource() {
        InstitutionResource actualToResourceResult = InstitutionMapper.toResource(new Institution());
        assertNull(actualToResourceResult.getExternalId());
        assertNull(actualToResourceResult.getId());
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
        institutionRequest.setImported(true);
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
        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setImported(true);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        institutionRequest.setGeographicTaxonomies(null);
        institutionRequest.setPaymentServiceProvider(null);
        institutionRequest.setDataProtectionOfficer(null);
        institutionRequest.setAttributes(null);
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "foo");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertTrue(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        List<GeographicTaxonomies> geographicTaxonomies = actualToInstitutionResult.getGeographicTaxonomies();
        assertTrue(geographicTaxonomies.isEmpty());
        assertEquals("foo", actualToInstitutionResult.getExternalId());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution3() {
        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("Code");
        geoTaxonomies.setDesc("The characteristics of someone or something");
        geoTaxonomies.setEnable(true);

        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        geoTaxonomiesList.add(geoTaxonomies);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setImported(true);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setPaymentServiceProvider(null);
        institutionRequest.setDataProtectionOfficer(null);
        institutionRequest.setAttributes(null);
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "foo");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertTrue(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertEquals(1, actualToInstitutionResult.getGeographicTaxonomies().size());
        assertEquals("foo", actualToInstitutionResult.getExternalId());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        assertTrue(actualToInstitutionResult.getAttributes().isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution4() {
        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("Code");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("Origin");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setImported(true);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        institutionRequest.setGeographicTaxonomies(null);
        institutionRequest.setPaymentServiceProvider(null);
        institutionRequest.setDataProtectionOfficer(null);
        institutionRequest.setAttributes(attributesRequestList);
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "foo");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertTrue(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        assertTrue(actualToInstitutionResult.getGeographicTaxonomies().isEmpty());
        assertEquals("foo", actualToInstitutionResult.getExternalId());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        List<Attributes> attributes = actualToInstitutionResult.getAttributes();
        assertEquals(1, attributes.size());
        Attributes getResult = attributes.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution5() {
        COWArrayList<GeoTaxonomies> geoTaxonomiesList = (COWArrayList<GeoTaxonomies>) mock(COWArrayList.class);
        when(geoTaxonomiesList.iterator()).thenReturn(MappingIterator.emptyIterator());

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setImported(true);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("4105551212");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setPaymentServiceProvider(null);
        institutionRequest.setDataProtectionOfficer(null);
        institutionRequest.setAttributes(null);
        Institution actualToInstitutionResult = InstitutionMapper.toInstitution(institutionRequest, "foo");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertTrue(actualToInstitutionResult.isImported());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        List<GeographicTaxonomies> geographicTaxonomies = actualToInstitutionResult.getGeographicTaxonomies();
        assertTrue(geographicTaxonomies.isEmpty());
        assertEquals("foo", actualToInstitutionResult.getExternalId());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        verify(geoTaxonomiesList).iterator();
    }
}

