package it.pagopa.selfcare.mscore.connector.azure_storage.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AzureStorageConfigTest {
    /**
     * Method under test: {@link AzureStorageConfig#getConnectionString()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetConnectionString() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R027 Missing beans when creating Spring context.
        //   Failed to create Spring context due to missing beans
        //   in the current Spring profile:
        //       it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig
        //   See https://diff.blue/R027 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Base64$Decoder.decode(Base64.java:558)
        //       at it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig.getConnectionString(AzureStorageConfig.java:25)
        //   See https://diff.blue/R013 to resolve this issue.

        (new AzureStorageConfig()).getConnectionString();
    }

    /**
     * Method under test: {@link AzureStorageConfig#getConnectionString()}
     */
    @Test
    void testGetConnectionString2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R027 Missing beans when creating Spring context.
        //   Failed to create Spring context due to missing beans
        //   in the current Spring profile:
        //       it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig
        //   See https://diff.blue/R027 to resolve this issue.

        AzureStorageConfig azureStorageConfig = new AzureStorageConfig();
        azureStorageConfig.setConnectionString("42");
        assertEquals("�", azureStorageConfig.getConnectionString());
    }

    /**
     * Method under test: {@link AzureStorageConfig#getContractsTemplateContainer()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetContractsTemplateContainer() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R027 Missing beans when creating Spring context.
        //   Failed to create Spring context due to missing beans
        //   in the current Spring profile:
        //       it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig
        //   See https://diff.blue/R027 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Base64$Decoder.decode(Base64.java:558)
        //       at it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig.getContractsTemplateContainer(AzureStorageConfig.java:29)
        //   See https://diff.blue/R013 to resolve this issue.

        (new AzureStorageConfig()).getContractsTemplateContainer();
    }

    /**
     * Method under test: {@link AzureStorageConfig#getContractsTemplateContainer()}
     */
    @Test
    void testGetContractsTemplateContainer2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R027 Missing beans when creating Spring context.
        //   Failed to create Spring context due to missing beans
        //   in the current Spring profile:
        //       it.pagopa.selfcare.mscore.connector.azure_storage.config.AzureStorageConfig
        //   See https://diff.blue/R027 to resolve this issue.

        AzureStorageConfig azureStorageConfig = new AzureStorageConfig();
        azureStorageConfig.setContractsTemplateContainer("42");
        assertEquals("�", azureStorageConfig.getContractsTemplateContainer());
    }
}

