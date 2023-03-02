package it.pagopa.selfcare.mscore.connector.azure_storage;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobProperties;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig;
import it.pagopa.selfcare.mscore.model.ResourceResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AzureBlobClientTest {

    @Mock
    private CloudBlobClient cloudBlobClient;


    @Test
    @Disabled
    void getFile() throws URISyntaxException, StorageException, InvalidKeyException {
        AzureStorageConfig azureStorageConfig = new AzureStorageConfig();
        azureStorageConfig.setConnectionString("DefaultEndpointsProtocol=https;AccountName=accountName;AccountKey=key");
        azureStorageConfig.setContractPath("contractPath");
        azureStorageConfig.setContractsTemplateContainer("contractsTemplateContainer");
        AzureBlobClient azureBlobClient = new AzureBlobClient(azureStorageConfig);
        CloudBlobContainer blobContainer = mock(CloudBlobContainer.class);
        CloudBlockBlob blob = mock(CloudBlockBlob.class);

        when(cloudBlobClient.getContainerReference(any())).thenReturn(blobContainer);
        when(blobContainer.getBlockBlobReference(any())).thenReturn(blob);
        when(blob.getProperties()).thenReturn(new BlobProperties());

        ResourceResponse response = azureBlobClient.getFile("file");
    }

}

