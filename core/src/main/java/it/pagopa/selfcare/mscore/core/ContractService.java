package it.pagopa.selfcare.mscore.core;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import it.pagopa.selfcare.commons.utils.crypto.model.SignatureInformation;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignService;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignServiceImpl;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.User;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.GENERIC_ERROR;
import static it.pagopa.selfcare.mscore.core.util.PdfMapper.*;

@Slf4j
@Service
public class ContractService {

    private final PagoPaSignatureConfig pagoPaSignatureConfig;
    private final PadesSignService padesSignService;
    private final FileStorageConnector fileStorageConnector;
    private final EmailConnector emailConnector;
    private final CoreConfig coreConfig;
    private final MailParametersMapper mailParametersMapper;

    public ContractService(PagoPaSignatureConfig pagoPaSignatureConfig,
                           FileStorageConnector fileStorageConnector,
                           EmailConnector emailConnector,
                           CoreConfig coreConfig,
                           MailParametersMapper mapper,
                           Pkcs7HashSignService pkcs7HashSignService) {
        this.pagoPaSignatureConfig = pagoPaSignatureConfig;
        this.padesSignService = new PadesSignServiceImpl(pkcs7HashSignService);
        this.fileStorageConnector = fileStorageConnector;
        this.emailConnector = emailConnector;
        this.coreConfig = coreConfig;
        this.mailParametersMapper = mapper;
    }

    public File createContractPDF(String contractTemplate, OnboardedUser validManager, List<OnboardedUser> users, Institution institution, OnboardingRequest request, List<GeographicTaxonomies> geographicTaxonomies) {
        log.info("START - createContractPdf");
        String builder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + UUID.randomUUID() + "_contratto_interoperabilita.";
        try {
            Path files = Files.createTempFile(builder, ".pdf");
            Map<String, Object> data = setUpCommonData(validManager, users, institution, request, geographicTaxonomies);
            if ("prod-pagopa".equalsIgnoreCase(request.getProductId()) &&
                    (InstitutionType.PSP == institution.getInstitutionType()
                            || InstitutionType.PSP == request.getInstitutionUpdate().getInstitutionType())) {
                setupPSPData(data, validManager, institution);
            } else if ("prod-io".equalsIgnoreCase(request.getProductId())
                    || "prod-io-premium".equalsIgnoreCase(request.getProductId())) {
                setupProdIOData(data, validManager, institution, request);
            }
            log.debug("data Map for PDF: {}", data);
            getPDFAsFile(files, contractTemplate, data);
            return signContract(institution, request, files.toFile());
        } catch (IOException e) {
            throw new InvalidRequestException(GENERIC_ERROR.getMessage(), GENERIC_ERROR.getCode());
        }
    }

    private File signContract(Institution institution, OnboardingRequest request, File pdf) {
        log.info("START - signContract for pdf: {}", pdf.getName());
        if (pagoPaSignatureConfig.isApplyOnboardingEnabled() && request.isSignContract())
            return signPdf(pdf, buildSignatureReason(institution, request));
        else {
            if (!pagoPaSignatureConfig.isApplyOnboardingEnabled() && request.isSignContract()) {
                log.info("Skipping PagoPA contract pdf sign due to global disabling");
            }
            return pdf;
        }
    }

    private File signPdf(File pdf, String signReason) {
        if (pagoPaSignatureConfig.isEnabled()) {
            log.info("Signing input file {} using reason {}", pdf.getName(), signReason);
            Path signedPdf = Paths.get(pdf.getAbsolutePath().replace(".pdf", "-signed.pdf"));
            padesSignService.padesSign(pdf, signedPdf.toFile(), buildSignatureInfo(signReason));
            return signedPdf.toFile();
        } else {
            log.info("Skipping PagoPA pdf sign due to global disabling");
            return pdf;
        }
    }

    private SignatureInformation buildSignatureInfo(String signReason) {
        return new SignatureInformation(
                pagoPaSignatureConfig.getSigner(),
                pagoPaSignatureConfig.getLocation(),
                signReason
        );
    }

    private String buildSignatureReason(Institution institution, OnboardingRequest request) {
        return pagoPaSignatureConfig.getApplyOnboardingTemplateReason()
                .replace("${institutionName}", institution.getDescription())
                .replace("${productName}", request.getProductName());
    }

    private void getPDFAsFile(Path files, String contractTemplate, Map<String, Object> data) throws IOException {
        log.debug("Getting PDF for HTML template...");
        String html = StringSubstitutor.replace(contractTemplate, data);
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.useProtocolsStreamImplementation(new ClassPathStreamFactory(), "classpath");
        var doc = Jsoup.parse(html, "UTF-8");
        var dom = W3CDom.convert(doc);
        builder.withW3cDocument(dom, null);
        builder.useSVGDrawer(new BatikSVGDrawer());
        FileOutputStream fout = new FileOutputStream(files.toFile());
        builder.toStream(fout);
        builder.run();
        log.debug("PDF stream properly retrieved");
    }

    public String extractTemplate(OnboardingRequest request) {
        return fileStorageConnector.getTemplateFile(request.getContract().getPath());
    }

    public void sendMail(File pdf, Institution institution, User user, OnboardingRequest request) {
        List<String> destinationMail;
        Map<String, String> mailParameters;
        if (InstitutionType.PA == institution.getInstitutionType()) {
            mailParameters = mailParametersMapper.getOnboardingMailParameter(user, request);
            log.debug("mailParameters: {}", mailParameters);
            destinationMail = coreConfig.getDestinationMails() != null ? coreConfig.getDestinationMails() : List.of(institution.getDigitalAddress());
            log.info("destinationsMail: {}", destinationMail);
            emailConnector.sendMail(mailParametersMapper.getOnboardingPath(), destinationMail, pdf, request.getProductName(), mailParameters);
        } else {
            mailParameters = mailParametersMapper.getOnboardingMailNotificationParameter(user, request);
            log.debug("mailParameters: {}", mailParameters);
            destinationMail = mailParametersMapper.getOnboardingNotificationAdminEmail();
            log.info("destinationsMail: {}", destinationMail);
            emailConnector.sendMail(mailParametersMapper.getOnboardingNotificationPath(), destinationMail, pdf, request.getProductName(), mailParameters);
        }
    }
}
