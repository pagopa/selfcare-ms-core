package it.pagopa.selfcare.mscore.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/core-config.properties")
@ConfigurationProperties(prefix = "mscore.pagopa-signature")
@Data
@ToString
public class PagoPaSignatureConfig {

    private boolean enabled;
    private String signer;
    private String location;
    private boolean applyOnboardingEnabled;
    private String applyOnboardingTemplateReason;
    private boolean verifyEnabled;

    private String euListOfTrustedListsURL;
    private String euOfficialJournalUrl;

}
