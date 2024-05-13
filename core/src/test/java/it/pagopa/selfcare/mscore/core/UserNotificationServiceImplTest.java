package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.api.NotificationServiceConnector;
import it.pagopa.selfcare.mscore.model.onboarding.MailTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserNotificationServiceImplTest {

    @InjectMocks
    private UserNotificationServiceImpl userNotificationService;

    @Mock
    private NotificationServiceConnector notificationConnector;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private ObjectMapper objectMapper;


    @Test
    void sendDelegationUserNotification() throws JsonProcessingException {
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("template");
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setBody(Base64.getEncoder().encodeToString("test".getBytes(StandardCharsets.UTF_8)));
        mailTemplate.setSubject(Base64.getEncoder().encodeToString("test".getBytes(StandardCharsets.UTF_8)));
        when(objectMapper.readValue(anyString(),(Class) any())).thenReturn(mailTemplate);
        assertDoesNotThrow(() -> userNotificationService.sendDelegationUserNotification(List.of("id"),"institution","product", new HashMap<>()));
    }
}
