package it.pagopa.selfcare.mscore.connector.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.EmailConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.exception.MailException;
import it.pagopa.selfcare.mscore.model.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;

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
                              CoreConfig coreConfig)
    {
        this.mailSender = javaMailSender;
        this.fileStorageConnector = fileStorageConnector;
        this.mapper = mapper;
        this.coreConfig = coreConfig;
    }

    @Override
    public void sendMail(String templateName, List<String> destinationMail, File pdf, String productName, Map<String, String> mailParameters) throws MailException {
        try {
            log.info("START - sendMail to {}, with file {}, for product {}", destinationMail, pdf.getName(), productName);
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
            message.addAttachment(productName + "_accordo_adesione.pdf", pdf);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new MailException(e);
        }
        log.trace("sendMessage end");
    }
}

