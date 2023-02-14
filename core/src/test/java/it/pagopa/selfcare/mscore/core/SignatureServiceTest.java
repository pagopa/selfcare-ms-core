package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.MaskGenerationFunction;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.simplereport.jaxb.XmlSimpleReport;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.timestamp.DetachedTimestampValidator;
import eu.europa.esig.dss.xades.validation.XAdESSignature;
import eu.europa.esig.validationreport.enums.SignatureValidationProcessID;
import eu.europa.esig.validationreport.enums.TypeOfProof;
import eu.europa.esig.validationreport.jaxb.POEType;
import eu.europa.esig.validationreport.jaxb.SignatureAttributesType;
import eu.europa.esig.validationreport.jaxb.SignatureIdentifierType;
import eu.europa.esig.validationreport.jaxb.SignatureQualityType;
import eu.europa.esig.validationreport.jaxb.SignatureValidationPolicyType;
import eu.europa.esig.validationreport.jaxb.SignatureValidationProcessType;
import eu.europa.esig.validationreport.jaxb.SignatureValidationReportType;
import eu.europa.esig.validationreport.jaxb.SignerInformationType;
import eu.europa.esig.validationreport.jaxb.SignersDocumentType;
import eu.europa.esig.validationreport.jaxb.VOReferenceType;
import eu.europa.esig.validationreport.jaxb.ValidationConstraintsEvaluationReportType;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import eu.europa.esig.validationreport.jaxb.ValidationStatusType;
import eu.europa.esig.validationreport.jaxb.ValidationTimeInfoType;
import eu.europa.esig.xades.jaxb.xades132.DigestAlgAndValueType;
import eu.europa.esig.xades.jaxb.xades132.SignaturePolicyIdType;
import eu.europa.esig.xades.jaxb.xades132.SignaturePolicyIdentifierType;
import eu.europa.esig.xmldsig.jaxb.DigestMethodType;
import eu.europa.esig.xmldsig.jaxb.SignatureValueType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.User;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.batik.anim.dom.BindableElement;
import org.apache.batik.anim.dom.SVG12DOMImplementation;
import org.apache.batik.anim.dom.SVG12OMDocument;
import org.apache.batik.dom.GenericDocumentType;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SignatureService.class})
@ExtendWith(SpringExtension.class)
class SignatureServiceTest {
    @Autowired
    private SignatureService signatureService;

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
    void testVerifyOriginalDocument3() {
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getSignatures()).thenReturn(new ArrayList<>());
        signatureService.verifyOriginalDocument(detachedTimestampValidator);
        verify(detachedTimestampValidator).getSignatures();
    }

    /**
     * Method under test: {@link SignatureService#validateDocument(SignedDocumentValidator)}
     */
    @Test
    void testValidateDocument() {

        SignedDocumentValidator signedDocumentValidator = Mockito.mock(SignedDocumentValidator.class);
        when(signedDocumentValidator.validateDocument()).thenReturn(Mockito.mock(Reports.class));
        assertDoesNotThrow(() -> this.signatureService.validateDocument(signedDocumentValidator));
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
        SignedDocumentValidator signedDocumentValidator = mock(SignedDocumentValidator.class);
        when(signedDocumentValidator.getSignatures()).thenReturn(new ArrayList<>());
        assertEquals("VALID", signatureService.verifySignatureForm(signedDocumentValidator));
        verify(signedDocumentValidator).getSignatures();
    }

    /**
     * Method under test: {@link SignatureService#verifySignatureForm(SignedDocumentValidator)}
     */
    @Test
    void testVerifySignatureForm4() {
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
    void testVerifySignatureForm6() {
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
        List<OrphanCertificateTokenWrapper> expectedSignatureValidationReport = reports.getDiagnosticData()
                .getAllOrphanCertificateObjects();
        assertEquals(expectedSignatureValidationReport,
                reports.getEtsiValidationReportJaxb().getSignatureValidationReport());
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
        ValidationReportType validationReportType = mock(ValidationReportType.class);
        when(validationReportType.getSignatureValidationReport()).thenReturn(new ArrayList<>());
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        assertEquals("Document signature is invalid", signatureService.verifySignature(
                new Reports(diagnosticDataJaxb, detailedReport, new XmlSimpleReport(), validationReportType)));
        verify(validationReportType).getSignatureValidationReport();
    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    void testVerifySignature4() throws UnsupportedEncodingException {
        DigestMethodType digestMethodType = new DigestMethodType();
        digestMethodType.setAlgorithm("42");

        DigestAlgAndValueType digestAlgAndValueType = new DigestAlgAndValueType();
        digestAlgAndValueType.setDigestMethod(digestMethodType);
        digestAlgAndValueType.setDigestValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureValueType signatureValueType = new SignatureValueType();
        signatureValueType.setId("42");
        signatureValueType.setValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureIdentifierType signatureIdentifierType = new SignatureIdentifierType();
        signatureIdentifierType.setDAIdentifier("42");
        signatureIdentifierType.setDigestAlgAndValue(digestAlgAndValueType);
        signatureIdentifierType.setDocHashOnly(true);
        signatureIdentifierType.setHashOnly(true);
        signatureIdentifierType.setId("42");
        signatureIdentifierType.setSignatureValue(signatureValueType);

        SignatureValidationProcessType signatureValidationProcessType = new SignatureValidationProcessType();
        signatureValidationProcessType.setAny("Value");
        signatureValidationProcessType.setSignatureValidationPracticeStatement("42");
        signatureValidationProcessType.setSignatureValidationProcessID(SignatureValidationProcessID.BASIC);
        signatureValidationProcessType.setSignatureValidationServicePolicy("42");

        ValidationStatusType validationStatusType = new ValidationStatusType();
        validationStatusType.setMainIndication(Indication.TOTAL_PASSED);

        VOReferenceType voReferenceType = new VOReferenceType();
        voReferenceType.setAny("Value");

        SignerInformationType signerInformationType = new SignerInformationType();
        signerInformationType.setAny("Value");
        signerInformationType.setPseudonym(true);
        signerInformationType.setSigner("42");
        signerInformationType.setSignerCertificate(voReferenceType);

        VOReferenceType voReferenceType1 = new VOReferenceType();
        voReferenceType1.setAny("Value");

        SignaturePolicyIdentifierType signaturePolicyIdentifierType = new SignaturePolicyIdentifierType();
        signaturePolicyIdentifierType.setSignaturePolicyId(new SignaturePolicyIdType());
        signaturePolicyIdentifierType.setSignaturePolicyImplied("Value");

        SignatureValidationPolicyType signatureValidationPolicyType = new SignatureValidationPolicyType();
        signatureValidationPolicyType.setFormalPolicyObject(voReferenceType1);
        signatureValidationPolicyType.setFormalPolicyURI("42");
        signatureValidationPolicyType.setPolicyName("42");
        signatureValidationPolicyType.setReadablePolicyURI("42");
        signatureValidationPolicyType.setSignaturePolicyIdentifier(signaturePolicyIdentifierType);

        ValidationConstraintsEvaluationReportType validationConstraintsEvaluationReportType = new ValidationConstraintsEvaluationReportType();
        validationConstraintsEvaluationReportType.setSignatureValidationPolicy(signatureValidationPolicyType);

        VOReferenceType voReferenceType2 = new VOReferenceType();
        voReferenceType2.setAny("Value");

        POEType poeType = new POEType();
        poeType.setPOEObject(voReferenceType2);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        poeType.setPOETime(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        poeType.setTypeOfProof(TypeOfProof.VALIDATION);

        ValidationTimeInfoType validationTimeInfoType = new ValidationTimeInfoType();
        validationTimeInfoType.setBestSignatureTime(poeType);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        validationTimeInfoType.setValidationTime(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        SignatureValidationReportType signatureValidationReportType = new SignatureValidationReportType();
        signatureValidationReportType.setSignatureAttributes(new SignatureAttributesType());
        signatureValidationReportType.setSignatureIdentifier(signatureIdentifierType);
        signatureValidationReportType.setSignatureQuality(new SignatureQualityType());
        signatureValidationReportType.setSignatureValidationProcess(signatureValidationProcessType);
        signatureValidationReportType.setSignatureValidationStatus(validationStatusType);
        signatureValidationReportType.setSignerInformation(signerInformationType);
        signatureValidationReportType.setSignersDocument(new SignersDocumentType());
        signatureValidationReportType.setValidationConstraintsEvaluationReport(validationConstraintsEvaluationReportType);
        signatureValidationReportType.setValidationTimeInfo(validationTimeInfoType);

        ArrayList<SignatureValidationReportType> signatureValidationReportTypeList = new ArrayList<>();
        signatureValidationReportTypeList.add(signatureValidationReportType);
        ValidationReportType validationReportType = mock(ValidationReportType.class);
        when(validationReportType.getSignatureValidationReport()).thenReturn(signatureValidationReportTypeList);
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        assertEquals("VALID", signatureService.verifySignature(
                new Reports(diagnosticDataJaxb, detailedReport, new XmlSimpleReport(), validationReportType)));
        verify(validationReportType).getSignatureValidationReport();
    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    void testVerifySignature5() throws UnsupportedEncodingException {
        DigestMethodType digestMethodType = new DigestMethodType();
        digestMethodType.setAlgorithm("42");

        DigestAlgAndValueType digestAlgAndValueType = new DigestAlgAndValueType();
        digestAlgAndValueType.setDigestMethod(digestMethodType);
        digestAlgAndValueType.setDigestValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureValueType signatureValueType = new SignatureValueType();
        signatureValueType.setId("42");
        signatureValueType.setValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureIdentifierType signatureIdentifierType = new SignatureIdentifierType();
        signatureIdentifierType.setDAIdentifier("42");
        signatureIdentifierType.setDigestAlgAndValue(digestAlgAndValueType);
        signatureIdentifierType.setDocHashOnly(true);
        signatureIdentifierType.setHashOnly(true);
        signatureIdentifierType.setId("42");
        signatureIdentifierType.setSignatureValue(signatureValueType);

        SignatureValidationProcessType signatureValidationProcessType = new SignatureValidationProcessType();
        signatureValidationProcessType.setAny("Value");
        signatureValidationProcessType.setSignatureValidationPracticeStatement("42");
        signatureValidationProcessType.setSignatureValidationProcessID(SignatureValidationProcessID.BASIC);
        signatureValidationProcessType.setSignatureValidationServicePolicy("42");

        ValidationStatusType validationStatusType = new ValidationStatusType();
        validationStatusType.setMainIndication(Indication.TOTAL_PASSED);

        VOReferenceType voReferenceType = new VOReferenceType();
        voReferenceType.setAny("Value");

        SignerInformationType signerInformationType = new SignerInformationType();
        signerInformationType.setAny("Value");
        signerInformationType.setPseudonym(true);
        signerInformationType.setSigner("42");
        signerInformationType.setSignerCertificate(voReferenceType);

        VOReferenceType voReferenceType1 = new VOReferenceType();
        voReferenceType1.setAny("Value");

        SignaturePolicyIdentifierType signaturePolicyIdentifierType = new SignaturePolicyIdentifierType();
        signaturePolicyIdentifierType.setSignaturePolicyId(new SignaturePolicyIdType());
        signaturePolicyIdentifierType.setSignaturePolicyImplied("Value");

        SignatureValidationPolicyType signatureValidationPolicyType = new SignatureValidationPolicyType();
        signatureValidationPolicyType.setFormalPolicyObject(voReferenceType1);
        signatureValidationPolicyType.setFormalPolicyURI("42");
        signatureValidationPolicyType.setPolicyName("42");
        signatureValidationPolicyType.setReadablePolicyURI("42");
        signatureValidationPolicyType.setSignaturePolicyIdentifier(signaturePolicyIdentifierType);

        ValidationConstraintsEvaluationReportType validationConstraintsEvaluationReportType = new ValidationConstraintsEvaluationReportType();
        validationConstraintsEvaluationReportType.setSignatureValidationPolicy(signatureValidationPolicyType);

        VOReferenceType voReferenceType2 = new VOReferenceType();
        voReferenceType2.setAny("Value");

        POEType poeType = new POEType();
        poeType.setPOEObject(voReferenceType2);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        poeType.setPOETime(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        poeType.setTypeOfProof(TypeOfProof.VALIDATION);

        ValidationTimeInfoType validationTimeInfoType = new ValidationTimeInfoType();
        validationTimeInfoType.setBestSignatureTime(poeType);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        validationTimeInfoType.setValidationTime(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        SignatureValidationReportType signatureValidationReportType = new SignatureValidationReportType();
        signatureValidationReportType.setSignatureAttributes(new SignatureAttributesType());
        signatureValidationReportType.setSignatureIdentifier(signatureIdentifierType);
        signatureValidationReportType.setSignatureQuality(new SignatureQualityType());
        signatureValidationReportType.setSignatureValidationProcess(signatureValidationProcessType);
        signatureValidationReportType.setSignatureValidationStatus(validationStatusType);
        signatureValidationReportType.setSignerInformation(signerInformationType);
        signatureValidationReportType.setSignersDocument(new SignersDocumentType());
        signatureValidationReportType.setValidationConstraintsEvaluationReport(validationConstraintsEvaluationReportType);
        signatureValidationReportType.setValidationTimeInfo(validationTimeInfoType);

        DigestMethodType digestMethodType1 = new DigestMethodType();
        digestMethodType1.setAlgorithm("42");

        DigestAlgAndValueType digestAlgAndValueType1 = new DigestAlgAndValueType();
        digestAlgAndValueType1.setDigestMethod(digestMethodType1);
        digestAlgAndValueType1.setDigestValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureValueType signatureValueType1 = new SignatureValueType();
        signatureValueType1.setId("42");
        signatureValueType1.setValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureIdentifierType signatureIdentifierType1 = new SignatureIdentifierType();
        signatureIdentifierType1.setDAIdentifier("42");
        signatureIdentifierType1.setDigestAlgAndValue(digestAlgAndValueType1);
        signatureIdentifierType1.setDocHashOnly(true);
        signatureIdentifierType1.setHashOnly(true);
        signatureIdentifierType1.setId("42");
        signatureIdentifierType1.setSignatureValue(signatureValueType1);

        SignatureValidationProcessType signatureValidationProcessType1 = new SignatureValidationProcessType();
        signatureValidationProcessType1.setAny("Value");
        signatureValidationProcessType1.setSignatureValidationPracticeStatement("42");
        signatureValidationProcessType1.setSignatureValidationProcessID(SignatureValidationProcessID.BASIC);
        signatureValidationProcessType1.setSignatureValidationServicePolicy("42");

        ValidationStatusType validationStatusType1 = new ValidationStatusType();
        validationStatusType1.setMainIndication(Indication.TOTAL_PASSED);

        VOReferenceType voReferenceType3 = new VOReferenceType();
        voReferenceType3.setAny("Value");

        SignerInformationType signerInformationType1 = new SignerInformationType();
        signerInformationType1.setAny("Value");
        signerInformationType1.setPseudonym(true);
        signerInformationType1.setSigner("42");
        signerInformationType1.setSignerCertificate(voReferenceType3);

        VOReferenceType voReferenceType4 = new VOReferenceType();
        voReferenceType4.setAny("Value");

        SignaturePolicyIdentifierType signaturePolicyIdentifierType1 = new SignaturePolicyIdentifierType();
        signaturePolicyIdentifierType1.setSignaturePolicyId(new SignaturePolicyIdType());
        signaturePolicyIdentifierType1.setSignaturePolicyImplied("Value");

        SignatureValidationPolicyType signatureValidationPolicyType1 = new SignatureValidationPolicyType();
        signatureValidationPolicyType1.setFormalPolicyObject(voReferenceType4);
        signatureValidationPolicyType1.setFormalPolicyURI("42");
        signatureValidationPolicyType1.setPolicyName("42");
        signatureValidationPolicyType1.setReadablePolicyURI("42");
        signatureValidationPolicyType1.setSignaturePolicyIdentifier(signaturePolicyIdentifierType1);

        ValidationConstraintsEvaluationReportType validationConstraintsEvaluationReportType1 = new ValidationConstraintsEvaluationReportType();
        validationConstraintsEvaluationReportType1.setSignatureValidationPolicy(signatureValidationPolicyType1);

        VOReferenceType voReferenceType5 = new VOReferenceType();
        voReferenceType5.setAny("Value");

        POEType poeType1 = new POEType();
        poeType1.setPOEObject(voReferenceType5);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        poeType1.setPOETime(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        poeType1.setTypeOfProof(TypeOfProof.VALIDATION);

        ValidationTimeInfoType validationTimeInfoType1 = new ValidationTimeInfoType();
        validationTimeInfoType1.setBestSignatureTime(poeType1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        validationTimeInfoType1.setValidationTime(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));

        SignatureValidationReportType signatureValidationReportType1 = new SignatureValidationReportType();
        signatureValidationReportType1.setSignatureAttributes(new SignatureAttributesType());
        signatureValidationReportType1.setSignatureIdentifier(signatureIdentifierType1);
        signatureValidationReportType1.setSignatureQuality(new SignatureQualityType());
        signatureValidationReportType1.setSignatureValidationProcess(signatureValidationProcessType1);
        signatureValidationReportType1.setSignatureValidationStatus(validationStatusType1);
        signatureValidationReportType1.setSignerInformation(signerInformationType1);
        signatureValidationReportType1.setSignersDocument(new SignersDocumentType());
        signatureValidationReportType1
                .setValidationConstraintsEvaluationReport(validationConstraintsEvaluationReportType1);
        signatureValidationReportType1.setValidationTimeInfo(validationTimeInfoType1);

        ArrayList<SignatureValidationReportType> signatureValidationReportTypeList = new ArrayList<>();
        signatureValidationReportTypeList.add(signatureValidationReportType1);
        signatureValidationReportTypeList.add(signatureValidationReportType);
        ValidationReportType validationReportType = mock(ValidationReportType.class);
        when(validationReportType.getSignatureValidationReport()).thenReturn(signatureValidationReportTypeList);
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        assertEquals("VALID", signatureService.verifySignature(
                new Reports(diagnosticDataJaxb, detailedReport, new XmlSimpleReport(), validationReportType)));
        verify(validationReportType).getSignatureValidationReport();
    }

    /**
     * Method under test: {@link SignatureService#verifySignature(Reports)}
     */
    @Test
    void testVerifySignature8() throws UnsupportedEncodingException {
        DigestMethodType digestMethodType = new DigestMethodType();
        digestMethodType.setAlgorithm("42");

        DigestAlgAndValueType digestAlgAndValueType = new DigestAlgAndValueType();
        digestAlgAndValueType.setDigestMethod(digestMethodType);
        digestAlgAndValueType.setDigestValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureValueType signatureValueType = new SignatureValueType();
        signatureValueType.setId("42");
        signatureValueType.setValue("AAAAAAAA".getBytes("UTF-8"));

        SignatureIdentifierType signatureIdentifierType = new SignatureIdentifierType();
        signatureIdentifierType.setDAIdentifier("42");
        signatureIdentifierType.setDigestAlgAndValue(digestAlgAndValueType);
        signatureIdentifierType.setDocHashOnly(true);
        signatureIdentifierType.setHashOnly(true);
        signatureIdentifierType.setId("42");
        signatureIdentifierType.setSignatureValue(signatureValueType);

        SignatureValidationProcessType signatureValidationProcessType = new SignatureValidationProcessType();
        signatureValidationProcessType.setAny("Value");
        signatureValidationProcessType.setSignatureValidationPracticeStatement("42");
        signatureValidationProcessType.setSignatureValidationProcessID(SignatureValidationProcessID.BASIC);
        signatureValidationProcessType.setSignatureValidationServicePolicy("42");

        ValidationStatusType validationStatusType = new ValidationStatusType();
        validationStatusType.setMainIndication(Indication.TOTAL_PASSED);

        VOReferenceType voReferenceType = new VOReferenceType();
        voReferenceType.setAny("Value");

        SignerInformationType signerInformationType = new SignerInformationType();
        signerInformationType.setAny("Value");
        signerInformationType.setPseudonym(true);
        signerInformationType.setSigner("42");
        signerInformationType.setSignerCertificate(voReferenceType);

        VOReferenceType voReferenceType1 = new VOReferenceType();
        voReferenceType1.setAny("Value");

        SignaturePolicyIdentifierType signaturePolicyIdentifierType = new SignaturePolicyIdentifierType();
        signaturePolicyIdentifierType.setSignaturePolicyId(new SignaturePolicyIdType());
        signaturePolicyIdentifierType.setSignaturePolicyImplied("Value");

        SignatureValidationPolicyType signatureValidationPolicyType = new SignatureValidationPolicyType();
        signatureValidationPolicyType.setFormalPolicyObject(voReferenceType1);
        signatureValidationPolicyType.setFormalPolicyURI("42");
        signatureValidationPolicyType.setPolicyName("42");
        signatureValidationPolicyType.setReadablePolicyURI("42");
        signatureValidationPolicyType.setSignaturePolicyIdentifier(signaturePolicyIdentifierType);

        ValidationConstraintsEvaluationReportType validationConstraintsEvaluationReportType = new ValidationConstraintsEvaluationReportType();
        validationConstraintsEvaluationReportType.setSignatureValidationPolicy(signatureValidationPolicyType);

        VOReferenceType voReferenceType2 = new VOReferenceType();
        voReferenceType2.setAny("Value");

        POEType poeType = new POEType();
        poeType.setPOEObject(voReferenceType2);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        poeType.setPOETime(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        poeType.setTypeOfProof(TypeOfProof.VALIDATION);

        ValidationTimeInfoType validationTimeInfoType = new ValidationTimeInfoType();
        validationTimeInfoType.setBestSignatureTime(poeType);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        validationTimeInfoType.setValidationTime(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        ValidationStatusType validationStatusType1 = mock(ValidationStatusType.class);
        when(validationStatusType1.getMainIndication()).thenReturn(Indication.TOTAL_FAILED);
        doNothing().when(validationStatusType1).setMainIndication((Indication) any());
        validationStatusType1.setMainIndication(Indication.TOTAL_PASSED);
        SignatureValidationReportType signatureValidationReportType = mock(SignatureValidationReportType.class);
        when(signatureValidationReportType.getSignatureValidationStatus()).thenReturn(validationStatusType1);
        doNothing().when(signatureValidationReportType).setSignatureAttributes((SignatureAttributesType) any());
        doNothing().when(signatureValidationReportType).setSignatureIdentifier((SignatureIdentifierType) any());
        doNothing().when(signatureValidationReportType).setSignatureQuality((SignatureQualityType) any());
        doNothing().when(signatureValidationReportType)
                .setSignatureValidationProcess((SignatureValidationProcessType) any());
        doNothing().when(signatureValidationReportType).setSignatureValidationStatus((ValidationStatusType) any());
        doNothing().when(signatureValidationReportType).setSignerInformation((SignerInformationType) any());
        doNothing().when(signatureValidationReportType).setSignersDocument((SignersDocumentType) any());
        doNothing().when(signatureValidationReportType)
                .setValidationConstraintsEvaluationReport((ValidationConstraintsEvaluationReportType) any());
        doNothing().when(signatureValidationReportType).setValidationTimeInfo((ValidationTimeInfoType) any());
        signatureValidationReportType.setSignatureAttributes(new SignatureAttributesType());
        signatureValidationReportType.setSignatureIdentifier(signatureIdentifierType);
        signatureValidationReportType.setSignatureQuality(new SignatureQualityType());
        signatureValidationReportType.setSignatureValidationProcess(signatureValidationProcessType);
        signatureValidationReportType.setSignatureValidationStatus(validationStatusType);
        signatureValidationReportType.setSignerInformation(signerInformationType);
        signatureValidationReportType.setSignersDocument(new SignersDocumentType());
        signatureValidationReportType.setValidationConstraintsEvaluationReport(validationConstraintsEvaluationReportType);
        signatureValidationReportType.setValidationTimeInfo(validationTimeInfoType);

        ArrayList<SignatureValidationReportType> signatureValidationReportTypeList = new ArrayList<>();
        signatureValidationReportTypeList.add(signatureValidationReportType);
        ValidationReportType validationReportType = mock(ValidationReportType.class);
        when(validationReportType.getSignatureValidationReport()).thenReturn(signatureValidationReportTypeList);
        XmlDiagnosticData diagnosticDataJaxb = new XmlDiagnosticData();
        XmlDetailedReport detailedReport = new XmlDetailedReport();
        assertEquals("Document signature is invalid", signatureService.verifySignature(
                new Reports(diagnosticDataJaxb, detailedReport, new XmlSimpleReport(), validationReportType)));
        verify(validationReportType).getSignatureValidationReport();
        verify(signatureValidationReportType, atLeast(1)).getSignatureValidationStatus();
        verify(signatureValidationReportType).setSignatureAttributes((SignatureAttributesType) any());
        verify(signatureValidationReportType).setSignatureIdentifier((SignatureIdentifierType) any());
        verify(signatureValidationReportType).setSignatureQuality((SignatureQualityType) any());
        verify(signatureValidationReportType).setSignatureValidationProcess((SignatureValidationProcessType) any());
        verify(signatureValidationReportType).setSignatureValidationStatus((ValidationStatusType) any());
        verify(signatureValidationReportType).setSignerInformation((SignerInformationType) any());
        verify(signatureValidationReportType).setSignersDocument((SignersDocumentType) any());
        verify(signatureValidationReportType)
                .setValidationConstraintsEvaluationReport((ValidationConstraintsEvaluationReportType) any());
        verify(signatureValidationReportType).setValidationTimeInfo((ValidationTimeInfoType) any());
        verify(validationStatusType1).getMainIndication();
        verify(validationStatusType1).setMainIndication((Indication) any());
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
        DetachedTimestampValidator detachedTimestampValidator = mock(DetachedTimestampValidator.class);
        when(detachedTimestampValidator.getSignatures()).thenReturn(new ArrayList<>());
        assertEquals("VALID", signatureService.verifyDigest(detachedTimestampValidator, "Checksum"));
        verify(detachedTimestampValidator).getSignatures();
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
        assertEquals(userList, reports.getDiagnosticDataJaxb().getUsedCertificates());
    }

    /**
     * Method under test: {@link SignatureService#verifyManagerTaxCode(Reports, List)}
     */
    @Test
    void testVerifyManagerTaxCode3() {
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
    void testVerifyManagerTaxCode4() throws UnsupportedEncodingException {
        XmlBasicSignature xmlBasicSignature = new XmlBasicSignature();
        xmlBasicSignature.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature.setSignatureIntact(true);
        xmlBasicSignature.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue.setDigestValue("AAAAAAAA".getBytes("UTF-8"));
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
        xmlDigestAlgoAndValue1.setDigestValue("AAAAAAAA".getBytes("UTF-8"));
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
        xmlSigningCertificate.setPublicKey("AAAAAAAA".getBytes("UTF-8"));

        XmlCertificate xmlCertificate = new XmlCertificate();
        xmlCertificate.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate.setBase64Encoded("AAAAAAAA".getBytes("UTF-8"));
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate.setNotAfter(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate.setNotBefore(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
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
        xmlSigningCertificate1.setPublicKey("AAAAAAAA".getBytes("UTF-8"));

        XmlCertificate xmlCertificate1 = new XmlCertificate();
        xmlCertificate1.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate1.setBase64Encoded("AAAAAAAA".getBytes("UTF-8"));
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
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate1.setNotAfter(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate1.setNotBefore(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
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
    void testVerifyManagerTaxCode5() throws UnsupportedEncodingException {
        XmlBasicSignature xmlBasicSignature = new XmlBasicSignature();
        xmlBasicSignature.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature.setSignatureIntact(true);
        xmlBasicSignature.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue.setDigestValue("AAAAAAAA".getBytes("UTF-8"));
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
        xmlDigestAlgoAndValue1.setDigestValue("AAAAAAAA".getBytes("UTF-8"));
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
        xmlSigningCertificate.setPublicKey("AAAAAAAA".getBytes("UTF-8"));

        XmlCertificate xmlCertificate = new XmlCertificate();
        xmlCertificate.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate.setBase64Encoded("AAAAAAAA".getBytes("UTF-8"));
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate.setNotAfter(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate.setNotBefore(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
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
        xmlSigningCertificate1.setPublicKey("AAAAAAAA".getBytes("UTF-8"));

        XmlCertificate xmlCertificate1 = new XmlCertificate();
        xmlCertificate1.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate1.setBase64Encoded("AAAAAAAA".getBytes("UTF-8"));
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
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate1.setNotAfter(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate1.setNotBefore(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
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
        xmlBasicSignature2.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature2.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature2.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature2.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature2.setSignatureIntact(true);
        xmlBasicSignature2.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue2 = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue2.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue2.setDigestValue("AAAAAAAA".getBytes("UTF-8"));
        xmlDigestAlgoAndValue2.setMatch(true);

        XmlOriginalThirdCountryQcStatementsMapping xmlOriginalThirdCountryQcStatementsMapping1 = new XmlOriginalThirdCountryQcStatementsMapping();
        xmlOriginalThirdCountryQcStatementsMapping1.setOtherOIDs(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcCClegislation(new ArrayList<>());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcCompliance(new XmlQcCompliance());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcSSCD(new XmlQcSSCD());
        xmlOriginalThirdCountryQcStatementsMapping1.setQcTypes(new ArrayList<>());

        XmlMRACertificateMapping xmlMRACertificateMapping1 = new XmlMRACertificateMapping();
        xmlMRACertificateMapping1.setEnactedTrustServiceLegalIdentifier("42");
        xmlMRACertificateMapping1.setOriginalThirdCountryMapping(xmlOriginalThirdCountryQcStatementsMapping1);

        XmlPSD2QcInfo xmlPSD2QcInfo1 = new XmlPSD2QcInfo();
        xmlPSD2QcInfo1.setNcaId("42");
        xmlPSD2QcInfo1.setNcaName("42");
        xmlPSD2QcInfo1.setRolesOfPSP(new ArrayList<>());

        XmlQcCompliance xmlQcCompliance1 = new XmlQcCompliance();
        xmlQcCompliance1.setPresent(true);

        XmlQcEuLimitValue xmlQcEuLimitValue1 = new XmlQcEuLimitValue();
        xmlQcEuLimitValue1.setAmount(42);
        xmlQcEuLimitValue1.setCurrency("42");
        xmlQcEuLimitValue1.setExponent(42);

        XmlQcSSCD xmlQcSSCD1 = new XmlQcSSCD();
        xmlQcSSCD1.setPresent(true);

        XmlOID xmlOID1 = new XmlOID();
        xmlOID1.setDescription("42");
        xmlOID1.setValue("42");

        XmlQcStatements xmlQcStatements2 = new XmlQcStatements();
        xmlQcStatements2.setEnactedMRA(true);
        xmlQcStatements2.setMRACertificateMapping(xmlMRACertificateMapping1);
        xmlQcStatements2.setOtherOIDs(new ArrayList<>());
        xmlQcStatements2.setPSD2QcInfo(xmlPSD2QcInfo1);
        xmlQcStatements2.setQcCClegislation(new ArrayList<>());
        xmlQcStatements2.setQcCompliance(xmlQcCompliance1);
        xmlQcStatements2.setQcEuLimitValue(xmlQcEuLimitValue1);
        xmlQcStatements2.setQcEuPDS(new ArrayList<>());
        xmlQcStatements2.setQcEuRetentionPeriod(42);
        xmlQcStatements2.setQcSSCD(xmlQcSSCD1);
        xmlQcStatements2.setQcTypes(new ArrayList<>());
        xmlQcStatements2.setSemanticsIdentifier(xmlOID1);

        XmlBasicSignature xmlBasicSignature3 = new XmlBasicSignature();
        xmlBasicSignature3.setDigestAlgoUsedToSignThisToken(DigestAlgorithm.SHA1);
        xmlBasicSignature3.setEncryptionAlgoUsedToSignThisToken(EncryptionAlgorithm.RSA);
        xmlBasicSignature3.setKeyLengthUsedToSignThisToken("42");
        xmlBasicSignature3.setMaskGenerationFunctionUsedToSignThisToken(MaskGenerationFunction.MGF1);
        xmlBasicSignature3.setSignatureIntact(true);
        xmlBasicSignature3.setSignatureValid(true);

        XmlDigestAlgoAndValue xmlDigestAlgoAndValue3 = new XmlDigestAlgoAndValue();
        xmlDigestAlgoAndValue3.setDigestMethod(DigestAlgorithm.SHA1);
        xmlDigestAlgoAndValue3.setDigestValue("AAAAAAAA".getBytes("UTF-8"));
        xmlDigestAlgoAndValue3.setMatch(true);

        XmlQcStatements xmlQcStatements3 = new XmlQcStatements();
        xmlQcStatements3.setEnactedMRA(true);
        xmlQcStatements3.setMRACertificateMapping(new XmlMRACertificateMapping());
        xmlQcStatements3.setOtherOIDs(new ArrayList<>());
        xmlQcStatements3.setPSD2QcInfo(new XmlPSD2QcInfo());
        xmlQcStatements3.setQcCClegislation(new ArrayList<>());
        xmlQcStatements3.setQcCompliance(new XmlQcCompliance());
        xmlQcStatements3.setQcEuLimitValue(new XmlQcEuLimitValue());
        xmlQcStatements3.setQcEuPDS(new ArrayList<>());
        xmlQcStatements3.setQcEuRetentionPeriod(42);
        xmlQcStatements3.setQcSSCD(new XmlQcSSCD());
        xmlQcStatements3.setQcTypes(new ArrayList<>());
        xmlQcStatements3.setSemanticsIdentifier(new XmlOID());

        XmlSigningCertificate xmlSigningCertificate2 = new XmlSigningCertificate();
        xmlSigningCertificate2.setCertificate(new XmlCertificate());
        xmlSigningCertificate2.setPublicKey("AAAAAAAA".getBytes("UTF-8"));

        XmlCertificate xmlCertificate2 = new XmlCertificate();
        xmlCertificate2.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate2.setBase64Encoded("AAAAAAAA".getBytes("UTF-8"));
        xmlCertificate2.setBasicSignature(xmlBasicSignature3);
        xmlCertificate2.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate2.setCertificateChain(new ArrayList<>());
        xmlCertificate2.setCertificatePolicies(new ArrayList<>());
        xmlCertificate2.setCommonName("42");
        xmlCertificate2.setCountryName("42");
        xmlCertificate2.setDigestAlgoAndValue(xmlDigestAlgoAndValue3);
        xmlCertificate2.setEmail("42");
        xmlCertificate2.setEntityKey("42");
        xmlCertificate2.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate2.setGivenName("42");
        xmlCertificate2.setId("42");
        xmlCertificate2.setIdPkixOcspNoCheck(true);
        xmlCertificate2.setKeyUsageBits(new ArrayList<>());
        xmlCertificate2.setLocality("42");
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate2.setNotAfter(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate2.setNotBefore(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        xmlCertificate2.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate2.setOrganizationIdentifier("42");
        xmlCertificate2.setOrganizationName("42");
        xmlCertificate2.setOrganizationalUnit("42");
        xmlCertificate2.setPseudonym("42");
        xmlCertificate2.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.RSA);
        xmlCertificate2.setPublicKeySize(42);
        xmlCertificate2.setQcStatements(xmlQcStatements3);
        xmlCertificate2.setRevocations(new ArrayList<>());
        xmlCertificate2.setSelfSigned(true);
        xmlCertificate2.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate2.setSigningCertificate(xmlSigningCertificate2);
        xmlCertificate2.setSources(new ArrayList<>());
        xmlCertificate2.setState("42");
        xmlCertificate2.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate2.setSubjectSerialNumber("42");
        xmlCertificate2.setSurname("42");
        xmlCertificate2.setTitle("42");
        xmlCertificate2.setTrusted(true);
        xmlCertificate2.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate2.setValAssuredShortTermCertificate(true);

        XmlSigningCertificate xmlSigningCertificate3 = new XmlSigningCertificate();
        xmlSigningCertificate3.setCertificate(xmlCertificate2);
        xmlSigningCertificate3.setPublicKey("AAAAAAAA".getBytes("UTF-8"));

        XmlCertificate xmlCertificate3 = new XmlCertificate();
        xmlCertificate3.setAuthorityInformationAccessUrls(new ArrayList<>());
        xmlCertificate3.setBase64Encoded("AAAAAAAA".getBytes("UTF-8"));
        xmlCertificate3.setBasicSignature(xmlBasicSignature2);
        xmlCertificate3.setCRLDistributionPoints(new ArrayList<>());
        xmlCertificate3.setCertificateChain(new ArrayList<>());
        xmlCertificate3.setCertificatePolicies(new ArrayList<>());
        xmlCertificate3.setCommonName("42");
        xmlCertificate3.setCountryName("42");
        xmlCertificate3.setDigestAlgoAndValue(xmlDigestAlgoAndValue2);
        xmlCertificate3.setEmail("42");
        xmlCertificate3.setEntityKey("42");
        xmlCertificate3.setExtendedKeyUsages(new ArrayList<>());
        xmlCertificate3.setGivenName("42");
        xmlCertificate3.setId("42");
        xmlCertificate3.setIdPkixOcspNoCheck(true);
        xmlCertificate3.setKeyUsageBits(new ArrayList<>());
        xmlCertificate3.setLocality("42");
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate3.setNotAfter(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        xmlCertificate3.setNotBefore(Date.from(atStartOfDayResult7.atZone(ZoneId.of("UTC")).toInstant()));
        xmlCertificate3.setOCSPAccessUrls(new ArrayList<>());
        xmlCertificate3.setOrganizationIdentifier("42");
        xmlCertificate3.setOrganizationName("42");
        xmlCertificate3.setOrganizationalUnit("42");
        xmlCertificate3.setPseudonym("42");
        xmlCertificate3.setPublicKeyEncryptionAlgo(EncryptionAlgorithm.RSA);
        xmlCertificate3.setPublicKeySize(42);
        xmlCertificate3.setQcStatements(xmlQcStatements2);
        xmlCertificate3.setRevocations(new ArrayList<>());
        xmlCertificate3.setSelfSigned(true);
        xmlCertificate3.setSerialNumber(BigInteger.valueOf(42L));
        xmlCertificate3.setSigningCertificate(xmlSigningCertificate3);
        xmlCertificate3.setSources(new ArrayList<>());
        xmlCertificate3.setState("42");
        xmlCertificate3.setSubjectAlternativeNames(new ArrayList<>());
        xmlCertificate3.setSubjectSerialNumber("42");
        xmlCertificate3.setSurname("42");
        xmlCertificate3.setTitle("42");
        xmlCertificate3.setTrusted(true);
        xmlCertificate3.setTrustedServiceProviders(new ArrayList<>());
        xmlCertificate3.setValAssuredShortTermCertificate(true);

        ArrayList<XmlCertificate> xmlCertificateList = new ArrayList<>();
        xmlCertificateList.add(xmlCertificate3);
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
    void testVerifyManagerTaxCode7() {
        Reports reports = mock(Reports.class);
        when(reports.getDiagnosticData()).thenReturn(null);
        assertEquals("No tax code has been found in digital signature",
                signatureService.verifyManagerTaxCode(reports, new ArrayList<>()));
        verify(reports).getDiagnosticData();
    }
}

