package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MailParametersMapperTest {

    @InjectMocks
    private MailParametersMapper mailParametersMapper;

    @Mock
    private MailTemplateConfig mailTemplateConfig;

    @Test
    void getOnboardingMailParameter(){
        when(mailTemplateConfig.getProductName()).thenReturn("productName");
        when(mailTemplateConfig.getUserName()).thenReturn("userName");
        when(mailTemplateConfig.getUserSurname()).thenReturn("userSurname");
        when(mailTemplateConfig.getRejectTokenName()).thenReturn("rejectTokenName");
        when(mailTemplateConfig.getRejectTokenPlaceholder()).thenReturn("rejectTokenPlaceholder");
        when(mailTemplateConfig.getConfirmTokenName()).thenReturn("confirmTokenName");
        when(mailTemplateConfig.getConfirmTokenPlaceholder()).thenReturn("confirmTokenPlaceholder");
        User user = new User();
        CertifiedField<String> certifiedField = new CertifiedField();
        certifiedField.setValue("val");
        user.setName(certifiedField);
        user.setFamilyName(certifiedField);
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId("productId");
        Map<String, String> map = mailParametersMapper.getOnboardingMailParameter(user, request, "");
        Assertions.assertNotNull(map);
    }


    @Test
    void getOnboardingMailNotificationParameter(){
        when(mailTemplateConfig.getProductName()).thenReturn("productName");
        when(mailTemplateConfig.getUserName()).thenReturn("userName");
        when(mailTemplateConfig.getUserSurname()).thenReturn("userSurname");
        when(mailTemplateConfig.getRejectTokenName()).thenReturn("rejectTokenName");
        when(mailTemplateConfig.getRejectTokenPlaceholder()).thenReturn("rejectTokenPlaceholder");
        when(mailTemplateConfig.getConfirmTokenName()).thenReturn("confirmTokenName");
        when(mailTemplateConfig.getConfirmTokenPlaceholder()).thenReturn("confirmTokenPlaceholder");
        when(mailTemplateConfig.getInstitutionDescription()).thenReturn("institutionDescription");
        when(mailTemplateConfig.getAdminLink()).thenReturn("institutionDescription");
        User user = new User();
        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setValue("42");
        user.setName(certifiedField);
        user.setFamilyName(certifiedField);
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId("productId");
        request.setInstitutionUpdate(new InstitutionUpdate());
        Map<String, String> map = mailParametersMapper.getOnboardingMailNotificationParameter(user, request, "");
        Assertions.assertNotNull(map);
    }

    @Test
    void getOnboardingNotificationPath(){
        when(mailTemplateConfig.getNotificationPath()).thenReturn("path");
        Assertions.assertNotNull(mailParametersMapper.getOnboardingNotificationPath());
    }

    @Test
    void getOnboardingCompletePath(){
        when(mailTemplateConfig.getCompletePath()).thenReturn("path");
        Assertions.assertNotNull(mailParametersMapper.getOnboardingCompletePath());
    }

    @Test
    void getCompleteOnbordingMailParameter(){
        when(mailTemplateConfig.getRejectProductName()).thenReturn("productName");
        when(mailTemplateConfig.getRejectOnboardingUrlPlaceholder()).thenReturn("getRejectOnboardingUrlPlaceholder");
        when(mailTemplateConfig.getRejectOnboardingUrlValue()).thenReturn("getRejectOnboardingUrlValue");

        Assertions.assertNotNull(mailParametersMapper.getCompleteOnbordingMailParameter("productName"));
    }

    @Test
    void getOnboardingNotificationAdminEmail(){
        when(mailTemplateConfig.getNotificationAdminEmail()).thenReturn("admin");

        Assertions.assertNotNull(mailParametersMapper.getOnboardingNotificationAdminEmail());
    }

    @Test
    void getOnboardingRejectNotificationPath(){
        when(mailTemplateConfig.getRejectPath()).thenReturn("path");

        Assertions.assertNotNull(mailParametersMapper.getOnboardingRejectNotificationPath());
    }

    @Test
    void getOnboardingRejectMailParameters(){
        when(mailTemplateConfig.getRejectProductName()).thenReturn("path");
        when(mailTemplateConfig.getRejectOnboardingUrlPlaceholder()).thenReturn("path");
        when(mailTemplateConfig.getRejectOnboardingUrlValue()).thenReturn("path");

        Assertions.assertNotNull(mailParametersMapper.getOnboardingRejectMailParameters("productName","productId"));
    }
}

