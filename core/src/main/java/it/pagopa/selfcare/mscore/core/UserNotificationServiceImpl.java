package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.api.NotificationServiceConnector;
import it.pagopa.selfcare.mscore.model.notification.MultipleReceiverMessageRequest;
import it.pagopa.selfcare.mscore.model.onboarding.MailTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {

    private static final String ACTIVATE_SUBJECT = "Il tuo ruolo è stato riabilitato";
    private static final String DELETE_SUBJECT = "Il tuo ruolo è stato rimosso";
    private static final String SUSPEND_SUBJECT = "Il tuo ruolo è sospeso";
    private static final String CREATE_SUBJECT = "Hai un nuovo ruolo per un prodotto PagoPA";
    private static final String ACTIVATE_TEMPLATE = "user_activated.ftlh";
    private static final String DELETE_TEMPLATE = "user_deleted.ftlh";
    private static final String SUSPEND_TEMPLATE = "user_suspended.ftlh";
    private static final String CREATE_TEMPLATE_SINGLE_ROLE = "user_added_single_role.ftlh";
    private static final String CREATE_TEMPLATE_MULTIPLE_ROLE = "user_added_multi_role.ftlh";
    private static final String A_PRODUCT_TITLE_IS_REQUIRED = "A product Title is required";
    private static final String INSTITUTION_ID_IS_REQUIRED = "An institution id is required";
    private static final String PRODUCT_ROLES_ARE_REQUIRED = "ProductRoles are required";

    private static final String NAME_IS_REQUIRED = "RequesterName is required";

    private static final String SURNAME_IS_REQUIRED = "RequesterSurname is required";


    private final NotificationServiceConnector notificationConnector;
    private final FileStorageConnector fileStorageConnector;
    private final ObjectMapper mapper;

    @Override
    public void sendDelegationUserNotification(List<String> to, String templateName, String productName, Map<String, String> mailParameters) {
        try {
            String template =  fileStorageConnector.getTemplateFile(templateName);
            MailTemplate mailTemplate = mapper.readValue(template, MailTemplate.class);
            String html = StringSubstitutor.replace(mailTemplate.getBody(), mailParameters);

            MultipleReceiverMessageRequest messageRequest = new MultipleReceiverMessageRequest();
            messageRequest.setContent(html);
            messageRequest.setReceiverEmails(to);
            messageRequest.setSubject(productName + ": " + mailTemplate.getSubject());
            notificationConnector.sendNotificationToUsers(messageRequest);
        } catch (Exception e) {
            throw new MailPreparationException(e);
        }
    }

}
