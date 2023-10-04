package it.pagopa.selfcare.mscore.web;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionRequest;

import java.util.ArrayList;

public class TestUtils {

    public static Billing createSimpleBilling() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        return billing;
    }

    public static DataProtectionOfficer createSimpleDataProtectionOfficer() {

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");
        return dataProtectionOfficer;
    }



    public static PaymentServiceProvider createSimplePaymentServiceProvider() {

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);
        return paymentServiceProvider;
    }

    public static Institution createSimpleInstitutionPA() {
        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(createSimpleBilling());

        institution.setDataProtectionOfficer(createSimpleDataProtectionOfficer());
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOriginId("Ipa Code");
        institution.setOrigin(Origin.MOCK.name());
        institution.setOnboarding(new ArrayList<>());

        institution.setPaymentServiceProvider(createSimplePaymentServiceProvider());
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        institution.setShareCapital("Share Capital");
        institution.setRea("Rea");

        return institution;
    }

    public static Institution createSimpleInstitutionSA() {
        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(createSimpleBilling());

        institution.setDataProtectionOfficer(createSimpleDataProtectionOfficer());
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.SA);
        institution.setOriginId("ANAC Code");
        institution.setOrigin(Origin.ANAC.name());
        institution.setOnboarding(new ArrayList<>());

        institution.setPaymentServiceProvider(createSimplePaymentServiceProvider());
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        institution.setShareCapital("Share Capital");
        institution.setRea("Rea");

        return institution;
    }

    public static InstitutionRequest createSimpleInstitutionRequest() {
        InstitutionRequest institution = new InstitutionRequest();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());

        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOriginId("Ipa Code");
        institution.setOrigin(Origin.MOCK.name());
        institution.setOnboarding(new ArrayList<>());

        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        institution.setShareCapital("Share Capital");
        institution.setRea("Rea");

        return institution;
    }
}
