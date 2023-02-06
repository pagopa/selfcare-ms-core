package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.UserRegistryConnectorImpl;
import it.pagopa.selfcare.mscore.connector.rest.client.UserRegistryRestClient;

import java.util.EnumSet;
import java.util.UUID;

import it.pagopa.selfcare.mscore.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;

@ContextConfiguration(classes = {UserRegistryConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class UserRegistryConnectorImplTest {
    @Autowired
    private UserRegistryConnectorImpl userRegistryConnectorImpl;

    @MockBean
    private UserRegistryRestClient userRegistryRestClient;

    /**
     * Method under test: {@link UserRegistryConnectorImpl#getUserByInternalId(String, EnumSet)}
     */
    @Test
    void testGetUserByInternalId() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid.toString());
        Mockito.when(userRegistryRestClient.getUserByInternalId(any(),any()))
                        .thenReturn(user);
        EnumSet<User.Fields> enumSet = EnumSet.allOf(User.Fields.class);
        userRegistryConnectorImpl.getUserByInternalId(uuid.toString(),enumSet);
    }
}

