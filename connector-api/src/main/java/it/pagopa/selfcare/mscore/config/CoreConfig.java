package it.pagopa.selfcare.mscore.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:config/core-config.properties")
@ConfigurationProperties(prefix = "mscore")
@Data
@ToString
public class CoreConfig {

    private String logoPath;
    private String senderMail;
    private List<String> destinationMails;
    private String institutionAlternativeEmail;
    private boolean sendEmailToInstitution;
    private Integer onboardingExpiringDate;
    private boolean infoCamereEnable;
    private boolean enableSendDelegationMail;
}
