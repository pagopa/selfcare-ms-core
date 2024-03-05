package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailNotificationServiceImpl implements MailNotificationService {

    private static final String MAIL_PARAMETER_LOG = "mailParameters: {}";
    private static final String DESTINATION_MAIL_LOG = "destinationMails: {}";
    private final InstitutionConnector institutionConnector;
    private final ProductService productService;
    private final MailTemplateConfig mailTemplateConfig;
    private final UserNotificationService userNotificationService;
    private final EmailConnector emailConnector;
    private final MailParametersMapper mailParametersMapper;
    private final CoreConfig coreConfig;
    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

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

    public void sendMailForDelegation(String institutionName, String productId, String partnerId) {
        try {
            Map<String, String> mailParameters;
            Product product = productService.getProduct(productId);
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
