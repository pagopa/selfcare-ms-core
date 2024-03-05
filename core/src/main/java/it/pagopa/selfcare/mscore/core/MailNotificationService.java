package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.product.entity.Product;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface MailNotificationService {

    void setCompletedPGOnboardingMail(String destinationMail, String businessName);

    void sendAutocompleteMail(List<String> destinationMail, Map<String, String> templateParameters, File file, String fileName, String productName);

    void sendMailWithContract(File pdf, Institution institution, User user, OnboardingRequest request, String token, boolean fromApprove);

    void sendMailForApprove(User user, OnboardingRequest request, String token);

    void sendMailForRegistrationNotificationApprove(User user, OnboardingRequest request, String token);

    void sendCompletedEmail(List<User> managers, Institution institution, Product product, File logo);
    void sendCompletedEmail(List<User> managers, Institution institution, Product product, File logo, String templatePath);

    void sendRejectMail(File logo, Institution institution, Product product);

    void sendMailForDelegation(String institutionName, String productId, String partnerId);

    void sendMailForRegistration(User user, Institution institution, OnboardingRequest onboardingRequest);
}
