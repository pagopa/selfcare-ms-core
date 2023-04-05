package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailService {

    private static final String MAIL_PARAMETER_LOG = "mailParameters: {}";
    private static final String DESTINATION_MAIL_LOG = "destinationMails: {}";

    private final EmailConnector emailConnector;
    private final MailParametersMapper mailParametersMapper;
    private final CoreConfig coreConfig;

    public EmailService(EmailConnector emailConnector,
                        MailParametersMapper mailParametersMapper,
                        CoreConfig coreConfig) {
        this.emailConnector = emailConnector;
        this.mailParametersMapper = mailParametersMapper;
        this.coreConfig = coreConfig;
    }


    public void sendMail(File pdf, Institution institution, User user, OnboardingRequest request, String token, boolean isApproved) {
        List<String> destinationMail;
        Map<String, String> mailParameters;
        if (InstitutionType.PA == institution.getInstitutionType() || isApproved) {
            mailParameters = mailParametersMapper.getOnboardingMailParameter(user, request, token);
            log.debug(MAIL_PARAMETER_LOG, mailParameters);
            destinationMail = coreConfig.getDestinationMails() != null ? coreConfig.getDestinationMails() : List.of(institution.getDigitalAddress());
            log.info(DESTINATION_MAIL_LOG, destinationMail);
            emailConnector.sendMail(mailParametersMapper.getOnboardingPath(), destinationMail, pdf, request.getProductId(), mailParameters, request.getProductId() + "_accordo_adesione.pdf");
            log.info(" onboarding-contract-email Email successful sent");
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
            destinationMails.addAll(managers.stream().filter(user -> user.getEmail() != null && !destinationMails.contains(user.getEmail()))
                    .map(User::getEmail).collect(Collectors.toList()));
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
        if(coreConfig.getDestinationMails()!=null){
            return coreConfig.getDestinationMails();
        }else if(institution.getDigitalAddress()!=null){
            return List.of(institution.getDigitalAddress());
        }else{
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
