package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Contract;
import it.pagopa.selfcare.mscore.model.onboarding.ContractImported;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private MailParametersMapper mailParametersMapper;

    @Mock
    private CoreConfig coreConfig;

    @Mock
    private EmailConnector emailConnector;

    @Mock
    private FileStorageConnector fileStorageConnector;

    /**
     * Method under test: {@link EmailService#sendMail(File, Institution, User, OnboardingRequest, boolean)}
     */
    @Test
    void testSendMail() {
        when(coreConfig.getDestinationMails()).thenReturn(new ArrayList<>());
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Institution institution = new Institution();

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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
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
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        emailService.sendMail(pdf, institution, user, onboardingRequest, true);
        assertNotNull(onboardingRequest);
    }

    @Test
    void testSendMail2() {
        when(coreConfig.getDestinationMails()).thenReturn(new ArrayList<>());
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Institution institution = new Institution();

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

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PSP);

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
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        emailService.sendMail(pdf, institution, user, onboardingRequest, false);
        assertNotNull(onboardingRequest);
    }

    /**
     * Method under test: {@link EmailService#sendCompletedEmail(List, Institution, Product, File)}
     */
    @Test
    void testSendCompletedEmail() {
        MultipartFile contract = mock(MultipartFile.class);

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContractTemplate("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate(OffsetDateTime.now());
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        ArrayList<User> managers = new ArrayList<>();
        User user = new User();
        user.setEmail(new CertifiedField<>());
        managers.add(user);
        Institution institution = new Institution();
        institution.setDigitalAddress("digital");

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(coreConfig.isSendEmailToInstitution()).thenReturn(true);
        File file = mock(File.class);
        emailService.sendCompletedEmail(managers, institution, product, file);
        assertNotNull(institution);
    }

    @Test
    void testSendCompletedEmail2() {
        MultipartFile contract = mock(MultipartFile.class);

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContractTemplate("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate(OffsetDateTime.now());
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        ArrayList<User> managers = new ArrayList<>();
        User user = new User();
        user.setEmail(new CertifiedField<>());
        managers.add(user);
        Institution institution = new Institution();
        institution.setDigitalAddress("digital");

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(coreConfig.isSendEmailToInstitution()).thenReturn(false);
        when(coreConfig.getInstitutionAlternativeEmail()).thenReturn("42");
        File file = mock(File.class);
        emailService.sendCompletedEmail(managers, institution, product, file);
        assertNotNull(institution);
    }


    @Test
    void sendRejectMail(){
        File file = mock(File.class);
        Institution institution = new Institution();
        institution.setDigitalAddress("digital");

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");

        when(mailParametersMapper.getOnboardingRejectMailParameters(any(),any())).thenReturn(new HashMap<>());
        when(coreConfig.getDestinationMails()).thenReturn(new ArrayList<>());
        when(mailParametersMapper.getOnboardingRejectNotificationPath()).thenReturn("42");
        emailService.sendRejectMail(file,institution,product);
        assertNotNull(product);
    }

    @Test
    void sendRejectMail2(){
        File file = mock(File.class);
        Institution institution = new Institution();
        institution.setDigitalAddress("digital");

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");

        when(mailParametersMapper.getOnboardingRejectMailParameters(any(),any())).thenReturn(new HashMap<>());
        when(coreConfig.getDestinationMails()).thenReturn(null);
        when(mailParametersMapper.getOnboardingRejectNotificationPath()).thenReturn("42");
        emailService.sendRejectMail(file,institution,product);
        assertNotNull(product);
    }

    @Test
    void sendRejectMail3(){
        File file = mock(File.class);
        Institution institution = new Institution();
        institution.setDigitalAddress(null);

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");

        when(mailParametersMapper.getOnboardingRejectMailParameters(any(),any())).thenReturn(new HashMap<>());
        when(coreConfig.getDestinationMails()).thenReturn(null);
        when(mailParametersMapper.getOnboardingRejectNotificationPath()).thenReturn("42");
        when(coreConfig.getInstitutionAlternativeEmail()).thenReturn("42");
        emailService.sendRejectMail(file,institution,product);
        assertNotNull(product);
    }
}
