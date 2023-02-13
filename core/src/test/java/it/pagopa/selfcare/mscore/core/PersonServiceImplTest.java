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
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    @Mock
    private UserConnector userConnector;

    @Test
    void testCreateUser() {
        when(userConnector.getByUser(any())).thenReturn(new ArrayList<>());
        when(userConnector.save(any())).thenReturn(new OnboardedUser());
        assertNotNull(personServiceImpl.createUser(new OnboardedUser()));
    }

    @Test
    void testCreateUser2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId("42");
        onboardedUser.setUser("User");
        List<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);

        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);

        assertThrows(ResourceNotFoundException.class, () -> personServiceImpl.createUser(new OnboardedUser()));
    }


    @Test
    void testFindByUserId() {
        when(userConnector.getByUser(any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> personServiceImpl.findByUserId("42"));
        verify(userConnector).getByUser(any());
    }

    @Test
    void testFindByUserId2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setId("42");
        onboardedUser.setUser("User");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        assertSame(onboardedUser, personServiceImpl.findByUserId("42"));
        verify(userConnector).getByUser(any());
    }
}

