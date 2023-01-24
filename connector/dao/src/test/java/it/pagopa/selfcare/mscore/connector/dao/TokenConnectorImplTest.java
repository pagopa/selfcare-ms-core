package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.model.Token;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TokenConnectorImplTest {

    @InjectMocks
    TokenConnectorImpl tokenConnectorImpl;

    @Mock
    TokenRepository tokenRepository;

    @Test
    void deleteById(){
        doNothing().when(tokenRepository).deleteById(any());
        tokenConnectorImpl.deleteById("507f1f77bcf86cd799439011");
    }

    @Test
    void findActiveContractTest(){
        TokenEntity token = new TokenEntity();
        token.setId(new ObjectId("507f1f77bcf86cd799439011"));
        when(tokenRepository.find(any(),any())).thenReturn(List.of(token));
        List<Token> tokenList = tokenConnectorImpl.findActiveContract("42","42","42");
        Assertions.assertEquals(1,tokenList.size());
        Assertions.assertEquals("507f1f77bcf86cd799439011",tokenList.get(0).getId());
    }

    @Test
    void save(){
        Token token = new Token();
        token.setId("507f1f77bcf86cd799439011");
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        when(tokenRepository.save(any())).thenReturn(tokenEntity);
        Token response = tokenConnectorImpl.save(token);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }
}
