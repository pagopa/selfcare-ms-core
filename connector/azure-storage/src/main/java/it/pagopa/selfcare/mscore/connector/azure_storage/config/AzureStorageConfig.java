package it.pagopa.selfcare.mscore.connector.azure_storage.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

    public String getConnectionString() {
        return new String(Base64.getDecoder().decode(connectionString), StandardCharsets.UTF_8);
    }

    public String getContractsTemplateContainer() {
        return new String(Base64.getDecoder().decode(contractsTemplateContainer), StandardCharsets.UTF_8);
    }
}
