package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TokenServiceImplTest {
    @Mock
    private TokenConnector tokenConnector;

    @InjectMocks
    private TokenServiceImpl tokenServiceImpl;

    @Mock
    private UserService userService;

    @Test
    void getToken(){
        when(tokenConnector.findById(any())).thenReturn(new Token());
        when(userService.findAllByIds(any())).thenReturn(new ArrayList<>());
        assertNotNull(tokenServiceImpl.getToken("id"));
    }

    /**
     * Method under test: {@link TokenServiceImpl#findActiveContract(String, String, String)}
     */
    @Test
    void testFindActiveContract2() {
        when(tokenConnector.findActiveContract( any(),  any(),  any())).thenReturn(new Token());
        assertNull(tokenServiceImpl.findActiveContract("42", "42", "42"));
        verify(tokenConnector).findActiveContract( any(),  any(),  any());
    }

    /**
     * Method under test: {@link TokenServiceImpl#findActiveContract(String, String, String)}
     */
    @Test
    void testFindActiveContract3() {
        when(tokenConnector.findActiveContract( any(),  any(),  any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> tokenServiceImpl.findActiveContract("42", "42", "42"));
        verify(tokenConnector).findActiveContract( any(),  any(),  any());
    }

    @Test
    void verifyToken(){
        Token token = new Token();
        ArrayList<String> users = new ArrayList<>();
        users.add("user");
        token.setUsers(users);
        token.setStatus(RelationshipState.ACTIVE);
        when(tokenConnector.findById(any())).thenReturn(token);
        assertThrows(ResourceConflictException.class, () -> tokenServiceImpl.verifyToken("42"));
    }

    @Test
    void verifyToken2(){
        Token token = new Token();
        ArrayList<String> users = new ArrayList<>();
        users.add("user");
        token.setUsers(users);
        token.setStatus(RelationshipState.TOBEVALIDATED);
        when(tokenConnector.findById(any())).thenReturn(token);
        Token response = tokenServiceImpl.verifyToken("token");
        assertNotNull(response);
    }

    @Test
    void verifyOnboarding(){
        Token token = new Token();
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(token);
        when(tokenConnector.findWithFilter(any(),any(),any())).thenReturn(tokens);
        Assertions.assertDoesNotThrow(() -> tokenServiceImpl.verifyOnboarding("42","42",new ArrayList<>()));
    }

    @Test
    void verifyOnboarding2(){
        when(tokenConnector.findWithFilter(any(),any(),any())).thenReturn(new ArrayList<>());
        Assertions.assertThrows(InvalidRequestException.class, () -> tokenServiceImpl.verifyOnboarding("42","42",new ArrayList<>()));
    }

}

