package it.pagopa.selfcare.mscore.connector.azure_storage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobProperties;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@Slf4j
@Service
@PropertySource("classpath:config/azure-storage-config.properties")
@Profile("AzureStorage")
class AzureBlobClient implements FileStorageConnector {

    private final CloudBlobClient blobClient;
    private final AzureStorageConfig azureStorageConfig;

    AzureBlobClient(AzureStorageConfig azureStorageConfig) throws URISyntaxException {
        log.trace("AzureBlobClient.AzureBlobClient");
        log.debug("storageConnectionString = {}", azureStorageConfig.getConnectionString());
        this.azureStorageConfig = azureStorageConfig;
        final CloudStorageAccount storageAccount = buildStorageAccount();
        this.blobClient = storageAccount.createCloudBlobClient();
    }

    private CloudStorageAccount buildStorageAccount() throws URISyntaxException {
        StorageCredentials storageCredentials = new StorageCredentialsAccountAndKey(azureStorageConfig.getAccountName(), azureStorageConfig.getAccountKey());
        return new CloudStorageAccount(storageCredentials,
                true,
                azureStorageConfig.getEndpointSuffix(),
                azureStorageConfig.getAccountName());
    }

    public ResourceResponse getFile(String fileName) {
        log.info("START - getFile for path: {}", fileName);
        try {
            ResourceResponse response = new ResourceResponse();
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContractsTemplateContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BlobProperties properties = blob.getProperties();
            blob.download(outputStream);
            log.info("END - getFile - path {}", fileName);
            response.setData(outputStream.toByteArray());
            response.setFileName(blob.getName());
            response.setMimetype(properties.getContentType());
            return response;
        } catch (StorageException | URISyntaxException e) {
            log.error(String.format(ERROR_DURING_DOWNLOAD_FILE.getMessage(), fileName), e);
            throw new MsCoreException(String.format(ERROR_DURING_DOWNLOAD_FILE.getMessage(), fileName),
                    ERROR_DURING_DOWNLOAD_FILE.getCode());
        }
    }

    @Override
    public String getTemplateFile(String templateName) {
        log.info("START - getTemplateFile for template: {}", templateName);
        try {
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContractsTemplateContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(templateName);
            String downloaded = blob.downloadText();
            log.info("END - getTemplateFile - Downloaded {}", templateName);
            return downloaded;
        } catch (StorageException | URISyntaxException | IOException e) {
            log.error(String.format(ERROR_DURING_DOWNLOAD_FILE.getMessage(), templateName), e);
            throw new MsCoreException(String.format(ERROR_DURING_DOWNLOAD_FILE.getMessage(), templateName),
                    ERROR_DURING_DOWNLOAD_FILE.getCode());
        }
    }

    @Override
    public String uploadContract(String id, MultipartFile contract) {
        log.info("START - uploadContract for token: {}", id);
        String fileName = Paths.get(azureStorageConfig.getContractPath(), id, contract.getOriginalFilename()).toString();
        log.debug("uploadContract fileName = {}, contentType = {}", fileName, contract.getContentType());
        try {
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContractsTemplateContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName);
            blob.getProperties().setContentType(contract.getContentType());
            blob.upload(contract.getInputStream(), contract.getInputStream().available());
            log.info("Uploaded {}", fileName);
            return fileName;
        } catch (StorageException | URISyntaxException | IOException e) {
            log.error(String.format(ERROR_DURING_UPLOAD_FILE.getMessage(), fileName), e);
            throw new MsCoreException(String.format(ERROR_DURING_UPLOAD_FILE.getMessage(), fileName),
                    ERROR_DURING_UPLOAD_FILE.getCode());
        }
    }

    @Override
    public void removeContract(String fileName, String tokenId) {
        log.info("START - deleteContract for token: {}", tokenId);

        try {
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContractsTemplateContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName);
            blob.deleteIfExists();
            log.info("Deleted {}", fileName);
        } catch (StorageException | URISyntaxException e) {
            log.error(String.format(ERROR_DURING_DELETED_FILE.getMessage(), fileName), e);
            throw new MsCoreException(String.format(ERROR_DURING_DELETED_FILE.getMessage(), fileName),
                    ERROR_DURING_DELETED_FILE.getCode());
        }
    }
}