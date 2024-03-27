package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignService;
import it.pagopa.selfcare.commons.utils.crypto.service.PadesSignServiceImpl;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static it.pagopa.selfcare.mscore.constant.GenericError.UNABLE_TO_DOWNLOAD_FILE;

@Slf4j
@Service
public class ContractService {
    private final PagoPaSignatureConfig pagoPaSignatureConfig;
    private final PadesSignService padesSignService;
    private final FileStorageConnector fileStorageConnector;
    private final CoreConfig coreConfig;
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
