package it.pagopa.selfcare.mscore.core.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/kafka.properties")
@ConfigurationProperties(prefix = "kafka-manager")
@Data
@ToString
public class KafkaPropertiesConfig {
    private String datalakeContractsSaslJaasConfig;
    private String usersSaslJaasConfig;
    private String datalakeContractsTopic ;
    private String scUsersTopic;
    private String saslMechanism;
    private String securityProtocol;
    private String bootstrapServers;
}
