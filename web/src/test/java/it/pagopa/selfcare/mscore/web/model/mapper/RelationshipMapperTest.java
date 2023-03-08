package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelationshipMapperTest {

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(Token, Institution)}
     */
    @Test
    void testToRelationshipResult2() {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContractTemplate("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate(OffsetDateTime.now());
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        RelationshipResponse actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(token, null);
        assertNull(actualToRelationshipResultResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualToRelationshipResultResult.getState());
        assertEquals("42", actualToRelationshipResultResult.getProduct());
        assertEquals("42", actualToRelationshipResultResult.getId());
        assertNull(actualToRelationshipResultResult.getCreatedAt());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(Token, Institution)}
     */
    @Test
    void testToRelationshipResult3() {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContractTemplate("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate(OffsetDateTime.now());
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());

        Institution institution = new Institution();
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        RelationshipResponse actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(token,
                institution);
        assertNull(actualToRelationshipResultResult.getUpdatedAt());
        assertNull(actualToRelationshipResultResult.getTo());
        assertEquals(RelationshipState.PENDING, actualToRelationshipResultResult.getState());
        assertEquals("42", actualToRelationshipResultResult.getProduct());
        assertEquals("42", actualToRelationshipResultResult.getId());
        assertNull(actualToRelationshipResultResult.getCreatedAt());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResultResult.getInstitutionUpdate();
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
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(Token, Institution)}
     */
    @Test
    void testToRelationshipResult4() {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContractTemplate("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate(OffsetDateTime.now());
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        assertThrows(InvalidRequestException.class,
                () -> RelationshipMapper.toRelationshipResult(token,
                        new Institution("42", "42", Origin.SELC, "Ipa Code", "The characteristics of someone or something", InstitutionType.PA,
                                "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboarding, geographicTaxonomies,
                                attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "Rea", "Share Capital",
                                "Business Register Place",true, OffsetDateTime.now(), OffsetDateTime.now())));
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(Token, Institution)}
     */
    @Test
    void testToRelationshipResult5() {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContractTemplate("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate(OffsetDateTime.now());
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());

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
        Billing billing1 = new Billing();
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        RelationshipResponse actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(token,
                new Institution("42", "42", Origin.SELC, "Ipa Code", "The characteristics of someone or something", InstitutionType.PA,
                        "42 Main St", "42 Main St", "21654", "Tax Code", billing1, onboardingList, geographicTaxonomiesList,
                        attributes, paymentServiceProvider, dataProtectionOfficer, null, null, "Rea", "Share Capital",
                        "Business Register Place",true, OffsetDateTime.now(), OffsetDateTime.now()));
        assertNull(actualToRelationshipResultResult.getUpdatedAt());
        assertNull(actualToRelationshipResultResult.getCreatedAt());
        assertEquals("42", actualToRelationshipResultResult.getId());
        assertEquals("42", actualToRelationshipResultResult.getTo());
        assertEquals(RelationshipState.PENDING, actualToRelationshipResultResult.getState());
        assertEquals("42", actualToRelationshipResultResult.getProduct());
        assertEquals("Pricing Plan", actualToRelationshipResultResult.getPricingPlan());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResultResult.getInstitutionUpdate();
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        BillingResponse billingResponse = actualToRelationshipResultResult.getBillingResponse();
        assertTrue(billingResponse.isPublicServices());
        assertEquals("Recipient Code", billingResponse.getRecipientCode());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertEquals("42", billingResponse.getVatNumber());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("42 Main St", institutionUpdate.getAddress());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(Token, Institution)}
     */
    @Test
    void testToRelationshipResult6() {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContractTemplate("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate(OffsetDateTime.now());
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Premium premium1 = new Premium();
        premium1.setContract("Contract");
        premium1.setStatus(RelationshipState.PENDING);

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1.setContract("Contract");
        onboarding1.setCreatedAt(null);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("Product Id");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        Billing billing2 = new Billing();
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        RelationshipResponse actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(token,
                new Institution("42", "42", Origin.SELC, "Ipa Code", "The characteristics of someone or something", InstitutionType.PA,
                        "42 Main St", "42 Main St", "21654", "Tax Code", billing2, onboardingList, geographicTaxonomiesList,
                        attributes, paymentServiceProvider, dataProtectionOfficer, null, null, "Rea", "Share Capital",
                        "Business Register Place",true, OffsetDateTime.now(), OffsetDateTime.now()));
        assertNull(actualToRelationshipResultResult.getUpdatedAt());
        assertNull(actualToRelationshipResultResult.getCreatedAt());
        assertEquals("42", actualToRelationshipResultResult.getId());
        assertEquals("42", actualToRelationshipResultResult.getTo());
        assertEquals(RelationshipState.PENDING, actualToRelationshipResultResult.getState());
        assertEquals("42", actualToRelationshipResultResult.getProduct());
        assertEquals("Pricing Plan", actualToRelationshipResultResult.getPricingPlan());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResultResult.getInstitutionUpdate();
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        BillingResponse billingResponse = actualToRelationshipResultResult.getBillingResponse();
        assertTrue(billingResponse.isPublicServices());
        assertEquals("Recipient Code", billingResponse.getRecipientCode());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertEquals("42", billingResponse.getVatNumber());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("42 Main St", institutionUpdate.getAddress());
    }


    @Test
    void testToRelationshipResult12() {
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
        institution.setOnboarding(new ArrayList<>());
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        Institution institution1 = new Institution();

        RelationshipInfo relationshipInfo = new RelationshipInfo(institution1, "42", new OnboardedProduct());
        relationshipInfo.setInstitution(institution);
        RelationshipResult actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(relationshipInfo);
        assertNull(actualToRelationshipResultResult.getTo());
        assertNull(actualToRelationshipResultResult.getState());
        assertNull(actualToRelationshipResultResult.getRole());
        assertNull(actualToRelationshipResultResult.getId());
        assertEquals("42", actualToRelationshipResultResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResultResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        ProductInfo product = actualToRelationshipResultResult.getProduct();
        assertNull(product.getId());
        assertNull(product.getCreatedAt());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(RelationshipInfo)}
     */
    @Test
    void testToRelationshipResult13() {
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
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies1 = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();

        Institution institution = new Institution("42", "42", Origin.SELC, "Ipa Code", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboarding,
                geographicTaxonomies1, attributes, paymentServiceProvider, dataProtectionOfficer, null, null, "Rea",
                "Share Capital", "Business Register Place",true, OffsetDateTime.now(), OffsetDateTime.now());
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        Institution institution1 = new Institution();

        RelationshipInfo relationshipInfo = new RelationshipInfo(institution1, "42", new OnboardedProduct());
        relationshipInfo.setInstitution(institution);
        RelationshipResult actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(relationshipInfo);
        assertEquals("42", actualToRelationshipResultResult.getTo());
        assertNull(actualToRelationshipResultResult.getState());
        assertNull(actualToRelationshipResultResult.getRole());
        assertNull(actualToRelationshipResultResult.getId());
        assertEquals("42", actualToRelationshipResultResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResultResult.getInstitutionUpdate();
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        ProductInfo product = actualToRelationshipResultResult.getProduct();
        assertNull(product.getCreatedAt());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
        assertNull(product.getId());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(RelationshipInfo)}
     */
    @Test
    void testToRelationshipResult14() {
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
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        Institution institution1 = new Institution();

        RelationshipInfo relationshipInfo = new RelationshipInfo(institution1, "42", new OnboardedProduct());
        relationshipInfo.setInstitution(institution);
        RelationshipResult actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(relationshipInfo);
        assertNull(actualToRelationshipResultResult.getTo());
        assertNull(actualToRelationshipResultResult.getState());
        assertNull(actualToRelationshipResultResult.getRole());
        assertNull(actualToRelationshipResultResult.getId());
        assertEquals("42", actualToRelationshipResultResult.getFrom());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResultResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        ProductInfo product = actualToRelationshipResultResult.getProduct();
        assertNull(product.getId());
        assertNull(product.getCreatedAt());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(RelationshipInfo)}
     */
    @Test
    void testToRelationshipResult15() {
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
        institution.setGeographicTaxonomies(geographicTaxonomiesList);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("42");

        RelationshipInfo relationshipInfo = new RelationshipInfo(new Institution(), "42", onboardedProduct);
        relationshipInfo.setInstitution(institution);
        RelationshipResult actualToRelationshipResultResult = RelationshipMapper.toRelationshipResult(relationshipInfo);
        assertNull(actualToRelationshipResultResult.getId());
        assertNull(actualToRelationshipResultResult.getRole());
        assertNull(actualToRelationshipResultResult.getTo());
        assertNull(actualToRelationshipResultResult.getState());
        assertEquals("42", actualToRelationshipResultResult.getFrom());
        assertEquals("Pricing Plan", actualToRelationshipResultResult.getPricingPlan());
        ProductInfo product = actualToRelationshipResultResult.getProduct();
        assertEquals("42", product.getId());
        assertNull(product.getCreatedAt());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResultResult.getInstitutionUpdate();
        assertNull(institutionUpdate.getZipCode());
        assertNull(institutionUpdate.getTaxCode());
        assertNull(institutionUpdate.getSupportPhone());
        assertNull(institutionUpdate.getSupportEmail());
        assertNull(institutionUpdate.getShareCapital());
        assertNull(institutionUpdate.getRea());
        assertNull(institutionUpdate.getPaymentServiceProvider());
        assertNull(institutionUpdate.getInstitutionType());
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
        assertNull(institutionUpdate.getDataProtectionOfficer());
        assertNull(institutionUpdate.getBusinessRegisterPlace());
        assertNull(institutionUpdate.getAddress());
        BillingResponse billing1 = actualToRelationshipResultResult.getBilling();
        assertEquals("42", billing1.getVatNumber());
        assertEquals("Recipient Code", billing1.getRecipientCode());
        assertTrue(billing1.isPublicServices());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResultList(List)}
     */
    @Test
    void testToRelationshipResultList() {
        assertTrue(RelationshipMapper.toRelationshipResultList(new ArrayList<>()).isEmpty());
    }


    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResultList(List)}
     */
    @Test
    void testToRelationshipResultList6() {
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
        institution.setOnboarding(new ArrayList<>());
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        Institution institution1 = new Institution();

        RelationshipInfo relationshipInfo = new RelationshipInfo(institution1, "42", new OnboardedProduct());
        relationshipInfo.setInstitution(institution);

        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        relationshipInfoList.add(relationshipInfo);
        assertEquals(1, RelationshipMapper.toRelationshipResultList(relationshipInfoList).size());
        assertFalse(relationshipInfoList.get(0).getInstitution().isImported());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResultList(List)}
     */
    @Test
    void testToRelationshipResultList7() {
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
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies1 = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();

        Institution institution = new Institution("42", "42", Origin.SELC, "Ipa Code", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboarding,
                geographicTaxonomies1, attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "Rea",
                "Share Capital", "Business Register Place",true, OffsetDateTime.now(), OffsetDateTime.now());
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        Institution institution1 = new Institution();

        RelationshipInfo relationshipInfo = new RelationshipInfo(institution1, "42", new OnboardedProduct());
        relationshipInfo.setInstitution(institution);

        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        relationshipInfoList.add(relationshipInfo);
        assertEquals(1, RelationshipMapper.toRelationshipResultList(relationshipInfoList).size());
        assertTrue(relationshipInfoList.get(0).getInstitution().isImported());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResultList(List)}
     */
    @Test
    void testToRelationshipResultList8() {
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
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        Institution institution1 = new Institution();

        RelationshipInfo relationshipInfo = new RelationshipInfo(institution1, "42", new OnboardedProduct());
        relationshipInfo.setInstitution(institution);

        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        relationshipInfoList.add(relationshipInfo);
        assertEquals(1, RelationshipMapper.toRelationshipResultList(relationshipInfoList).size());
        assertFalse(relationshipInfoList.get(0).getInstitution().isImported());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResultList(List)}
     */
    @Test
    void testToRelationshipResultList9() {
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
        institution.setGeographicTaxonomies(geographicTaxonomiesList);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("42");

        RelationshipInfo relationshipInfo = new RelationshipInfo(new Institution(), "42", onboardedProduct);
        relationshipInfo.setInstitution(institution);

        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        relationshipInfoList.add(relationshipInfo);
        assertEquals(1, RelationshipMapper.toRelationshipResultList(relationshipInfoList).size());
        assertFalse(relationshipInfoList.get(0).getInstitution().isImported());
    }

}
