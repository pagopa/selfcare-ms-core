package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.utils.OriginEnum;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
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
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(
                new Institution("42", "42", OriginEnum.MOCK, "42", "The characteristics of someone or something",
                        InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboardingList,
                        geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(), "Rea",
                        "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true, null, null));
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertTrue(actualToInstitutionResponseResult.isImported());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResponseResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResponseResult.getRea());
        assertEquals("42", actualToInstitutionResponseResult.getOriginId());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionResponseResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        PaymentServiceProviderResponse paymentServiceProvider1 = actualToInstitutionResponseResult
                .getPaymentServiceProvider();
        assertNull(paymentServiceProvider1.getLegalRegisterNumber());
        assertFalse(paymentServiceProvider1.isVatNumberGroup());
        DataProtectionOfficerResponse dataProtectionOfficer = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertNull(dataProtectionOfficer.getEmail());
        assertNull(paymentServiceProvider1.getAbiCode());
        assertNull(dataProtectionOfficer.getAddress());
        assertNull(paymentServiceProvider1.getLegalRegisterName());
        assertNull(dataProtectionOfficer.getPec());
        assertNull(paymentServiceProvider1.getBusinessRegisterNumber());
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
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(
                new Institution("42", "42", OriginEnum.MOCK, "42", "The characteristics of someone or something",
                        InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboardingList,
                        geographicTaxonomiesList, attributes, paymentServiceProvider, new DataProtectionOfficer(), "Rea",
                        "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true, null, null));
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertTrue(actualToInstitutionResponseResult.isImported());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResponseResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResponseResult.getRea());
        assertEquals("42", actualToInstitutionResponseResult.getOriginId());
        List<GeoTaxonomies> geographicTaxonomies1 = actualToInstitutionResponseResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies1.size());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionResponseResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        DataProtectionOfficerResponse dataProtectionOfficer = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertNull(dataProtectionOfficer.getPec());
        PaymentServiceProviderResponse paymentServiceProvider1 = actualToInstitutionResponseResult
                .getPaymentServiceProvider();
        assertNull(paymentServiceProvider1.getLegalRegisterNumber());
        assertFalse(paymentServiceProvider1.isVatNumberGroup());
        assertNull(paymentServiceProvider1.getAbiCode());
        assertNull(paymentServiceProvider1.getBusinessRegisterNumber());
        assertNull(dataProtectionOfficer.getEmail());
        assertNull(paymentServiceProvider1.getLegalRegisterName());
        assertNull(dataProtectionOfficer.getAddress());
        GeoTaxonomies getResult = geographicTaxonomies1.get(0);
        assertEquals("The characteristics of someone or something", getResult.getDesc());
        assertEquals("Code", getResult.getCode());
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
        InstitutionResponse actualToInstitutionResponseResult = InstitutionMapper.toInstitutionResponse(
                new Institution("42", "42", OriginEnum.MOCK, "42", "The characteristics of someone or something",
                        InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboardingList,
                        geographicTaxonomies, attributesList, paymentServiceProvider, new DataProtectionOfficer(), "Rea",
                        "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true, null, null));
        assertEquals("42 Main St", actualToInstitutionResponseResult.getAddress());
        assertTrue(actualToInstitutionResponseResult.isImported());
        assertEquals("21654", actualToInstitutionResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResponseResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionResponseResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResponseResult.getRea());
        assertEquals("42", actualToInstitutionResponseResult.getOriginId());
        List<AttributesResponse> attributes1 = actualToInstitutionResponseResult.getAttributes();
        assertEquals(1, attributes1.size());
        assertEquals("42", actualToInstitutionResponseResult.getId());
        assertEquals("The characteristics of someone or something", actualToInstitutionResponseResult.getDescription());
        assertEquals(InstitutionType.PA, actualToInstitutionResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionResponseResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResponseResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResponseResult.getExternalId());
        DataProtectionOfficerResponse dataProtectionOfficer = actualToInstitutionResponseResult
                .getDataProtectionOfficer();
        assertNull(dataProtectionOfficer.getPec());
        PaymentServiceProviderResponse paymentServiceProvider1 = actualToInstitutionResponseResult
                .getPaymentServiceProvider();
        assertNull(paymentServiceProvider1.getLegalRegisterNumber());
        assertFalse(paymentServiceProvider1.isVatNumberGroup());
        assertNull(paymentServiceProvider1.getAbiCode());
        assertNull(paymentServiceProvider1.getBusinessRegisterNumber());
        assertNull(dataProtectionOfficer.getAddress());
        assertNull(dataProtectionOfficer.getEmail());
        assertNull(paymentServiceProvider1.getLegalRegisterName());
        AttributesResponse getResult = attributes1.get(0);
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse5() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setInstitution(institution);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(productManagerInfo, "42");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        assertEquals("Pricing Plan", actualToInstitutionManagerResponseResult.getPricingPlan());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        assertFalse(institutionUpdate.isImported());
        BillingResponse billing1 = actualToInstitutionManagerResponseResult.getBilling();
        assertEquals("42", billing1.getVatNumber());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertNull(institutionUpdate.getZipCode());
        assertTrue(billing1.isPublicServices());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse6() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(onboardedProductList);
        productManagerInfo.setInstitution(institution);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(productManagerInfo, "42");
        assertNull(actualToInstitutionManagerResponseResult.getUpdatedAt());
        assertNull(actualToInstitutionManagerResponseResult.getCreatedAt());
        assertEquals("42", actualToInstitutionManagerResponseResult.getId());
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertEquals(RelationshipState.PENDING, actualToInstitutionManagerResponseResult.getState());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        assertEquals("Pricing Plan", actualToInstitutionManagerResponseResult.getPricingPlan());
        ProductInfo product = actualToInstitutionManagerResponseResult.getProduct();
        assertEquals("42", product.getId());
        assertNull(product.getCreatedAt());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        assertFalse(institutionUpdate.isImported());
        BillingResponse billing1 = actualToInstitutionManagerResponseResult.getBilling();
        assertEquals("42", billing1.getVatNumber());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertEquals("Product Role", product.getRole());
        assertTrue(billing1.isPublicServices());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse7() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution toInstitutionResult = InstitutionMapper.toInstitution(new InstitutionRequest(), "42");
        toInstitutionResult.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        productManagerInfo.setProducts(onboardedProductList);
        productManagerInfo.setInstitution(toInstitutionResult);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(productManagerInfo, "42");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        assertEquals("Pricing Plan", actualToInstitutionManagerResponseResult.getPricingPlan());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        assertFalse(institutionUpdate.isImported());
        BillingResponse billing1 = actualToInstitutionManagerResponseResult.getBilling();
        assertEquals("42", billing1.getVatNumber());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertNull(institutionUpdate.getZipCode());
        assertTrue(billing1.isPublicServices());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse8() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("Product Id");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setInstitution(institution);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(productManagerInfo, "42");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getZipCode());
        assertFalse(institutionUpdate.isImported());
    }


    /**
     * Method under test: {@link InstitutionMapper#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse10() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(onboardedProductList);
        productManagerInfo.setInstitution(institution);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapper
                .toInstitutionManagerResponse(productManagerInfo, "Product Id");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getZipCode());
        assertFalse(institutionUpdate.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse2() {
        assertNull(InstitutionMapper.toInstitutionBillingResponse(null, "foo"));
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse3() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapper
                .toInstitutionBillingResponse(institution, "42");
        assertNull(actualToInstitutionBillingResponseResult.getAddress());
        assertNull(actualToInstitutionBillingResponseResult.getZipCode());
        assertNull(actualToInstitutionBillingResponseResult.getTaxCode());
        assertNull(actualToInstitutionBillingResponseResult.getOriginId());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionType());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionId());
        assertNull(actualToInstitutionBillingResponseResult.getExternalId());
        assertNull(actualToInstitutionBillingResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionBillingResponseResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse4() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapper
                .toInstitutionBillingResponse(institution, "42");
        assertNull(actualToInstitutionBillingResponseResult.getAddress());
        assertNull(actualToInstitutionBillingResponseResult.getZipCode());
        assertNull(actualToInstitutionBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToInstitutionBillingResponseResult.getPricingPlan());
        assertNull(actualToInstitutionBillingResponseResult.getOriginId());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionType());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionId());
        assertNull(actualToInstitutionBillingResponseResult.getExternalId());
        assertNull(actualToInstitutionBillingResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionBillingResponseResult.getDescription());
        BillingResponse billing1 = actualToInstitutionBillingResponseResult.getBilling();
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertTrue(billing1.isPublicServices());
        assertEquals("42", billing1.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse5() {
        Billing billing = new Billing();
        billing.setPublicServices(false);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapper
                .toInstitutionBillingResponse(institution, "42");
        assertNull(actualToInstitutionBillingResponseResult.getAddress());
        assertNull(actualToInstitutionBillingResponseResult.getZipCode());
        assertNull(actualToInstitutionBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToInstitutionBillingResponseResult.getPricingPlan());
        assertNull(actualToInstitutionBillingResponseResult.getOriginId());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionType());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionId());
        assertNull(actualToInstitutionBillingResponseResult.getExternalId());
        assertNull(actualToInstitutionBillingResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionBillingResponseResult.getDescription());
        BillingResponse billing1 = actualToInstitutionBillingResponseResult.getBilling();
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertFalse(billing1.isPublicServices());
        assertEquals("42", billing1.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse6() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("Product Id");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapper
                .toInstitutionBillingResponse(institution, "42");
        assertNull(actualToInstitutionBillingResponseResult.getAddress());
        assertNull(actualToInstitutionBillingResponseResult.getZipCode());
        assertNull(actualToInstitutionBillingResponseResult.getTaxCode());
        assertNull(actualToInstitutionBillingResponseResult.getOriginId());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionType());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionId());
        assertNull(actualToInstitutionBillingResponseResult.getExternalId());
        assertNull(actualToInstitutionBillingResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionBillingResponseResult.getDescription());
    }


    /**
     * Method under test: {@link InstitutionMapper#toInstitutionUpdateResponse(Institution)}
     */
    @Test
    void testToInstitutionUpdateResponse2() {
        Institution institution = new Institution();
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        InstitutionUpdateResponse actualToInstitutionUpdateResponseResult = InstitutionMapper
                .toInstitutionUpdateResponse(institution);
        assertNull(actualToInstitutionUpdateResponseResult.getAddress());
        assertNull(actualToInstitutionUpdateResponseResult.getZipCode());
        assertNull(actualToInstitutionUpdateResponseResult.getTaxCode());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportPhone());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportEmail());
        assertNull(actualToInstitutionUpdateResponseResult.getShareCapital());
        assertNull(actualToInstitutionUpdateResponseResult.getRea());
        assertNull(actualToInstitutionUpdateResponseResult.getPaymentServiceProvider());
        assertNull(actualToInstitutionUpdateResponseResult.getInstitutionType());
        assertNull(actualToInstitutionUpdateResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionUpdateResponseResult.getDescription());
        assertNull(actualToInstitutionUpdateResponseResult.getDataProtectionOfficer());
        assertNull(actualToInstitutionUpdateResponseResult.getBusinessRegisterPlace());
        assertFalse(institution.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionUpdateResponse(Institution)}
     */
    @Test
    void testToInstitutionUpdateResponse4() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        Institution institution = new Institution("42", "42", OriginEnum.MOCK, "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "Tax Code", billing, onboardingList, geographicTaxonomies, attributes, paymentServiceProvider,
                dataProtectionOfficer, "Rea", "Share Capital", "Business Register Place", "jane.doe@example.org",
                "4105551212", true, null, null);

        InstitutionUpdateResponse actualToInstitutionUpdateResponseResult = InstitutionMapper
                .toInstitutionUpdateResponse(institution);
        assertEquals("42 Main St", actualToInstitutionUpdateResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionUpdateResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionUpdateResponseResult.getTaxCode());
        assertEquals("4105551212", actualToInstitutionUpdateResponseResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionUpdateResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionUpdateResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionUpdateResponseResult.getRea());
        assertSame(paymentServiceProvider, actualToInstitutionUpdateResponseResult.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, actualToInstitutionUpdateResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionUpdateResponseResult.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something",
                actualToInstitutionUpdateResponseResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionUpdateResponseResult.getDigitalAddress());
        assertSame(dataProtectionOfficer, actualToInstitutionUpdateResponseResult.getDataProtectionOfficer());
        assertTrue(institution.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionUpdate(InstitutionPut)}
     */
    @Test
    void testToInstitutionUpdate() {
        InstitutionPut institutionPut = new InstitutionPut();
        institutionPut.setGeographicTaxonomyCodes(new ArrayList<>());
        assertTrue(InstitutionMapper.toInstitutionUpdate(institutionPut).getGeographicTaxonomyCodes().isEmpty());
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
        paymentServiceProviderRequest.setVatNumberGroup(false);

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
        assertFalse(paymentServiceProvider.isVatNumberGroup());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Onboarding, Institution)}
     */
    @Test
    void testToBillingResponse() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        BillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(onboarding, new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertTrue(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBillingResponse(Onboarding, Institution)}
     */
    @Test
    void testToBillingResponse2() {
        Billing billing = new Billing();
        billing.setPublicServices(false);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        BillingResponse actualToBillingResponseResult = InstitutionMapper.toBillingResponse(onboarding,
                new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertFalse(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#getBillingFromOnboarding(Onboarding, Institution)}
     */
    @Test
    void testGetBillingFromOnboarding() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        InstitutionMapper.getBillingFromOnboarding(onboarding, new Institution());
    }

    /**
     * Method under test: {@link InstitutionMapper#toBilling(BillingRequest)}
     */
    @Test
    void testToBilling() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");
        Billing actualToBillingResult = InstitutionMapper.toBilling(billingRequest);
        assertEquals("Recipient Code", actualToBillingResult.getRecipientCode());
        assertFalse(actualToBillingResult.isPublicServices());
        assertEquals("42", actualToBillingResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#toAttributeResponse(List)}
     */
    @Test
    void testToAttributeResponse() {
        assertTrue(InstitutionMapper.toAttributeResponse(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapper#toAttributeResponse(List)}
     */
    @Test
    void testToAttributeResponse2() {
        Attributes attributes = new Attributes();
        attributes.setCode("Code");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Origin");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);
        List<AttributesResponse> actualToAttributeResponseResult = InstitutionMapper.toAttributeResponse(attributesList);
        assertEquals(1, actualToAttributeResponseResult.size());
        AttributesResponse getResult = actualToAttributeResponseResult.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#toGeoTaxonomies(List)}
     */
    @Test
    void testToGeoTaxonomies() {
        assertTrue(InstitutionMapper.toGeoTaxonomies(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapper#toGeoTaxonomies(List)}
     */
    @Test
    void testToGeoTaxonomies2() {
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
        List<GeoTaxonomies> actualToGeoTaxonomiesResult = InstitutionMapper.toGeoTaxonomies(geographicTaxonomiesList);
        assertEquals(1, actualToGeoTaxonomiesResult.size());
        GeoTaxonomies getResult = actualToGeoTaxonomiesResult.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("The characteristics of someone or something", getResult.getDesc());
    }

    /**
     * Method under test: {@link InstitutionMapper#toOnboardedProducts(List)}
     */
    @Test
    void testToOnboardedProducts() {
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        assertEquals(0, InstitutionMapper.toOnboardedProducts(onboardingList).getProducts().size());
    }

    /**
     * Method under test: {@link InstitutionMapper#toOnboardedProducts(List)}
     */
    @Test
    void testToOnboardedProducts2() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        List<InstitutionProduct> products = InstitutionMapper.toOnboardedProducts(onboardingList).getProducts();
        assertEquals(1, products.size());
        InstitutionProduct getResult = products.get(0);
        assertEquals("42", getResult.getId());
        assertEquals(RelationshipState.PENDING, getResult.getState());
    }

    /**
     * Method under test: {@link InstitutionMapper#toOnboardedProducts(List)}
     */
    @Test
    void testToOnboardedProducts3() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        List<InstitutionProduct> products = InstitutionMapper.toOnboardedProducts(onboardingList).getProducts();
        assertEquals(2, products.size());
        InstitutionProduct getResult = products.get(0);
        assertEquals(RelationshipState.PENDING, getResult.getState());
        InstitutionProduct getResult1 = products.get(1);
        assertEquals(RelationshipState.PENDING, getResult1.getState());
        assertEquals("42", getResult1.getId());
        assertEquals("42", getResult.getId());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionProduct(List)}
     */
    @Test
    void testToInstitutionProduct() {
        assertTrue(InstitutionMapper.toInstitutionProduct(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionProduct(List)}
     */
    @Test
    void testToInstitutionProduct2() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        List<InstitutionProduct> actualToInstitutionProductResult = InstitutionMapper
                .toInstitutionProduct(onboardingList);
        assertEquals(1, actualToInstitutionProductResult.size());
        InstitutionProduct getResult = actualToInstitutionProductResult.get(0);
        assertEquals("42", getResult.getId());
        assertEquals(RelationshipState.PENDING, getResult.getState());
    }

    /**
     * Method under test: {@link InstitutionMapper#toInstitutionProduct(List)}
     */
    @Test
    void testToInstitutionProduct3() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        List<InstitutionProduct> actualToInstitutionProductResult = InstitutionMapper
                .toInstitutionProduct(onboardingList);
        assertEquals(2, actualToInstitutionProductResult.size());
        InstitutionProduct getResult = actualToInstitutionProductResult.get(0);
        assertEquals(RelationshipState.PENDING, getResult.getState());
        InstitutionProduct getResult1 = actualToInstitutionProductResult.get(1);
        assertEquals(RelationshipState.PENDING, getResult1.getState());
        assertEquals("42", getResult1.getId());
        assertEquals("42", getResult.getId());
    }
}

