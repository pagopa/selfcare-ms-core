package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import it.pagopa.selfcare.commons.utils.crypto.model.SignatureInformation;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignService;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignServiceImpl;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;
import static it.pagopa.selfcare.mscore.core.SignatureService.VALID_CHECK;
import static it.pagopa.selfcare.mscore.core.util.PdfMapper.*;

@Slf4j
@Service
public class ContractService {

    private final PagoPaSignatureConfig pagoPaSignatureConfig;
    private final PadesSignService padesSignService;
    private final FileStorageConnector fileStorageConnector;
    private final CoreConfig coreConfig;
    private final SignatureService signatureService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaPropertiesConfig kafkaPropertiesConfig;
    private final ObjectMapper mapper;

    public ContractService(PagoPaSignatureConfig pagoPaSignatureConfig,
                           FileStorageConnector fileStorageConnector,
                           CoreConfig coreConfig,
                           Pkcs7HashSignService pkcs7HashSignService,
                           SignatureService signatureService,
                           KafkaTemplate<String, String> kafkaTemplate,
                           KafkaPropertiesConfig kafkaPropertiesConfig) {
        this.pagoPaSignatureConfig = pagoPaSignatureConfig;
        this.padesSignService = new PadesSignServiceImpl(pkcs7HashSignService);
        this.fileStorageConnector = fileStorageConnector;
        this.coreConfig = coreConfig;
        this.signatureService = signatureService;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaPropertiesConfig = kafkaPropertiesConfig;
        this.mapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(OffsetDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(offsetDateTime));
            }
        });
        mapper.registerModule(simpleModule);
    }

    public File createContractPDF(String contractTemplate, User validManager, List<User> users, Institution institution, OnboardingRequest request, List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        log.info("START - createContractPdf");
        String builder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + UUID.randomUUID() + "_contratto_interoperabilita.";
        try {
            Path files = Files.createTempFile(builder, ".pdf");
            Map<String, Object> data = setUpCommonData(validManager, users, institution, request, geographicTaxonomies);
            if ("prod-pagopa".equalsIgnoreCase(request.getProductId()) &&
                    InstitutionType.PSP == institution.getInstitutionType()) {
                setupPSPData(data, validManager, institution);
            } else if ("prod-io".equalsIgnoreCase(request.getProductId())
                    || "prod-io-premium".equalsIgnoreCase(request.getProductId())) {
                setupProdIOData(data, validManager, institution, request);
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
        if (pagoPaSignatureConfig.isApplyOnboardingEnabled() && request.isSignContract()) {
            return signPdf(pdf, buildSignatureReason(institution, request));
        } else {
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
                .replace("${productName}", request.getProductId());
    }

    private void validateSignature(String... errorEnums) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> strings = Arrays.stream(errorEnums).filter(s -> !VALID_CHECK.equals(s)).collect(Collectors.toList());
        if (!strings.isEmpty()) {
            for (String s : strings) {
                stringBuilder.append(s);
                stringBuilder.append("\n");
            }
            throw new MsCoreException(String.format(INVALID_SIGNATURE.getMessage(), stringBuilder), INVALID_SIGNATURE.getCode());
        }
    }

    public void verifySignature(MultipartFile contract, Token token, List<User> users) {
        try {
            SignedDocumentValidator validator = signatureService.createDocumentValidator(contract.getInputStream().readAllBytes());
            signatureService.isDocumentSigned(validator);
            signatureService.verifyOriginalDocument(validator);
            Reports reports = signatureService.validateDocument(validator);
            validateSignature(
                    signatureService.verifySignatureForm(validator),
                    signatureService.verifySignature(reports),
                    signatureService.verifyDigest(validator, token.getChecksum()),
                    signatureService.verifyManagerTaxCode(reports, users)
            );
        } catch (MsCoreException e) {
            throw e;
        } catch (Exception e) {
            throw new MsCoreException(GENERIC_ERROR.getMessage(), GENERIC_ERROR.getCode());
        }
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

    public void sendDataLakeNotification(Institution institution, Token token) {
        NotificationToSend notification = toNotificationToSend(institution, token);
        try {
            String msg = mapper.writeValueAsString(notification);
            sendNotification(msg, token.getId());
        } catch (JsonProcessingException e) {
            log.warn("error during send dataLake notification for token {}", notification.getId());
        }
    }

    private NotificationToSend toNotificationToSend(Institution institution, Token token) {
        NotificationToSend notification = new NotificationToSend();
        notification.setId(token.getId());
        notification.setInternalInstitutionID(institution.getId());
        notification.setProduct(token.getProductId());
        notification.setState(RelationshipState.ACTIVE);
        notification.setFilePath(token.getContractSigned());
        notification.setOnboardingTokenId(token.getId());
        notification.setInstitution(institution);

        if (token.getProductId() != null) {
            Onboarding onboarding = institution.getOnboarding().stream()
                    .filter(o -> token.getProductId().equalsIgnoreCase(o.getProductId()))
                    .findFirst().orElseThrow(() -> new InvalidRequestException(String.format("Product %s not found", token.getProductId()), "0000"));
            notification.setPricingPlan(onboarding.getPricingPlan());
            notification.setBilling(onboarding.getBilling() != null ? onboarding.getBilling() : institution.getBilling());
            if (onboarding.getUpdatedAt() != null) {
                notification.setUpdatedAt(onboarding.getUpdatedAt());
            }
        }

        return notification;
    }

    private void sendNotification(String message, String tokenId) {
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(kafkaPropertiesConfig.getDatalakeContractsTopic(), message);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("sent dataLake notification for token : {}", tokenId);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("error during send dataLake notification for token {}: {} ", tokenId, ex.getMessage(), ex);
            }
        });

    }
}
