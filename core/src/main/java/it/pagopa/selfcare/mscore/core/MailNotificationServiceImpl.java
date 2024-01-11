package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

import static it.pagopa.selfcare.mscore.constant.ProductId.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailNotificationServiceImpl implements MailNotificationService {

    private static final String MAIL_PARAMETER_LOG = "mailParameters: {}";
    private static final String DESTINATION_MAIL_LOG = "destinationMails: {}";
    public static final String PAGOPA_LOGO_FILENAME = "pagopa-logo.png";
    private final InstitutionConnector institutionConnector;
    private final ProductConnector productConnector;
    private final MailTemplateConfig mailTemplateConfig;
    private final UserNotificationService userNotificationService;
    private final EmailConnector emailConnector;
    private final MailParametersMapper mailParametersMapper;
    private final CoreConfig coreConfig;
    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

    @Override
    public void setCompletedPGOnboardingMail(String destinationMail, String businessName) {
        log.trace("setCompletedPGOnboardingMail start");
        log.debug("setCompletedPGOnboardingMail destinationMail = {}, businessName = {}", destinationMail, businessName);
        emailConnector.sendMailPNPG(mailTemplateConfig.getPath(), destinationMail, businessName);
        log.trace("setCompletedPGOnboardingMail end");
    }

    public void sendAutocompleteMail(List<String> destinationMail, Map<String, String> templateParameters, File file, String fileName, String productName) {
        emailConnector.sendMail(mailTemplateConfig.getAutocompletePath(), destinationMail, file, productName, templateParameters, fileName);
    }

    public void sendMailWithContract(File pdf, Institution institution, User user, OnboardingRequest request, String token, boolean fromApprove) {
        List<String> destinationMail;
        Map<String, String> mailParameters;
        mailParameters = mailParametersMapper.getOnboardingMailParameter(user, request, token, institution.getDescription(), fromApprove);
        log.debug(MAIL_PARAMETER_LOG, mailParameters);
        destinationMail = Objects.nonNull(coreConfig.getDestinationMails()) && !coreConfig.getDestinationMails().isEmpty()
                ? coreConfig.getDestinationMails() : List.of(institution.getDigitalAddress());
        log.info(DESTINATION_MAIL_LOG, destinationMail);
        emailConnector.sendMail(mailTemplateConfig.getPath(), destinationMail, pdf, request.getProductName(), mailParameters, request.getProductName() + "_accordo_adesione.pdf");
        log.info("onboarding-contract-email Email successful sent");
    }

    public void sendMailForApprove(User user, OnboardingRequest request, String token) {
        Map<String, String> mailParameters = mailParametersMapper.getOnboardingMailNotificationParameter(user, request, token);
        log.debug(MAIL_PARAMETER_LOG, mailParameters);
        List<String> destinationMail = mailParametersMapper.getOnboardingNotificationAdminEmail();
        log.info(DESTINATION_MAIL_LOG, destinationMail);
        emailConnector.sendMail(mailParametersMapper.getOnboardingNotificationPath(), destinationMail, null, request.getProductName(), mailParameters, null);
        log.info("onboarding-complete-email-notification Email successful sent");

    }

    public void sendMailForRegistrationNotificationApprove(User user, OnboardingRequest request, String token) {
        Map<String, String> mailParameters = mailParametersMapper.getOnboardingMailNotificationParameter(user, request, token);
        log.debug(MAIL_PARAMETER_LOG, mailParameters);
        List<String> destinationMail = mailParametersMapper.getOnboardingNotificationAdminEmail();
        log.info(DESTINATION_MAIL_LOG, destinationMail);
        emailConnector.sendMail(mailParametersMapper.getRegistrationNotificationAdminPath(), destinationMail, null, request.getProductName(), mailParameters, null);
        log.info("onboarding-registration-email-notification Email successful sent");
    }

    public void sendMailForRegistration(User user, Institution institution, OnboardingRequest request) {
        Map<String, String> mailParameters = mailParametersMapper.getRegistrationRequestParameter(user, request);
        log.debug(MAIL_PARAMETER_LOG, mailParameters);
        List<String> destinationMail = Objects.nonNull(coreConfig.getDestinationMails()) && !coreConfig.getDestinationMails().isEmpty()
                ? coreConfig.getDestinationMails() : List.of(institution.getDigitalAddress());
        log.info(DESTINATION_MAIL_LOG, destinationMail);
        emailConnector.sendMail(mailParametersMapper.getRegistrationRequestPath(), destinationMail, null, request.getProductName(), mailParameters, null);
        log.info("registration-request-email Email successful sent");

    }

    public void sendCompletedEmail(List<User> managers, Institution institution, Product product, File logo, String templatePath) {
        List<String> destinationMails = new ArrayList<>(getDestinationMails(institution));
        if (managers != null && !managers.isEmpty()) {
            for (User user : managers) {
                if (user.getWorkContacts() != null && user.getWorkContacts().containsKey(institution.getId())) {
                    WorkContact certifiedWorkContact = user.getWorkContacts().get(institution.getId());
                    if (StringUtils.hasText(certifiedWorkContact.getEmail())) {
                        destinationMails.add(certifiedWorkContact.getEmail());
                    }
                }
            }
        }
        Map<String, String> mailParameter = mailParametersMapper.getCompleteOnbordingMailParameter(product.getTitle());
        emailConnector.sendMail(templatePath, destinationMails, logo, product.getTitle(), mailParameter, PAGOPA_LOGO_FILENAME);
    }

    public void sendCompletedEmail(List<User> managers, Institution institution, Product product, File logo) {
        String templatePath = product.getId().equals(PROD_FD.getValue())||product.getId().equals(PROD_FD_GARANTITO.getValue()) ? mailParametersMapper.getFdOnboardingCompletePath() : mailParametersMapper.getOnboardingCompletePath();
        this.sendCompletedEmail(managers, institution, product, logo, templatePath);
    }

    public void sendRejectMail(File logo, Institution institution, Product product) {
        Map<String, String> mailParameters = mailParametersMapper.getOnboardingRejectMailParameters(product.getTitle(), product.getId());
        log.debug(MAIL_PARAMETER_LOG, mailParameters);

        List<String> destinationMail = new ArrayList<>(getRejectDestinationMails(institution));
        log.info(DESTINATION_MAIL_LOG, destinationMail);

        emailConnector.sendMail(mailParametersMapper.getOnboardingRejectNotificationPath(), destinationMail, logo, product.getTitle(), mailParameters, "_pagopa-logo.png");
    }

    public void sendMailForDelegation(String institutionName, String productId, String partnerId) {
        try {
            Map<String, String> mailParameters;
            Product product = productConnector.getProductById(productId);
            Institution partnerInstitution = institutionConnector.findById(partnerId);
            if(Objects.isNull(product) || Objects.isNull(partnerInstitution)) {
                log.error("create-delegation-email-notification :: Impossible to send email. Error: partner institution or product is null");
                return;
            }
            mailParameters = mailParametersMapper.getDelegationNotificationParameter(institutionName, product.getTitle(), partnerInstitution.getDescription());
            log.debug(MAIL_PARAMETER_LOG, mailParameters);
            List<String> userDestinationMail = getUsersEmailByInstitutionAndProduct(partnerInstitution.getId(), productId);
            log.info(DESTINATION_MAIL_LOG, userDestinationMail);
            userNotificationService.sendDelegationUserNotification(userDestinationMail, mailParametersMapper.getDelegationUserNotificationPath(), product.getTitle(), mailParameters);
            log.info("create-delegation-user-email-notification :: Email successful sent");

            List<String> institutionDestinationMail = getDestinationMails(partnerInstitution);
            log.info(DESTINATION_MAIL_LOG, institutionDestinationMail);
            emailConnector.sendMail(mailParametersMapper.getDelegationNotificationPath(), institutionDestinationMail, null, product.getTitle(), mailParameters, null);
            log.info("create-delegation-institution-email-notification :: Email successful sent");
        } catch (Exception e) {
            log.error("create-delegation-email-notification :: Impossible to send email. Error: {}", e.getMessage(), e);
        }
    }

    private List<String> getRejectDestinationMails(Institution institution) {
        if (coreConfig.isSendEmailToInstitution()) {
            return List.of(institution.getDigitalAddress());
        } else {
            return List.of(coreConfig.getInstitutionAlternativeEmail());
        }
    }

    private List<String> getDestinationMails(Institution institution) {
        if (coreConfig.isSendEmailToInstitution()) {
            return List.of(institution.getDigitalAddress());
        } else {
            return List.of(coreConfig.getInstitutionAlternativeEmail());
        }
    }

    private List<String> getUsersEmailByInstitutionAndProduct(String institutionId, String productId) {
        return userConnector.findUsersByInstitutionIdAndProductId(institutionId, productId).stream()
                .map(userId -> userRegistryConnector.getUserByInternalIdWithCustomFields(userId, "workContacts"))
                .map(User::getWorkContacts)
                .filter(Objects::nonNull)
                .map(workContactMap -> workContactMap.get(institutionId))
                .filter(Objects::nonNull)
                .map(WorkContact::getEmail)
                .filter(StringUtils::hasText)
                .toList();
    }
}
