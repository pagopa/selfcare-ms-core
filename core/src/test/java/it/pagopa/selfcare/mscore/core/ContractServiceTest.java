package it.pagopa.selfcare.mscore.core;

import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.simplereport.jaxb.XmlSimpleReport;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignService;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private CoreConfig coreConfig;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private PagoPaSignatureConfig pagoPaSignatureConfig;

    @Mock
    private Pkcs7HashSignService pkcs7HashSignService;

    @Mock
    private SignatureService signatureService;

    @Mock
    private PadesSignService padesSignService;

    @Test
    void extractTemplate(){
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("value");
        assertNotNull(contractService.extractTemplate("path"));
    }

    @Test
    void getFile(){
        when(fileStorageConnector.getFile(any())).thenReturn(new ResourceResponse());
        assertNotNull(contractService.getFile("path"));
    }

    @Test
    void getLogoFile(){
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("path");
        assertNotNull(contractService.getLogoFile());
    }

    @Test
    void getLogoFile2(){
        InvalidRequestException invalidRequestException = mock(InvalidRequestException.class);
        when(fileStorageConnector.getTemplateFile(any())).thenThrow(invalidRequestException);
        assertThrows(InvalidRequestException.class, () -> contractService.getLogoFile());
    }

    @Test
    void createContractPDF(){
        String contractTemplate = "contractTemplate";
        User validManager = new User();
        validManager.setEmail(new CertifiedField<>());
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        OnboardingRequest request = new OnboardingRequest();
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();

        assertNotNull(contractService.createContractPDF(contractTemplate,validManager,users,institution,request,geographicTaxonomies));
    }

    @Test
    void createContractPDF2(){
        String contractTemplate = "contractTemplate";
        User validManager = new User();
        validManager.setEmail(new CertifiedField<>());
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PSP);
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId("prod-pagopa");
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();

        assertNotNull(contractService.createContractPDF(contractTemplate,validManager,users,institution,request,geographicTaxonomies));
    }

    @Test
    void createContractPDF3(){
        String contractTemplate = "contractTemplate";
        User validManager = new User();
        validManager.setEmail(new CertifiedField<>());
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        institution.setDescription("description");
        OnboardingRequest request = new OnboardingRequest();
        request.setSignContract(true);
        request.setProductId("prod-io");
        request.setProductName("productName");
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(true);
        when(pagoPaSignatureConfig.getApplyOnboardingTemplateReason()).thenReturn("${institutionName}${productName}");
        assertNotNull(contractService.createContractPDF(contractTemplate,validManager,users,institution,request,geographicTaxonomies));
    }

    @Test
    void createContractPDF4(){
        String contractTemplate = "contractTemplate";
        User validManager = new User();
        validManager.setEmail(new CertifiedField<>());
        List<User> users = new ArrayList<>();
        Institution institution = new Institution();
        institution.setDescription("description");
        OnboardingRequest request = new OnboardingRequest();
        request.setSignContract(true);
        request.setProductId("prod-io");
        request.setProductName("productName");
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        when(pagoPaSignatureConfig.isApplyOnboardingEnabled()).thenReturn(true);
        when(pagoPaSignatureConfig.isEnabled()).thenReturn(true);
        when(pagoPaSignatureConfig.getApplyOnboardingTemplateReason()).thenReturn("${institutionName}${productName}");
        doNothing().when(padesSignService).padesSign(any(),any(),any());
        when(pagoPaSignatureConfig.getSigner()).thenReturn("value");
        when(pagoPaSignatureConfig.getLocation()).thenReturn("value");
        assertThrows(IllegalStateException.class, () -> contractService.createContractPDF(contractTemplate,validManager,users,institution,request,geographicTaxonomies));
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
        when(signatureService.verifyDigest(any(),any())).thenReturn("path");
        when(signatureService.verifyManagerTaxCode(any(),any())).thenReturn("path");
        MultipartFile file = mock(MultipartFile.class);
        Token token = new Token();
        InputStream inputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getInputStream().readAllBytes()).thenReturn(new byte[]{1});
        assertThrows(MsCoreException.class, () -> contractService.verifySignature(file,token, new ArrayList<>()));

    }
}

