package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.web.TestUtils;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MigrationInstitutionMapperCustomTest {

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse2() {
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

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setClosedAt(null);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setTokenId("42");
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setPaymentServiceProvider(null);
        institution.setDataProtectionOfficer(null);
        institution.setOnboarding(onboardingList);
        institution.setBilling(null);

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
        productManagerInfo.setInstitution(institution);
        productManagerInfo.setProducts(onboardedProductList);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getInstitutionType());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse3() {
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

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setClosedAt(null);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setTokenId("42");
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setPaymentServiceProvider(null);
        institution.setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institution.setOnboarding(onboardingList);
        institution.setBilling(null);

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
        productManagerInfo.setInstitution(institution);
        productManagerInfo.setProducts(onboardedProductList);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getInstitutionType());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getDescription());
        DataProtectionOfficerResponse dataProtectionOfficer = institutionUpdate.getDataProtectionOfficer();
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse4() {
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

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setClosedAt(null);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setTokenId("42");
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institution.setDataProtectionOfficer(null);
        institution.setOnboarding(onboardingList);
        institution.setBilling(null);

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
        productManagerInfo.setInstitution(institution);
        productManagerInfo.setProducts(onboardedProductList);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getInstitutionType());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getDescription());
        PaymentServiceProviderResponse paymentServiceProvider = institutionUpdate.getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertTrue(paymentServiceProvider.isVatNumberGroup());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
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

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setClosedAt(null);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setTokenId("42");
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setPaymentServiceProvider(null);
        institution.setDataProtectionOfficer(null);
        institution.setOnboarding(onboardingList);
        institution.setBilling(null);

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
        productManagerInfo.setInstitution(institution);
        productManagerInfo.setProducts(onboardedProductList);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getInstitutionType());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse9() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());

        ProductManagerInfo productManagerInfo = new ProductManagerInfo("id", institution, new ArrayList<>());
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "42");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getZipCode());
    }

    @Test
    void testToInstitutionManagerResponse10() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        List<OnboardedProduct> onboardedProducts = new ArrayList<>();
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("42");
        onboardedProducts.add(onboardedProduct);
        ProductManagerInfo productManagerInfo = new ProductManagerInfo("id", institution, onboardedProducts);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "42");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getInstitutionType());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getZipCode());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse12() {
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));

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

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setClosedAt(null);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setTokenId("42");
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setPaymentServiceProvider(null);
        institution.setDataProtectionOfficer(null);
        institution.setOnboarding(onboardingList);
        institution.setBilling(null);

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
        productManagerInfo.setInstitution(institution);
        productManagerInfo.setProducts(onboardedProductList);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getInstitutionType());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getDigitalAddress());
        List<String> geographicTaxonomyCodes = institutionUpdate.getGeographicTaxonomyCodes();
        assertEquals(1, geographicTaxonomyCodes.size());
        assertEquals("Code", geographicTaxonomyCodes.get(0));
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionManagerResponse(ProductManagerInfo, String)}
     */
    @Test
    void testToInstitutionManagerResponse13() {
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));

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

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setClosedAt(null);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setTokenId("42");
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setPaymentServiceProvider(null);
        institution.setDataProtectionOfficer(null);
        institution.setOnboarding(onboardingList);
        institution.setBilling(null);

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
        productManagerInfo.setInstitution(institution);
        productManagerInfo.setProducts(onboardedProductList);
        InstitutionManagerResponse actualToInstitutionManagerResponseResult = InstitutionMapperCustom
                .toInstitutionManagerResponse(productManagerInfo, "foo");
        assertNull(actualToInstitutionManagerResponseResult.getTo());
        assertNull(actualToInstitutionManagerResponseResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToInstitutionManagerResponseResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getInstitutionType());
        assertFalse(institutionUpdate.isImported());
        assertNull(institutionUpdate.getAddress());
        assertNull(institutionUpdate.getDigitalAddress());
        List<String> geographicTaxonomyCodes = institutionUpdate.getGeographicTaxonomyCodes();
        assertEquals(2, geographicTaxonomyCodes.size());
        assertEquals("Code", geographicTaxonomyCodes.get(0));
        assertEquals("Code", geographicTaxonomyCodes.get(1));
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToInstitutionBillingResponse2() {
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

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setClosedAt(null);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setTokenId("42");
        onboarding1.setUpdatedAt(null);
        onboarding1.setBilling(null);

        Onboarding onboarding2 = new Onboarding();
        onboarding2.setClosedAt(null);
        onboarding2.setContract("Contract");
        onboarding2.setCreatedAt(null);
        onboarding2.setPricingPlan("Pricing Plan");
        onboarding2.setProductId("42");
        onboarding2.setStatus(RelationshipState.PENDING);
        onboarding2.setTokenId("42");
        onboarding2.setUpdatedAt(null);
        onboarding2.setBilling(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        institution.setBilling(null);
        InstitutionBillingResponse actualToInstitutionBillingResponseResult = InstitutionMapperCustom
                .toInstitutionBillingResponse(institution, "foo");
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
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdateResponse(Institution)}
     */
    @Test
    void testToInstitutionUpdateResponse() {
        Institution institution = new Institution();
        InstitutionUpdateResponse actualToInstitutionUpdateResponseResult = InstitutionMapperCustom
                .toInstitutionUpdateResponse(institution);
        assertNull(actualToInstitutionUpdateResponseResult.getAddress());
        assertNull(actualToInstitutionUpdateResponseResult.getZipCode());
        assertNull(actualToInstitutionUpdateResponseResult.getTaxCode());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportPhone());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportEmail());
        assertNull(actualToInstitutionUpdateResponseResult.getShareCapital());
        assertNull(actualToInstitutionUpdateResponseResult.getRea());
        assertNull(actualToInstitutionUpdateResponseResult.getInstitutionType());
        assertNull(actualToInstitutionUpdateResponseResult.getBusinessRegisterPlace());
        assertNull(actualToInstitutionUpdateResponseResult.getDescription());
        assertNull(actualToInstitutionUpdateResponseResult.getDigitalAddress());
        assertFalse(institution.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdateResponse(Institution)}
     */
    @Test
    void testToInstitutionUpdateResponse2() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setPaymentServiceProvider(null);
        institution.setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        InstitutionUpdateResponse actualToInstitutionUpdateResponseResult = InstitutionMapperCustom
                .toInstitutionUpdateResponse(institution);
        assertNull(actualToInstitutionUpdateResponseResult.getAddress());
        assertNull(actualToInstitutionUpdateResponseResult.getZipCode());
        assertNull(actualToInstitutionUpdateResponseResult.getTaxCode());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportPhone());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportEmail());
        assertNull(actualToInstitutionUpdateResponseResult.getShareCapital());
        assertNull(actualToInstitutionUpdateResponseResult.getRea());
        assertNull(actualToInstitutionUpdateResponseResult.getInstitutionType());
        assertNull(actualToInstitutionUpdateResponseResult.getBusinessRegisterPlace());
        assertNull(actualToInstitutionUpdateResponseResult.getDescription());
        assertNull(actualToInstitutionUpdateResponseResult.getDigitalAddress());
        DataProtectionOfficerResponse dataProtectionOfficer = actualToInstitutionUpdateResponseResult
                .getDataProtectionOfficer();
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
        assertFalse(institution.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdateResponse(Institution)}
     */
    @Test
    void testToInstitutionUpdateResponse3() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institution.setDataProtectionOfficer(null);
        InstitutionUpdateResponse actualToInstitutionUpdateResponseResult = InstitutionMapperCustom
                .toInstitutionUpdateResponse(institution);
        assertNull(actualToInstitutionUpdateResponseResult.getAddress());
        assertNull(actualToInstitutionUpdateResponseResult.getZipCode());
        assertNull(actualToInstitutionUpdateResponseResult.getTaxCode());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportPhone());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportEmail());
        assertNull(actualToInstitutionUpdateResponseResult.getShareCapital());
        assertNull(actualToInstitutionUpdateResponseResult.getRea());
        assertNull(actualToInstitutionUpdateResponseResult.getInstitutionType());
        assertNull(actualToInstitutionUpdateResponseResult.getBusinessRegisterPlace());
        assertNull(actualToInstitutionUpdateResponseResult.getDescription());
        assertNull(actualToInstitutionUpdateResponseResult.getDigitalAddress());
        PaymentServiceProviderResponse paymentServiceProvider = actualToInstitutionUpdateResponseResult
                .getPaymentServiceProvider();
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertTrue(paymentServiceProvider.isVatNumberGroup());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertFalse(institution.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdateResponse(Institution)}
     */
    @Test
    void testToInstitutionUpdateResponse4() {

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setImported(true);

        InstitutionUpdateResponse actualToInstitutionUpdateResponseResult = InstitutionMapperCustom
                .toInstitutionUpdateResponse(institution);
        assertEquals("42 Main St", actualToInstitutionUpdateResponseResult.getAddress());
        assertEquals("21654", actualToInstitutionUpdateResponseResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionUpdateResponseResult.getTaxCode());
        assertEquals(InstitutionType.PA, actualToInstitutionUpdateResponseResult.getInstitutionType());
        assertEquals("The characteristics of someone or something",
                actualToInstitutionUpdateResponseResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionUpdateResponseResult.getDigitalAddress());
        assertTrue(institution.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdateResponse(Institution)}
     */
    @Test
    void testToInstitutionUpdateResponse5() {
        Institution institution = new Institution();
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setPaymentServiceProvider(null);
        institution.setDataProtectionOfficer(null);
        InstitutionUpdateResponse actualToInstitutionUpdateResponseResult = InstitutionMapperCustom
                .toInstitutionUpdateResponse(institution);
        assertNull(actualToInstitutionUpdateResponseResult.getAddress());
        assertNull(actualToInstitutionUpdateResponseResult.getZipCode());
        assertNull(actualToInstitutionUpdateResponseResult.getTaxCode());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportPhone());
        assertNull(actualToInstitutionUpdateResponseResult.getSupportEmail());
        assertNull(actualToInstitutionUpdateResponseResult.getShareCapital());
        assertNull(actualToInstitutionUpdateResponseResult.getRea());
        assertNull(actualToInstitutionUpdateResponseResult.getInstitutionType());
        assertNull(actualToInstitutionUpdateResponseResult.getBusinessRegisterPlace());
        assertNull(actualToInstitutionUpdateResponseResult.getDescription());
        assertNull(actualToInstitutionUpdateResponseResult.getDigitalAddress());
        assertFalse(institution.isImported());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdate(InstitutionPut)}
     */
    @Test
    void testToInstitutionUpdate() {
        InstitutionPut institutionPut = new InstitutionPut();
        List<InstitutionGeographicTaxonomies> stringList = new ArrayList<>();
        stringList.add(new InstitutionGeographicTaxonomies());
        institutionPut.setGeographicTaxonomyCodes(stringList.stream().map(InstitutionGeographicTaxonomies::getCode).collect(Collectors.toList()));
        assertEquals(stringList, InstitutionMapperCustom.toInstitutionUpdate(institutionPut).getGeographicTaxonomies());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdate(InstitutionPut)}
     */
    @Test
    void testToInstitutionUpdatePG() {
        InstitutionPut institutionPut = new InstitutionPut();
        institutionPut.setDescription("description");
        institutionPut.setDigitalAddress("digitalAddress");
        assertEquals("description", InstitutionMapperCustom.toInstitutionUpdate( institutionPut).getDescription());
        assertEquals("digitalAddress", InstitutionMapperCustom.toInstitutionUpdate( institutionPut).getDigitalAddress());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdate(InstitutionPut)}
     */
    @Test
    void testToInstitutionUpdate3() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");

        InstitutionPut institutionPut = new InstitutionPut();
        institutionPut.setGeographicTaxonomyCodes(stringList);
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = InstitutionMapperCustom.toInstitutionUpdate(institutionPut)
                .getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies.size());
        InstitutionGeographicTaxonomies getResult = geographicTaxonomies.get(0);
        assertEquals("foo", getResult.getCode());
        assertNull(getResult.getDesc());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionUpdate(InstitutionPut)}
     */
    @Test
    void testToInstitutionUpdate4() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("42");
        stringList.add("foo");

        InstitutionPut institutionPut = new InstitutionPut();
        institutionPut.setGeographicTaxonomyCodes(stringList);
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = InstitutionMapperCustom.toInstitutionUpdate(institutionPut)
                .getGeographicTaxonomies();
        assertEquals(2, geographicTaxonomies.size());
        InstitutionGeographicTaxonomies getResult = geographicTaxonomies.get(1);
        assertNull(getResult.getDesc());
        InstitutionGeographicTaxonomies getResult1 = geographicTaxonomies.get(0);
        assertNull(getResult1.getDesc());
        assertEquals("42", getResult1.getCode());
        assertEquals("foo", getResult.getCode());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse2() {
        assertNull(InstitutionMapperCustom.toInstitutionBillingResponse(null, "foo"));
    }


    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
     */
    @Test
    void testToBillingResponse6() {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        institution.setInstitutionType(InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toInstitutionBillingResponse(institution, "42");
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
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
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
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        institution.setInstitutionType(InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toInstitutionBillingResponse(institution, "42");
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
     * Method under test: {@link InstitutionMapperCustom#toInstitutionBillingResponse(Institution, String)}
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
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        institution.setInstitutionType(InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toInstitutionBillingResponse(institution,
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

    @Test
    void testToBillingResponse9() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setBilling(billing);
        institution.setOnboarding(onboardingList);
        institution.setInstitutionType(InstitutionType.GSP);
        InstitutionBillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toInstitutionBillingResponse(institution, "42");
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
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
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
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "42");
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
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "42");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertFalse(actualToInstitutionResult.isImported());
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
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
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
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "42");
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
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
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
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "42");
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

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution5() {
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
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
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
    void testToInstitution6() {
        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        institutionRequest.setGeographicTaxonomies(null);
        institutionRequest.setPaymentServiceProvider(null);
        institutionRequest.setDataProtectionOfficer(null);
        institutionRequest.setAttributes(null);
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "foo");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("6625550144", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = actualToInstitutionResult.getGeographicTaxonomies();
        assertTrue(geographicTaxonomies.isEmpty());
        assertEquals("foo", actualToInstitutionResult.getExternalId());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution7() {
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
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        institutionRequest.setGeographicTaxonomies(null);
        institutionRequest.setPaymentServiceProvider(null);
        institutionRequest.setDataProtectionOfficer(null);
        institutionRequest.setAttributes(attributesRequestList);
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "foo");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("6625550144", actualToInstitutionResult.getSupportPhone());
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
     * Method under test: {@link InstitutionMapperCustom#toInstitution(InstitutionRequest, String)}
     */
    @Test
    void testToInstitution8() {
        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("Code");
        geoTaxonomies.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        geoTaxonomiesList.add(geoTaxonomies);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setPaymentServiceProvider(null);
        institutionRequest.setDataProtectionOfficer(null);
        institutionRequest.setAttributes(null);
        Institution actualToInstitutionResult = InstitutionMapperCustom.toInstitution(institutionRequest, "foo");
        assertEquals("42 Main St", actualToInstitutionResult.getAddress());
        assertEquals("21654", actualToInstitutionResult.getZipCode());
        assertEquals("Tax Code", actualToInstitutionResult.getTaxCode());
        assertEquals("6625550144", actualToInstitutionResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualToInstitutionResult.getSupportEmail());
        assertEquals("Share Capital", actualToInstitutionResult.getShareCapital());
        assertEquals("Rea", actualToInstitutionResult.getRea());
        assertEquals(InstitutionType.PA, actualToInstitutionResult.getInstitutionType());
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = actualToInstitutionResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies.size());
        assertEquals("foo", actualToInstitutionResult.getExternalId());
        assertTrue(actualToInstitutionResult.getAttributes().isEmpty());
        assertEquals("Business Register Place", actualToInstitutionResult.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", actualToInstitutionResult.getDescription());
        assertEquals("42 Main St", actualToInstitutionResult.getDigitalAddress());
        InstitutionGeographicTaxonomies getResult = geographicTaxonomies.get(0);
        assertEquals("Code", getResult.getCode());
        assertEquals("The characteristics of someone or something", getResult.getDesc());
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
        BillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toBillingResponse(onboarding,
                new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertTrue(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toBillingResponse(Onboarding, Institution)}
     */
    @Test
    void testToBillingResponse3() {
        Billing billing = new Billing();
        billing.setPublicServices(false);
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
        BillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toBillingResponse(onboarding,
                new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertFalse(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    @Test
    void testToBillingResponse4() {
        Billing billing = new Billing();
        billing.setPublicServices(false);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        Institution institution = new Institution();
        institution.setBilling(billing);
        BillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toBillingResponse(onboarding,
                institution);
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertFalse(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toBillingResponse(Onboarding, Institution)}
     */
    @Test
    void testToBillingResponse5() {
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
        BillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toBillingResponse(onboarding,
                new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertTrue(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toBillingResponse(Onboarding, Institution)}
     */
    @Test
    void testToBillingResponse11() {
        Onboarding onboarding = new Onboarding();
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        onboarding.setBilling(null);

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Institution institution = new Institution();
        institution.setBilling(billing);
        BillingResponse actualToBillingResponseResult = InstitutionMapperCustom.toBillingResponse(onboarding, institution);
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertTrue(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#getBillingFromOnboarding(Onboarding, Institution)}
     */
    @Test
    void testGetBillingFromOnboarding2() {
        Onboarding onboarding = new Onboarding();
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        onboarding.setBilling(null);
        assertNull(InstitutionMapperCustom.getBillingFromOnboarding(onboarding, new Institution()));
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
     * Method under test: {@link InstitutionMapperCustom#toPaymentServiceProvider(PaymentServiceProviderRequest)}
     */
    @Test
    void testToPaymentServiceProvider() {
        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);
        PaymentServiceProvider actualToPaymentServiceProviderResult = InstitutionMapperCustom
                .toPaymentServiceProvider(paymentServiceProviderRequest);
        assertEquals("Abi Code", actualToPaymentServiceProviderResult.getAbiCode());
        assertTrue(actualToPaymentServiceProviderResult.isVatNumberGroup());
        assertEquals("42", actualToPaymentServiceProviderResult.getLegalRegisterNumber());
        assertEquals("Legal Register Name", actualToPaymentServiceProviderResult.getLegalRegisterName());
        assertEquals("42", actualToPaymentServiceProviderResult.getBusinessRegisterNumber());
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
        assertTrue(InstitutionMapperCustom.toGeoTaxonomies(null).isEmpty());
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
     * Method under test: {@link InstitutionMapperCustom#toOnboardedProducts(List)}
     */
    @Test
    void testToOnboardedProducts2() {
        assertTrue(InstitutionMapperCustom.toOnboardedProducts(null).getProducts().isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toOnboardedProducts(List)}
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

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        List<InstitutionProduct> products = InstitutionMapperCustom.toOnboardedProducts(onboardingList).getProducts();
        assertEquals(1, products.size());
        InstitutionProduct getResult = products.get(0);
        assertEquals("42", getResult.getId());
        assertEquals(RelationshipState.PENDING, getResult.getState());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toOnboardedProducts(List)}
     */
    @Test
    void testToOnboardedProducts4() {
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
        List<InstitutionProduct> products = InstitutionMapperCustom.toOnboardedProducts(onboardingList).getProducts();
        assertEquals(2, products.size());
        InstitutionProduct getResult = products.get(1);
        assertEquals(RelationshipState.PENDING, getResult.getState());
        InstitutionProduct getResult1 = products.get(0);
        assertEquals(RelationshipState.ACTIVE, getResult1.getState());
        assertEquals("Product Id", getResult1.getId());
        assertEquals("42", getResult.getId());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionProduct(List)}
     */
    @Test
    void testToInstitutionProduct() {
        assertTrue(InstitutionMapperCustom.toInstitutionProduct(new ArrayList<>()).isEmpty());
        assertTrue(InstitutionMapperCustom.toInstitutionProduct(null).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionProduct(List)}
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
        List<InstitutionProduct> actualToInstitutionProductResult = InstitutionMapperCustom
                .toInstitutionProduct(onboardingList);
        assertEquals(1, actualToInstitutionProductResult.size());
        InstitutionProduct getResult = actualToInstitutionProductResult.get(0);
        assertEquals("42", getResult.getId());
        assertEquals(RelationshipState.PENDING, getResult.getState());
    }

    /**
     * Method under test: {@link InstitutionMapperCustom#toInstitutionProduct(List)}
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
        List<InstitutionProduct> actualToInstitutionProductResult = InstitutionMapperCustom
                .toInstitutionProduct(onboardingList);
        assertEquals(2, actualToInstitutionProductResult.size());
        InstitutionProduct getResult = actualToInstitutionProductResult.get(0);
        assertEquals(RelationshipState.ACTIVE, getResult.getState());
        InstitutionProduct getResult1 = actualToInstitutionProductResult.get(1);
        assertEquals(RelationshipState.PENDING, getResult1.getState());
        assertEquals("42", getResult1.getId());
        assertEquals("Product Id", getResult.getId());
    }

}
