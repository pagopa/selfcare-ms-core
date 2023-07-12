package it.pagopa.selfcare.mscore.connector.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.onboarding.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;

import static it.pagopa.selfcare.mscore.constant.GenericError.ERROR_DURING_SEND_MAIL;

@Slf4j
@Service
public class EmailConnectorImpl implements EmailConnector {

    private final JavaMailSender mailSender;
    private final FileStorageConnector fileStorageConnector;
    private final ObjectMapper mapper;
    private final CoreConfig coreConfig;

    @Autowired
    public EmailConnectorImpl(JavaMailSender javaMailSender,
                              FileStorageConnector fileStorageConnector,
                              ObjectMapper mapper,
                              CoreConfig coreConfig) {
        this.mailSender = javaMailSender;
        this.fileStorageConnector = fileStorageConnector;
        this.mapper = mapper;
        this.coreConfig = coreConfig;
    }

    @Override
    public void sendMail(String templateName, List<String> destinationMail, File pdf, String productName, Map<String, String> mailParameters, String fileName) {
        try {
            log.info("START - sendMail to {}, for product {}", destinationMail, productName);
            String template = fileStorageConnector.getTemplateFile(templateName);
            MailTemplate mailTemplate = mapper.readValue(template, MailTemplate.class);

            String html = StringSubstitutor.replace(mailTemplate.getBody(), mailParameters);
            log.trace("sendMessage start");
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            message.setSubject(productName + ": " + mailTemplate.getSubject());
            message.setFrom(coreConfig.getSenderMail());
            message.setTo(destinationMail.toArray(new String[0]));
            message.setText(html, true);
            if(pdf != null && StringUtils.hasText(fileName)) {
                message.addAttachment(fileName, pdf);
                log.info("sendMail to: {}, attached file: {}, for product {}", destinationMail, pdf.getName(), productName);
            }
            mailSender.send(mimeMessage);
            log.info("END - sendMail to {}, for product {}", destinationMail, productName);
        } catch (Exception e) {
            log.error(ERROR_DURING_SEND_MAIL.getMessage() + ":", e.getMessage(), e);
            throw new MsCoreException(ERROR_DURING_SEND_MAIL.getMessage(), ERROR_DURING_SEND_MAIL.getCode());
        }
        log.trace("sendMessage end");
    }
}

