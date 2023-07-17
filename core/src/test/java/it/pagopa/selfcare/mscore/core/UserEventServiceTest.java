package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.core.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.core.util.model.DummyUser;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEventServiceTest {
    @Mock
    private KafkaTemplate<String, String> kafkaTemplateUsers;
    @InjectMocks
    private UserEventService userEventService;
    @Mock
    private KafkaPropertiesConfig kafkaPropertiesConfig;
    @Spy
    private ObjectMapper mapper;
    @Mock
    private UserConnector userConnector;
    @Mock
    private UserRegistryConnector userRegistryConnector;
    @Mock
    private CoreConfig coreConfig;


    @Test
    void sendLegalTokenUserNotification(){

    }

    @Test
    void toUserToNotifyFromToken(){
        //given
        Optional<String> tokenId = Optional.of(UUID.randomUUID().toString());
        Optional<String> relationshipId = Optional.empty();
        String userId = UUID.randomUUID().toString();
        String institutionId = UUID.randomUUID().toString();
        String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setTokenId(tokenId.get());
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any(), any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);


        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(1, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId, EnumSet.allOf(User.Fields.class));
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }

    @Test
    void userToNotifyFromRelationship(){
        //given
        Optional<String> tokenId = Optional.empty();
        Optional<String> relationshipId = Optional.of(UUID.randomUUID().toString());
        String userId = UUID.randomUUID().toString();
        String institutionId = UUID.randomUUID().toString();
        String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId.get());
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any(), any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(1, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId, EnumSet.allOf(User.Fields.class));
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }

    @Test
    void toUserToNotifyNoProduct(){
        //given
        Optional<String> tokenId = Optional.empty();
        Optional<String> relationshipId = Optional.of(UUID.randomUUID().toString());
        String userId = UUID.randomUUID().toString();
        String institutionId = UUID.randomUUID().toString();
        String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId.get());
        onboardedProduct.setProductId("prod-diff");
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any(), any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(0, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId, EnumSet.allOf(User.Fields.class));
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }

    @Test
    void toUserToNotifyDifferentInstitution(){
        //given
        Optional<String> tokenId = Optional.empty();
        Optional<String> relationshipId = Optional.of(UUID.randomUUID().toString());
        String userId = UUID.randomUUID().toString();
        String institutionId = UUID.randomUUID().toString();
        String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId.get());
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(UUID.randomUUID().toString(), List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any(), any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(0, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId, EnumSet.allOf(User.Fields.class));
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }
}