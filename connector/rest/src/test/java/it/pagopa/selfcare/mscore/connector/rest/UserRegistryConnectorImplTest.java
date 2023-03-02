package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.client.UserRegistryRestClient;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.EnumSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserRegistryConnectorImplTest {
    @InjectMocks
    private UserRegistryConnectorImpl userRegistryConnectorImpl;

    @Mock
    private UserRegistryRestClient restClient;

    /**
     * Method under test: {@link UserRegistryConnectorImpl#getUserByInternalId(String, EnumSet)}
     */
    @Test
    void testGetUserByInternalId() {
        when(restClient.getUserByInternalId(any(),any())).thenReturn(new User());
        assertNotNull(userRegistryConnectorImpl.getUserByInternalId(UUID.randomUUID().toString(), EnumSet.allOf(User.Fields.class)));
    }
}

