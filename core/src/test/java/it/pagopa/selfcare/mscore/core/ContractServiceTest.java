package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.exception.FileDownloadException;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MailException;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
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

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ContractServiceTest {

    /**
     * Method under test: {@link ContractService#createContractPDF(String, OnboardedUser, List, Institution, OnboardingRequest, List)}
     */
    @Test
    void testCreateContractPDF2() throws IOException {
        try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {

            mockFiles.when(() -> Files.createTempFile((any()), any(),any()))
                    .thenThrow(new IOException());
            PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
            FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
            EmailConnector emailConnector = mock(EmailConnector.class);
            CoreConfig coreConfig = new CoreConfig();
            ContractService contractService = new ContractService(pagoPaSignatureConfig, fileStorageConnector,
                    emailConnector, coreConfig, new MailParametersMapper(), mock(Pkcs7HashSignService.class));

            OnboardedUser onboardedUser = new OnboardedUser();
            onboardedUser.setBindings(new HashMap<>());
            onboardedUser.setCreatedAt(null);
            onboardedUser.setEmail("jane.doe@example.org");
            onboardedUser.setId("42");
            onboardedUser.setName("Name");
            onboardedUser.setProductRole(new ArrayList<>());
            onboardedUser.setRole(PartyRole.MANAGER);
            onboardedUser.setSurname("Doe");
            onboardedUser.setTaxCode("Tax Code");
            onboardedUser.setUser("User");
            ArrayList<OnboardedUser> users = new ArrayList<>();
            Institution institution = new Institution();

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

            List<GeographicTaxonomies> list = new ArrayList<>();
            assertThrows(InvalidRequestException.class, () -> contractService.createContractPDF("Contract Template",
                    onboardedUser, users, institution, onboardingRequest,list));
            mockFiles.verify(() -> Files.createTempFile(any(), any(),any()));
        }
    }

    /**
     * Method under test: {@link ContractService#extractTemplate(OnboardingRequest)}
     */
    @Test
    void testExtractTemplate() throws FileDownloadException {

        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile((any()))).thenReturn("Template File");
        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        EmailConnector emailConnector = mock(EmailConnector.class);
        CoreConfig coreConfig = new CoreConfig();
        ContractService contractService = new ContractService(pagoPaSignatureConfig, fileStorageConnector, emailConnector,
                coreConfig, new MailParametersMapper(), mock(Pkcs7HashSignService.class));

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
        assertEquals("Template File", contractService.extractTemplate(onboardingRequest));
        verify(fileStorageConnector).getTemplateFile(any());
    }


    /**
     * Method under test: {@link ContractService#sendMail(File, Institution, User, OnboardingRequest)}
     */
    @Test
    void testSendMail2() throws MailException {
        EmailConnector emailConnector = mock(EmailConnector.class);
        doNothing().when(emailConnector)
                .sendMail(any(),any(), any(), any(),  any());
        MailParametersMapper mailParametersMapper = mock(MailParametersMapper.class);
        when(mailParametersMapper.getOnboardingNotificationPath()).thenReturn("Onboarding Notification Path");
        when(mailParametersMapper.getOnboardingNotificationAdminEmail()).thenReturn(new ArrayList<>());
        when(mailParametersMapper.getOnboardingMailNotificationParameter(any(), any()))
                .thenReturn(new HashMap<>());
        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        ContractService contractService = new ContractService(pagoPaSignatureConfig, fileStorageConnector, emailConnector,
                new CoreConfig(), mailParametersMapper, mock(Pkcs7HashSignService.class));
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Institution institution = new Institution();

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
        contractService.sendMail(pdf, institution, user, onboardingRequest);
        verify(emailConnector).sendMail(any(),any(), any(), any(),
                 any());
        verify(mailParametersMapper).getOnboardingNotificationPath();
        verify(mailParametersMapper).getOnboardingNotificationAdminEmail();
        verify(mailParametersMapper).getOnboardingMailNotificationParameter(any(), any());
    }

    /**
     * Method under test: {@link ContractService#sendMail(File, Institution, User, OnboardingRequest)}
     */
    @Test
    void testSendMail3() throws MailException {

        EmailConnector emailConnector = mock(EmailConnector.class);
        doNothing().when(emailConnector)
                .sendMail(any(),any(), any(), any(),  any());
        MailParametersMapper mailParametersMapper = mock(MailParametersMapper.class);
        when(mailParametersMapper.getOnboardingPath()).thenReturn("Onboarding Path");
        when(mailParametersMapper.getOnboardingMailParameter(any(), any()))
                .thenReturn(new HashMap<>());
        when(mailParametersMapper.getOnboardingNotificationPath()).thenReturn("Onboarding Notification Path");
        when(mailParametersMapper.getOnboardingNotificationAdminEmail()).thenReturn(new ArrayList<>());
        when(mailParametersMapper.getOnboardingMailNotificationParameter(any(), any()))
                .thenReturn(new HashMap<>());
        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        ContractService contractService = new ContractService(pagoPaSignatureConfig, fileStorageConnector, emailConnector,
                new CoreConfig(), mailParametersMapper, mock(Pkcs7HashSignService.class));
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", "mailParameters: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "mailParameters: {}", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        contractService.sendMail(pdf, institution, user, onboardingRequest);
        verify(emailConnector).sendMail(any(),any(), any(), any(),
                 any());
        verify(mailParametersMapper).getOnboardingPath();
        verify(mailParametersMapper).getOnboardingMailParameter(any(), any());
    }

    /**
     * Method under test: {@link ContractService#sendMail(File, Institution, User, OnboardingRequest)}
     */
    @Test
    void testSendMail7() throws MailException {
        EmailConnector emailConnector = mock(EmailConnector.class);
        doNothing().when(emailConnector)
                .sendMail(any(),any(), any(), any(),  any());

        CoreConfig coreConfig = new CoreConfig();
        coreConfig.setDestinationMails(new ArrayList<>());
        MailParametersMapper mailParametersMapper = mock(MailParametersMapper.class);
        when(mailParametersMapper.getOnboardingPath()).thenReturn("Onboarding Path");
        when(mailParametersMapper.getOnboardingMailParameter(any(), any()))
                .thenReturn(new HashMap<>());
        when(mailParametersMapper.getOnboardingNotificationPath()).thenReturn("Onboarding Notification Path");
        when(mailParametersMapper.getOnboardingNotificationAdminEmail()).thenReturn(new ArrayList<>());
        when(mailParametersMapper.getOnboardingMailNotificationParameter(any(), any()))
                .thenReturn(new HashMap<>());
        ContractService contractService = new ContractService(new PagoPaSignatureConfig(),
                mock(FileStorageConnector.class), emailConnector, coreConfig, mailParametersMapper,
                mock(Pkcs7HashSignService.class));
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", "mailParameters: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "mailParameters: {}", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        contractService.sendMail(pdf, institution, user, onboardingRequest);
        verify(emailConnector).sendMail(any(),any(), any(), any(),
                 any());
        verify(mailParametersMapper).getOnboardingPath();
        verify(mailParametersMapper).getOnboardingMailParameter(any(), any());
    }
}

