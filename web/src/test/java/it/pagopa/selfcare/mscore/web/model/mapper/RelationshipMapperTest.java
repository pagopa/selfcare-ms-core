package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelationshipMapperTest {

    @Test
    void testToRelationshipResult12() {
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
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
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(RelationshipInfo)}
     */
    @Test
    void testToRelationshipResult13() {
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        geographicTaxonomiesList.add(geographicTaxonomies);
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();

        Institution institution = new Institution("42", "42", Origin.SELC.name(), "Ipa Code", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboarding,
                geographicTaxonomiesList, attributes, paymentServiceProvider, dataProtectionOfficer, null, null, "Rea",
                "Share Capital", "Business Register Place",true, OffsetDateTime.now(), OffsetDateTime.now(), "BB123", "UO","AA123");
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
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        ProductInfo product = actualToRelationshipResultResult.getProduct();
        assertNull(product.getCreatedAt());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
        assertNull(product.getId());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(RelationshipInfo)}
     */
    @Test
    void testToRelationshipResult14() {
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
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
        assertNull(institutionUpdate.getInstitutionType());
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResult(RelationshipInfo)}
     */
    @Test
    void testToRelationshipResult15() {
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");
        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
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
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
        assertNull(institutionUpdate.getDigitalAddress());
        assertNull(institutionUpdate.getDescription());
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
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
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
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        geographicTaxonomiesList.add(geographicTaxonomies);
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();

        Institution institution = new Institution("42", "42", Origin.SELC.name(), "Ipa Code", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboarding,
                geographicTaxonomiesList, attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "Rea",
                "Share Capital", "Business Register Place",true, OffsetDateTime.now(), OffsetDateTime.now(),"BB123", "UO","AA123");
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
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
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
        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setDesc("The characteristics of someone or something");

        List<InstitutionGeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
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
