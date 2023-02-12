package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionProduct;
import it.pagopa.selfcare.mscore.web.model.institution.ProductState;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class OnboardedProductMapperTest {
    /**
     * Method under test: {@link ProductMapper#toOnboardedProducts(List)}
     */
    @Test
    void testToOnboardedProducts() {
        assertTrue(ProductMapper.toOnboardedProducts(new ArrayList<>()).getProducts().isEmpty());
    }

    /**
     * Method under test: {@link ProductMapper#toResource(Onboarding)}
     */
    @Test
    void testToResource() {
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
        InstitutionProduct actualToResourceResult = ProductMapper.toResource(onboarding);
        assertEquals("42", actualToResourceResult.getId());
        assertEquals(ProductState.PENDING, actualToResourceResult.getState());
    }
}

