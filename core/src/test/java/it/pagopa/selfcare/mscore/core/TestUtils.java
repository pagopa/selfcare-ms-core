package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static ContractImported dummyContractImported() {

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");
        return contractImported;
    }

    public static Token dummyToken() {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(new InstitutionUpdate());
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        TokenUser user = new TokenUser();
        user.setUserId("id");
        user.setRole(PartyRole.MANAGER);
        token.setUsers(List.of(user));
        return token;
    }

    public static OnboardingRequest dummyOnboardingRequest() {
        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("prod-io");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setBillingRequest(new Billing());
        onboardingRequest.setUsers(new ArrayList<>());
        return onboardingRequest;
    }

    public static InstitutionUpdate createSimpleInstitutionUpdate() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        return institutionUpdate;
    }

    public static InstitutionUpdate createSimpleInstitutionUpdatePT() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PT);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        return institutionUpdate;
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

    public static Billing createSimpleBilling() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        return billing;
    }

    public static Contract createSimpleContract() {

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");
        return contract;
    }

    public static Institution dummyInstitution() {
        Institution expectedInstitution = new Institution();
        expectedInstitution.setId("institutionId");
        expectedInstitution.setDescription("description");
        expectedInstitution.setParentDescription("parentDescription");
        return expectedInstitution;
    }

    public static User createSimpleUser() {
        User user = new User();
        user.setId("1");
        user.setFiscalCode("ABC123XYZ");
        CertifiedField<String> nome = new CertifiedField<>();
        nome.setValue("nome");
        user.setName(nome);
        user.setFamilyName(nome);
        user.setEmail(nome);
        return user;
    }
}
