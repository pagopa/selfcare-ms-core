package it.pagopa.selfcare.mscore.core;

import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.spi.x509.aia.DefaultAIASource;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.validationreport.jaxb.SignatureValidationReportType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.DOCUMENT_VALIDATION_FAIL;
import static it.pagopa.selfcare.mscore.constant.GenericError.GENERIC_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.INVALID_CONTRACT_DIGEST;
import static it.pagopa.selfcare.mscore.constant.GenericError.INVALID_DOCUMENT_SIGNATURE;
import static it.pagopa.selfcare.mscore.constant.GenericError.INVALID_SIGNATURE_FORMS;
import static it.pagopa.selfcare.mscore.constant.GenericError.INVALID_SIGNATURE_TAX_CODE;
import static it.pagopa.selfcare.mscore.constant.GenericError.ORIGINAL_DOCUMENT_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.GenericError.SIGNATURE_VALIDATION_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.TAX_CODE_NOT_FOUND_IN_SIGNATURE;

@Slf4j
@Service
public class SignatureService {

    static final String VALID_CHECK = "VALID";
    private static final Integer CF_MATCHER_GROUP = 2;
    private static final Pattern signatureRegex = Pattern.compile("(TINIT-)(.*)");

    public SignedDocumentValidator createDocumentValidator(byte[] bytes) {

        var trustedListsCertificateSource = new TrustedListsCertificateSource();

        CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
        certificateVerifier.setTrustedCertSources(trustedListsCertificateSource);
        certificateVerifier.setAIASource(new DefaultAIASource());
        certificateVerifier.setOcspSource(new OnlineOCSPSource());
        certificateVerifier.setCrlSource(new OnlineCRLSource());
        try {
            DSSDocument document = new InMemoryDocument(bytes);
            SignedDocumentValidator documentValidator = SignedDocumentValidator.fromDocument(document);
            documentValidator.setCertificateVerifier(certificateVerifier);
            return documentValidator;
        } catch (Exception e) {
            log.error("Error message: {}", e.getMessage(), e);
            throw new MsCoreException(GENERIC_ERROR.getMessage(), GENERIC_ERROR.getCode());
        }
    }

    public void isDocumentSigned(SignedDocumentValidator documentValidator) {
        if (documentValidator.getSignatures().isEmpty()) {
            throw new MsCoreException(SIGNATURE_VALIDATION_ERROR.getMessage(), SIGNATURE_VALIDATION_ERROR.getCode());
        }
    }

    public void verifyOriginalDocument(SignedDocumentValidator validator) {
        List<AdvancedSignature> advancedSignatures = validator.getSignatures();
        List<DSSDocument> dssDocuments = new ArrayList<>();
        if (advancedSignatures != null && !advancedSignatures.isEmpty()) {
            for (AdvancedSignature a : advancedSignatures) {
                Optional.ofNullable(validator.getOriginalDocuments(a.getId())).ifPresent(dssDocuments::addAll);
            }
        }
        if (dssDocuments.isEmpty()) {
            throw new MsCoreException(ORIGINAL_DOCUMENT_NOT_FOUND.getMessage(), ORIGINAL_DOCUMENT_NOT_FOUND.getCode());
        }
    }

    public Reports validateDocument(SignedDocumentValidator signedDocumentValidator) {
        try {
            return signedDocumentValidator.validateDocument();
        } catch (Exception e) {
            throw new MsCoreException(String.format(DOCUMENT_VALIDATION_FAIL.getMessage(), e.getMessage()), DOCUMENT_VALIDATION_FAIL.getCode());
        }
    }

    public String verifySignatureForm(SignedDocumentValidator validator) {
        List<AdvancedSignature> advancedSignatures = validator.getSignatures();
        List<SignatureForm> signatureForms = advancedSignatures.stream()
                .map(AdvancedSignature::getSignatureForm)
                .filter(signatureForm -> signatureForm != SignatureForm.CAdES)
                .collect(Collectors.toList());

        if (!signatureForms.isEmpty()) {
            return String.format(INVALID_SIGNATURE_FORMS.getMessage(), String.join(", ", signatureForms.toString()));
        }
        return VALID_CHECK;
    }

    public String verifySignature(Reports reports) {
        List<SignatureValidationReportType> signatureValidationReportTypes = new ArrayList<>();
        if (reports.getEtsiValidationReportJaxb() != null) {
            signatureValidationReportTypes = reports.getEtsiValidationReportJaxb().getSignatureValidationReport();
        }
        if (signatureValidationReportTypes.isEmpty()
                || (!signatureValidationReportTypes.stream().allMatch(s -> s.getSignatureValidationStatus() != null
                && Indication.TOTAL_PASSED == s.getSignatureValidationStatus().getMainIndication()))) {
            return INVALID_DOCUMENT_SIGNATURE.getMessage();
        }
        return VALID_CHECK;
    }

    public String verifyDigest(SignedDocumentValidator validator, String checksum) {
        List<AdvancedSignature> advancedSignatures = validator.getSignatures();
        if (advancedSignatures != null && !advancedSignatures.isEmpty()) {
            for (AdvancedSignature a : advancedSignatures) {
                List<DSSDocument> dssDocuments = validator.getOriginalDocuments(a.getId());
                if (!dssDocuments.stream().map(dssDocument -> dssDocument.getDigest(DigestAlgorithm.SHA256))
                        .collect(Collectors.toList()).contains(checksum)) {
                    return INVALID_CONTRACT_DIGEST.getMessage();
                }
            }
        }
        return VALID_CHECK;
    }

    public String verifyManagerTaxCode(Reports reports, List<User> users) {
        List<String> signatureTaxCodes = extractSubjectSNCFs(reports);
        if (signatureTaxCodes.isEmpty()) {
            return TAX_CODE_NOT_FOUND_IN_SIGNATURE.getMessage();
        }

        List<String> taxCodes = extractTaxCode(signatureTaxCodes);

        if (taxCodes.isEmpty() || !isSignedByLegal(users, taxCodes)) {
            return INVALID_SIGNATURE_TAX_CODE.getMessage();
        }
        return VALID_CHECK;
    }

    private List<String> extractTaxCode(List<String> signatureTaxCodes) {
        List<String> taxCode = new ArrayList<>();
        signatureTaxCodes.forEach(s -> {
            Matcher matcher = signatureRegex.matcher(s);
            if (matcher.matches()) {
                taxCode.add(matcher.group(CF_MATCHER_GROUP));
            }
        });
        return taxCode;
    }

    private boolean isSignedByLegal(List<User> users, List<String> signatureTaxCodes) {
        List<String> taxCodes = users.stream().map(User::getFiscalCode).collect(Collectors.toList());
        return !signatureTaxCodes.isEmpty() && !taxCodes.isEmpty() && new HashSet<>(signatureTaxCodes).containsAll(taxCodes);
    }

    private List<String> extractSubjectSNCFs(Reports reports) {
        if (reports.getDiagnosticData() != null && reports.getDiagnosticData().getUsedCertificates() != null) {
            List<String> subjectSNCFs = reports.getDiagnosticData().getUsedCertificates()
                    .stream().map(CertificateWrapper::getSubjectSerialNumber)
                    .filter(this::serialNumberMatch).collect(Collectors.toList());
            if (!subjectSNCFs.isEmpty()) {
                return subjectSNCFs;
            }
        }
        return Collections.emptyList();
    }

    private boolean serialNumberMatch(String s) {
        if (!StringUtils.isEmpty(s)) {
            return signatureRegex.matcher(s).matches();
        }
        return false;
    }
}
