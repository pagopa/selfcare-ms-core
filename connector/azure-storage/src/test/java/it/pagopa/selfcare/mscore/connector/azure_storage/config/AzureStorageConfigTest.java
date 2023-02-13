package it.pagopa.selfcare.mscore.connector.azure_storage.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AzureStorageConfigTest {

    /**
     * Method under test: {@link AzureStorageConfig#getConnectionString()}
     */
    @Test
    void testGetConnectionString2() {

        AzureStorageConfig azureStorageConfig = new AzureStorageConfig();
        azureStorageConfig.setConnectionString("42");
        assertEquals("�", azureStorageConfig.getConnectionString());
    }

    /**
     * Method under test: {@link AzureStorageConfig#getContractsTemplateContainer()}
     */
    @Test
    void testGetContractsTemplateContainer2() {

        AzureStorageConfig azureStorageConfig = new AzureStorageConfig();
        azureStorageConfig.setContractsTemplateContainer("42");
        assertEquals("�", azureStorageConfig.getContractsTemplateContainer());
    }
}

