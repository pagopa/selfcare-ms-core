package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {
    @Mock
    private TokenConnector tokenConnector;

    @InjectMocks
    private TokenServiceImpl tokenServiceImpl;

    @Test
    void verifyToken(){
        Token token = new Token();
        ArrayList<TokenUser>users = new ArrayList<>();
       users.add(new TokenUser());
        token.setUsers(users);
        token.setStatus(RelationshipState.ACTIVE);
        when(tokenConnector.findById(any())).thenReturn(token);
        assertThrows(ResourceConflictException.class, () -> tokenServiceImpl.verifyToken("42"));
    }

    @Test
    void verifyToken2(){
        Token token = new Token();
        ArrayList<TokenUser> stringList = new ArrayList<>();
        stringList.add(new TokenUser());
        token.setUsers(stringList);
        token.setStatus(RelationshipState.TOBEVALIDATED);
        when(tokenConnector.findById(any())).thenReturn(token);
        Token response = tokenServiceImpl.verifyToken("token");
        assertNotNull(response);
    }

    @Test
    void verifyOnboarding(){
        Token token = new Token();
        when(tokenConnector.findWithFilter(any(),any())).thenReturn(token);
        Assertions.assertDoesNotThrow(() -> tokenServiceImpl.verifyOnboarding("42","42"));
    }

}

