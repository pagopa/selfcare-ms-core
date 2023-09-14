package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MailParametersMapper {

    @Autowired
    MailTemplateConfig mailTemplateConfig;

    public Map<String, String> getOnboardingMailParameter(User user, OnboardingRequest request, String token) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getProductName(), request.getProductName());
        if(user.getName()!=null) {
            map.put(mailTemplateConfig.getUserName(), user.getName());
        }
        if(user.getFamilyName()!=null) {
            map.put(mailTemplateConfig.getUserSurname(), user.getFamilyName());
        }
        StringBuilder confirmLink = new StringBuilder(mailTemplateConfig.getConfirmTokenPlaceholder());
        StringBuilder rejectLink = new StringBuilder(mailTemplateConfig.getRejectTokenPlaceholder());
        map.put(mailTemplateConfig.getRejectTokenName(), rejectLink.append(token).toString());
        map.put(mailTemplateConfig.getConfirmTokenName(), confirmLink.append(token).toString());
        return map;
    }

    public Map<String, String> getOnboardingMailNotificationParameter(User user, OnboardingRequest request, String token) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getNotificationProductName(), request.getProductName());
        if(user.getName()!=null) {
            map.put(mailTemplateConfig.getNotificationRequesterName(), user.getName());
        }
        if(user.getFamilyName()!=null) {
            map.put(mailTemplateConfig.getNotificationRequesterSurname(), user.getFamilyName());
        }
        if(request.getInstitutionUpdate()!=null) {
            map.put(mailTemplateConfig.getInstitutionDescription(), request.getInstitutionUpdate().getDescription());
        }
        StringBuilder adminApproveLink = new StringBuilder(mailTemplateConfig.getAdminLink());
        map.put(mailTemplateConfig.getConfirmTokenName(), adminApproveLink.append(token).toString());
        return map;
    }

    public Map<String, String> getDelegationNotificationParameter(String institutionName, String productName) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getNotificationProductName(), productName);
        map.put(mailTemplateConfig.getInstitutionDescription(), institutionName);
        return map;
    }

    public String getOnboardingNotificationPath() {
        return mailTemplateConfig.getNotificationPath();
    }

    public String getDelegationNotificationPath() {
        return mailTemplateConfig.getDelegationNotificationPath();
    }

    public String getOnboardingCompletePath() {
        return mailTemplateConfig.getCompletePath();
    }

    public String getFdOnboardingCompletePath(){
        return mailTemplateConfig.getCompletePathFd();
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
