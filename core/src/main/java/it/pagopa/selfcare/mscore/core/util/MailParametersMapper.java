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

    public Map<String, String> getOnboardingMailParameter(User user, OnboardingRequest request, String token, String description, boolean fromApprove) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getProductName(), request.getProductName());
        if (fromApprove) {
            map.put(mailTemplateConfig.getUserName(), description);
            map.put(mailTemplateConfig.getUserSurname(), "");
        } else {
            if (user.getName() != null) {
                map.put(mailTemplateConfig.getUserName(), user.getName());
            }
            if (user.getFamilyName() != null) {
                map.put(mailTemplateConfig.getUserSurname(), user.getFamilyName());
            }
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

    public Map<String, String> getRegistrationRequestParameter(User user, OnboardingRequest request) {
        Map<String, String> map = new HashMap<>();
        if(user.getName()!=null) {
            map.put(mailTemplateConfig.getNotificationRequesterName(), user.getName());
        }
        if(user.getFamilyName()!=null) {
            map.put(mailTemplateConfig.getNotificationRequesterSurname(), user.getFamilyName());
        }
        if(request.getInstitutionUpdate()!=null) {
            map.put(mailTemplateConfig.getInstitutionDescription(), request.getInstitutionUpdate().getDescription());
        }
        return map;
    }

    public Map<String, String> getDelegationNotificationParameter(String institutionName, String productName, String partnerName) {
        Map<String, String> map = new HashMap<>();
        map.put(mailTemplateConfig.getNotificationProductName(), productName);
        map.put(mailTemplateConfig.getInstitutionDescription(), institutionName);
        map.put(mailTemplateConfig.getDelegationPartnerName(), partnerName);
        return map;
    }

    public String getOnboardingNotificationPath() {
        return mailTemplateConfig.getNotificationPath();
    }

    public String getRegistrationRequestPath() {
        return mailTemplateConfig.getRegistrationRequestPath();
    }

    public String getRegistrationNotificationAdminPath() {
        return mailTemplateConfig.getRegistrationNotificationAdminPath();
    }

    public String getDelegationNotificationPath() {
        return mailTemplateConfig.getDelegationNotificationPath();
    }

    public String getDelegationUserNotificationPath() {
        return mailTemplateConfig.getDelegationUserNotificationPath();
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
