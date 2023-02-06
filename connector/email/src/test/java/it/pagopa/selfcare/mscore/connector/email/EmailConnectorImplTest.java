package it.pagopa.selfcare.mscore.connector.email;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.exception.FileDownloadException;
import it.pagopa.selfcare.mscore.exception.MailException;
import it.pagopa.selfcare.mscore.model.MailTemplate;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

class EmailConnectorImplTest {
    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail() throws FileDownloadException, MailException {
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("Template File");
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        ObjectMapper mapper = new ObjectMapper();
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, mapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(fileStorageConnector).getTemplateFile(any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail2() throws FileDownloadException, MailException {

        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("Template File");
        ObjectMapper mapper = new ObjectMapper();
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(null, fileStorageConnector, mapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(fileStorageConnector).getTemplateFile(any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail3() throws FileDownloadException, MailException {

        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("42");
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        ObjectMapper mapper = new ObjectMapper();
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, mapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(fileStorageConnector).getTemplateFile(any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail4() throws FileDownloadException, MailException {

        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("");
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        ObjectMapper mapper = new ObjectMapper();
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, mapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(fileStorageConnector).getTemplateFile(any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail6() throws JsonProcessingException, FileDownloadException, MailException {

        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("Template File");

        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setBody("Not all who wander are lost");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue((String) any(), (Class<MailTemplate>) any())).thenReturn(mailTemplate);
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, objectMapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(fileStorageConnector).getTemplateFile(any());
        verify(objectMapper).readValue((String) any(), (Class<MailTemplate>) any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail7() throws JsonProcessingException, FileDownloadException, MailException {

        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile((any()))).thenReturn("Template File");
        MailTemplate mailTemplate = mock(MailTemplate.class);
        when(mailTemplate.getBody()).thenReturn("Not all who wander are lost");
        when(mailTemplate.getSubject()).thenReturn("Hello from the Dreaming Spires");
        doNothing().when(mailTemplate).setBody((any()));
        doNothing().when(mailTemplate).setSubject((any()));
        mailTemplate.setBody("Not all who wander are lost");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue((String) any(), (Class<MailTemplate>) any())).thenReturn(mailTemplate);
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, objectMapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(fileStorageConnector).getTemplateFile(any());
        verify(objectMapper).readValue((String) any(), (Class<MailTemplate>) any());
        verify(mailTemplate).getBody();
        verify(mailTemplate).getSubject();
        verify(mailTemplate).setBody(any());
        verify(mailTemplate).setSubject(any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail9() throws JsonProcessingException, FileDownloadException, MailException {

        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("Template File");
        MailTemplate mailTemplate = mock(MailTemplate.class);
        when(mailTemplate.getBody()).thenReturn("Not all who wander are lost");
        when(mailTemplate.getSubject()).thenReturn("Hello from the Dreaming Spires");
        doNothing().when(mailTemplate).setBody(any());
        doNothing().when(mailTemplate).setSubject(any());
        mailTemplate.setBody("Not all who wander are lost");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue((String) any(), (Class<MailTemplate>) any())).thenReturn(mailTemplate);
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, objectMapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(javaMailSender).createMimeMessage();
        verify(fileStorageConnector).getTemplateFile(any());
        verify(objectMapper).readValue((String) any(), (Class<MailTemplate>) any());
        verify(mailTemplate).getBody();
        verify(mailTemplate).getSubject();
        verify(mailTemplate).setBody(any());
        verify(mailTemplate).setSubject(any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail11() throws JsonProcessingException, FileDownloadException, MailException {

        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("Template File");
        MailTemplate mailTemplate = mock(MailTemplate.class);
        when(mailTemplate.getBody()).thenReturn("START - sendMail to {}, with file {}, for product {}");
        when(mailTemplate.getSubject()).thenReturn("Hello from the Dreaming Spires");
        doNothing().when(mailTemplate).setBody(any());
        doNothing().when(mailTemplate).setSubject(any());
        mailTemplate.setBody("Not all who wander are lost");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue((String) any(), (Class<MailTemplate>) any())).thenReturn(mailTemplate);
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, objectMapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(javaMailSender).createMimeMessage();
        verify(fileStorageConnector).getTemplateFile(any());
        verify(objectMapper).readValue((String) any(), (Class<MailTemplate>) any());
        verify(mailTemplate).getBody();
        verify(mailTemplate).getSubject();
        verify(mailTemplate).setBody(any());
        verify(mailTemplate).setSubject(any());
    }

    /**
     * Method under test: {@link EmailConnectorImpl#sendMail(String, List, File, String, Map)}
     */
    @Test
    void testSendMail12() throws JsonProcessingException, FileDownloadException, MailException {

        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("Template File");
        MailTemplate mailTemplate = mock(MailTemplate.class);
        when(mailTemplate.getBody()).thenReturn("");
        when(mailTemplate.getSubject()).thenReturn("Hello from the Dreaming Spires");
        doNothing().when(mailTemplate).setBody((any()));
        doNothing().when(mailTemplate).setSubject((any()));
        mailTemplate.setBody("Not all who wander are lost");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue((String) any(), (Class<MailTemplate>) any())).thenReturn(mailTemplate);
        EmailConnectorImpl emailConnectorImpl = new EmailConnectorImpl(javaMailSender, fileStorageConnector, objectMapper,
                new CoreConfig());
        ArrayList<String> destinationMail = new ArrayList<>();
        File pdf = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();
        Map<String, String> map = new HashMap<>();
        assertThrows(MailException.class,
                () -> emailConnectorImpl.sendMail("Template Name", destinationMail, pdf, "Product Name", map));
        verify(javaMailSender).createMimeMessage();
        verify(fileStorageConnector).getTemplateFile(any());
        verify(objectMapper).readValue((String) any(), (Class<MailTemplate>) any());
        verify(mailTemplate).getBody();
        verify(mailTemplate).getSubject();
        verify(mailTemplate).setBody(any());
        verify(mailTemplate).setSubject(any());
    }
}

