package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
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
    void testToRelationshipResult3() {
        Token token = new Token();
        token.setChecksum("Checksum");
        
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
