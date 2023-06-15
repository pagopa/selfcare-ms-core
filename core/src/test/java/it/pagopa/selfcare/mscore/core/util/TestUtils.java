package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.model.institution.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class TestUtils {


    public static Institution dummyInstitutionPa() {
        return dummyInstitution(InstitutionType.PA);
    }
    public static Institution dummyInstitutionGsp() {
        return dummyInstitution(InstitutionType.GSP);
    }
    public static Institution dummyInstitutionPt() {
        return dummyInstitution(InstitutionType.PT);
    }

    private static Institution dummyInstitution(InstitutionType institutionType) {

        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();

        return new Institution("42", "42", Origin.SELC.name(), "START - setupCommonData",
                "The characteristics of someone or something", institutionType, "42 Main St", "42 Main St", "21654",
                "TaxCode", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null, "START - setupCommonData", "START - setupCommonData",
                "START - setupCommonData", true, OffsetDateTime.now(), OffsetDateTime.now(), null, null, new PaAttributes());
    }
}
