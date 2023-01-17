package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.model.Token;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = TokenConnectorImpl.class)
@ExtendWith(SpringExtension.class)
class TokenConnectorImplTest {

    @Autowired
    TokenConnectorImpl tokenConnectorImpl;

    @MockBean
    TokenRepository tokenRepository;

    @Test
    void findActiveContractTest(){
        TokenEntity token = new TokenEntity();
        token.setId(new ObjectId("507f1f77bcf86cd799439011"));
        when(tokenRepository.find(any(),any())).thenReturn(List.of(token));
        List<Token> tokenList = tokenConnectorImpl.findActiveContract("42","42","42");
        Assertions.assertEquals(1,tokenList.size());
        Assertions.assertEquals("507f1f77bcf86cd799439011",tokenList.get(0).getId());
    }
}
