package it.pagopa.selfcare.mscore.connector.rest.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Configuration
public class GeoTaxonomiesFeignClientConfig {
    @Value("${mscore.geotaxonomy.apikey}")
    String apiKey;
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                template.header("x-api-key" , apiKey);
            }
        };
    }
}
