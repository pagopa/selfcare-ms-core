package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.web.TestUtils;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstitutionMapperCustomTest {

    Institution createDmmyInstitution() {

        Institution dummyInstitution = new Institution();
        dummyInstitution.setId("42");
        dummyInstitution.setExternalId("42");
        dummyInstitution.setOrigin(Origin.MOCK.name());
        dummyInstitution.setOriginId("42");
        dummyInstitution.setDescription("The characteristics of someone or something");
        dummyInstitution.setInstitutionType(InstitutionType.PA);
        dummyInstitution.setDigitalAddress("42 Main St");
        dummyInstitution.setTaxCode("taxCode");
        dummyInstitution.setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        dummyInstitution.setAddress("42 Main St");
        dummyInstitution.setBusinessRegisterPlace("Business Register Place");
        dummyInstitution.setRea("Rea");
        dummyInstitution.setZipCode("zipCpde");
        dummyInstitution.setShareCapital("Share Capital");


        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);
        dummyInstitution.setPaymentServiceProvider(paymentServiceProvider);

        return dummyInstitution;
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
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

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(new ArrayList<>());
        productManagerInfo.setInstitution(institution);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
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
        assertNull(institutionUpdate.getInstitutionType());
        BillingResponse billing1 = actualToInstitutionManagerResponseResult.getBilling();
        assertTrue(billing1.isPublicServices());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertEquals("42", billing1.getVatNumber());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getZipCode());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
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

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
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
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
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
        assertNull(institutionUpdate.getInstitutionType());
        BillingResponse billing1 = actualToInstitutionManagerResponseResult.getBilling();
        assertTrue(billing1.isPublicServices());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertEquals("42", billing1.getVatNumber());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDigitalAddress());
        assertEquals("Product Role", product.getRole());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
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
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        OnboardedProduct onboardedProduct1 = new OnboardedProduct();
        onboardedProduct1.setContract("it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct");
        onboardedProduct1.setCreatedAt(null);
        onboardedProduct1.setEnv(Env.DEV);
        onboardedProduct1.setProductId("Product Id");
        onboardedProduct1.setProductRole("it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct");
        onboardedProduct1.setRelationshipId("Relationship Id");
        onboardedProduct1.setRole(PartyRole.DELEGATE);
        onboardedProduct1.setStatus(RelationshipState.ACTIVE);
        onboardedProduct1.setTokenId("ABC123");
        onboardedProduct1.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct1);
        onboardedProductList.add(onboardedProduct);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        productManagerInfo.setProducts(onboardedProductList);
        productManagerInfo.setInstitution(institution);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
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
        assertNull(institutionUpdate.getInstitutionType());
        BillingResponse billing1 = actualToInstitutionManagerResponseResult.getBilling();
        assertTrue(billing1.isPublicServices());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertEquals("42", billing1.getVatNumber());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDigitalAddress());
        assertEquals("Product Role", product.getRole());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse9() {
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
        Institution toInstitutionResult = InstitutionMapperCustom.toInstitution(new InstitutionRequest(), "42");
        toInstitutionResult.setOnboarding(onboardingList);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        productManagerInfo.setProducts(onboardedProductList);
        productManagerInfo.setInstitution(toInstitutionResult);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
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
        assertNull(institutionUpdate.getInstitutionType());
        BillingResponse billing1 = actualToInstitutionManagerResponseResult.getBilling();
        assertTrue(billing1.isPublicServices());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertEquals("42", billing1.getVatNumber());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getZipCode());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse10() {
        Billing billing = TestUtils.createSimpleBilling();

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

        Institution institution = createDmmyInstitution();
        institution.setOnboarding(onboardingList);
        institution.setImported(true);

        ProductManagerInfo productManagerInfo = new ProductManagerInfo();
        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        productManagerInfo.setProducts(onboardedProductList);
        productManagerInfo.setInstitution(institution);

        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "42");

        assertEquals("42", actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        assertEquals(onboarding.getPricingPlan(), actualToInstitutionManagerResponseResult.getPricingPlan());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertEquals(institution.getTaxCode(), institutionUpdate.getTaxCode());
        assertEquals(institution.getSupportPhone(), institutionUpdate.getSupportPhone());
        assertEquals(institution.getSupportEmail(), institutionUpdate.getSupportEmail());
        assertEquals(institution.getShareCapital(), institutionUpdate.getShareCapital());
        assertEquals(institution.getRea(), institutionUpdate.getRea());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        BillingResponse billing2 = actualToInstitutionManagerResponseResult.getBilling();
        assertTrue(billing2.isPublicServices());
        assertEquals("Recipient Code", billing2.getRecipientCode());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("42", billing2.getVatNumber());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals(institution.getDigitalAddress(), institutionUpdate.getDigitalAddress());
        assertEquals(institution.getZipCode(), institutionUpdate.getZipCode());
        assertTrue(institutionUpdate.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse2() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapperCustom
                .toInstitutionBillingResponse(institution, "42");
        assertNull(actualToInstitutionBillingResponseResult.getAddress());
        assertNull(actualToInstitutionBillingResponseResult.getZipCode());
        assertNull(actualToInstitutionBillingResponseResult.getTaxCode());
        assertNull(actualToInstitutionBillingResponseResult.getOriginId());
        assertNull(actualToInstitutionBillingResponseResult.getOrigin());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionType());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionId());
        assertNull(actualToInstitutionBillingResponseResult.getExternalId());
        assertNull(actualToInstitutionBillingResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionBillingResponseResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse3() {
        assertNull(InstitutionMapperCustom.toInstitutionBillingResponse(null, "42"));
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
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
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapperCustom
                .toInstitutionBillingResponse(institution, "42");
        assertNull(actualToInstitutionBillingResponseResult.getAddress());
        assertNull(actualToInstitutionBillingResponseResult.getZipCode());
        assertNull(actualToInstitutionBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToInstitutionBillingResponseResult.getPricingPlan());
        assertNull(actualToInstitutionBillingResponseResult.getOriginId());
        assertNull(actualToInstitutionBillingResponseResult.getOrigin());
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
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse5() {
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
        billing1.setPublicServices(false);
        billing1.setRecipientCode("it.pagopa.selfcare.mscore.model.institution.Billing");
        billing1.setVatNumber("Vat Number");

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setClosedAt(null);
        onboarding1.setContract("it.pagopa.selfcare.mscore.model.institution.Onboarding");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("it.pagopa.selfcare.mscore.model.institution.Onboarding");
        onboarding1.setProductId("Product Id");
        onboarding1.setStatus(RelationshipState.ACTIVE);
        onboarding1.setTokenId("ABC123");
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapperCustom
                .toInstitutionBillingResponse(institution, "42");
        assertNull(actualToInstitutionBillingResponseResult.getAddress());
        assertNull(actualToInstitutionBillingResponseResult.getZipCode());
        assertNull(actualToInstitutionBillingResponseResult.getTaxCode());
        assertEquals("Pricing Plan", actualToInstitutionBillingResponseResult.getPricingPlan());
        assertNull(actualToInstitutionBillingResponseResult.getOriginId());
        assertNull(actualToInstitutionBillingResponseResult.getOrigin());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionType());
        assertNull(actualToInstitutionBillingResponseResult.getInstitutionId());
        assertNull(actualToInstitutionBillingResponseResult.getExternalId());
        assertNull(actualToInstitutionBillingResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionBillingResponseResult.getDescription());
        BillingResponse billing2 = actualToInstitutionBillingResponseResult.getBilling();
        assertEquals("Recipient Code", billing2.getRecipientCode());
        assertTrue(billing2.isPublicServices());
        assertEquals("42", billing2.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

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
        institutionRequest.setBilling(billingRequest);
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setCreatedAt(null);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setExternalId("42");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setId("42");
        institutionRequest.setImported(true);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setOnboarding(new ArrayList<>());
        institutionRequest.setOrigin(Origin.MOCK.name());
        institutionRequest.setOriginId("42");
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setUpdatedAt(null);
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("6625550144", actualToInstitutionResult.getSupportPhone());
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
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution2() {
        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("Code");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("Origin");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);

        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

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
        institutionRequest.setBilling(billingRequest);
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setCreatedAt(null);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setExternalId("42");
        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setId("42");
        institutionRequest.setImported(true);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setOnboarding(new ArrayList<>());
        institutionRequest.setOrigin(Origin.MOCK.name());
        institutionRequest.setOriginId("42");
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setUpdatedAt(null);
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("6625550144", actualToInstitutionResult.getSupportPhone());
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
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution3() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");

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
        institutionRequest.setBilling(billingRequest);
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setCreatedAt(null);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setExternalId("42");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setId("42");
        institutionRequest.setImported(true);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setOnboarding(new ArrayList<>());
        institutionRequest.setOrigin(Origin.MOCK.name());
        institutionRequest.setOriginId("42");
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setUpdatedAt(null);
        institutionRequest.setZipCode("21654");
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("6625550144", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("42", actualToInstitutionResult.getExternalId());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = actualToInstitutionResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies.size());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toBillingResponse(Onboarding, Institution)}
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
        BillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toBillingResponse(onboarding, new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertTrue(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#getBillingFromOnboarding(Onboarding, Institution)}
     */
    @Test
    void testGetBillingFromOnboarding2() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");
        Onboarding onboarding = mock(Onboarding.class);
        when(onboarding.getBilling()).thenReturn(billing1);
        doNothing().when(onboarding).setBilling(any());
        doNothing().when(onboarding).setClosedAt(any());
        doNothing().when(onboarding).setContract(any());
        doNothing().when(onboarding).setCreatedAt(any());
        doNothing().when(onboarding).setPricingPlan(any());
        doNothing().when(onboarding).setProductId(any());
        doNothing().when(onboarding).setStatus(any());
        doNothing().when(onboarding).setTokenId(any());
        doNothing().when(onboarding).setUpdatedAt(any());
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        InstitutionMapperCustom.getBillingFromOnboarding(onboarding, new Institution());
        verify(onboarding, atLeast(1)).getBilling();
        verify(onboarding).setBilling( any());
        verify(onboarding).setClosedAt(any());
        verify(onboarding).setContract(any());
        verify(onboarding).setCreatedAt(any());
        verify(onboarding).setPricingPlan(any());
        verify(onboarding).setProductId(any());
        verify(onboarding).setStatus(any());
        verify(onboarding).setTokenId(any());
        verify(onboarding).setUpdatedAt(any());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toBilling(BillingRequest)}
     */
    @Test
    void testToBilling() {
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setPublicServices(true);
        billingRequest.setRecipientCode("Recipient Code");
        billingRequest.setVatNumber("42");
        Billing actualToBillingResult = InstitutionMapperCustom.toBilling(billingRequest);
        assertEquals("Recipient Code", actualToBillingResult.getRecipientCode());
        assertFalse(actualToBillingResult.isPublicServices());
        assertEquals("42", actualToBillingResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toDataProtectionOfficer(DataProtectionOfficerRequest)}
     */
    @Test
    void testToDataProtectionOfficer() {
        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");
        DataProtectionOfficer actualToDataProtectionOfficerResult = InstitutionMapperCustom
                .toDataProtectionOfficer(dataProtectionOfficerRequest);
        assertEquals("42 Main St", actualToDataProtectionOfficerResult.getAddress());
        assertEquals("Pec", actualToDataProtectionOfficerResult.getPec());
        assertEquals("jane.doe@example.org", actualToDataProtectionOfficerResult.getEmail());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toDataProtectionOfficerResponse(DataProtectionOfficer)}
     */
    @Test
    void testToDataProtectionOfficerResponse() {
        DataProtectionOfficerResponse actualToDataProtectionOfficerResponseResult = InstitutionMapperCustom
                .toDataProtectionOfficerResponse(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        assertEquals("42 Main St", actualToDataProtectionOfficerResponseResult.getAddress());
        assertEquals("Pec", actualToDataProtectionOfficerResponseResult.getPec());
        assertEquals("jane.doe@example.org", actualToDataProtectionOfficerResponseResult.getEmail());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toAttributes(List)}
     */
    @Test
    void testToAttributes2() {
        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("Code");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("Origin");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);
        List<Attributes> actualToAttributesResult = InstitutionMapperCustom.toAttributes(attributesRequestList);
        assertEquals(1, actualToAttributesResult.size());
        Attributes getResult = actualToAttributesResult.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toAttributeResponse(List)}
     */
    @Test
    void testToAttributeResponse() {
        assertTrue(InstitutionMapperCustom.toAttributeResponse(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toAttributeResponse(List)}
     */
    @Test
    void testToAttributeResponse2() {
        Attributes attributes = new Attributes();
        attributes.setCode("Code");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Origin");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);
        List<AttributesResponse> actualToAttributeResponseResult = InstitutionMapperCustom.toAttributeResponse(attributesList);
        assertEquals(1, actualToAttributeResponseResult.size());
        AttributesResponse getResult = actualToAttributeResponseResult.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("Origin", getResult.getOrigin());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toGeoTaxonomies(List)}
     */
    @Test
    void testToGeoTaxonomies() {
        assertTrue(InstitutionMapperCustom.toGeoTaxonomies(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toGeoTaxonomies(List)}
     */
    @Test
    void testToGeoTaxonomies2() {
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));
        List<GeoTaxonomies> actualToGeoTaxonomiesResult = InstitutionMapperCustom
                .toGeoTaxonomies(institutionGeographicTaxonomiesList);
        assertEquals(1, actualToGeoTaxonomiesResult.size());
        GeoTaxonomies getResult = actualToGeoTaxonomiesResult.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("The characteristics of someone or something", getResult.getDesc());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagementResponse(Institution)}
     */
    @Test
    void testToInstitutionManagementResponse() {
        InstitutionManagementResponse actualToInstitutionManagementResponseResult = InstitutionMapperCustom
                .toInstitutionManagementResponse(new Institution());
        assertNull(actualToInstitutionManagementResponseResult.getAddress());
        assertFalse(actualToInstitutionManagementResponseResult.isImported());
        assertNull(actualToInstitutionManagementResponseResult.getZipCode());
        assertNull(actualToInstitutionManagementResponseResult.getUpdatedAt());
        assertNull(actualToInstitutionManagementResponseResult.getTaxCode());
        assertNull(actualToInstitutionManagementResponseResult.getSupportPhone());
        assertNull(actualToInstitutionManagementResponseResult.getSupportEmail());
        assertNull(actualToInstitutionManagementResponseResult.getShareCapital());
        assertNull(actualToInstitutionManagementResponseResult.getRea());
        assertNull(actualToInstitutionManagementResponseResult.getOriginId());
        assertNull(actualToInstitutionManagementResponseResult.getOrigin());
        assertNull(actualToInstitutionManagementResponseResult.getInstitutionType());
        assertNull(actualToInstitutionManagementResponseResult.getId());
        assertNull(actualToInstitutionManagementResponseResult.getExternalId());
        assertNull(actualToInstitutionManagementResponseResult.getDigitalAddress());
        assertNull(actualToInstitutionManagementResponseResult.getDescription());
        assertNull(actualToInstitutionManagementResponseResult.getCreatedAt());
        assertNull(actualToInstitutionManagementResponseResult.getBusinessRegisterPlace());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagementResponse(Institution)}
     */
    @Test
    void testToInstitutionManagementResponse4() {

        Institution institution = createDmmyInstitution();
        institution.setImported(true);
        institution.setOnboarding(List.of());

        InstitutionManagementResponse actualToInstitutionManagementResponseResult = InstitutionMapperCustom
                .toInstitutionManagementResponse(institution);

        assertEquals("42 Main St", actualToInstitutionManagementResponseResult.getAddress());
        assertTrue(actualToInstitutionManagementResponseResult.isImported());
        assertEquals(institution.getZipCode(), actualToInstitutionManagementResponseResult.getZipCode());
        assertNull(actualToInstitutionManagementResponseResult.getUpdatedAt());
        assertEquals(institution.getTaxCode(), actualToInstitutionManagementResponseResult.getTaxCode());
        assertEquals(institution.getSupportPhone(), actualToInstitutionManagementResponseResult.getSupportPhone());
        assertEquals(institution.getSupportEmail(), actualToInstitutionManagementResponseResult.getSupportEmail());
        assertEquals(institution.getShareCapital(), actualToInstitutionManagementResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionManagementResponseResult.getRea());
        assertTrue(actualToInstitutionManagementResponseResult.getProducts().isEmpty());
        assertEquals("42", actualToInstitutionManagementResponseResult.getOriginId());
        assertNull(actualToInstitutionManagementResponseResult.getCreatedAt());
        assertEquals("42", actualToInstitutionManagementResponseResult.getExternalId());
        assertEquals(Origin.MOCK.name(), actualToInstitutionManagementResponseResult.getOrigin());
        assertEquals("42", actualToInstitutionManagementResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionManagementResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionManagementResponseResult.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something",
                actualToInstitutionManagementResponseResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionManagementResponseResult.getDigitalAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagementResponse(Institution)}
     */
    @Test
    void testToInstitutionManagementResponse5() {
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

        Institution institution = createDmmyInstitution();
        institution.setOnboarding(onboardingList);
        institution.setImported(true);

        InstitutionManagementResponse actualToInstitutionManagementResponseResult = InstitutionMapperCustom
                .toInstitutionManagementResponse(institution);
        assertEquals("42 Main St", actualToInstitutionManagementResponseResult.getAddress());
        assertTrue(actualToInstitutionManagementResponseResult.isImported());
        assertEquals(institution.getZipCode(), actualToInstitutionManagementResponseResult.getZipCode());
        assertNull(actualToInstitutionManagementResponseResult.getUpdatedAt());
        assertEquals(institution.getTaxCode(), actualToInstitutionManagementResponseResult.getTaxCode());
        assertEquals(institution.getSupportPhone(), actualToInstitutionManagementResponseResult.getSupportPhone());
        assertEquals(institution.getSupportEmail(), actualToInstitutionManagementResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionManagementResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionManagementResponseResult.getRea());
        Map<String, ProductsManagement> products = actualToInstitutionManagementResponseResult.getProducts();
        assertEquals(1, products.size());
        assertNull(actualToInstitutionManagementResponseResult.getCreatedAt());
        assertEquals("42", actualToInstitutionManagementResponseResult.getExternalId());
        assertEquals("42", actualToInstitutionManagementResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionManagementResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionManagementResponseResult.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something",
                actualToInstitutionManagementResponseResult.getDescription());
        assertEquals(Origin.MOCK.name(), actualToInstitutionManagementResponseResult.getOrigin());
        assertEquals("42", actualToInstitutionManagementResponseResult.getOriginId());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagementResponse(Institution)}
     */
    @Test
    void testToInstitutionManagementResponse6() {
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));
        Institution institution = createDmmyInstitution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setImported(true);
        institution.setOnboarding(List.of());

        InstitutionManagementResponse actualToInstitutionManagementResponseResult = InstitutionMapperCustom
                .toInstitutionManagementResponse(institution);
        assertEquals("42 Main St", actualToInstitutionManagementResponseResult.getAddress());
        assertTrue(actualToInstitutionManagementResponseResult.isImported());
        assertEquals(institution.getZipCode(), actualToInstitutionManagementResponseResult.getZipCode());
        assertNull(actualToInstitutionManagementResponseResult.getUpdatedAt());
        assertEquals(institution.getTaxCode(), actualToInstitutionManagementResponseResult.getTaxCode());
        assertEquals(institution.getSupportPhone(), actualToInstitutionManagementResponseResult.getSupportPhone());
        assertEquals(institution.getSupportEmail(), actualToInstitutionManagementResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionManagementResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionManagementResponseResult.getRea());
        assertTrue(actualToInstitutionManagementResponseResult.getProducts().isEmpty());
        assertEquals("42", actualToInstitutionManagementResponseResult.getOriginId());
        assertNull(actualToInstitutionManagementResponseResult.getCreatedAt());
        assertEquals("42", actualToInstitutionManagementResponseResult.getExternalId());
        List<GeoTaxonomies> geographicTaxonomies = actualToInstitutionManagementResponseResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies.size());
        assertEquals("42", actualToInstitutionManagementResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionManagementResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionManagementResponseResult.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something",
                actualToInstitutionManagementResponseResult.getDescription());
        assertEquals(Origin.MOCK.name(), actualToInstitutionManagementResponseResult.getOrigin());
        assertEquals("42 Main St", actualToInstitutionManagementResponseResult.getDigitalAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagementResponse(Institution)}
     */
    @Test
    void testToInstitutionManagementResponse7() {
        Attributes attributes = new Attributes();
        attributes.setCode("Code");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Origin");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);

        Institution institution = createDmmyInstitution();
        institution.setAttributes(attributesList);
        institution.setImported(true);
        institution.setOnboarding(List.of());

        InstitutionManagementResponse actualToInstitutionManagementResponseResult = InstitutionMapperCustom
                .toInstitutionManagementResponse(institution);
        assertEquals("42 Main St", actualToInstitutionManagementResponseResult.getAddress());
        assertTrue(actualToInstitutionManagementResponseResult.isImported());
        assertEquals(institution.getZipCode(), actualToInstitutionManagementResponseResult.getZipCode());
        assertNull(actualToInstitutionManagementResponseResult.getUpdatedAt());
        assertEquals(institution.getTaxCode(), actualToInstitutionManagementResponseResult.getTaxCode());
        assertEquals(institution.getSupportPhone(), actualToInstitutionManagementResponseResult.getSupportPhone());
        assertEquals(institution.getSupportEmail(), actualToInstitutionManagementResponseResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionManagementResponseResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionManagementResponseResult.getRea());
        assertTrue(actualToInstitutionManagementResponseResult.getProducts().isEmpty());
        assertEquals("42", actualToInstitutionManagementResponseResult.getOriginId());
        List<AttributesResponse> attributes1 = actualToInstitutionManagementResponseResult.getAttributes();
        assertEquals(1, attributes1.size());
        assertNull(actualToInstitutionManagementResponseResult.getCreatedAt());
        assertEquals("42", actualToInstitutionManagementResponseResult.getExternalId());
        assertEquals("42", actualToInstitutionManagementResponseResult.getId());
        assertEquals(InstitutionType.PA, actualToInstitutionManagementResponseResult.getInstitutionType());
        assertEquals("Business Register Place", actualToInstitutionManagementResponseResult.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something",
                actualToInstitutionManagementResponseResult.getDescription());
        assertEquals(Origin.MOCK.name(), actualToInstitutionManagementResponseResult.getOrigin());
        assertEquals("42 Main St", actualToInstitutionManagementResponseResult.getDigitalAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionListResponse(List)}
     */
    @Test
    void testToInstitutionListResponse() {
        assertTrue(InstitutionMapperCustom.toInstitutionListResponse(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionListResponse(List)}
     */
    @Test
    void testToInstitutionListResponse2() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        assertEquals(1, InstitutionMapperCustom.toInstitutionListResponse(institutionList).size());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionListResponse(List)}
     */
    @Test
    void testToInstitutionListResponse4() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(InstitutionMapperCustom.toInstitution(new InstitutionRequest(), "42"));
        assertEquals(1, InstitutionMapperCustom.toInstitutionListResponse(institutionList).size());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionListResponse(List)}
     */
    @Test
    void testToInstitutionListResponse5() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(createDmmyInstitution());
        assertEquals(1, InstitutionMapperCustom.toInstitutionListResponse(institutionList).size());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionListResponse(List)}
     */
    @Test
    void testToInstitutionListResponse6() {
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

        Institution institution = createDmmyInstitution();
        institution.setOnboarding(onboardingList);

        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(institution);
        assertEquals(1, InstitutionMapperCustom.toInstitutionListResponse(institutionList).size());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionListResponse(List)}
     */
    @Test
    void testToInstitutionListResponse7() {
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));

        Institution institution = createDmmyInstitution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);

        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(institution);
        assertEquals(1, InstitutionMapperCustom.toInstitutionListResponse(institutionList).size());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionListResponse(List)}
     */
    @Test
    void testToInstitutionListResponse8() {
        Attributes attributes = new Attributes();
        attributes.setCode("Code");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Origin");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);

        Institution institution = createDmmyInstitution();
        institution.setAttributes(attributesList);

        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(institution);
        assertEquals(1, InstitutionMapperCustom.toInstitutionListResponse(institutionList).size());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionAttributeResponse(List, String)}
     */
    @Test
    void testToInstitutionAttributeResponse2() {
        Attributes attributes = new Attributes();
        attributes.setCode("Attributes for institution %s not found");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Attributes for institution %s not found");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);
        List<AttributesResponse> actualToInstitutionAttributeResponseResult = InstitutionMapperCustom
                .toInstitutionAttributeResponse(attributesList, "42");
        assertEquals(1, actualToInstitutionAttributeResponseResult.size());
        AttributesResponse getResult = actualToInstitutionAttributeResponseResult.get(0);
        assertEquals("Attributes for institution %s not found", getResult.getCode());
        assertEquals("Attributes for institution %s not found", getResult.getOrigin());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }
}

