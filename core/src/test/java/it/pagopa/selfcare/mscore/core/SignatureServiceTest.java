package it.pagopa.selfcare.mscore.core;

import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.diagnostic.OrphanCertificateTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlBasicSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificate;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestAlgoAndValue;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMRACertificateMapping;
import eu.europa.esig.dss.diagnostic.jaxb.XmlOID;
import eu.europa.esig.dss.diagnostic.jaxb.XmlOriginalThirdCountryQcStatementsMapping;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPSD2QcInfo;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcCompliance;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcEuLimitValue;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcSSCD;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcStatements;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSigningCertificate;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.MaskGenerationFunction;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.simplereport.jaxb.XmlSimpleReport;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.TokenIdentifierProvider;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.timestamp.DetachedTimestampValidator;
import eu.europa.esig.dss.xades.validation.XAdESSignature;
import eu.europa.esig.validationreport.jaxb.SignatureValidationReportType;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.user.User;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.apache.batik.anim.dom.BindableElement;
import org.apache.batik.anim.dom.SVG12DOMImplementation;
import org.apache.batik.anim.dom.SVG12OMDocument;
import org.apache.batik.dom.GenericDocumentType;
import org.apache.xml.security.utils.HelperNodeList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
     * Method under test: {@link SignatureService#isDocumentSigned(SignedDocumentValidator)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testIsDocumentSigned2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.SignatureService.isDocumentSigned(SignatureService.java:57)
        //   See https://diff.blue/R013 to resolve this issue.

        signatureService.isDocumentSigned(null);
    }

    /**
     * Method under test: {@link SignatureService#isDocumentSigned(SignedDocumentValidator)}
     */
    @Test
    void testIsDocumentSigned3() {
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getSignatures()).thenReturn(new ArrayList<>());
        assertThrows(MsCoreException.class, () -> signatureService.isDocumentSigned(detachedTimestampValidator));
        verify(detachedTimestampValidator).getSignatures();
    }

    /**
     * Method under test: {@link SignatureService#isDocumentSigned(SignedDocumentValidator)}
     */
    @Test
    void testIsDocumentSigned4() {
        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        GenericDocumentType dt = new GenericDocumentType("Qualified Name", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("Prefix", new SVG12OMDocument(dt, new SVG12DOMImplementation()), "Ns", "Ln")));
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        signatureService.isDocumentSigned(detachedTimestampValidator);
        verify(detachedTimestampValidator).getSignatures();
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

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    void testVerifyOriginalDocument2() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(
                mock(DigestDocument.class));
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
        assertTrue(detachedTimestampValidator.getSignatures().isEmpty());
    }

    /**
     * Method under test: {@link SignatureService#validateDocument(SignedDocumentValidator)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testValidateDocument() {
        // TODO: Complete this test.
        //   Reason: R011 Sandboxing policy violation.
        //   Diffblue Cover ran code in your project that tried
        //     to access 'sun.misc'.
        //   Diffblue Cover's default sandboxing policy disallows this in order to prevent
        //   your code from damaging your system environment.
        //   See https://diff.blue/R011 to resolve this issue.

        // Arrange
        // TODO: Populate arranged inputs
        SignedDocumentValidator signedDocumentValidator = null;

        // Act
        Reports actualValidateDocumentResult = this.signatureService.validateDocument(signedDocumentValidator);

        // Assert
        // TODO: Add assertions on result
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
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyOriginalDocument5() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyOriginalDocument(SignatureService.java:63)
        //   See https://diff.blue/R013 to resolve this issue.

        signatureService.verifyOriginalDocument(null);
    }

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    void testVerifyOriginalDocument6() {
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getSignatures()).thenReturn(new ArrayList<>());
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
        verify(detachedTimestampValidator).getSignatures();
    }

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    void testVerifyOriginalDocument7() {
        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        GenericDocumentType dt = new GenericDocumentType("Qualified Name", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("Prefix", new SVG12OMDocument(dt, new SVG12DOMImplementation()), "Ns", "Ln")));
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(new ArrayList<>());
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
        verify(detachedTimestampValidator).getSignatures();
        verify(detachedTimestampValidator).getOriginalDocuments((String) any());
    }

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    void testVerifyOriginalDocument8() {
        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        GenericDocumentType dt = new GenericDocumentType("Qualified Name", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("Prefix", new SVG12OMDocument(dt, new SVG12DOMImplementation()), "Ns", "Ln")));

        ArrayList<DSSDocument> dssDocumentList = new ArrayList<>();
        dssDocumentList.add(new DigestDocument());
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(dssDocumentList);
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        assertThrows(MsCoreException.class, () -> signatureService.verifyOriginalDocument(detachedTimestampValidator));
        verify(detachedTimestampValidator).getSignatures();
        verify(detachedTimestampValidator).getOriginalDocuments((String) any());
    }

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyOriginalDocument9() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.RuntimeException: Could not resolve the node to a handle
        //       at com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault.getDTMHandleFromNode(DTMManagerDefault.java:578)
        //       at com.sun.org.apache.xpath.internal.XPathContext.getDTMHandleFromNode(XPathContext.java:184)
        //       at com.sun.org.apache.xpath.internal.XPath.execute(XPath.java:304)
        //       at com.sun.org.apache.xpath.internal.jaxp.XPathImplUtil.eval(XPathImplUtil.java:101)
        //       at com.sun.org.apache.xpath.internal.jaxp.XPathExpressionImpl.eval(XPathExpressionImpl.java:80)
        //       at com.sun.org.apache.xpath.internal.jaxp.XPathExpressionImpl.evaluate(XPathExpressionImpl.java:89)
        //       at eu.europa.esig.dss.DomUtils.getNodeList(DomUtils.java:383)
        //       at eu.europa.esig.dss.DomUtils.getNode(DomUtils.java:400)
        //       at eu.europa.esig.dss.DomUtils.getElement(DomUtils.java:417)
        //       at eu.europa.esig.dss.xades.validation.XAdESSignature.getSigningTime(XAdESSignature.java:389)
        //       at eu.europa.esig.dss.validation.AbstractSignatureIdentifierBuilder.writeSignedProperties(AbstractSignatureIdentifierBuilder.java:83)
        //       at eu.europa.esig.dss.validation.AbstractSignatureIdentifierBuilder.buildBinaries(AbstractSignatureIdentifierBuilder.java:67)
        //       at eu.europa.esig.dss.validation.AbstractSignatureIdentifierBuilder.build(AbstractSignatureIdentifierBuilder.java:57)
        //       at eu.europa.esig.dss.validation.DefaultAdvancedSignature.getDSSId(DefaultAdvancedSignature.java:215)
        //       at eu.europa.esig.dss.validation.DefaultAdvancedSignature.getId(DefaultAdvancedSignature.java:222)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyOriginalDocument(SignatureService.java:67)
        //   See https://diff.blue/R013 to resolve this issue.

        Element element = mock(Element.class);
        when(element.hasChildNodes()).thenReturn(true);
        when(element.getLocalName()).thenReturn("Local Name");
        when(element.getNamespaceURI()).thenReturn("Namespace URI");
        GenericDocumentType dt = new GenericDocumentType("Qualified Name", "42", "42");

        when(element.getOwnerDocument()).thenReturn(new SVG12OMDocument(dt, new SVG12DOMImplementation()));
        GenericDocumentType dt1 = new GenericDocumentType("Qualified Name", "42", "42");

        when(element.getParentNode()).thenReturn(
                new BindableElement("Prefix", new SVG12OMDocument(dt1, new SVG12DOMImplementation()), "Ns", "Ln"));
        when(element.getNodeType()).thenReturn((short) 1);
        when(element.getChildNodes()).thenReturn(new HelperNodeList(true));
        XAdESSignature e = new XAdESSignature(element);

        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        advancedSignatureList.add(e);
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(new ArrayList<>());
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
    }

    /**
     * Method under test: {@link SignatureService#verifyOriginalDocument(SignedDocumentValidator)}
     */
    @Test
    void testVerifyOriginalDocument10() {
        XAdESSignature xAdESSignature = mock(XAdESSignature.class);
        when(xAdESSignature.getId()).thenReturn("42");

        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        advancedSignatureList.add(xAdESSignature);
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(new ArrayList<>());
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
        verify(detachedTimestampValidator).getSignatures();
        verify(detachedTimestampValidator).getOriginalDocuments((String) any());
        verify(xAdESSignature).getId();
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
    void testVerifySignatureForm2() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(mock(DSSDocument.class));
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
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifySignatureForm4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifySignatureForm(SignatureService.java:84)
        //   See https://diff.blue/R013 to resolve this issue.

        signatureService.verifySignatureForm(null);
    }

    /**
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    void testVerifySignatureForm5() {
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        when(signedDocumentValidator.getSignatures()).thenReturn(new ArrayList<>());
        assertEquals("VALID", signatureService.verifySignatureForm(signedDocumentValidator));
        verify(signedDocumentValidator).getSignatures();
    }

    /**
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    void testVerifySignatureForm6() {
        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        GenericDocumentType dt = new GenericDocumentType("VALID", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("VALID", new SVG12OMDocument(dt, new SVG12DOMImplementation()), "VALID", "VALID")));
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        when(signedDocumentValidator.getSignatures()).thenReturn(advancedSignatureList);
        assertEquals("Only CAdES signature form is admitted. Invalid signatures forms detected: [XAdES]",
                signatureService.verifySignatureForm(signedDocumentValidator));
        verify(signedDocumentValidator).getSignatures();
    }

    /**
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifySignatureForm7() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1654)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:913)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:578)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifySignatureForm(SignatureService.java:88)
        //   See https://diff.blue/R013 to resolve this issue.

        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        advancedSignatureList.add(null);
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        when(signedDocumentValidator.getSignatures()).thenReturn(advancedSignatureList);
        signatureService.verifySignatureForm(signedDocumentValidator);
    }

    /**
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    void testVerifySignatureForm8() {
        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        GenericDocumentType dt = new GenericDocumentType("Qualified Name", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("Prefix", new SVG12OMDocument(dt, new SVG12DOMImplementation()), "Ns", "Ln")));
        advancedSignatureList.addAll(new ArrayList<>());
        GenericDocumentType dt1 = new GenericDocumentType("VALID", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("VALID", new SVG12OMDocument(dt1, new SVG12DOMImplementation()), "VALID", "VALID")));
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        when(signedDocumentValidator.getSignatures()).thenReturn(advancedSignatureList);
        assertEquals("Only CAdES signature form is admitted. Invalid signatures forms detected: [XAdES, XAdES]",
                signatureService.verifySignatureForm(signedDocumentValidator));
        verify(signedDocumentValidator).getSignatures();
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
    void testVerifySignature3() {
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(diagnosticDataJaxb, detailedReport, simpleReport, new ValidationReportType());

        assertEquals("Document signature is invalid", signatureService.verifySignature(reports));
        List<OrphanCertificateTokenWrapper> expectedSignatureValidationReport = reports.getDiagnosticData()
                .getAllOrphanCertificateObjects();
        assertEquals(expectedSignatureValidationReport,
                reports.getEtsiValidationReportJaxb().getSignatureValidationReport());
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
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    void testVerifySignature5() {
        XmlDiagnosticData diagnosticDataJaxb = mock(XmlDiagnosticData.class);
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(diagnosticDataJaxb, detailedReport, simpleReport, new ValidationReportType());

        assertEquals("Document signature is invalid", signatureService.verifySignature(reports));
        List<OrphanCertificateTokenWrapper> expectedSignatureValidationReport = reports.getDiagnosticData()
                .getAllOrphanCertificateObjects();
        assertEquals(expectedSignatureValidationReport,
                reports.getEtsiValidationReportJaxb().getSignatureValidationReport());
    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifySignature6() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   it.pagopa.selfcare.mscore.exception.MsCoreException: An error occurred
        //       at eu.europa.esig.validationreport.jaxb.ValidationReportType.getSignatureValidationReport(ValidationReportType.java:87)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifySignature(SignatureService.java:99)
        //   See https://diff.blue/R013 to resolve this issue.

        ValidationReportType validationReportType = mock(ValidationReportType.class);
        when(validationReportType.getSignatureValidationReport())
                .thenThrow(new MsCoreException("An error occurred", "Code"));
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        signatureService.verifySignature(
                new Reports(diagnosticDataJaxb, detailedReport, new XmlSimpleReport(), validationReportType));
    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifySignature7() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifySignature(SignatureService.java:98)
        //   See https://diff.blue/R013 to resolve this issue.

        new MsCoreException("An error occurred", "Code");

        signatureService.verifySignature(null);
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
    void testVerifyDigest2() {
        DetachedTimestampValidator detachedTimestampValidator = new DetachedTimestampValidator(
                mock(DigestDocument.class));
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
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyDigest4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyDigest(SignatureService.java:110)
        //   See https://diff.blue/R013 to resolve this issue.

        signatureService.verifyDigest(null, "Checksum");
    }

    /**
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    void testVerifyDigest5() {
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getSignatures()).thenReturn(new ArrayList<>());
        assertEquals("VALID", signatureService.verifyDigest(detachedTimestampValidator, "Checksum"));
        verify(detachedTimestampValidator).getSignatures();
    }

    /**
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    void testVerifyDigest6() {
        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        GenericDocumentType dt = new GenericDocumentType("VALID", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("VALID", new SVG12OMDocument(dt, new SVG12DOMImplementation()), "VALID", "VALID")));
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(new ArrayList<>());
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        assertEquals("Invalid file digest", signatureService.verifyDigest(detachedTimestampValidator, "Checksum"));
        verify(detachedTimestampValidator).getSignatures();
        verify(detachedTimestampValidator).getOriginalDocuments((String) any());
    }

    /**
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyDigest7() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: The digest document does not contain a digest value for the algorithm : SHA256
        //       at eu.europa.esig.dss.model.DigestDocument.getDigest(DigestDocument.java:92)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.lambda$verifyDigest$2(SignatureService.java:114)
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1654)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:913)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:578)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyDigest(SignatureService.java:115)
        //   See https://diff.blue/R013 to resolve this issue.

        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        GenericDocumentType dt = new GenericDocumentType("VALID", "42", "42");

        advancedSignatureList.add(new XAdESSignature(
                new BindableElement("VALID", new SVG12OMDocument(dt, new SVG12DOMImplementation()), "VALID", "VALID")));

        ArrayList<DSSDocument> dssDocumentList = new ArrayList<>();
        dssDocumentList.add(new DigestDocument());
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(dssDocumentList);
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        signatureService.verifyDigest(detachedTimestampValidator, "Checksum");
    }

    /**
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyDigest8() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.RuntimeException: Could not resolve the node to a handle
        //       at com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault.getDTMHandleFromNode(DTMManagerDefault.java:578)
        //       at com.sun.org.apache.xpath.internal.XPathContext.getDTMHandleFromNode(XPathContext.java:184)
        //       at com.sun.org.apache.xpath.internal.XPath.execute(XPath.java:304)
        //       at com.sun.org.apache.xpath.internal.jaxp.XPathImplUtil.eval(XPathImplUtil.java:101)
        //       at com.sun.org.apache.xpath.internal.jaxp.XPathExpressionImpl.eval(XPathExpressionImpl.java:80)
        //       at com.sun.org.apache.xpath.internal.jaxp.XPathExpressionImpl.evaluate(XPathExpressionImpl.java:89)
        //       at eu.europa.esig.dss.DomUtils.getNodeList(DomUtils.java:383)
        //       at eu.europa.esig.dss.DomUtils.getNode(DomUtils.java:400)
        //       at eu.europa.esig.dss.DomUtils.getElement(DomUtils.java:417)
        //       at eu.europa.esig.dss.xades.validation.XAdESSignature.getSigningTime(XAdESSignature.java:389)
        //       at eu.europa.esig.dss.validation.AbstractSignatureIdentifierBuilder.writeSignedProperties(AbstractSignatureIdentifierBuilder.java:83)
        //       at eu.europa.esig.dss.validation.AbstractSignatureIdentifierBuilder.buildBinaries(AbstractSignatureIdentifierBuilder.java:67)
        //       at eu.europa.esig.dss.validation.AbstractSignatureIdentifierBuilder.build(AbstractSignatureIdentifierBuilder.java:57)
        //       at eu.europa.esig.dss.validation.DefaultAdvancedSignature.getDSSId(DefaultAdvancedSignature.java:215)
        //       at eu.europa.esig.dss.validation.DefaultAdvancedSignature.getId(DefaultAdvancedSignature.java:222)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyDigest(SignatureService.java:113)
        //   See https://diff.blue/R013 to resolve this issue.

        Element element = mock(Element.class);
        when(element.hasChildNodes()).thenReturn(true);
        when(element.getLocalName()).thenReturn("Local Name");
        when(element.getNamespaceURI()).thenReturn("Namespace URI");
        GenericDocumentType dt = new GenericDocumentType("Qualified Name", "42", "42");

        when(element.getOwnerDocument()).thenReturn(new SVG12OMDocument(dt, new SVG12DOMImplementation()));
        GenericDocumentType dt1 = new GenericDocumentType("Qualified Name", "42", "42");

        when(element.getParentNode()).thenReturn(
                new BindableElement("Prefix", new SVG12OMDocument(dt1, new SVG12DOMImplementation()), "Ns", "Ln"));
        when(element.getNodeType()).thenReturn((short) 1);
        when(element.getChildNodes()).thenReturn(new HelperNodeList(true));
        XAdESSignature e = new XAdESSignature(element);

        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        advancedSignatureList.add(e);
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(new ArrayList<>());
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        signatureService.verifyDigest(detachedTimestampValidator, "Checksum");
    }

    /**
     * Method under test: {@link SignatureService#verifyDigest(SignedDocumentValidator, String)}
     */
    @Test
    void testVerifyDigest9() {
        XAdESSignature xAdESSignature = mock(XAdESSignature.class);
        when(xAdESSignature.getId()).thenReturn("42");

        ArrayList<AdvancedSignature> advancedSignatureList = new ArrayList<>();
        advancedSignatureList.add(xAdESSignature);
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getOriginalDocuments((String) any())).thenReturn(new ArrayList<>());
        when(detachedTimestampValidator.getSignatures()).thenReturn(advancedSignatureList);
        assertEquals("Invalid file digest", signatureService.verifyDigest(detachedTimestampValidator, "Checksum"));
        verify(detachedTimestampValidator).getSignatures();
        verify(detachedTimestampValidator).getOriginalDocuments((String) any());
        verify(xAdESSignature).getId();
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
    void testVerifyManagerTaxCode2() {
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(diagnosticDataJaxb, detailedReport, simpleReport, new ValidationReportType());

        ArrayList<User> userList = new ArrayList<>();
        assertEquals("No tax code has been found in digital signature",
                signatureService.verifyManagerTaxCode(reports, userList));
        assertEquals(userList, reports.getDiagnosticDataJaxb().getUsedCertificates());
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyManagerTaxCode3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at eu.europa.esig.dss.diagnostic.DiagnosticData.getUsedCertificates(DiagnosticData.java:931)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.extractSubjectSNCFs(SignatureService.java:153)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyManagerTaxCode(SignatureService.java:124)
        //   See https://diff.blue/R013 to resolve this issue.

        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(null, detailedReport, simpleReport, new ValidationReportType());

        signatureService.verifyManagerTaxCode(reports, new ArrayList<>());
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

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    void testVerifyManagerTaxCode5() {
        XmlDiagnosticData xmlDiagnosticData = mock(XmlDiagnosticData.class);
        when(xmlDiagnosticData.getUsedCertificates()).thenReturn(new ArrayList<>());
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(xmlDiagnosticData, detailedReport, simpleReport, new ValidationReportType());

        assertEquals("No tax code has been found in digital signature",
                signatureService.verifyManagerTaxCode(reports, new ArrayList<>()));
        verify(xmlDiagnosticData).getUsedCertificates();
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    void testVerifyManagerTaxCode6() {
        XmlBasicSignature xmlBasicSignature = new XmlBasicSignature();
        xmlBasicSignature.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature.setSignatureIntact(true);
        xmlBasicSignature.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue.setDigestValue(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlDigestAlgoAndValue.setMatch(true);

        XmlOriginalThirdCountryQcStatementsMapping xmlOriginalThirdCountryQcStatementsMapping = new XmlOriginalThirdCountryQcStatementsMapping();
        xmlOriginalThirdCountryQcStatementsMapping.setOtherOIDs(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping.setQcCClegislation(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping.setQcCompliance(new XmlQcCompliance());
        xmlOriginalThirdCountryQcStatementsMapping.setQcSSCD(new XmlQcSSCD());
        xmlOriginalThirdCountryQcStatementsMapping.setQcTypes(new ArrayList<>());

        XmlMRACertificateMapping xmlMRACertificateMapping = new XmlMRACertificateMapping();
        xmlMRACertificateMapping.setEnactedTrustServiceLegalIdentifier("42");
        xmlMRACertificateMapping.setOriginalThirdCountryMapping(xmlOriginalThirdCountryQcStatementsMapping);

        XmlPSD2QcInfo xmlPSD2QcInfo = new XmlPSD2QcInfo();
        xmlPSD2QcInfo.setNcaId("42");
        xmlPSD2QcInfo.setNcaName("42");
        xmlPSD2QcInfo.setRolesOfPSP(new ArrayList<>());

        XmlQcCompliance xmlQcCompliance = new XmlQcCompliance();
        xmlQcCompliance.setPresent(true);

        XmlQcEuLimitValue xmlQcEuLimitValue = new XmlQcEuLimitValue();
        xmlQcEuLimitValue.setAmount(42);
        xmlQcEuLimitValue.setCurrency("42");
        xmlQcEuLimitValue.setExponent(42);

        XmlQcSSCD xmlQcSSCD = new XmlQcSSCD();
        xmlQcSSCD.setPresent(true);

        XmlOID xmlOID = new XmlOID();
        xmlOID.setDescription("42");
        xmlOID.setValue("42");

        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setEnactedMRA(true);
        xmlQcStatements.setMRACertificateMapping(xmlMRACertificateMapping);
        xmlQcStatements.setOtherOIDs(new ArrayList<>());
        xmlQcStatements.setPSD2QcInfo(xmlPSD2QcInfo);
        xmlQcStatements.setQcCClegislation(new ArrayList<>());
        xmlQcStatements.setQcCompliance(xmlQcCompliance);
        xmlQcStatements.setQcEuLimitValue(xmlQcEuLimitValue);
        xmlQcStatements.setQcEuPDS(new ArrayList<>());
        xmlQcStatements.setQcEuRetentionPeriod(42);
        xmlQcStatements.setQcSSCD(xmlQcSSCD);
        xmlQcStatements.setQcTypes(new ArrayList<>());
        xmlQcStatements.setSemanticsIdentifier(xmlOID);

        XmlBasicSignature xmlBasicSignature1 = new XmlBasicSignature();
        xmlBasicSignature1.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature1.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature1.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature1.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature1.setSignatureIntact(true);
        xmlBasicSignature1.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue1 = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue1.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue1.setDigestValue(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlDigestAlgoAndValue1.setMatch(true);

        XmlQcStatements xmlQcStatements1 = new XmlQcStatements();
        xmlQcStatements1.setEnactedMRA(true);
        xmlQcStatements1.setMRACertificateMapping(new XmlMRACertificateMapping());
        xmlQcStatements1.setOtherOIDs(new ArrayList<>());
        xmlQcStatements1.setPSD2QcInfo(new XmlPSD2QcInfo());
        xmlQcStatements1.setQcCClegislation(new ArrayList<>());
        xmlQcStatements1.setQcCompliance(new XmlQcCompliance());
        xmlQcStatements1.setQcEuLimitValue(new XmlQcEuLimitValue());
        xmlQcStatements1.setQcEuPDS(new ArrayList<>());
        xmlQcStatements1.setQcEuRetentionPeriod(42);
        xmlQcStatements1.setQcSSCD(new XmlQcSSCD());
        xmlQcStatements1.setQcTypes(new ArrayList<>());
        xmlQcStatements1.setSemanticsIdentifier(new XmlOID());

        XmlSigningCertificate xmlSigningCertificate = new XmlSigningCertificate();
        xmlSigningCertificate.setCertificate(new XmlCertificate());
        xmlSigningCertificate.setPublicKey(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});

        XmlCertificate xmlCertificate = new XmlCertificate();
        xmlCertificate.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate.setBase64Encoded(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlCertificate.setBasicSignature(xmlBasicSignature1);
        xmlCertificate.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate.setCertificateChain(new ArrayList<>());
        xmlCertificate.setCertificatePolicies(new ArrayList<>());
        xmlCertificate.setCommonName("42");
        xmlCertificate.setCountryName("42");
        xmlCertificate.setDigestAlgoAndValue(xmlDigestAlgoAndValue1);
        xmlCertificate.setEmail("42");
        xmlCertificate.setEntityKey("42");
        xmlCertificate.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate.setGivenName("42");
        xmlCertificate.setId("42");
        xmlCertificate.setIdPkixOcspNoCheck(true);
        xmlCertificate.setKeyUsageBits(new ArrayList<>());
        xmlCertificate.setLocality("42");
        xmlCertificate
                .setNotAfter(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate.setNotBefore(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate.setOrganizationIdentifier("42");
        xmlCertificate.setOrganizationName("42");
        xmlCertificate.setOrganizationalUnit("42");
        xmlCertificate.setPseudonym("42");
        xmlCertificate.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.RSA);
        xmlCertificate.setPublicKeySize(42);
        xmlCertificate.setQcStatements(xmlQcStatements1);
        xmlCertificate.setRevocations(new ArrayList<>());
        xmlCertificate.setSelfSigned(true);
        xmlCertificate.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate.setSigningCertificate(xmlSigningCertificate);
        xmlCertificate.setSources(new ArrayList<>());
        xmlCertificate.setState("42");
        xmlCertificate.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate.setSubjectSerialNumber("42");
        xmlCertificate.setSurname("42");
        xmlCertificate.setTitle("42");
        xmlCertificate.setTrusted(true);
        xmlCertificate.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate.setValAssuredShortTermCertificate(true);

        XmlSigningCertificate xmlSigningCertificate1 = new XmlSigningCertificate();
        xmlSigningCertificate1.setCertificate(xmlCertificate);
        xmlSigningCertificate1.setPublicKey(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});

        XmlCertificate xmlCertificate1 = new XmlCertificate();
        xmlCertificate1.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate1.setBase64Encoded(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlCertificate1.setBasicSignature(xmlBasicSignature);
        xmlCertificate1.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate1.setCertificateChain(new ArrayList<>());
        xmlCertificate1.setCertificatePolicies(new ArrayList<>());
        xmlCertificate1.setCommonName("42");
        xmlCertificate1.setCountryName("42");
        xmlCertificate1.setDigestAlgoAndValue(xmlDigestAlgoAndValue);
        xmlCertificate1.setEmail("42");
        xmlCertificate1.setEntityKey("42");
        xmlCertificate1.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate1.setGivenName("42");
        xmlCertificate1.setId("42");
        xmlCertificate1.setIdPkixOcspNoCheck(true);
        xmlCertificate1.setKeyUsageBits(new ArrayList<>());
        xmlCertificate1.setLocality("42");
        xmlCertificate1
                .setNotAfter(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate1.setNotBefore(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate1.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate1.setOrganizationIdentifier("42");
        xmlCertificate1.setOrganizationName("42");
        xmlCertificate1.setOrganizationalUnit("42");
        xmlCertificate1.setPseudonym("42");
        xmlCertificate1.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.RSA);
        xmlCertificate1.setPublicKeySize(42);
        xmlCertificate1.setQcStatements(xmlQcStatements);
        xmlCertificate1.setRevocations(new ArrayList<>());
        xmlCertificate1.setSelfSigned(true);
        xmlCertificate1.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate1.setSigningCertificate(xmlSigningCertificate1);
        xmlCertificate1.setSources(new ArrayList<>());
        xmlCertificate1.setState("42");
        xmlCertificate1.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate1.setSubjectSerialNumber("42");
        xmlCertificate1.setSurname("42");
        xmlCertificate1.setTitle("42");
        xmlCertificate1.setTrusted(true);
        xmlCertificate1.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate1.setValAssuredShortTermCertificate(true);

        ArrayList<XmlCertificate> xmlCertificateList = new ArrayList<>();
        xmlCertificateList.add(xmlCertificate1);
        XmlDiagnosticData xmlDiagnosticData = mock(XmlDiagnosticData.class);
        when(xmlDiagnosticData.getUsedCertificates()).thenReturn(xmlCertificateList);
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(xmlDiagnosticData, detailedReport, simpleReport, new ValidationReportType());

        assertEquals("No tax code has been found in digital signature",
                signatureService.verifyManagerTaxCode(reports, new ArrayList<>()));
        verify(xmlDiagnosticData).getUsedCertificates();
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyManagerTaxCode7() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalStateException: No match found
        //       at java.util.regex.Matcher.group(Matcher.java:645)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.lambda$extractTaxCode$3(SignatureService.java:141)
        //       at java.util.ArrayList.forEach(ArrayList.java:1540)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.extractTaxCode(SignatureService.java:139)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyManagerTaxCode(SignatureService.java:129)
        //   See https://diff.blue/R013 to resolve this issue.

        XmlBasicSignature xmlBasicSignature = new XmlBasicSignature();
        xmlBasicSignature.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature.setSignatureIntact(true);
        xmlBasicSignature.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue.setDigestValue(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlDigestAlgoAndValue.setMatch(true);

        XmlOriginalThirdCountryQcStatementsMapping xmlOriginalThirdCountryQcStatementsMapping = new XmlOriginalThirdCountryQcStatementsMapping();
        xmlOriginalThirdCountryQcStatementsMapping.setOtherOIDs(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping.setQcCClegislation(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping.setQcCompliance(new XmlQcCompliance());
        xmlOriginalThirdCountryQcStatementsMapping.setQcSSCD(new XmlQcSSCD());
        xmlOriginalThirdCountryQcStatementsMapping.setQcTypes(new ArrayList<>());

        XmlMRACertificateMapping xmlMRACertificateMapping = new XmlMRACertificateMapping();
        xmlMRACertificateMapping.setEnactedTrustServiceLegalIdentifier("42");
        xmlMRACertificateMapping.setOriginalThirdCountryMapping(xmlOriginalThirdCountryQcStatementsMapping);

        XmlPSD2QcInfo xmlPSD2QcInfo = new XmlPSD2QcInfo();
        xmlPSD2QcInfo.setNcaId("42");
        xmlPSD2QcInfo.setNcaName("42");
        xmlPSD2QcInfo.setRolesOfPSP(new ArrayList<>());

        XmlQcCompliance xmlQcCompliance = new XmlQcCompliance();
        xmlQcCompliance.setPresent(true);

        XmlQcEuLimitValue xmlQcEuLimitValue = new XmlQcEuLimitValue();
        xmlQcEuLimitValue.setAmount(42);
        xmlQcEuLimitValue.setCurrency("42");
        xmlQcEuLimitValue.setExponent(42);

        XmlQcSSCD xmlQcSSCD = new XmlQcSSCD();
        xmlQcSSCD.setPresent(true);

        XmlOID xmlOID = new XmlOID();
        xmlOID.setDescription("42");
        xmlOID.setValue("42");

        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setEnactedMRA(true);
        xmlQcStatements.setMRACertificateMapping(xmlMRACertificateMapping);
        xmlQcStatements.setOtherOIDs(new ArrayList<>());
        xmlQcStatements.setPSD2QcInfo(xmlPSD2QcInfo);
        xmlQcStatements.setQcCClegislation(new ArrayList<>());
        xmlQcStatements.setQcCompliance(xmlQcCompliance);
        xmlQcStatements.setQcEuLimitValue(xmlQcEuLimitValue);
        xmlQcStatements.setQcEuPDS(new ArrayList<>());
        xmlQcStatements.setQcEuRetentionPeriod(42);
        xmlQcStatements.setQcSSCD(xmlQcSSCD);
        xmlQcStatements.setQcTypes(new ArrayList<>());
        xmlQcStatements.setSemanticsIdentifier(xmlOID);

        XmlBasicSignature xmlBasicSignature1 = new XmlBasicSignature();
        xmlBasicSignature1.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature1.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature1.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature1.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature1.setSignatureIntact(true);
        xmlBasicSignature1.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue1 = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue1.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue1.setDigestValue(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlDigestAlgoAndValue1.setMatch(true);

        XmlQcStatements xmlQcStatements1 = new XmlQcStatements();
        xmlQcStatements1.setEnactedMRA(true);
        xmlQcStatements1.setMRACertificateMapping(new XmlMRACertificateMapping());
        xmlQcStatements1.setOtherOIDs(new ArrayList<>());
        xmlQcStatements1.setPSD2QcInfo(new XmlPSD2QcInfo());
        xmlQcStatements1.setQcCClegislation(new ArrayList<>());
        xmlQcStatements1.setQcCompliance(new XmlQcCompliance());
        xmlQcStatements1.setQcEuLimitValue(new XmlQcEuLimitValue());
        xmlQcStatements1.setQcEuPDS(new ArrayList<>());
        xmlQcStatements1.setQcEuRetentionPeriod(42);
        xmlQcStatements1.setQcSSCD(new XmlQcSSCD());
        xmlQcStatements1.setQcTypes(new ArrayList<>());
        xmlQcStatements1.setSemanticsIdentifier(new XmlOID());

        XmlSigningCertificate xmlSigningCertificate = new XmlSigningCertificate();
        xmlSigningCertificate.setCertificate(new XmlCertificate());
        xmlSigningCertificate.setPublicKey(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});

        XmlCertificate xmlCertificate = new XmlCertificate();
        xmlCertificate.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate.setBase64Encoded(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlCertificate.setBasicSignature(xmlBasicSignature1);
        xmlCertificate.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate.setCertificateChain(new ArrayList<>());
        xmlCertificate.setCertificatePolicies(new ArrayList<>());
        xmlCertificate.setCommonName("42");
        xmlCertificate.setCountryName("42");
        xmlCertificate.setDigestAlgoAndValue(xmlDigestAlgoAndValue1);
        xmlCertificate.setEmail("42");
        xmlCertificate.setEntityKey("42");
        xmlCertificate.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate.setGivenName("42");
        xmlCertificate.setId("42");
        xmlCertificate.setIdPkixOcspNoCheck(true);
        xmlCertificate.setKeyUsageBits(new ArrayList<>());
        xmlCertificate.setLocality("42");
        xmlCertificate
                .setNotAfter(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate.setNotBefore(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate.setOrganizationIdentifier("42");
        xmlCertificate.setOrganizationName("42");
        xmlCertificate.setOrganizationalUnit("42");
        xmlCertificate.setPseudonym("42");
        xmlCertificate.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.RSA);
        xmlCertificate.setPublicKeySize(42);
        xmlCertificate.setQcStatements(xmlQcStatements1);
        xmlCertificate.setRevocations(new ArrayList<>());
        xmlCertificate.setSelfSigned(true);
        xmlCertificate.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate.setSigningCertificate(xmlSigningCertificate);
        xmlCertificate.setSources(new ArrayList<>());
        xmlCertificate.setState("42");
        xmlCertificate.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate.setSubjectSerialNumber("42");
        xmlCertificate.setSurname("42");
        xmlCertificate.setTitle("42");
        xmlCertificate.setTrusted(true);
        xmlCertificate.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate.setValAssuredShortTermCertificate(true);

        XmlSigningCertificate xmlSigningCertificate1 = new XmlSigningCertificate();
        xmlSigningCertificate1.setCertificate(xmlCertificate);
        xmlSigningCertificate1.setPublicKey(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});

        XmlCertificate xmlCertificate1 = new XmlCertificate();
        xmlCertificate1.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate1.setBase64Encoded(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        xmlCertificate1.setBasicSignature(xmlBasicSignature);
        xmlCertificate1.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate1.setCertificateChain(new ArrayList<>());
        xmlCertificate1.setCertificatePolicies(new ArrayList<>());
        xmlCertificate1.setCommonName("42");
        xmlCertificate1.setCountryName("42");
        xmlCertificate1.setDigestAlgoAndValue(xmlDigestAlgoAndValue);
        xmlCertificate1.setEmail("42");
        xmlCertificate1.setEntityKey("42");
        xmlCertificate1.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate1.setGivenName("42");
        xmlCertificate1.setId("42");
        xmlCertificate1.setIdPkixOcspNoCheck(true);
        xmlCertificate1.setKeyUsageBits(new ArrayList<>());
        xmlCertificate1.setLocality("42");
        xmlCertificate1
                .setNotAfter(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate1.setNotBefore(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate1.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate1.setOrganizationIdentifier("42");
        xmlCertificate1.setOrganizationName("42");
        xmlCertificate1.setOrganizationalUnit("42");
        xmlCertificate1.setPseudonym("42");
        xmlCertificate1.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.RSA);
        xmlCertificate1.setPublicKeySize(42);
        xmlCertificate1.setQcStatements(xmlQcStatements);
        xmlCertificate1.setRevocations(new ArrayList<>());
        xmlCertificate1.setSelfSigned(true);
        xmlCertificate1.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate1.setSigningCertificate(xmlSigningCertificate1);
        xmlCertificate1.setSources(new ArrayList<>());
        xmlCertificate1.setState("42");
        xmlCertificate1.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate1.setSubjectSerialNumber("42");
        xmlCertificate1.setSurname("42");
        xmlCertificate1.setTitle("42");
        xmlCertificate1.setTrusted(true);
        xmlCertificate1.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate1.setValAssuredShortTermCertificate(true);

        XmlBasicSignature xmlBasicSignature2 = new XmlBasicSignature();
        xmlBasicSignature2.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA224);
        xmlBasicSignature2.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.DSA);
        xmlBasicSignature2.setKeyLengthUsedToSignThisToken("TINIT-U");
        xmlBasicSignature2.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature2.setSignatureIntact(false);
        xmlBasicSignature2.setSignatureValid(false);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue2 = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue2.setDigestMethod(DigestAlgorithm.SHA224);
        xmlDigestAlgoAndValue2.setDigestValue(new byte[]{'A', 6, 'A', 6, 'A', 6, 'A', 6});
        xmlDigestAlgoAndValue2.setMatch(false);

        XmlOriginalThirdCountryQcStatementsMapping xmlOriginalThirdCountryQcStatementsMapping1 = new XmlOriginalThirdCountryQcStatementsMapping();
        xmlOriginalThirdCountryQcStatementsMapping1.setOtherOIDs(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcCClegislation(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcCompliance(new XmlQcCompliance());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcSSCD(new XmlQcSSCD());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcTypes(new ArrayList<>());

        XmlMRACertificateMapping xmlMRACertificateMapping1 = new XmlMRACertificateMapping();
        xmlMRACertificateMapping1.setEnactedTrustServiceLegalIdentifier("TINIT-U");
        xmlMRACertificateMapping1.setOriginalThirdCountryMapping(xmlOriginalThirdCountryQcStatementsMapping1);

        XmlPSD2QcInfo xmlPSD2QcInfo1 = new XmlPSD2QcInfo();
        xmlPSD2QcInfo1.setNcaId("TINIT-U");
        xmlPSD2QcInfo1.setNcaName("TINIT-U");
        xmlPSD2QcInfo1.setRolesOfPSP(new ArrayList<>());

        XmlQcCompliance xmlQcCompliance1 = new XmlQcCompliance();
        xmlQcCompliance1.setPresent(false);

        XmlQcEuLimitValue xmlQcEuLimitValue1 = new XmlQcEuLimitValue();
        xmlQcEuLimitValue1.setAmount(6);
        xmlQcEuLimitValue1.setCurrency("TINIT-U");
        xmlQcEuLimitValue1.setExponent(6);

        XmlQcSSCD xmlQcSSCD1 = new XmlQcSSCD();
        xmlQcSSCD1.setPresent(false);

        XmlOID xmlOID1 = new XmlOID();
        xmlOID1.setDescription("TINIT-U");
        xmlOID1.setValue("TINIT-U");

        XmlQcStatements xmlQcStatements2 = new XmlQcStatements();
        xmlQcStatements2.setEnactedMRA(false);
        xmlQcStatements2.setMRACertificateMapping(xmlMRACertificateMapping1);
        xmlQcStatements2.setOtherOIDs(new ArrayList<>());
        xmlQcStatements2.setPSD2QcInfo(xmlPSD2QcInfo1);
        xmlQcStatements2.setQcCClegislation(new ArrayList<>());
        xmlQcStatements2.setQcCompliance(xmlQcCompliance1);
        xmlQcStatements2.setQcEuLimitValue(xmlQcEuLimitValue1);
        xmlQcStatements2.setQcEuPDS(new ArrayList<>());
        xmlQcStatements2.setQcEuRetentionPeriod(6);
        xmlQcStatements2.setQcSSCD(xmlQcSSCD1);
        xmlQcStatements2.setQcTypes(new ArrayList<>());
        xmlQcStatements2.setSemanticsIdentifier(xmlOID1);

        XmlBasicSignature xmlBasicSignature3 = new XmlBasicSignature();
        xmlBasicSignature3.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA224);
        xmlBasicSignature3.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.DSA);
        xmlBasicSignature3.setKeyLengthUsedToSignThisToken("TINIT-U");
        xmlBasicSignature3.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature3.setSignatureIntact(false);
        xmlBasicSignature3.setSignatureValid(false);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue3 = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue3.setDigestMethod(DigestAlgorithm.SHA224);
        xmlDigestAlgoAndValue3.setDigestValue(new byte[]{'A', 6, 'A', 6, 'A', 6, 'A', 6});
        xmlDigestAlgoAndValue3.setMatch(false);

        XmlQcStatements xmlQcStatements3 = new XmlQcStatements();
        xmlQcStatements3.setEnactedMRA(false);
        xmlQcStatements3.setMRACertificateMapping(new XmlMRACertificateMapping());
        xmlQcStatements3.setOtherOIDs(new ArrayList<>());
        xmlQcStatements3.setPSD2QcInfo(new XmlPSD2QcInfo());
        xmlQcStatements3.setQcCClegislation(new ArrayList<>());
        xmlQcStatements3.setQcCompliance(new XmlQcCompliance());
        xmlQcStatements3.setQcEuLimitValue(new XmlQcEuLimitValue());
        xmlQcStatements3.setQcEuPDS(new ArrayList<>());
        xmlQcStatements3.setQcEuRetentionPeriod(6);
        xmlQcStatements3.setQcSSCD(new XmlQcSSCD());
        xmlQcStatements3.setQcTypes(new ArrayList<>());
        xmlQcStatements3.setSemanticsIdentifier(new XmlOID());

        XmlSigningCertificate xmlSigningCertificate2 = new XmlSigningCertificate();
        xmlSigningCertificate2.setCertificate(new XmlCertificate());
        xmlSigningCertificate2.setPublicKey(new byte[]{'A', 6, 'A', 6, 'A', 6, 'A', 6});

        XmlCertificate xmlCertificate2 = new XmlCertificate();
        xmlCertificate2.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate2.setBase64Encoded(new byte[]{'A', 6, 'A', 6, 'A', 6, 'A', 6});
        xmlCertificate2.setBasicSignature(xmlBasicSignature3);
        xmlCertificate2.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate2.setCertificateChain(new ArrayList<>());
        xmlCertificate2.setCertificatePolicies(new ArrayList<>());
        xmlCertificate2.setCommonName("TINIT-U");
        xmlCertificate2.setCountryName("TINIT-U");
        xmlCertificate2.setDigestAlgoAndValue(xmlDigestAlgoAndValue3);
        xmlCertificate2.setEmail("TINIT-U");
        xmlCertificate2.setEntityKey("TINIT-U");
        xmlCertificate2.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate2.setGivenName("TINIT-U");
        xmlCertificate2.setId("TINIT-U");
        xmlCertificate2.setIdPkixOcspNoCheck(false);
        xmlCertificate2.setKeyUsageBits(new ArrayList<>());
        xmlCertificate2.setLocality("TINIT-U");
        xmlCertificate2
                .setNotAfter(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate2.setNotBefore(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate2.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate2.setOrganizationIdentifier("TINIT-U");
        xmlCertificate2.setOrganizationName("TINIT-U");
        xmlCertificate2.setOrganizationalUnit("TINIT-U");
        xmlCertificate2.setPseudonym("TINIT-U");
        xmlCertificate2.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.DSA);
        xmlCertificate2.setPublicKeySize(6);
        xmlCertificate2.setQcStatements(xmlQcStatements3);
        xmlCertificate2.setRevocations(new ArrayList<>());
        xmlCertificate2.setSelfSigned(false);
        xmlCertificate2.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate2.setSigningCertificate(xmlSigningCertificate2);
        xmlCertificate2.setSources(new ArrayList<>());
        xmlCertificate2.setState("TINIT-U");
        xmlCertificate2.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate2.setSubjectSerialNumber("TINIT-U");
        xmlCertificate2.setSurname("TINIT-U");
        xmlCertificate2.setTitle("TINIT-U");
        xmlCertificate2.setTrusted(false);
        xmlCertificate2.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate2.setValAssuredShortTermCertificate(false);

        XmlSigningCertificate xmlSigningCertificate3 = new XmlSigningCertificate();
        xmlSigningCertificate3.setCertificate(xmlCertificate2);
        xmlSigningCertificate3.setPublicKey(new byte[]{'A', 6, 'A', 6, 'A', 6, 'A', 6});

        XmlCertificate xmlCertificate3 = new XmlCertificate();
        xmlCertificate3.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate3.setBase64Encoded(new byte[]{'A', 6, 'A', 6, 'A', 6, 'A', 6});
        xmlCertificate3.setBasicSignature(xmlBasicSignature2);
        xmlCertificate3.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate3.setCertificateChain(new ArrayList<>());
        xmlCertificate3.setCertificatePolicies(new ArrayList<>());
        xmlCertificate3.setCommonName("TINIT-U");
        xmlCertificate3.setCountryName("TINIT-U");
        xmlCertificate3.setDigestAlgoAndValue(xmlDigestAlgoAndValue2);
        xmlCertificate3.setEmail("TINIT-U");
        xmlCertificate3.setEntityKey("TINIT-U");
        xmlCertificate3.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate3.setGivenName("TINIT-U");
        xmlCertificate3.setId("TINIT-U");
        xmlCertificate3.setIdPkixOcspNoCheck(false);
        xmlCertificate3.setKeyUsageBits(new ArrayList<>());
        xmlCertificate3.setLocality("TINIT-U");
        xmlCertificate3
                .setNotAfter(java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate3.setNotBefore(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        xmlCertificate3.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate3.setOrganizationIdentifier("TINIT-U");
        xmlCertificate3.setOrganizationName("TINIT-U");
        xmlCertificate3.setOrganizationalUnit("TINIT-U");
        xmlCertificate3.setPseudonym("TINIT-U");
        xmlCertificate3.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.DSA);
        xmlCertificate3.setPublicKeySize(6);
        xmlCertificate3.setQcStatements(xmlQcStatements2);
        xmlCertificate3.setRevocations(new ArrayList<>());
        xmlCertificate3.setSelfSigned(false);
        xmlCertificate3.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate3.setSigningCertificate(xmlSigningCertificate3);
        xmlCertificate3.setSources(new ArrayList<>());
        xmlCertificate3.setState("TINIT-U");
        xmlCertificate3.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate3.setSubjectSerialNumber("TINIT-U");
        xmlCertificate3.setSurname("TINIT-U");
        xmlCertificate3.setTitle("TINIT-U");
        xmlCertificate3.setTrusted(false);
        xmlCertificate3.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate3.setValAssuredShortTermCertificate(false);

        ArrayList<XmlCertificate> xmlCertificateList = new ArrayList<>();
        xmlCertificateList.add(xmlCertificate3);
        xmlCertificateList.add(xmlCertificate1);
        XmlDiagnosticData xmlDiagnosticData = mock(XmlDiagnosticData.class);
        when(xmlDiagnosticData.getUsedCertificates()).thenReturn(xmlCertificateList);
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        XmlSimpleReport simpleReport = new XmlSimpleReport();
        Reports reports = new Reports(xmlDiagnosticData, detailedReport, simpleReport, new ValidationReportType());

        signatureService.verifyManagerTaxCode(reports, new ArrayList<>());
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testVerifyManagerTaxCode8() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.SignatureService.extractSubjectSNCFs(SignatureService.java:152)
        //       at it.pagopa.selfcare.mscore.core.SignatureService.verifyManagerTaxCode(SignatureService.java:124)
        //   See https://diff.blue/R013 to resolve this issue.

        signatureService.verifyManagerTaxCode(null, new ArrayList<>());
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    void testVerifyManagerTaxCode9() {
        Reports reports = mock(Reports.class);
        when(reports.getDiagnosticData()).thenReturn(null);
        assertEquals("No tax code has been found in digital signature",
                signatureService.verifyManagerTaxCode(reports, new ArrayList<>()));
        verify(reports).getDiagnosticData();
    }
}

