package it.pagopa.selfcare.mscore.core;

import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.simplereport.jaxb.XmlSimpleReport;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private Pkcs7HashSignService pkcs7HashSignService;

    @InjectMocks
    private ContractService contractService;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private SignatureService signatureService;

    @Mock
    private PagoPaSignatureConfig pagoPaSignatureConfig;

    @Mock
    private CoreConfig coreConfig;

    @Mock
    private KafkaPropertiesConfig kafkaPropertiesConfig;

    @Test
    void createContractPDF() {
        String contract = "contract";
        User validManager = new User();
        CertifiedField<String> emailCert = new CertifiedField<>();
        emailCert.setValue("email");
        WorkContact workContact = new WorkContact();
        workContact.setEmail(emailCert);
        Map<String, WorkContact> map = new HashMap<>();
        map.put("id", workContact);
        validManager.setWorkContacts(map);
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        institution.setId("id");
        institution.setInstitutionType(InstitutionType.PSP);
        institution.setDescription("42");
        OnboardingRequest request = new OnboardingRequest();
        request.getInstitutionUpdate().setInstitutionType(InstitutionType.PSP);
        request.setProductId("prod-pagopa");
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = request.getInstitutionUpdate().getInstitutionType();
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(false);
        assertNotNull(contractService.createContractPDF(contract, validManager, users, institution, request, geographicTaxonomies, institutionType));
    }

    @Test
    void createContractPDF1() {
        String contract = "contract";
        User validManager = new User();
        CertifiedField<String> emailCert = new CertifiedField<>();
        emailCert.setValue("email");
        WorkContact workContact = new WorkContact();
        workContact.setEmail(emailCert);
        Map<String, WorkContact> map = new HashMap<>();
        map.put("id", workContact);
        validManager.setWorkContacts(map);
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        institution.setId("id");
        institution.setInstitutionType(InstitutionType.PSP);
        institution.setDescription("42");
        OnboardingRequest request = new OnboardingRequest();
        request.getInstitutionUpdate().setInstitutionType(InstitutionType.PSP);
        request.setProductId("prod-io");
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = request.getInstitutionUpdate().getInstitutionType();
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(false);
        assertNotNull(contractService.createContractPDF(contract, validManager, users, institution, request, geographicTaxonomies, institutionType));
    }

    @Test
    void createContractPDF2() {
        String contract = "contract";
        User validManager = new User();
        CertifiedField<String> emailCert = new CertifiedField<>();
        emailCert.setValue("email");
        WorkContact workContact = new WorkContact();
        workContact.setEmail(emailCert);
        Map<String, WorkContact> map = new HashMap<>();
        map.put("id", workContact);
        validManager.setWorkContacts(map);
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PSP);
        institution.setId("id");
        institution.setDescription("42");
        OnboardingRequest request = new OnboardingRequest();
        request.getInstitutionUpdate().setInstitutionType(InstitutionType.PSP);
        request.setProductId("prod-pagopa");
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = request.getInstitutionUpdate().getInstitutionType();
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(true);
        when(pagoPaSignatureConfig.isEnabled()).thenReturn(false);
        when(pagoPaSignatureConfig.getApplyOnboardingTemplateReason()).thenReturn("${institutionName}${productName}");
        assertNotNull(contractService.createContractPDF(contract, validManager, users, institution, request, geographicTaxonomies, institutionType));
    }

    @Test
    void createContractPDF3() {
        String contract = "contract";
        User validManager = new User();
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PSP);
        institution.setDescription("42");
        OnboardingRequest request = new OnboardingRequest();
        request.getInstitutionUpdate().setInstitutionType(InstitutionType.PSP);
        request.setProductId("prod-pagopa");
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = request.getInstitutionUpdate().getInstitutionType();
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> contractService.createContractPDF(contract, validManager, users, institution, request, geographicTaxonomies, institutionType),
                "Manager email not found");
    }

    @Test
    void getLogoFile() {
        when(coreConfig.getLogoPath()).thenReturn("42");
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("42");
        assertNotNull(contractService.getLogoFile());
    }

    @Test
    void getLogoFile1() {
        when(coreConfig.getLogoPath()).thenReturn("42");
        InvalidRequestException ioException = mock(InvalidRequestException.class);
        when(fileStorageConnector.getTemplateFile(any())).thenThrow(ioException);
        assertThrows(InvalidRequestException.class, () -> contractService.getLogoFile());
    }

    /**
     * Method under test: {@link ContractService#sendDataLakeNotification(Institution, Token)}
     */
    @Test
    void testSendDataLakeNotification2() throws ExecutionException, InterruptedException {
        ProducerFactory<String, String> producerFactory = (ProducerFactory<String, String>) mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        CoreConfig coreConfig = new CoreConfig();
        Pkcs7HashSignService pkcs7HashSignService = mock(Pkcs7HashSignService.class);
        SignatureService signatureService = new SignatureService();
        ContractService contractService = new ContractService(pagoPaSignatureConfig, null, coreConfig,
                pkcs7HashSignService, signatureService, kafkaTemplate, new KafkaPropertiesConfig());

        Institution institution = new Institution();
        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("prod");
        institution.setOnboarding(List.of(onboarding));

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

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("prod");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> contractService.sendDataLakeNotification(institution, token),
                "Topic cannot be null");
    }

    @Test
    void extractTemplate() {
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("value");
        assertNotNull(contractService.extractTemplate("path"));
    }


    /**
     * Method under test: {@link ContractService#extractTemplate(String)}
     */
    @Test
    void testExtractTemplate2() {

        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile((String) any())).thenReturn("Template File");
        ProducerFactory<String, String> producerFactory = (ProducerFactory<String, String>) mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        CoreConfig coreConfig = new CoreConfig();
        Pkcs7HashSignService pkcs7HashSignService = mock(Pkcs7HashSignService.class);
        SignatureService signatureService = new SignatureService();
        assertEquals("Template File", (new ContractService(pagoPaSignatureConfig, fileStorageConnector, coreConfig,
                pkcs7HashSignService, signatureService, kafkaTemplate, new KafkaPropertiesConfig())).extractTemplate("Path"));
        verify(fileStorageConnector).getTemplateFile((String) any());
        verify(producerFactory).transactionCapable();
    }

    @Test
    void getFile() {
        when(fileStorageConnector.getFile(any())).thenReturn(new ResourceResponse());
        assertNotNull(contractService.getFile("path"));
    }

    /**
     * Method under test: {@link ContractService#getFile(String)}
     */
    @Test
    void testGetFile2() throws UnsupportedEncodingException {

        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getFile((String) any())).thenReturn(resourceResponse);
        ProducerFactory<String, String> producerFactory = (ProducerFactory<String, String>) mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        CoreConfig coreConfig = new CoreConfig();
        Pkcs7HashSignService pkcs7HashSignService = mock(Pkcs7HashSignService.class);
        SignatureService signatureService = new SignatureService();
        assertSame(resourceResponse, (new ContractService(pagoPaSignatureConfig, fileStorageConnector, coreConfig,
                pkcs7HashSignService, signatureService, kafkaTemplate, new KafkaPropertiesConfig())).getFile("Path"));
        verify(fileStorageConnector).getFile((String) any());
        verify(producerFactory).transactionCapable();
    }

    @Test
    void verifySignature() throws IOException {
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        when(signatureService.createDocumentValidator(any())).thenReturn(signedDocumentValidator);
        doNothing().when(signatureService).isDocumentSigned(any());
        doNothing().when(signatureService).verifyOriginalDocument(any());
        when(signatureService.validateDocument(any())).thenReturn(new Reports(new XmlDiagnosticData(), new XmlDetailedReport(), new XmlSimpleReport(), new ValidationReportType()));
        when(signatureService.verifySignatureForm(any())).thenReturn("path");
        when(signatureService.verifySignature(any())).thenReturn("path");
        when(signatureService.verifyDigest(any(), any())).thenReturn("path");
        when(signatureService.verifyManagerTaxCode(any(), any())).thenReturn("path");
        MultipartFile file = mock(MultipartFile.class);
        Token token = new Token();
        InputStream inputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getInputStream().readAllBytes()).thenReturn(new byte[]{1});
        assertThrows(InvalidRequestException.class, () -> contractService.verifySignature(file, token, new ArrayList<>()));

    }

    @Test
    void deleteContract() {
        doNothing().when(fileStorageConnector).removeContract(any(), any());
        assertDoesNotThrow(() -> contractService.deleteContract("fileName", "tokenId"));
    }

    @Test
    void uploadContract() {
        when(fileStorageConnector.uploadContract(any(), any())).thenReturn("fileName");
        MultipartFile file = mock(MultipartFile.class);
        assertDoesNotThrow(() -> contractService.uploadContract("fileName", file));
    }
}

