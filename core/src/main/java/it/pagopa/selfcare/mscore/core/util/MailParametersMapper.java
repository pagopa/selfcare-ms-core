package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MailParametersMapper {

    @Autowired
    MailTemplateConfig mailTemplateConfig;

    public Map<String, String> getOnboardingMailParameter(User user, OnboardingRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getProductName(), request.getProductId());
        if(user.getName()!=null) {
            map.put(mailTemplateConfig.getUserName(), user.getName().getValue());
        }
        if(user.getFamilyName()!=null) {
            map.put(mailTemplateConfig.getUserSurname(), user.getFamilyName().getValue());
        }
        map.put(mailTemplateConfig.getRejectTokenName(), mailTemplateConfig.getRejectTokenPlaceholder());
        map.put(mailTemplateConfig.getConfirmTokenName(), mailTemplateConfig.getConfirmTokenPlaceholder());
        return map;
    }

    public Map<String, String> getOnboardingMailNotificationParameter(User user, OnboardingRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getNotificationProductName(), request.getProductName());
        if(user.getName()!=null) {
            map.put(mailTemplateConfig.getNotificationRequesterName(), user.getName().getValue());
        }
        if(user.getFamilyName()!=null) {
            map.put(mailTemplateConfig.getNotificationRequesterSurname(), user.getFamilyName().getValue());
        }
        if(request.getInstitutionUpdate()!=null) {
            map.put(mailTemplateConfig.getInstitutionDescription(), request.getInstitutionUpdate().getDescription());
        }
        map.put(mailTemplateConfig.getConfirmTokenName(), mailTemplateConfig.getConfirmTokenPlaceholder());
        return map;
    }

    public String getOnboardingPath() {
        return mailTemplateConfig.getPath();
    }

    public String getOnboardingNotificationPath() {
        return mailTemplateConfig.getNotificationPath();
    }

    public String getOnboardingCompletePath() {
        return mailTemplateConfig.getCompletePath();
    }

    public List<String> getOnboardingNotificationAdminEmail() {
        return List.of(mailTemplateConfig.getNotificationAdminEmail());
    }

    public Map<String, String> getCompleteOnbordingMailParameter(String productName) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getCompleteProductName(), productName);
        map.put(mailTemplateConfig.getCompleteSelfcareName(), mailTemplateConfig.getCompleteSelfcarePlaceholder());
        return map;
    }

    public Map<String, String> getOnboardingRejectMailParameters(String productName, String productId){
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getRejectProductName(), productName);
        map.put(mailTemplateConfig.getRejectOnboardingUrlPlaceholder(), mailTemplateConfig.getRejectOnboardingUrlValue() + productId);
        return map;
    }

    public String getOnboardingRejectNotificationPath(){
        return mailTemplateConfig.getRejectPath();
    }
}
