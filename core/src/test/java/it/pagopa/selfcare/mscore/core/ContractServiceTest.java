package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eu.europa.esig.dss.validation.SignedDocumentValidator;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignService;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.User;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ContractService.class})
@ExtendWith(SpringExtension.class)
class ContractServiceTest {
    @Autowired
    private ContractService contractService;

    @MockBean
    private CoreConfig coreConfig;

    @MockBean
    private FileStorageConnector fileStorageConnector;

    @MockBean
    private PagoPaSignatureConfig pagoPaSignatureConfig;

    @MockBean
    private Pkcs7HashSignService pkcs7HashSignService;

    @MockBean
    private SignatureService signatureService;

    @MockBean
    private PadesSignService padesSignService;

    /**
     * Method under test: {@link ContractService#createContractPDF(String, User, List, Institution, OnboardingRequest, List)}
     */
    @Test
    void testCreateContractPDFWithoutSign() {

        User user = new User();

        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.NONE);
        certifiedField.setValue("42");
        user.setEmail(certifiedField);

        CertifiedField<String> certifiedField1 = new CertifiedField<>();
        certifiedField1.setCertification(Certification.NONE);
        certifiedField1.setValue("42");
        user.setFamilyName(certifiedField1);
        user.setFiscalCode("Fiscal Code");
        user.setId("42");

        CertifiedField<String> certifiedField2 = new CertifiedField<>();
        certifiedField2.setCertification(Certification.NONE);
        certifiedField2.setValue("42");
        user.setName(certifiedField2);
        user.setWorkContacts(new HashMap<>());
        ArrayList<User> users = new ArrayList<>();


        OnboardingRequest onboardingRequest = new OnboardingRequest();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        onboardingRequest.setBillingRequest(billing);

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("prod-pagopa");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());

        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PSP);

        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(false);

        contractService.createContractPDF("Contract Template", user, users, institution, onboardingRequest,
                new ArrayList<>());
    }

    /**
     * Method under test: {@link ContractService#createContractPDF(String, User, List, Institution, OnboardingRequest, List)}
     */
    @Test
    void testCreateContractPDFWithSign() {

        User user = new User();

        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.NONE);
        certifiedField.setValue("42");
        user.setEmail(certifiedField);

        CertifiedField<String> certifiedField1 = new CertifiedField<>();
        certifiedField1.setCertification(Certification.NONE);
        certifiedField1.setValue("42");
        user.setFamilyName(certifiedField1);
        user.setFiscalCode("Fiscal Code");
        user.setId("42");

        CertifiedField<String> certifiedField2 = new CertifiedField<>();
        certifiedField2.setCertification(Certification.NONE);
        certifiedField2.setValue("42");
        user.setName(certifiedField2);
        user.setWorkContacts(new HashMap<>());
        ArrayList<User> users = new ArrayList<>();


        OnboardingRequest onboardingRequest = new OnboardingRequest();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        onboardingRequest.setBillingRequest(billing);

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("prod-pagopa");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());

        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PSP);
        institution.setDescription("description");

        when(pagoPaSignatureConfig.getApplyOnboardingTemplateReason()).thenReturn("${institutionName} ${productName}");
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(true);
        when(pagoPaSignatureConfig.isEnabled()).thenReturn(false);

        assertDoesNotThrow(() -> contractService.createContractPDF("Contract Template", user, users, institution, onboardingRequest,
                new ArrayList<>()));
    }

    /**
     * Method under test: {@link ContractService#extractTemplate(String)}
     */
    @Test
    void testExtractTemplate() {
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("template");
        assertEquals(this.contractService.extractTemplate("Path"), "template");
    }

    /**
     * Method under test: {@link ContractService#getLogoFile()}
     */
    @Test
    void testGetLogoFile() {
        when(coreConfig.getLogoPath()).thenReturn("/logo");
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("<html></html>");

        assertDoesNotThrow(() -> contractService.getLogoFile());
    }

    /**
     * Method under test: {@link ContractService#verifySignature(MultipartFile, Token, List)}
     */
    @Test
    void testVerifySignature() {


        MockMultipartFile contract = new MockMultipartFile("Name", "AAAAAAAA".getBytes(StandardCharsets.UTF_8));

        Token token = new Token();
        token.setChecksum("checksum");
        SignedDocumentValidator mockValidator = mock(SignedDocumentValidator.class);
        when(signatureService.createDocumentValidator(any())).thenReturn(mockValidator);
        doNothing().when(signatureService).isDocumentSigned(any());
        doNothing().when(signatureService).verifyOriginalDocument(any());

        when(signatureService.verifySignatureForm(any())).thenReturn("VALID");
        when(signatureService.verifySignature(any())).thenReturn("VALID");
        when(signatureService.verifyDigest(any(), any())).thenReturn("VALID");
        when(signatureService.verifyManagerTaxCode(any(), any())).thenReturn("VALID");

        assertDoesNotThrow(() -> contractService.verifySignature(contract, token, new ArrayList<>()));
    }

    /**
     * Method under test: {@link ContractService#verifySignature(MultipartFile, Token, List)}
     */
    @Test
    void testVerifySignatureWithErrors() {


        MockMultipartFile contract = new MockMultipartFile("Name", "AAAAAAAA".getBytes(StandardCharsets.UTF_8));

        Token token = new Token();
        token.setChecksum("checksum");
        SignedDocumentValidator mockValidator = mock(SignedDocumentValidator.class);
        when(signatureService.createDocumentValidator(any())).thenReturn(mockValidator);
        doNothing().when(signatureService).isDocumentSigned(any());
        doNothing().when(signatureService).verifyOriginalDocument(any());

        when(signatureService.verifySignatureForm(any())).thenReturn("VALID");
        when(signatureService.verifySignature(any())).thenReturn("NOT VALID");
        when(signatureService.verifyDigest(any(), any())).thenReturn("VALID");
        when(signatureService.verifyManagerTaxCode(any(), any())).thenReturn("VALID");

        assertThrows(MsCoreException.class, () -> contractService.verifySignature(contract, token, new ArrayList<>()));
    }

}

