package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TokenServiceImpl.class})
@ExtendWith(SpringExtension.class)
class TokenServiceImplTest {
    @MockBean
    private TokenConnector tokenConnector;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    /**
     * Method under test: {@link TokenServiceImpl#verifyToken(String)}
     */
    @Test
    void testVerifyToken2() {
        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        Optional<Token> ofResult = Optional.of(token);
        when(tokenConnector.findById(any())).thenReturn(ofResult);
        assertSame(token, tokenServiceImpl.verifyToken("42"));
        verify(tokenConnector).findById(any());
    }

    /**
     * Method under test: {@link TokenServiceImpl#verifyToken(String)}
     */
    @Test
    void testVerifyToken4() {
        when(tokenConnector.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tokenServiceImpl.verifyToken("42"));
        verify(tokenConnector).findById(any());
    }
}

