package it.pagopa.selfcare.mscore.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/core-config.properties")
@ConfigurationProperties(prefix = "mscore.mail-template.placeholders.onboarding")
@Data
@ToString
public class MailTemplateConfig {

    private String completePath;
    private String completeProductName;
    private String completeSelfcarePlaceholder;
    private String completeSelfcareName;
    private String completePathFd;
    private String completePathPt;

    private String autocompletePath;

    private String delegationNotificationPath;
    private String delegationUserNotificationPath;
    private String delegationPartnerName;

    private String path;
    private String userName;
    private String userSurname;
    private String productName;
    private String institutionDescription;

    private String confirmTokenName;
    private String confirmTokenPlaceholder;
    private String rejectTokenName;
    private String rejectTokenPlaceholder;
    private String adminLink;

    private String notificationPath;
    private String notificationAdminEmail;
    private String notificationProductName;
    private String notificationRequesterName;
    private String notificationRequesterSurname;

    private String rejectPath;
    private String rejectProductName;
    private String rejectOnboardingUrlPlaceholder;
    private String rejectOnboardingUrlValue;

    private String registrationRequestPath;
    private String registrationNotificationAdminPath;
}
