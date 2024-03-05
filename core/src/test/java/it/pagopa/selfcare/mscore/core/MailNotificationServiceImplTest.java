package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailNotificationServiceImplTest {

    @InjectMocks
    private MailNotificationServiceImpl notificationService;

    @Mock
    private NotificationServiceConnector notificationConnector;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private InstitutionConnector institutionConnector;

    @Mock
    private ProductService productService;

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
    @Mock
    private UserConnector userConnector;
    @Mock
    private UserRegistryConnector userRegistryConnector;
    @Mock
    private UserNotificationService userNotificationService;


    private static final User user;

    static {
        user = new User();
        user.setId("1");
        user.setFiscalCode("ABC123XYZ");
        user.setName(new CertifiedField<>());
        user.setFamilyName(new CertifiedField<>());
        user.setEmail(new CertifiedField<>());

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user.setWorkContacts(workContacts1);
    }

    @Test
    void setCompletedPGOnboardingMail() {
        final String path = "path";
        final String destination = "dest";
        final String businessName = "buss";
        when(mailTemplateConfig.getPath()).thenReturn(path);
        assertDoesNotThrow(() ->notificationService.setCompletedPGOnboardingMail(destination,businessName));
        Mockito.verify(emailConnector, times(1)).sendMailPNPG(path,destination,businessName);
        Mockito.verifyNoMoreInteractions(emailConnector);
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
        OnboardingRequest onboardingRequest =  new OnboardingRequest();
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForApprove(new User(), onboardingRequest, "token"));
    }

    @Test
    void sendMailForRegistrationNotificationApprove(){
        OnboardingRequest onboardingRequest =  new OnboardingRequest();
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setInstitutionType(InstitutionType.PT);
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForRegistrationNotificationApprove(new User(), onboardingRequest, "token"));
    }

    @Test
    void sendMailForRegistration(){
        Institution institution =  new Institution();
        institution.setDigitalAddress("42");
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForRegistration(new User(), institution , new OnboardingRequest()));
    }

    @Test
    void sendNotificationDelegationMail() {
        Product product = new Product();
        product.setId("productId");
        product.setTitle("test");
        Institution institution = new Institution();
        institution.setId("institutionID");
        institution.setDigitalAddress("test@test.com");

        var mail = new CertifiedField<String>();
        mail.setValue("test@test2.com");
        var work = new WorkContact();
        work.setEmail(mail);
        var map = new HashMap<String, WorkContact>();
        map.put("institutionID", work);
        var user = new User();
        user.setId("userID");
        user.setWorkContacts(map);

        when(productService.getProduct(anyString())).thenReturn(product);
        when(institutionConnector.findById(anyString())).thenReturn(institution);
        when(userConnector.findUsersByInstitutionIdAndProductId(institution.getId(), product.getId())).thenReturn(List.of(user.getId()));
        when(userRegistryConnector.getUserByInternalIdWithCustomFields(user.getId(), "workContacts")).thenReturn(user);
        when(coreConfig.isSendEmailToInstitution()).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForDelegation("institutionName", "productId", "partnerId"));
    }

    @Test
    void sendNotificationDelegationMailWithEmptyProduct() {
        Institution institution = new Institution();
        institution.setDigitalAddress("test@test.com");
        when(productService.getProduct(anyString())).thenReturn(null);
        when(institutionConnector.findById(anyString())).thenReturn(institution);
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForDelegation("institutionName", "productId", "partnerId"));
    }

    @Test
    void sendCompletedEmail(){
        File file = mock(File.class);

        List<User> manager = new ArrayList<>();
        manager.add(user);
        Product product = mock(Product.class);
        when(product.getId()).thenReturn("test");
        Institution institution = new Institution();
        institution.setId("id");
        institution.setDigitalAddress("digital");
        when(coreConfig.isSendEmailToInstitution()).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> notificationService.sendCompletedEmail(manager,institution,product,file));
    }

    @Test
    void sendCompletedEmail2(){
        File file = mock(File.class);

        List<User> manager = new ArrayList<>();
        manager.add(user);
        Product product = mock(Product.class);
        when(product.getId()).thenReturn("test");

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

        List<User> manager = new ArrayList<>();
        manager.add(user);

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
