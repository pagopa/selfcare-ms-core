package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;

import java.io.File;

public interface MailNotificationService {

    void sendMailWithContract(File pdf, Institution institution, User user, OnboardingRequest request, String token, boolean fromApprove);

    void sendMailForDelegation(String institutionName, String productId, String partnerId);

}
