package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MailParametersMapper {

    @Autowired
    MailTemplateConfig mailTemplateConfig;

    public Map<String, String> getOnboardingMailParameter(User user, OnboardingRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getProductName(), request.getProductName());
        map.put(mailTemplateConfig.getUserName(), user.getName().getValue());
        map.put(mailTemplateConfig.getUserSurname(), user.getFamilyName().getValue());
        return map;
    }

    public Map<String, String> getOnboardingMailNotificationParameter(User user, OnboardingRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getNotificationProductName(), request.getProductName());
        map.put(mailTemplateConfig.getNotificationRequesterName(), user.getName().getValue());
        map.put(mailTemplateConfig.getNotificationRequesterSurname(), user.getFamilyName().getValue());
        map.put(mailTemplateConfig.getInstitutionDescription(), request.getInstitutionUpdate().getDescription());
        return map;
    }

    public String getOnboardingPath() {
        return mailTemplateConfig.getPath();
    }

    public String getOnboardingNotificationPath() {
        return mailTemplateConfig.getNotificationPath();
    }

    public List<String> getOnboardingNotificationAdminEmail() {
        return List.of(new String(Base64.getDecoder()
                        .decode(mailTemplateConfig.getNotificationAdminEmail()), StandardCharsets.UTF_8));
    }
}
