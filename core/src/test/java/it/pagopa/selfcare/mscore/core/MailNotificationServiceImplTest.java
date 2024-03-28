package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailNotificationServiceImplTest {

    @InjectMocks
    private MailNotificationServiceImpl notificationService;

    @Mock
    private InstitutionConnector institutionConnector;

    @Mock
    private ProductConnector productConnector;

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

        when(productConnector.getProductById(anyString())).thenReturn(product);
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
        when(productConnector.getProductById(anyString())).thenReturn(null);
        when(institutionConnector.findById(anyString())).thenReturn(institution);
        Assertions.assertDoesNotThrow(() -> notificationService.sendMailForDelegation("institutionName", "productId", "partnerId"));
    }
}
