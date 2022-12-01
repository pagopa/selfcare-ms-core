package it.pagopa.selfcare.mscore.connector.azure_storage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/azure-storage-config.properties")
@Profile("AzureStorage")
class AzureStorageConfig {
}
