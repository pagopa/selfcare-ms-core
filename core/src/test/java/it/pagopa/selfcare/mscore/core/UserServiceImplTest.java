package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.core.util.NotificationMapper;
import it.pagopa.selfcare.mscore.core.util.NotificationMapperImpl;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapper;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapperImpl;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRegistryConnector userRegistryConnector;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Spy
    private NotificationMapper notificationMapper = new NotificationMapperImpl();

    @Spy
    private UserNotificationMapper userNotificationMapper = new UserNotificationMapperImpl();

    /**
     * Method under test: {@link UserServiceImpl#retrieveUserFromUserRegistry(String)}
     */
    @Test
    void testRetrieveUserFromUserRegistry() {
        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.NONE);
        certifiedField.setValue("42");

        CertifiedField<String> certifiedField1 = new CertifiedField<>();
        certifiedField1.setCertification(Certification.NONE);
        certifiedField1.setValue("42");

        CertifiedField<String> certifiedField2 = new CertifiedField<>();
        certifiedField2.setCertification(Certification.NONE);
        certifiedField2.setValue("42");

        User user = new User();
        user.setEmail(certifiedField);
        user.setFamilyName(certifiedField1);
        user.setFiscalCode("Fiscal Code");
        user.setId("42");
        user.setName(certifiedField2);
        user.setWorkContacts(new HashMap<>());
        when(userRegistryConnector.getUserByInternalIdWithFiscalCode( any())).thenReturn(user);
        assertSame(user, userServiceImpl.retrieveUserFromUserRegistry("42"));
        verify(userRegistryConnector).getUserByInternalIdWithFiscalCode( user.getId());
    }


    @Test
    void retrieveUsersFromRegistry() {
        User user = new User();
        user.setId("fiscalCode");
        when(userRegistryConnector.getUserByFiscalCode(any())).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.retrieveUserFromUserRegistryByFiscalCode("fiscalCode"));
        assertEquals("fiscalCode", user.getId());
    }

    @Test
    void persistUsersFromRegistry() {
        User user = new User();
        user.setId("fiscalCode");
        when(userRegistryConnector.persistUserUsingPatch(any(), any(), any(), any(), any())).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.persistUserRegistry("name", "familyName", "fiscalCode", "email", "institutionId"));
    }

    @Test
    void persistWorksContractToUserRegistry() {
        User user = new User();
        user.setId("fiscalCode");
        when(userRegistryConnector.persistUserWorksContractUsingPatch("fiscalCode", "email", "institutionId")).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.persistWorksContractToUserRegistry( "fiscalCode", "email", "institutionId"));
    }

}

