package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TokenServiceImplTest {
    @Mock
    private TokenConnector tokenConnector;

    @InjectMocks
    private TokenServiceImpl tokenServiceImpl;

    /**
     * Method under test: {@link TokenServiceImpl#findActiveContract(String, String, String)}
     */
    @Test
    void testFindActiveContract() {
        when(tokenConnector.findActiveContract( any(),  any(),  any()))
                .thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> tokenServiceImpl.findActiveContract("42", "42", "42"));
        verify(tokenConnector).findActiveContract( any(),  any(),  any());
    }

    /**
     * Method under test: {@link TokenServiceImpl#findActiveContract(String, String, String)}
     */
    @Test
    void testFindActiveContract2() {
        ArrayList<Token> tokenList = new ArrayList<>();
        tokenList.add(new Token());
        when(tokenConnector.findActiveContract( any(),  any(),  any())).thenReturn(tokenList);
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

}

