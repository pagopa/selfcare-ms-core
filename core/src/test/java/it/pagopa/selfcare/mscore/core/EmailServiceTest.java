package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.User;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
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

@ContextConfiguration(classes = {EmailService.class})
@ExtendWith(SpringExtension.class)
class EmailServiceTest {
    @MockBean
    private CoreConfig coreConfig;

    @MockBean
    private EmailConnector emailConnector;

    @Autowired
    private EmailService emailService;

    @MockBean
    private FileStorageConnector fileStorageConnector;

    @MockBean
    private MailParametersMapper mailParametersMapper;

    /**
     * Method under test: {@link EmailService#sendMail(File, Institution, User, OnboardingRequest, boolean)}
     */
    @Test
    void testSendMail() {

        EmailConnector emailConnector = mock(EmailConnector.class);
        EmailService emailService = new EmailService(emailConnector, null, mailParametersMapper, new CoreConfig());
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PA);
        institution.setDigitalAddress("digitalAddress");

        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.NONE);
        certifiedField.setValue("42");

        CertifiedField<String> certifiedField1 = new CertifiedField<>();
        certifiedField1.setCertification(Certification.NONE);
        certifiedField1.setValue("42");

        CertifiedField<String> certifiedField2 = new CertifiedField<>();
        certifiedField2.setCertification(Certification.NONE);
        certifiedField2.setValue("42");

        User user = new User();
        user.setEmail(certifiedField);
        user.setFamilyName(certifiedField1);
        user.setFiscalCode("Fiscal Code");
        user.setId("42");
        user.setName(certifiedField2);
        user.setWorkContacts(new HashMap<>());

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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

        when(mailParametersMapper.getOnboardingMailParameter(any(), any())).thenReturn(new HashMap<>());
        when(coreConfig.getDestinationMails()).thenReturn(null);
        when(mailParametersMapper.getOnboardingPath()).thenReturn("onboarding path");
        doNothing().when(emailConnector).sendMail(any(), any(), any(), any(), any(), any());

        assertDoesNotThrow(() -> emailService.sendMail(pdf, institution, user, onboardingRequest, true));
    }

    /**
     * Method under test: {@link EmailService#sendMail(File, Institution, User, OnboardingRequest, boolean)}
     */
    @Test
    void testSendMail2() {

        EmailConnector emailConnector = mock(EmailConnector.class);
        EmailService emailService = new EmailService(emailConnector, null, mailParametersMapper, new CoreConfig());
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PG);
        institution.setDigitalAddress("digitalAddress");

        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.NONE);
        certifiedField.setValue("42");

        CertifiedField<String> certifiedField1 = new CertifiedField<>();
        certifiedField1.setCertification(Certification.NONE);
        certifiedField1.setValue("42");

        CertifiedField<String> certifiedField2 = new CertifiedField<>();
        certifiedField2.setCertification(Certification.NONE);
        certifiedField2.setValue("42");

        User user = new User();
        user.setEmail(certifiedField);
        user.setFamilyName(certifiedField1);
        user.setFiscalCode("Fiscal Code");
        user.setId("42");
        user.setName(certifiedField2);
        user.setWorkContacts(new HashMap<>());

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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

        when(mailParametersMapper.getOnboardingMailParameter(any(), any())).thenReturn(new HashMap<>());
        when(coreConfig.getDestinationMails()).thenReturn(null);
        when(mailParametersMapper.getOnboardingPath()).thenReturn("onboarding path");
        doNothing().when(emailConnector).sendMail(any(), any(), any(), any(), any(), any());

        assertDoesNotThrow(() -> emailService.sendMail(pdf, institution, user, onboardingRequest, false));
    }


    /**
     * Method under test: {@link EmailService#sendCompletedEmail(MultipartFile, Token, List, Institution, Product, File)}
     */
    @Test
    void testSendCompletedEmail() throws UnsupportedEncodingException {

        MockMultipartFile contract = new MockMultipartFile("Name", "AAAAAAAA".getBytes("UTF-8"));

        Token token = new Token();
        ArrayList<User> managers = new ArrayList<>();
        Institution institution = new Institution();
        institution.setDigitalAddress("digitalAddress");

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");

        when(coreConfig.isSendEmailToInstitution()).thenReturn(true);
        when(mailParametersMapper.getCompleteOnbordingMailParameter(any())).thenReturn(new HashMap<>());
        doNothing().when(emailConnector).sendMail(any(), any(), any(), any(), any(), any());
        doNothing().when(fileStorageConnector).uploadContract(any(), any());

        assertDoesNotThrow(() -> emailService.sendCompletedEmail(contract, token, managers, institution, product,
                Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));
    }

    /**
     * Method under test: {@link EmailService#sendRejectMail(File, Institution, Product)}
     */
    @Test
    void testSendRejectMail5() {

        EmailConnector emailConnector = mock(EmailConnector.class);
        doNothing().when(emailConnector)
                .sendMail(any(), any(), any(), any(), any(),
                        any());
        MailParametersMapper mailParametersMapper = mock(MailParametersMapper.class);
        when(mailParametersMapper.getOnboardingRejectNotificationPath())
                .thenReturn("Onboarding Reject Notification Path");
        when(mailParametersMapper.getOnboardingRejectMailParameters(any(), any()))
                .thenReturn(new HashMap<>());
        EmailService emailService = new EmailService(emailConnector, null, mailParametersMapper, new CoreConfig());
        File logo = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", "mailParameters: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "mailParameters: {}", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null, "mailParameters: {}", "mailParameters: {}", "mailParameters: {}",
                "jane.doe@example.org", "4105551212", true);

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        emailService.sendRejectMail(logo, institution, product);
        verify(emailConnector).sendMail(any(), any(), any(), any(),
                any(), any());
        verify(mailParametersMapper).getOnboardingRejectNotificationPath();
        verify(mailParametersMapper).getOnboardingRejectMailParameters(any(), any());
    }

    /**
     * Method under test: {@link EmailService#sendRejectMail(File, Institution, Product)}
     */
    @Test
    void testSendRejectMail6() {

        EmailConnector emailConnector = mock(EmailConnector.class);
        doNothing().when(emailConnector)
                .sendMail(any(), any(), any(), any(), any(),
                        any());
        MailParametersMapper mailParametersMapper = mock(MailParametersMapper.class);
        when(mailParametersMapper.getOnboardingRejectNotificationPath())
                .thenReturn("Onboarding Reject Notification Path");
        when(mailParametersMapper.getOnboardingRejectMailParameters(any(), any()))
                .thenReturn(new HashMap<>());

        CoreConfig coreConfig = new CoreConfig();
        coreConfig.setDestinationMails(new ArrayList<>());
        EmailService emailService = new EmailService(emailConnector, null, mailParametersMapper, coreConfig);
        File logo = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", "mailParameters: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "mailParameters: {}", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null, "mailParameters: {}", "mailParameters: {}", "mailParameters: {}",
                "jane.doe@example.org", "4105551212", true);

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        emailService.sendRejectMail(logo, institution, product);
        verify(emailConnector).sendMail(any(), any(), any(), any(),
                any(), any());
        verify(mailParametersMapper).getOnboardingRejectNotificationPath();
        verify(mailParametersMapper).getOnboardingRejectMailParameters(any(), any());
    }


}

