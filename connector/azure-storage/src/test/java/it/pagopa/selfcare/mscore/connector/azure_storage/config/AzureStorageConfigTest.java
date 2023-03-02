package it.pagopa.selfcare.mscore.connector.azure_storage.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /**
     * Method under test: {@link AzureStorageConfig#getContractsTemplateContainer()}
     */
    @Test
    void testGetContractsTemplateContainer3() {

        AzureStorageConfig azureStorageConfig = new AzureStorageConfig();
        azureStorageConfig.setContractPath("42");
        assertEquals("42", azureStorageConfig.getContractPath());
    }

    @Test
    void testGetContractsTemplateContainer4() {

        AzureStorageConfig azureStorageConfig = new AzureStorageConfig();
        azureStorageConfig.setConnectionString("42");
        azureStorageConfig.setContractsTemplateContainer("42");
        azureStorageConfig.setContractPath("42");

        assertEquals("AzureStorageConfig(connectionString=�, contractsTemplateContainer=�, contractPath=42)", azureStorageConfig.toString());
    }
}

