package it.pagopa.selfcare.mscore.connector.azure_storage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.ERROR_DURING_DOWNLOAD_FILE;
import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.ERROR_DURING_UPLOAD_FILE;

@Slf4j
@Service
@PropertySource("classpath:config/azure-storage-config.properties")
@Profile("AzureStorage")
class AzureBlobClient implements FileStorageConnector {

    private final CloudBlobClient blobClient;
    private final AzureStorageConfig azureStorageConfig;


    AzureBlobClient(AzureStorageConfig azureStorageConfig)
            throws URISyntaxException, InvalidKeyException {
        if (log.isDebugEnabled()) {
            log.trace("AzureBlobClient.AzureBlobClient");
            log.debug("storageConnectionString = {}", azureStorageConfig.getConnectionString());
        }
        this.azureStorageConfig = azureStorageConfig;
        final CloudStorageAccount storageAccount = CloudStorageAccount.parse(azureStorageConfig.getConnectionString());
        this.blobClient = storageAccount.createCloudBlobClient();

    }


    @Override
    public String getTemplateFile(String templateName){
        log.info("START - getTemplateFile for template: {}", templateName);
        try {
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContractsTemplateContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(templateName);
            String downloaded = blob.downloadText();
            log.info("END - getTemplateFile - Downloaded {}", templateName);
            return downloaded;
        } catch (StorageException | URISyntaxException | IOException e) {
            throw new MsCoreException(String.format(ERROR_DURING_DOWNLOAD_FILE.getMessage(), templateName),
                    ERROR_DURING_DOWNLOAD_FILE.getCode());
        }
    }

    @Override
    public void uploadContract(String id, MultipartFile contract) {
        log.info("START - uploadContract for token: {}", id);

        StringBuilder fileName = new StringBuilder(azureStorageConfig.getContractPath());
        fileName.append(id);
        fileName.append("/");
        fileName.append(contract.getOriginalFilename());

        if (log.isDebugEnabled()) {
            log.trace("uploadContract");
            log.debug("uploadContract fileName = {}, contentType = {}", fileName, contract.getContentType());
        }

        try {
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContractsTemplateContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName.toString());
            blob.getProperties().setContentType(contract.getContentType());
            blob.upload(contract.getInputStream(), contract.getInputStream().available());
            log.info("Uploaded {}", fileName);

        } catch (StorageException | URISyntaxException | IOException e) {
            throw new MsCoreException(String.format(ERROR_DURING_UPLOAD_FILE.getMessage(), fileName),
                    ERROR_DURING_UPLOAD_FILE.getCode());
        }
    }
}
