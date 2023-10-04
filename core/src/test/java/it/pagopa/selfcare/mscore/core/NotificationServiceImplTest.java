package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.MailTemplate;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationServiceConnector notificationConnector;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private InstitutionConnector institutionConnector;

    @Mock
    private ProductConnector productConnector;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private MailTemplateConfig mailTemplateConfig;

    @Mock
    private EmailConnector emailConnector;

    @Mock
    private MailParametersMapper mailParametersMapper;

    @Mock
    private CoreConfig coreConfig;

    @Test
    void setCompletedPGOnboardingMail() throws JsonProcessingException {
        when(mailTemplateConfig.getPath()).thenReturn("path");
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("templateFile");
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setBody("body");
        mailTemplate.setSubject("subject");
        when(mapper.readValue("templateFile", MailTemplate.class)).thenReturn(mailTemplate);
        assertDoesNotThrow(() ->notificationService.setCompletedPGOnboardingMail("dest","buss"));
    }

    @Test
    void setCompletedPGOnboardingMailThrow() throws JsonProcessingException {
        when(mailTemplateConfig.getPath()).thenReturn("path");
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("templateFile");
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setBody("body");
        mailTemplate.setSubject("subject");
        when(mapper.readValue("templateFile", MailTemplate.class)).thenReturn(mailTemplate);
        doThrow(new MsCoreException("An error occurred", "Code")).when(notificationConnector)
                .sendNotificationToUser(any());
        assertThrows(MsCoreException.class, () -> notificationService.setCompletedPGOnboardingMail("42","42"));
    }

    @Test
    void sendAutocompleteMail(){
        File file = mock(File.class);
        Assertions.assertDoesNotThrow(() -> notificationService.sendAutocompleteMail(new ArrayList<>(), new HashMap<>(), file, "fileName","product"));
    }

    @Test
    void sendRejectMail(){
        File file = mock(File.class);
        when(coreConfig.isSendEmailToInstitution()).thenReturn(false);
        when(coreConfig.getInstitutionAlternativeEmail()).thenReturn("email");
        Assertions.assertDoesNotThrow(() -> notificationService.sendRejectMail(file, new Institution(), new Product()));
    }

    @Test
    void sendRejectMail2(){
        File file = mock(File.class);
        when(coreConfig.isSendEmailToInstitution()).thenReturn(true);
        Institution institution = new Institution();
        institution.setDigitalAddress("digital");
        Assertions.assertDoesNotThrow(() -> notificationService.sendRejectMail(file, institution, new Product()));
    }

    @Test
    void sendMailWithContract(){
        File file = mock(File.class);
        Institution institution = new Institution();
        institution.setDigitalAddress("digital");
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailWithContract(file, institution, new User(), new OnboardingRequest(), "", false));
    }

    @Test
    void sendMailForApprove(){
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForApprove(new User(), new OnboardingRequest(), "token"));
    }

    @Test
    void sendNotificationDelegationMail() {
        Product product = new Product();
        product.setTitle("test");
        Institution institution = new Institution();
        institution.setDigitalAddress("test@test.com");
        when(productConnector.getProductById(anyString())).thenReturn(product);
        when(institutionConnector.findById(anyString())).thenReturn(institution);
        when(coreConfig.isSendEmailToInstitution()).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForDelegation("institutionName", "productId", "partnerId"));
    }

    @Test
    void sendNotificationDelegationMailWithEmptyProduct() {
        Institution institution = new Institution();
        institution.setDigitalAddress("test@test.com");
        when(productConnector.getProductById(anyString())).thenReturn(null);
        when(institutionConnector.findById(anyString())).thenReturn(institution);
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForDelegation("institutionName", "productId", "partnerId"));
    }

    @Test
    void sendCompletedEmail(){
        File file = mock(File.class);

        User user1 = new User();
        user1.setId("1");
        user1.setFiscalCode("ABC123XYZ");
        user1.setName(new CertifiedField<>());
        user1.setFamilyName(new CertifiedField<>());
        user1.setEmail(new CertifiedField<>());

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);

        List<User> manager = new ArrayList<>();
        manager.add(user1);
        Product product = mockInstance(new Product());

        Institution institution = new Institution();
        institution.setId("id");
        institution.setDigitalAddress("digital");
        when(coreConfig.isSendEmailToInstitution()).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> notificationService.sendCompletedEmail(manager,institution,product,file));
    }

    @Test
    void sendCompletedEmail2(){
        File file = mock(File.class);

        User user1 = new User();
        user1.setId("1");
        user1.setFiscalCode("ABC123XYZ");
        user1.setName(new CertifiedField<>());
        user1.setFamilyName(new CertifiedField<>());
        user1.setEmail(new CertifiedField<>());

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);

        List<User> manager = new ArrayList<>();
        manager.add(user1);
        Product product = mockInstance(new Product());
        Institution institution = new Institution();
        institution.setId("id");
        institution.setDigitalAddress("digital");
        when(coreConfig.isSendEmailToInstitution()).thenReturn(false);
        when(coreConfig.getInstitutionAlternativeEmail()).thenReturn("email");
        Assertions.assertDoesNotThrow(() -> notificationService.sendCompletedEmail(manager,institution,product,file));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"prod-fd", "prod-fd-garantito"})
    void sendCompletedMailFD(String productId){
        File file = mock(File.class);

        User user1 = new User();
        user1.setId("1");
        user1.setFiscalCode("ABC123XYZ");
        user1.setName(new CertifiedField<>());
        user1.setFamilyName(new CertifiedField<>());
        user1.setEmail(new CertifiedField<>());

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);

        List<User> manager = new ArrayList<>();
        manager.add(user1);

        Product prodFD = new Product();
        prodFD.setId(productId);
        Institution institution = new Institution();
        institution.setId("id");
        institution.setDigitalAddress("digital");
        when(coreConfig.isSendEmailToInstitution()).thenReturn(false);
        when(coreConfig.getInstitutionAlternativeEmail()).thenReturn("email");
        Assertions.assertDoesNotThrow(() -> notificationService.sendCompletedEmail(manager,institution,prodFD,file));
    }
}
