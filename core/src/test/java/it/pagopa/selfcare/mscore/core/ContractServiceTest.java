package it.pagopa.selfcare.mscore.core;

import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private PagoPaSignatureConfig pagoPaSignatureConfig;

    @Mock
    private CoreConfig coreConfig;

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
        request.setProductId("prod-pagopa");
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = InstitutionType.PSP;
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(false);
        assertNotNull(contractService.createContractPDF(contract, validManager, users, institution, request, geographicTaxonomies, institutionType));
    }

    @Test
    void createContractPDFSA() {
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
        institution.setInstitutionType(InstitutionType.SA);
        institution.setDescription("42");
        institution.setRea("rea");
        institution.setBusinessRegisterPlace("place");
        institution.setShareCapital("10000");
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setShareCapital("10000");
        institutionUpdate.setRea("rea");
        institutionUpdate.setBusinessRegisterPlace("place");
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId("prod-interop");
        request.setSignContract(true);
        request.setProductName("42");
        request.setInstitutionUpdate(institutionUpdate);
        InstitutionType institutionType = InstitutionType.SA;
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
        request.setProductId("prod-io");
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = InstitutionType.PSP;
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(false);
        assertNotNull(contractService.createContractPDF(contract, validManager, users, institution, request, geographicTaxonomies, institutionType));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "prod-io-sign",
            "prod-pagopa",
            "prod-pn"

    })
    void createContractPDF2(String productId) {
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
        request.setProductId(productId);
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = InstitutionType.PSP;
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
        request.setProductId("prod-pagopa");
        request.setSignContract(true);
        request.setProductName("42");
        InstitutionType institutionType = InstitutionType.PSP;
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
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("Template File");

        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        CoreConfig coreConfig = new CoreConfig();
        Pkcs7HashSignService pkcs7HashSignService = mock(Pkcs7HashSignService.class);
        SignatureService signatureService = new SignatureService(new TrustedListsCertificateSource());

        assertEquals("Template File", (new ContractService(pagoPaSignatureConfig, fileStorageConnector, coreConfig,
                pkcs7HashSignService, signatureService)).extractTemplate("Path"));
        verify(fileStorageConnector).getTemplateFile(any());
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
    void testGetFile2() {

        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getFile(any())).thenReturn(resourceResponse);

        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        CoreConfig coreConfig = new CoreConfig();
        Pkcs7HashSignService pkcs7HashSignService = mock(Pkcs7HashSignService.class);
        SignatureService signatureService = new SignatureService(new TrustedListsCertificateSource());

        assertSame(resourceResponse, (new ContractService(pagoPaSignatureConfig, fileStorageConnector, coreConfig,
                pkcs7HashSignService, signatureService)).getFile("Path"));
        verify(fileStorageConnector).getFile(any());
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

