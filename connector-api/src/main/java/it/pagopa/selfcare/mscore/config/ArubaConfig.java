package it.pagopa.selfcare.mscore.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/core-config.properties")
@ConfigurationProperties(prefix = "party-process.aruba")
@Data
@ToString
public class ArubaConfig {

    private String serviceUrl;
    private String typeOtpAuth;
    private String otpPwd;
    private String user;
    private String delegatedUser;
    private String delegatedPassword;
    private String delegatedDomain;

}
