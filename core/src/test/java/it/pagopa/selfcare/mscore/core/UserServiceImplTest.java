package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @Mock
    private UserConnector userConnector;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager() {
        when(userConnector.findOnboardedManager( any(),  any(),any()))
                .thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager( any(),  any(),any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager2() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUserList.add(onboardedUser);
        when(userConnector.findOnboardedManager( any(),  any(),any()))
                .thenReturn(onboardedUserList);
        assertSame(onboardedUser, userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager( any(),  any(),any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager3() {
        when(userConnector.findOnboardedManager( any(),  any(),any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager( any(),  any(),any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.getById( any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByUserId("42"));
        verify(userConnector).getById( any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId2() {
        when(userConnector.getById( any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByUserId("42"));
        verify(userConnector).getById( any());
    }
}

