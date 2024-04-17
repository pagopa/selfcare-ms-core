package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.client.UserApiRestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserApiConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class UserApiConnectorImplTest {

    @Autowired
    private UserApiConnectorImpl userApiConnector;

    @MockBean
    private UserApiRestClient userApiRestClient;

    @Test
    void getUserEmailsByInstitutionAndProduct(){
        //given
        final String institutionId = "institutionId";
        final String productId  = "productId";
        ResponseEntity<List<String>> emails = new ResponseEntity<>(List.of("email"), HttpStatus.OK);

        when(userApiRestClient._usersEmailsGet(anyString(), anyString())).thenReturn(emails);

        //when
        List<String> userEmails = userApiConnector.getUserEmails(institutionId, productId);
        //then
        assertFalse(userEmails.isEmpty());
        verify(userApiRestClient, times(1))._usersEmailsGet(institutionId, productId);

    }
}