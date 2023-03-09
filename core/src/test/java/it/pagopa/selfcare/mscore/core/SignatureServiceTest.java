package it.pagopa.selfcare.mscore.core;

import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.simplereport.jaxb.XmlSimpleReport;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.TokenIdentifierProvider;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.timestamp.DetachedTimestampValidator;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {SignatureService.class})
@ExtendWith(SpringExtension.class)
class SignatureServiceTest {
    @InjectMocks
    private SignatureService signatureService;


    /**
     * Method under test: {@link SignatureService#createDocumentValidator(byte[])}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateDocumentValidator() {
        // TODO: Complete this test.
        //   Reason: R005 Unable to load class.
        //   Class: javax.activation.DataSource
        //   Please check that the class is available on your test runtime classpath.
        //   See https://diff.blue/R005 to resolve this issue.

        // Arrange
        // TODO: Populate arranged inputs
        byte[] bytes = null;

        // Act
        SignedDocumentValidator actualCreateDocumentValidatorResult = this.signatureService
                .createDocumentValidator(bytes);

        // Assert
        // TODO: Add assertions on result
    }

    /**
     * Method under test: {@link SignatureService#isDocumentSigned(SignedDocumentValidator)}
     */
    @Test
    void testIsDocumentSigned() {
        assertThrows(MsCoreException.class,
                () -> signatureService.isDocumentSigned(new DetachedTimestampValidator(new DigestDocument())));
    }

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    void testVerifyOriginalDocument() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(new DigestDocument());
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
        assertTrue(detachedTimestampValidator.getSignatures().isEmpty());
    }

    @Test
    void validateDocument() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(new DigestDocument());
        assertThrows(NoClassDefFoundError.class, () -> signatureService.validateDocument(detachedTimestampValidator));
    }

    @Test
    void validateDocument2() {
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        MsCoreException msCoreException = mock(MsCoreException.class);
        when(signedDocumentValidator.validateDocument()).thenThrow(msCoreException);
        assertThrows(MsCoreException.class, () -> signatureService.validateDocument(signedDocumentValidator));
    }

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    void testVerifyOriginalDocument3() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(new DigestDocument());
        detachedTimestampValidator.setTokenIdentifierProvider(mock(TokenIdentifierProvider.class));
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
        assertTrue(detachedTimestampValidator.getSignatures().isEmpty());
    }

    @Test
    void testVerifyOriginalDocument4() {
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        AdvancedSignature advancedSignatures = mock(AdvancedSignature.class);
        List<AdvancedSignature> advancedSignatures1 = new ArrayList<>();
        advancedSignatures1.add(advancedSignatures);
        when(signedDocumentValidator.getSignatures()).thenReturn(advancedSignatures1);
        signatureService.verifyOriginalDocument(signedDocumentValidator);
        assertNotNull(advancedSignatures1);
    }


    /**
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    void testVerifySignatureForm() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(new DigestDocument());
        assertEquals("VALID", signatureService.verifySignatureForm(detachedTimestampValidator));
        assertTrue(detachedTimestampValidator.getSignatures().isEmpty());
    }


    /**
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    void testVerifySignatureForm3() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(new DigestDocument());
        detachedTimestampValidator.setTokenIdentifierProvider(mock(TokenIdentifierProvider.class));
        assertEquals("VALID", signatureService.verifySignatureForm(detachedTimestampValidator));
        assertTrue(detachedTimestampValidator.getSignatures().isEmpty());
    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    void testVerifySignature() {
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(diagnosticDataJaxb, detailedReport, simpleReport, new ValidationReportType());

        assertEquals("Document signature is invalid", signatureService.verifySignature(reports));

    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    void testVerifySignature2() {
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        assertEquals("Document signature is invalid", signatureService
                .verifySignature(new Reports(diagnosticDataJaxb, detailedReport, new XmlSimpleReport(), null)));
    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    void testVerifySignature4() {
        XmlDiagnosticData xmlDiagnosticData = new XmlDiagnosticData();
        xmlDiagnosticData.setValidationDate(mock(Date.class));
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(xmlDiagnosticData, detailedReport, simpleReport, new ValidationReportType());

        assertEquals("Document signature is invalid", signatureService.verifySignature(reports));
    }

    /**
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    void testVerifyDigest() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(new DigestDocument());
        assertEquals("VALID", signatureService.verifyDigest(detachedTimestampValidator, "Checksum"));
        assertTrue(detachedTimestampValidator.getSignatures().isEmpty());
    }

    /**
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    void testVerifyDigest3() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(new DigestDocument());
        detachedTimestampValidator.setTokenIdentifierProvider(mock(TokenIdentifierProvider.class));
        assertEquals("VALID", signatureService.verifyDigest(detachedTimestampValidator, "Checksum"));
        assertTrue(detachedTimestampValidator.getSignatures().isEmpty());
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    void testVerifyManagerTaxCode() {
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(diagnosticDataJaxb, detailedReport, simpleReport, new ValidationReportType());

        ArrayList<User> userList = new ArrayList<>();
        assertEquals("No tax code has been found in digital signature",
                signatureService.verifyManagerTaxCode(reports, userList));
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    void testVerifyManagerTaxCode4() {
        XmlDiagnosticData xmlDiagnosticData = new XmlDiagnosticData();
        xmlDiagnosticData.setValidationDate(mock(Date.class));
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(xmlDiagnosticData, detailedReport, simpleReport, new ValidationReportType());

        ArrayList<User> userList = new ArrayList<>();
        assertEquals("No tax code has been found in digital signature",
                signatureService.verifyManagerTaxCode(reports, userList));
    }
}

