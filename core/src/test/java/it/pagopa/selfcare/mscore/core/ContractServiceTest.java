package it.pagopa.selfcare.mscore.core;

import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.simplereport.jaxb.XmlSimpleReport;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private SignatureService signatureService;


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

