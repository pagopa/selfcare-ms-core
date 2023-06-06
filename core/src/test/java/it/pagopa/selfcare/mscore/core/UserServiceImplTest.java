package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRegistryConnector userRegistryConnector;

    @Mock
    private UserConnector userConnector;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager3() {
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        List<RelationshipState> states = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findOnboardedManager("42", "42", states));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager4() {
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        List<RelationshipState> states = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findOnboardedManager("42", "42", states));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    @Test
    void verifyUser() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.verifyUser("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveUserFromUserRegistry(String, EnumSet)}
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
        when(userRegistryConnector.getUserByInternalId( any(), any())).thenReturn(user);
        assertSame(user, userServiceImpl.retrieveUserFromUserRegistry("42", null));
        verify(userRegistryConnector).getUserByInternalId( any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId2() {
        when(userConnector.findById(any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId4() {
        when(userConnector.findById(any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findAllByIds(List)}
     */
    @Test
    void testFindAllByIds() {
        assertTrue(userServiceImpl.findAllByIds(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link UserServiceImpl#findAllByIds(List)}
     */
    @Test
    void testFindAllByIds2() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findAllByIds(any())).thenReturn(onboardedUserList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        List<OnboardedUser> actualFindAllByIdsResult = userServiceImpl.findAllByIds(stringList);
        assertSame(onboardedUserList, actualFindAllByIdsResult);
        assertTrue(actualFindAllByIdsResult.isEmpty());
        verify(userConnector).findAllByIds(any());
    }

    @Test
    void retrieveUsers() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findWithFilter(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);
        List<OnboardedUser> actualRetrieveAdminUsersResult = userServiceImpl.retrieveUsers("42", "42", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertSame(onboardedUserList, actualRetrieveAdminUsersResult);
        assertTrue(actualRetrieveAdminUsersResult.isEmpty());
        verify(userConnector).findWithFilter(any(), any(), any(),
                any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin() {
        when(userConnector.findActiveInstitutionUser(any(), any()
        )).thenReturn(new ArrayList<>());
        assertFalse(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser(any(), any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin2() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(new OnboardedUser());
        when(userConnector.findActiveInstitutionUser(any(), any()
        )).thenReturn(onboardedUserList);
        assertTrue(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser(any(), any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin3() {
        when(userConnector.findActiveInstitutionUser(any(), any()
        )).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser(any(), any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin4() {
        when(userConnector.findActiveInstitutionUser( any(),  any()
        )).thenReturn(new ArrayList<>());
        assertFalse(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser( any(),  any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin5() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(new OnboardedUser());
        when(userConnector.findActiveInstitutionUser( any(),  any()
        )).thenReturn(onboardedUserList);
        assertTrue(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser( any(),  any()
        );
    }

    @Test
    void findUserInstitutionAggregation() {
        UserInstitutionAggregation userInstitutionAggregation = new UserInstitutionAggregation();
        userInstitutionAggregation.setId("id");
        when(userConnector.findUserInstitutionAggregation(any())).thenReturn(List.of(userInstitutionAggregation));
        UserInstitutionFilter filter = new UserInstitutionFilter();
        filter.setUserId("id");
        Assertions.assertDoesNotThrow(() -> userServiceImpl.findUserInstitutionAggregation(filter));
    }
}

