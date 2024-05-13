package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Contract;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class TestUtils {


    public static Institution dummyInstitutionPa() {
        return dummyInstitution(InstitutionType.PA);
    }
    public static Institution dummyInstitutionGsp() {
        return dummyInstitution(InstitutionType.GSP);
    }

    public static Institution dummyInstitutionSa() {
        return dummyInstitution(InstitutionType.SA);
    }

    public static Institution dummyInstitutionAs() {
        return dummyInstitution(InstitutionType.AS);
    }
    public static Institution dummyInstitutionPt() {
        return dummyInstitution(InstitutionType.PT);
    }

    public static Institution dummyInstitutionPg() {
        return dummyInstitution(InstitutionType.PG);
    }

    private static Institution dummyInstitution(InstitutionType institutionType) {

        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();

        return new Institution("42", "42", Origin.SELC.name(), "START - setupCommonData",
                "The characteristics of someone or something", institutionType, "42 Main St", "42 Main St", "21654",
                "TaxCode","ivass", "taxCodeSfe", "city", "county", "country", "istatCode", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null, "START - setupCommonData", "START - setupCommonData",
                "START - setupCommonData", true, OffsetDateTime.now(), OffsetDateTime.now(), null, null, null, null, new PaAttributes(),false);
    }

    public static OnboardingRequest dummyOnboardingRequest(Billing billing, Contract contract, InstitutionUpdate institutionUpdate){
       OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        return onboardingRequest;
    }
}
