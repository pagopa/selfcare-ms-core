package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.notification.MessageRequest;
import it.pagopa.selfcare.mscore.model.onboarding.MailTemplate;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

import static it.pagopa.selfcare.mscore.constant.GenericError.ERROR_DURING_SEND_MAIL;
import static it.pagopa.selfcare.mscore.constant.ProductId.*;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String MAIL_PARAMETER_LOG = "mailParameters: {}";
    private static final String DESTINATION_MAIL_LOG = "destinationMails: {}";
    public static final String PAGOPA_LOGO_FILENAME = "pagopa-logo.png";
    private final NotificationServiceConnector notificationConnector;
    private final FileStorageConnector fileStorageConnector;
    private final InstitutionConnector institutionConnector;
    private final ProductConnector productConnector;
    private final ObjectMapper mapper;
    private final MailTemplateConfig mailTemplateConfig;
    private final EmailConnector emailConnector;
    private final MailParametersMapper mailParametersMapper;
    private final CoreConfig coreConfig;

    @Autowired
    public NotificationServiceImpl(NotificationServiceConnector notificationConnector,
                                   FileStorageConnector fileStorageConnector,
                                   InstitutionConnector institutionConnector,
                                   ProductConnector productConnector,
                                   ObjectMapper mapper,
                                   MailTemplateConfig mailTemplateConfig,
                                   EmailConnector emailConnector,
                                   MailParametersMapper mailParametersMapper,
                                   CoreConfig coreConfig) {
        this.notificationConnector = notificationConnector;
        this.fileStorageConnector = fileStorageConnector;
        this.institutionConnector = institutionConnector;
        this.productConnector = productConnector;
        this.mapper = mapper;
        this.mailTemplateConfig = mailTemplateConfig;
        this.emailConnector = emailConnector;
        this.mailParametersMapper = mailParametersMapper;
        this.coreConfig = coreConfig;
    }

    @Override
    public void setCompletedPGOnboardingMail(String destinationMail, String businessName) {
        try {
            log.info("START - sendMail to {}, for product {}", destinationMail, PROD_PN);
            String template = fileStorageConnector.getTemplateFile(mailTemplateConfig.getPath());
            MailTemplate mailTemplate = mapper.readValue(template, MailTemplate.class);
            MessageRequest messageRequest = constructMessageRequest(destinationMail, businessName, mailTemplate);
            log.trace("sendMessage start");
            notificationConnector.sendNotificationToUser(messageRequest);
        } catch (Exception e) {
            log.error(ERROR_DURING_SEND_MAIL.getMessage() + ":", e.getMessage(), e);
            throw new MsCoreException(ERROR_DURING_SEND_MAIL.getMessage(), ERROR_DURING_SEND_MAIL.getCode());
        }
        log.trace("sendMessage end");
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
        Map<String, String> mailParameters;
        mailParameters = mailParametersMapper.getOnboardingMailNotificationParameter(user, request, token);
        log.debug(MAIL_PARAMETER_LOG, mailParameters);
        List<String> destinationMail = mailParametersMapper.getOnboardingNotificationAdminEmail();
        log.info(DESTINATION_MAIL_LOG, destinationMail);
        if (!InstitutionType.PT.equals(request.getInstitutionUpdate().getInstitutionType())) {
            emailConnector.sendMail(mailParametersMapper.getOnboardingNotificationPath(), destinationMail, null, request.getProductName(), mailParameters, null);
        } else {
            emailConnector.sendMail(mailParametersMapper.getRegistrationNotificationAdminPath(), destinationMail, null, request.getProductName(), mailParameters, null);
        }
        log.info("onboarding-complete-email-notification Email successful sent");

    }

    public void sendMailToPT(User user,Institution institution, OnboardingRequest request) {
        Map<String, String> mailParameters;
        mailParameters = mailParametersMapper.getRegistrationRequestParameter(user, request);
        log.debug(MAIL_PARAMETER_LOG, mailParameters);
        List<String> destinationMail = Objects.nonNull(coreConfig.getDestinationMails()) && !coreConfig.getDestinationMails().isEmpty()
                ? coreConfig.getDestinationMails() : List.of(institution.getDigitalAddress());
        log.info(DESTINATION_MAIL_LOG, destinationMail);
        emailConnector.sendMail(mailParametersMapper.getRegistrationRequestPath(), destinationMail, null, request.getProductName(), mailParameters, null);
        log.info("registration-request-email Email successful sent");

    }

    public void sendCompletedEmail(List<User> managers, Institution institution, Product product, File logo) {
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
        String templatePath = product.getId().equals(PROD_FD.getValue())||product.getId().equals(PROD_FD_GARANTITO.getValue())? mailParametersMapper.getFdOnboardingCompletePath():mailParametersMapper.getOnboardingCompletePath();
        emailConnector.sendMail(templatePath, destinationMails, logo, product.getTitle(), mailParameter, PAGOPA_LOGO_FILENAME);
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
            mailParameters = mailParametersMapper.getDelegationNotificationParameter(institutionName, product.getTitle());
            log.debug(MAIL_PARAMETER_LOG, mailParameters);
            List<String> destinationMail = getDestinationMails(partnerInstitution);
            log.info(DESTINATION_MAIL_LOG, destinationMail);
            emailConnector.sendMail(mailParametersMapper.getDelegationNotificationPath(), destinationMail, null, product.getTitle(), mailParameters, null);
            log.info("create-delegation-email-notification :: Email successful sent");
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

    private MessageRequest constructMessageRequest(String destinationMail, String businessName, MailTemplate mailTemplate) {
        Map<String, String> mailParameters = new HashMap<>();
        mailParameters.put("businessName", businessName);
        String html = StringSubstitutor.replace(mailTemplate.getBody(), mailParameters);
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setSubject(mailTemplate.getSubject());
        messageRequest.setReceiverEmail(destinationMail);
        messageRequest.setContent(html);
        return messageRequest;
    }
}
