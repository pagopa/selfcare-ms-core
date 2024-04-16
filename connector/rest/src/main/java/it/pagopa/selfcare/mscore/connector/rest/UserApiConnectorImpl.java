package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.api.UserApiConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.UserApiRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserApiConnectorImpl implements UserApiConnector {

    private final UserApiRestClient userApiRestClient;

    public UserApiConnectorImpl(UserApiRestClient userApiRestClient) {
        this.userApiRestClient = userApiRestClient;
    }

    @Override
    public List<String> getUserEmails(String institutionId, String productId){
        ResponseEntity<List<String>> userEmails = userApiRestClient._usersEmailsGet(institutionId, productId);
        return userEmails.getBody();
    }

}
