package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmailService {

    private static final String MAIL_PARAMETER_LOG = "mailParameters: {}";
    private static final String DESTINATION_MAIL_LOG = "destinationMails: {}";
    public static final String PAGOPA_LOGO_FILENAME = "pagopa-logo.png";

    private final EmailConnector emailConnector;
    private final MailParametersMapper mailParametersMapper;
    private final CoreConfig coreConfig;
    private final MailTemplateConfig mailTemplateConfig;

    public EmailService(EmailConnector emailConnector,
                        MailParametersMapper mailParametersMapper,
                        CoreConfig coreConfig,
                        MailTemplateConfig mailTemplateConfig) {
        this.emailConnector = emailConnector;
        this.mailParametersMapper = mailParametersMapper;
        this.coreConfig = coreConfig;
        this.mailTemplateConfig = mailTemplateConfig;
    }

    public void sendAutocompleteMail(List<String> destinationMail, Map<String, String> templateParameters, File file, String fileName) {
        emailConnector.sendMail(mailTemplateConfig.getAutocompletePath(), destinationMail, file, "simpleMail", templateParameters, fileName);
    }

    public void sendMail(File pdf, Institution institution, User user, OnboardingRequest request, String token, boolean isApproved, InstitutionType institutionType) {
        List<String> destinationMail;
        Map<String, String> mailParameters;
        if (InstitutionType.PA == institutionType ||
                (InstitutionType.GSP == institutionType && request.getProductId().equals("prod-interop") && institution.getOrigin().equals("IPA"))
                || isApproved) {
            mailParameters = mailParametersMapper.getOnboardingMailParameter(user, request, token);
            log.debug(MAIL_PARAMETER_LOG, mailParameters);
            destinationMail = coreConfig.getDestinationMails() != null ? coreConfig.getDestinationMails() : List.of(institution.getDigitalAddress());
            log.info(DESTINATION_MAIL_LOG, destinationMail);
            emailConnector.sendMail(mailTemplateConfig.getPath(), destinationMail, pdf, request.getProductId(), mailParameters, request.getProductId() + "_accordo_adesione.pdf");
            log.info("onboarding-contract-email Email successful sent");
        } else {
            mailParameters = mailParametersMapper.getOnboardingMailNotificationParameter(user, request, token);
            log.debug(MAIL_PARAMETER_LOG, mailParameters);
            destinationMail = mailParametersMapper.getOnboardingNotificationAdminEmail();
            log.info(DESTINATION_MAIL_LOG, destinationMail);
            emailConnector.sendMail(mailParametersMapper.getOnboardingNotificationPath(), destinationMail, pdf, request.getProductId(), mailParameters, request.getProductId() + "_accordo_adesione.pdf");
            log.info("onboarding-complete-email-notification Email successful sent");
        }
    }

    public void sendCompletedEmail(List<User> managers, Institution institution, Product product, File logo) {
        List<String> destinationMails = new ArrayList<>(getCompleteDestinationMails(institution));
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
        emailConnector.sendMail(mailParametersMapper.getOnboardingCompletePath(), destinationMails, logo, product.getTitle(), mailParameter, "pagopa-logo.png");
    }

    public void sendRejectMail(File logo, Institution institution, Product product) {
        Map<String, String> mailParameters = mailParametersMapper.getOnboardingRejectMailParameters(product.getTitle(), product.getId());
        log.debug(MAIL_PARAMETER_LOG, mailParameters);

        List<String> destinationMail = new ArrayList<>(getRejectDestinationMails(institution));
        log.info(DESTINATION_MAIL_LOG, destinationMail);

        emailConnector.sendMail(mailParametersMapper.getOnboardingRejectNotificationPath(), destinationMail, logo, institution.getDescription(), mailParameters, "_pagopa-logo.png");
    }

    private List<String> getRejectDestinationMails(Institution institution) {
        if (coreConfig.getDestinationMails() != null) {
            return coreConfig.getDestinationMails();
        } else if (institution.getDigitalAddress() != null) {
            return List.of(institution.getDigitalAddress());
        } else {
            return List.of(coreConfig.getInstitutionAlternativeEmail());
        }
    }

    private List<String> getCompleteDestinationMails(Institution institution) {
        if (coreConfig.isSendEmailToInstitution()) {
            return List.of(institution.getDigitalAddress());
        } else {
            return List.of(coreConfig.getInstitutionAlternativeEmail());
        }
    }

}
