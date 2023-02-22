package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RelationshipMapperTest {

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResponse(Token, Institution)}
     */
    @Test
    void testToRelationshipResponse4() {
        Token token = new Token();

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

        Institution institution = new Institution("42", "42", "Ipa Code", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing, onboarding,
                geographicTaxonomies1, attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "Rea",
                "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true);
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        assertThrows(InvalidRequestException.class, () -> RelationshipMapper.toRelationshipResponse(token, institution));
    }

    /**
     * Method under test: {@link RelationshipMapper#toRelationshipResponse(Token, Institution)}
     */
    @Test
    void testToRelationshipResponse6() {
        Token token = new Token();
        token.setProductId("42");

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
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<GeographicTaxonomies> geographicTaxonomies1 = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();

        Institution institution = new Institution("42", "42", "Ipa Code", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "Tax Code", billing1, onboardingList,
                geographicTaxonomies1, attributes, paymentServiceProvider, dataProtectionOfficer, null, null, "Rea",
                "Share Capital", "Business Register Place", "jane.doe@example.org", "4105551212", true);
        institution.setGeographicTaxonomies(geographicTaxonomiesList);
        RelationshipResponse actualToRelationshipResponseResult = RelationshipMapper.toRelationshipResponse(token,
                institution);
        assertNull(actualToRelationshipResponseResult.getUpdatedAt());
        assertNull(actualToRelationshipResponseResult.getCreatedAt());
        assertNull(actualToRelationshipResponseResult.getId());
        assertEquals("42", actualToRelationshipResponseResult.getTo());
        assertNull(actualToRelationshipResponseResult.getState());
        assertEquals("42", actualToRelationshipResponseResult.getProduct());
        assertNull(actualToRelationshipResponseResult.getFrom());
        assertEquals("Pricing Plan", actualToRelationshipResponseResult.getPricingPlan());
        InstitutionUpdateResponse institutionUpdate = actualToRelationshipResponseResult.getInstitutionUpdate();
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals("4105551212", institutionUpdate.getSupportPhone());
        assertEquals("jane.doe@example.org", institutionUpdate.getSupportEmail());
        assertEquals("Share Capital", institutionUpdate.getShareCapital());
        assertEquals("Rea", institutionUpdate.getRea());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        BillingResponse billingResponse = actualToRelationshipResponseResult.getBillingResponse();
        assertTrue(billingResponse.isPublicServices());
        assertEquals("Recipient Code", billingResponse.getRecipientCode());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertEquals("42", billingResponse.getVatNumber());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals(1, institutionUpdate.getGeographicTaxonomyCodes().size());
        assertEquals("42 Main St", institutionUpdate.getAddress());
    }

    /**
     * Method under test: {@link RelationshipMapper#toBillingResponse(Onboarding, Institution)}
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
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        BillingResponse actualToBillingResponseResult = RelationshipMapper.toBillingResponse(onboarding, new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertTrue(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

    /**
     * Method under test: {@link RelationshipMapper#toBillingResponse(Onboarding, Institution)}
     */
    @Test
    void testToBillingResponse2() {
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
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        BillingResponse actualToBillingResponseResult = RelationshipMapper.toBillingResponse(onboarding,
                new Institution());
        assertEquals("Recipient Code", actualToBillingResponseResult.getRecipientCode());
        assertFalse(actualToBillingResponseResult.isPublicServices());
        assertEquals("42", actualToBillingResponseResult.getVatNumber());
    }

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
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        Institution institution = new Institution();
        institution.setBilling(billing);
        BillingResponse actualToBillingResponseResult = RelationshipMapper.toBillingResponse(onboarding,
                institution);
        assertFalse(actualToBillingResponseResult.isPublicServices());
    }
}

