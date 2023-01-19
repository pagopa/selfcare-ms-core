package it.pagopa.selfcare.mscore.connector.rest.config;

import it.pagopa.selfcare.commons.connector.rest.config.RestClientBaseConfig;
import it.pagopa.selfcare.mscore.connector.rest.client.PartyRegistryProxyRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestClientBaseConfig.class)
@EnableFeignClients(clients = PartyRegistryProxyRestClient.class)
@PropertySource("classpath:config/party-registry-proxy-rest-client.properties")
public class PartyRegistryProxyRestClientConfig {

}
