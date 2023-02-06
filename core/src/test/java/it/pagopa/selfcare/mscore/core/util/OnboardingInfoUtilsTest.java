package it.pagopa.selfcare.mscore.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class OnboardingInfoUtilsTest {

    /**
     * Method under test: {@link OnboardingInfoUtils#convertStatesToRelationshipsState(String[])}
     */
    @Test
    void testConvertStatesToRelationshipsState2() {
        assertTrue(OnboardingInfoUtils.convertStatesToRelationshipsState(new String[]{}).isEmpty());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getUserInstitutionsWithProductStatusIn(Map, List)}
     */
    @Test
    void testGetUserInstitutionsWithProductStatusIn() {
        HashMap<String, Map<String, Product>> userInstitutionToBeFiltered = new HashMap<>();
        assertThrows(ResourceNotFoundException.class, () -> OnboardingInfoUtils
                .getUserInstitutionsWithProductStatusIn(userInstitutionToBeFiltered, new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getUserInstitutionsWithProductStatusIn(Map, List)}
     */
    @Test
    void testGetUserInstitutionsWithProductStatusIn2() {
        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put("No onboarding information found for states {}", new HashMap<>());
        assertThrows(ResourceNotFoundException.class,
                () -> OnboardingInfoUtils.getUserInstitutionsWithProductStatusIn(stringMapMap, new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getUserInstitutionsWithProductStatusIn(Map, List)}
     */
    @Test
    void testGetUserInstitutionsWithProductStatusIn3() {
        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put("Key", new HashMap<>());
        stringMapMap.put("No onboarding information found for states {}", new HashMap<>());
        assertThrows(ResourceNotFoundException.class,
                () -> OnboardingInfoUtils.getUserInstitutionsWithProductStatusIn(stringMapMap, new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getUserInstitutionsWithProductStatusIn(Map, List)}
     */
    @Test
    void testGetUserInstitutionsWithProductStatusIn4() {
        Product product = new Product();
        product.setContract("No onboarding information found for states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("No onboarding information found for states {}", product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put("No onboarding information found for states {}", stringProductMap);
        assertThrows(ResourceNotFoundException.class,
                () -> OnboardingInfoUtils.getUserInstitutionsWithProductStatusIn(stringMapMap, new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getUserInstitutionsWithProductStatusIn(Map, List)}
     */
    @Test
    void testGetUserInstitutionsWithProductStatusIn6() {
        Product product = new Product();
        product.setContract("No onboarding information found for states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        Product product1 = new Product();
        product1.setContract("No onboarding information found for states {}");
        product1.setCreatedAt(null);
        product1.setRoles(new ArrayList<>());
        product1.setStatus(RelationshipState.PENDING);
        product1.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("Key", product1);
        stringProductMap.put("No onboarding information found for states {}", product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put("No onboarding information found for states {}", stringProductMap);
        assertThrows(ResourceNotFoundException.class,
                () -> OnboardingInfoUtils.getUserInstitutionsWithProductStatusIn(stringMapMap, new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getUserInstitutionsWithProductStatusIn(Map, List)}
     */
    @Test
    void testGetUserInstitutionsWithProductStatusIn7() {
        Product product = new Product();
        product.setContract("No onboarding information found for states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("No onboarding information found for states {}", product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put("No onboarding information found for states {}", stringProductMap);

        ArrayList<RelationshipState> relationshipStateList = new ArrayList<>();
        relationshipStateList.add(RelationshipState.PENDING);
        assertEquals(1,
                OnboardingInfoUtils.getUserInstitutionsWithProductStatusIn(stringMapMap, relationshipStateList).size());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#isStatusIn(RelationshipState, List)}
     */
    @Test
    void testIsStatusIn() {
        assertFalse(OnboardingInfoUtils.isStatusIn(RelationshipState.PENDING, new ArrayList<>()));
        assertFalse(OnboardingInfoUtils.isStatusIn(RelationshipState.ACTIVE, new ArrayList<>()));
        assertFalse(OnboardingInfoUtils.isStatusIn(RelationshipState.SUSPENDED, new ArrayList<>()));
        assertFalse(OnboardingInfoUtils.isStatusIn(RelationshipState.DELETED, new ArrayList<>()));
        assertFalse(OnboardingInfoUtils.isStatusIn(RelationshipState.TOBEVALIDATED, new ArrayList<>()));
        assertFalse(OnboardingInfoUtils.isStatusIn(RelationshipState.REJECTED, new ArrayList<>()));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#isStatusIn(RelationshipState, List)}
     */
    @Test
    void testIsStatusIn2() {
        ArrayList<RelationshipState> relationshipStateList = new ArrayList<>();
        relationshipStateList.add(RelationshipState.PENDING);
        assertTrue(OnboardingInfoUtils.isStatusIn(RelationshipState.PENDING, relationshipStateList));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#findOnboardingLinkedToProductWithStateIn(Map, Institution, List)}
     */
    @Test
    void testFindOnboardingLinkedToProductWithStateIn() {
        HashMap<String, Product> productsMap = new HashMap<>();
        Institution onboardedInstitution = new Institution();
        assertTrue(OnboardingInfoUtils
                .findOnboardingLinkedToProductWithStateIn(productsMap, onboardedInstitution, new ArrayList<>())
                .isEmpty());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#findOnboardingLinkedToProductWithStateIn(Map, Institution, List)}
     */
    @Test
    void testFindOnboardingLinkedToProductWithStateIn3() {
        Product product = new Product();
        product.setContract("Contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("Key", product);

        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        assertTrue(
                OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(stringProductMap, institution, new ArrayList<>())
                        .isEmpty());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#findOnboardingLinkedToProductWithStateIn(Map, Institution, List)}
     */
    @Test
    void testFindOnboardingLinkedToProductWithStateIn5() {
        Product product = new Product();
        product.setContract("Contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        Product product1 = new Product();
        product1.setContract("Contract");
        product1.setCreatedAt(null);
        product1.setRoles(new ArrayList<>());
        product1.setStatus(RelationshipState.PENDING);
        product1.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("42", product1);
        stringProductMap.put("Key", product);

        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        assertTrue(
                OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(stringProductMap, institution, new ArrayList<>())
                        .isEmpty());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#findOnboardingLinkedToProductWithStateIn(Map, Institution, List)}
     */
    @Test
    void testFindOnboardingLinkedToProductWithStateIn6() {
        Product product = new Product();
        product.setContract("Contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("Key", product);

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
        assertTrue(
                OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(stringProductMap, institution, new ArrayList<>())
                        .isEmpty());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#findOnboardingLinkedToProductWithStateIn(Map, Institution, List)}
     */
    @Test
    void testFindOnboardingLinkedToProductWithStateIn7() {
        Product product = new Product();
        product.setContract("Contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("Key", product);

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
        onboarding1.setPremium(premium1);
        onboarding1.setPricingPlan("Pricing Plan");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        assertTrue(
                OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(stringProductMap, institution, new ArrayList<>())
                        .isEmpty());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#findOnboardingLinkedToProductWithStateIn(Map, Institution, List)}
     */
    @Test
    void testFindOnboardingLinkedToProductWithStateIn8() {
        Product product = new Product();
        product.setContract("Contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        Product product1 = new Product();
        product1.setContract("Contract");
        product1.setCreatedAt(null);
        product1.setRoles(new ArrayList<>());
        product1.setStatus(RelationshipState.PENDING);
        product1.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put("42", product1);
        stringProductMap.put("Key", product);

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
        assertTrue(
                OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(stringProductMap, institution, new ArrayList<>())
                        .isEmpty());
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getRelationShipStateList(String[])}
     */
    @Test
    void testGetRelationShipStateList2() {
        List<RelationshipState> actualRelationShipStateList = OnboardingInfoUtils.getRelationShipStateList(null);
        assertEquals(2, actualRelationShipStateList.size());
        assertEquals(RelationshipState.ACTIVE, actualRelationShipStateList.get(0));
        assertEquals(RelationshipState.PENDING, actualRelationShipStateList.get(1));
    }

    /**
     * Method under test: {@link OnboardingInfoUtils#getRelationShipStateList(String[])}
     */
    @Test
    void testGetRelationShipStateList3() {
        List<RelationshipState> actualRelationShipStateList = OnboardingInfoUtils
                .getRelationShipStateList(new String[]{});
        assertEquals(2, actualRelationShipStateList.size());
        assertEquals(RelationshipState.ACTIVE, actualRelationShipStateList.get(0));
        assertEquals(RelationshipState.PENDING, actualRelationShipStateList.get(1));
    }
}

