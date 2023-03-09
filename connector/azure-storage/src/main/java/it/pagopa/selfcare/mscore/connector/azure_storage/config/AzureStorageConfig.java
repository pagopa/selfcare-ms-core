package it.pagopa.selfcare.mscore.connector.azure_storage.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/azure-storage-config.properties")
@ConfigurationProperties(prefix = "blob-storage")
@Profile("AzureStorage")
@Data
@ToString
public
class AzureStorageConfig {
    private String connectionString;
    private String contractsTemplateContainer;
    private String accountName;
    private String endpointSuffix;
    private String accountKey;

    private String contractPath;
}
