package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.client.UserRegistryRestClient;
import it.pagopa.selfcare.mscore.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class UserRegistryConnectorImplTest {
    @InjectMocks
    private UserRegistryConnectorImpl userRegistryConnectorImpl;

    /**
     * Method under test: {@link UserRegistryConnectorImpl#getUserByInternalId(String, EnumSet)}
     */
    @Test
    void testGetUserByInternalId() {
        User actualUserByInternalId = userRegistryConnectorImpl.getUserByInternalId("42", null);
        assertEquals("name", actualUserByInternalId.getName().getValue());
        assertEquals("flaminia.scarciofolo@nttdata.com", actualUserByInternalId.getEmail().getValue());
        assertEquals("surname", actualUserByInternalId.getFamilyName().getValue());
    }
}

