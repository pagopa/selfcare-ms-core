package it.pagopa.selfcare.mscore.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
    private Integer expiringDate;
    private boolean infoCamereEnable;

    public String getInstitutionAlternativeEmail() {
        return new String(Base64.getDecoder().decode(institutionAlternativeEmail), StandardCharsets.UTF_8);
    }
}
