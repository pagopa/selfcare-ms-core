package it.pagopa.selfcare.mscore.connector.azure_storage;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AzureBlobClientTest {

    @Mock
    private CloudBlobClient cloudBlobClient;

    @Mock
    private AzureStorageConfig azureStorageConfig;

    @InjectMocks
    private AzureBlobClient azureBlobClient;

}

