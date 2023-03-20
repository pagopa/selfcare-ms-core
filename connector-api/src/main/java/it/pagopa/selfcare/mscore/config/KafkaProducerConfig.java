package it.pagopa.selfcare.mscore.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Autowired
    private final KafkaPropertiesConfig kafkaPropertiesConfig;

    public KafkaProducerConfig(KafkaPropertiesConfig kafkaPropertiesConfig) {
        this.kafkaPropertiesConfig = kafkaPropertiesConfig;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaPropertiesConfig.getBootstrapServers());
        configProps.put(
                AdminClientConfig.SECURITY_PROTOCOL_CONFIG,
                kafkaPropertiesConfig.getSecurityProtocol());
        configProps.put(
                SaslConfigs.SASL_MECHANISM,
                kafkaPropertiesConfig.getSaslMechanism());
        configProps.put(
                SaslConfigs.SASL_JAAS_CONFIG,
                kafkaPropertiesConfig.getDatalakeContractsSaslJaasConfig());
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
