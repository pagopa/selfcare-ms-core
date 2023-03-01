package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TokenConnectorImplTest {

    @InjectMocks
    TokenConnectorImpl tokenConnectorImpl;

    @Mock
    TokenRepository tokenRepository;

    @Test
    void deleteById() {
        doNothing().when(tokenRepository).deleteById(any());
        Assertions.assertDoesNotThrow(() -> tokenConnectorImpl.deleteById("507f1f77bcf86cd799439011"));
    }

    @Test
    void findActiveContractTest() {
        TokenEntity token = new TokenEntity();
        token.setId("507f1f77bcf86cd799439011");
        when(tokenRepository.find(any(), any())).thenReturn(List.of(token));
        Token tokenResp = tokenConnectorImpl.findActiveContract("42", "42", "42");
        Assertions.assertEquals("507f1f77bcf86cd799439011", tokenResp.getId());
    }

    @Test
    void save() {
        Token token = new Token();
        token.setId("507f1f77bcf86cd799439011");
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setId("507f1f77bcf86cd799439011");
        when(tokenRepository.save(any())).thenReturn(tokenEntity);
        Token response = tokenConnectorImpl.save(token);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }

    @Test
    void save2() {
        Token token = new Token();
        token.setProductId("507f1f77bcf86cd799439011");
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setProductId("507f1f77bcf86cd799439011");
        when(tokenRepository.save(any())).thenReturn(tokenEntity);
        Token response = tokenConnectorImpl.save(token);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getProductId());
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findById(String)}
     */
    @Test
    void testFindById() {
        when(tokenRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tokenConnectorImpl.findById("42"));
        verify(tokenRepository).findById(any());
    }

    @Test
    void testFindById2() {
        TokenEntity tokenEntity = new TokenEntity();
        when(tokenRepository.findById(any())).thenReturn(Optional.of(tokenEntity));
        Token response = tokenConnectorImpl.findById("tokenId");
        assertNotNull(response);
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findAndUpdateToken(String, RelationshipState, String)}
     */
    @Test
    void testFindAndUpdateTokenUser() {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setContract("Contract");
        tokenEntity.setCreatedAt(null);
        tokenEntity.setExpiringDate("2020-03-01");
        tokenEntity.setId("42");
        tokenEntity.setInstitutionId("42");
        tokenEntity.setProductId("42");
        tokenEntity.setStatus(RelationshipState.PENDING);
        tokenEntity.setUpdatedAt(null);
        tokenEntity.setUsers(new ArrayList<>());
        when(tokenRepository.findAndModify(any(), any(), any(),any())).thenReturn(tokenEntity);
        tokenConnectorImpl.findAndUpdateToken("42", RelationshipState.REJECTED, "");
        verify(tokenRepository).findAndModify(any(), any(), any(), any());
    }

}
