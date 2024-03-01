package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.commons.utils.crypto.model.SignatureInformation;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignService;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignServiceImpl;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static it.pagopa.selfcare.mscore.constant.GenericError.GENERIC_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.UNABLE_TO_DOWNLOAD_FILE;
import static it.pagopa.selfcare.mscore.constant.ProductId.*;
import static it.pagopa.selfcare.mscore.core.util.PdfMapper.*;

@Slf4j
@Service
public class ContractService {
    private final PagoPaSignatureConfig pagoPaSignatureConfig;
    private final PadesSignService padesSignService;
    private final FileStorageConnector fileStorageConnector;
    private final CoreConfig coreConfig;
    private final SignatureService signatureService;
    private final ObjectMapper mapper;

    public ContractService(PagoPaSignatureConfig pagoPaSignatureConfig,
                           FileStorageConnector fileStorageConnector,
                           CoreConfig coreConfig,
                           Pkcs7HashSignService pkcs7HashSignService,
                           SignatureService signatureService) {
        this.pagoPaSignatureConfig = pagoPaSignatureConfig;
        this.padesSignService = new PadesSignServiceImpl(pkcs7HashSignService);
        this.fileStorageConnector = fileStorageConnector;
        this.coreConfig = coreConfig;
        this.signatureService = signatureService;
        this.mapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(OffsetDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime));
            }
        });
        mapper.registerModule(simpleModule);
    }

    public File createContractPDF(String contractTemplate, User validManager, List<User> users, Institution institution, OnboardingRequest request, List<InstitutionGeographicTaxonomies> geographicTaxonomies, InstitutionType institutionType) {
        log.info("START - createContractPdf");
        String builder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + UUID.randomUUID() + "_contratto_interoperabilita.";
        try {
            Path files = Files.createTempFile(builder, ".pdf");
            Map<String, Object> data = setUpCommonData(validManager, users, institution, request, geographicTaxonomies, institutionType);
            if (PROD_PAGOPA.getValue().equalsIgnoreCase(request.getProductId()) &&
                    InstitutionType.PSP == institutionType) {
                setupPSPData(data, validManager, institution);
            } else if (PROD_IO.getValue().equalsIgnoreCase(request.getProductId())
                    || PROD_IO_PREMIUM.getValue().equalsIgnoreCase(request.getProductId())
                    || PROD_IO_SIGN.getValue().equalsIgnoreCase(request.getProductId())) {
                setupProdIOData(data, validManager, institution, request, institutionType);
            } else if (PROD_PN.getValue().equalsIgnoreCase(request.getProductId())) {
                setupProdPNData(data, institution, request);
            } else if (PROD_INTEROP.getValue().equalsIgnoreCase(request.getProductId())) {
                setupSAProdInteropData(data, request.getInstitutionUpdate());
            }
            log.debug("data Map for PDF: {}", data);
            getPDFAsFile(files, contractTemplate, data);
            return signContract(institution, request, files.toFile());
        } catch (IOException e) {
            log.warn("can not create contract PDF", e);
            throw new InvalidRequestException(GENERIC_ERROR.getMessage(), GENERIC_ERROR.getCode());
        }
    }


    private File signContract(Institution institution, OnboardingRequest request, File pdf) {
        log.info("START - signContract for pdf: {}", pdf.getName());
        if (pagoPaSignatureConfig.isApplyOnboardingEnabled() && request.getSignContract()) {
            return signPdf(pdf, buildSignatureReason(institution, request));
        } else {
            if (!pagoPaSignatureConfig.isApplyOnboardingEnabled() && request.getSignContract()) {
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
                .replace("${productName}", request.getProductId());
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

    public String extractTemplate(String path) {
        return fileStorageConnector.getTemplateFile(path);
    }

    public ResourceResponse getFile(String path) {
        return fileStorageConnector.getFile(path);
    }

    public File getLogoFile() {
        StringBuilder stringBuilder = new StringBuilder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        stringBuilder.append("_").append(UUID.randomUUID()).append("_logo");
        try {
            Path path = Files.createTempFile(stringBuilder.toString(), ".png");
            Files.write(path, fileStorageConnector.getTemplateFile(coreConfig.getLogoPath()).getBytes(StandardCharsets.UTF_8));
            return path.toFile();
        } catch (IOException e) {
            throw new InvalidRequestException(String.format(UNABLE_TO_DOWNLOAD_FILE.getMessage(), coreConfig.getLogoPath()), UNABLE_TO_DOWNLOAD_FILE.getCode());
        }
    }

    public String uploadContract(String tokenId, MultipartFile contract) {
        return fileStorageConnector.uploadContract(tokenId, contract);

    }

    public void deleteContract(String fileName, String tokenId) {
        fileStorageConnector.removeContract(fileName, tokenId);
    }
}
