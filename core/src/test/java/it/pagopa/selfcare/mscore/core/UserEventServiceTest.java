package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.core.util.NotificationMapper;
import it.pagopa.selfcare.mscore.core.util.NotificationMapperImpl;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapper;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapperImpl;
import it.pagopa.selfcare.mscore.core.util.model.DummyUser;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static it.pagopa.selfcare.commons.utils.TestUtils.checkNotNullFields;
import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEventServiceTest {
    @Mock
    private KafkaTemplate<String, String> kafkaTemplateUsers;
    @InjectMocks
    private UserEventServiceImpl userEventService;
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
    @Mock
    private SendResult<String, String> mockSendResult;
    @Mock
    private ListenableFuture<SendResult<String, String>> mockFuture;

    @Spy
    private NotificationMapper notificationMapper = new NotificationMapperImpl();
    @Spy
    UserNotificationMapper userNotificationMapper = new UserNotificationMapperImpl();

    @Test
    void sendLegalTokenUserNotification_ok() {
        //given
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "productId";
        final String tokenId = UUID.randomUUID().toString();
        final String userId = UUID.randomUUID().toString();
        Token token = mockInstance(new Token());
        token.setId(tokenId);
        token.setProductId(productId);
        token.setInstitutionId(institutionId);
        TokenUser tokenUser = mockInstance(new TokenUser());
        tokenUser.setUserId(userId);
        token.setUsers(List.of(tokenUser));

        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setTokenId(tokenId);
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);

        when(kafkaTemplateUsers.send(any(), any()))
                .thenReturn(mockFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback callback = invocationOnMock.getArgument(0);
            callback.onSuccess(mockSendResult);
            return null;
        }).when(mockFuture).addCallback(any(ListenableFutureCallback.class));

        //when
        Executable executable = () -> userEventService.sendLegalTokenUserNotification(token);
        //then
        assertDoesNotThrow(executable);
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verify(kafkaTemplateUsers, times(1)).send(any(), any());
        verifyNoMoreInteractions(userConnector, userRegistryConnector, kafkaTemplateUsers);

    }

    @Test
    void sendLegalTokenUsersNotification_onFailure(){
        //given
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";
        final String tokenId = UUID.randomUUID().toString();
        final String userId = UUID.randomUUID().toString();

        Token token = mockInstance(new Token());
        token.setId(tokenId);
        token.setProductId(productId);
        token.setInstitutionId(institutionId);
        TokenUser tokenUser = mockInstance(new TokenUser());
        tokenUser.setUserId(userId);
        token.setUsers(List.of(tokenUser));

        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setTokenId(tokenId);
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser("institutionId");
        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);

        RuntimeException ex = new RuntimeException("error");
        when(kafkaTemplateUsers.send(any(), any()))
                .thenReturn(mockFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback callback = invocationOnMock.getArgument(0);
            callback.onFailure(ex);
            return null;
        }).when(mockFuture).addCallback(any(ListenableFutureCallback.class));

        //when
        Executable executable = () -> userEventService.sendLegalTokenUserNotification(token);
        //then
        assertDoesNotThrow(executable);
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verify(kafkaTemplateUsers, times(1)).send(any(), any());
        verifyNoMoreInteractions(userConnector, userRegistryConnector, kafkaTemplateUsers);
    }

    @Test
    void sendOperatorUserNotification(){
        //given
        final String relationshipId = UUID.randomUUID().toString();
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";

        RelationshipInfo relationshipInfo = mockInstance(new RelationshipInfo());
        relationshipInfo.setUserId(userId);
        Institution institution = mockInstance(new Institution());
        institution.setId(institutionId);
        relationshipInfo.setInstitution(institution);
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId);
        onboardedProduct.setProductId(productId);
        relationshipInfo.setOnboardedProduct(onboardedProduct);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        userBinding.setInstitutionId(institutionId);
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        when(kafkaTemplateUsers.send(any(), any()))
                .thenReturn(mockFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback callback = invocationOnMock.getArgument(0);
            callback.onSuccess(mockSendResult);
            return null;
        }).when(mockFuture).addCallback(any(ListenableFutureCallback.class));
        //when
        Executable executable = () -> userEventService.sendOperatorUserNotification(relationshipInfo, QueueEvent.ADD);
        //then
        assertDoesNotThrow(executable);
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verify(kafkaTemplateUsers, times(1)).send(any(), any());
        verifyNoMoreInteractions(userConnector, userRegistryConnector, kafkaTemplateUsers);

    }

    @Test
    void sendOperatorJsonError() throws JsonProcessingException {
        //given
        final String relationshipId = UUID.randomUUID().toString();
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";

        RelationshipInfo relationshipInfo = mockInstance(new RelationshipInfo());
        relationshipInfo.setUserId(userId);
        Institution institution = mockInstance(new Institution());
        institution.setId(institutionId);
        relationshipInfo.setInstitution(institution);
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId);
        onboardedProduct.setProductId(productId);
        relationshipInfo.setOnboardedProduct(onboardedProduct);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        userBinding.setInstitutionId(institutionId);
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);

        when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        //when
        Executable executable = () -> userEventService.sendOperatorUserNotification(relationshipInfo, QueueEvent.ADD);
        //then
        assertDoesNotThrow(executable);
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }
    @Test
    void toUserToNotifyFromToken() {
        //given
        final Optional<String> tokenId = Optional.of(UUID.randomUUID().toString());
        final Optional<String> relationshipId = Optional.empty();
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setTokenId(tokenId.get());
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser(institutionId);


        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);


        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(1, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }

    @Test
    void userToNotifyFromRelationship() {
        //given
        final Optional<String> tokenId = Optional.empty();
        final Optional<String> relationshipId = Optional.of(UUID.randomUUID().toString());
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId.get());
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(1, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }

    @Test
    void toUserToNotifyNoProduct() {
        //given
        final Optional<String> tokenId = Optional.empty();
        final Optional<String> relationshipId = Optional.of(UUID.randomUUID().toString());
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId.get());
        onboardedProduct.setProductId("prod-diff");
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(institutionId, List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(0, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }

    @Test
    void toUserToNotifyDifferentInstitution() {
        //given
        final Optional<String> tokenId = Optional.empty();
        final Optional<String> relationshipId = Optional.of(UUID.randomUUID().toString());
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";
        OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setRelationshipId(relationshipId.get());
        onboardedProduct.setProductId(productId);
        OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        UserBinding userBinding = new UserBinding(UUID.randomUUID().toString(), List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        final User userMock = new DummyUser(institutionId);
        when(userRegistryConnector.getUserByInternalId(any()))
                .thenReturn(userMock);
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        //when
        List<UserToNotify> usersToNotify = userEventService.toUserToNotify(userId, institutionId, productId, relationshipId, tokenId);
        //then
        assertEquals(0, usersToNotify.size());
        verify(userConnector, times(1)).findById(userId);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verifyNoMoreInteractions(userConnector, userRegistryConnector);
    }

    @Test
    void updateUserEventNotification() throws JsonProcessingException {
        //given
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "prod-test";
        when(kafkaTemplateUsers.send(any(), any()))
                .thenReturn(mockFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback callback = invocationOnMock.getArgument(0);
            callback.onSuccess(mockSendResult);
            return null;
        }).when(mockFuture).addCallback(any(ListenableFutureCallback.class));
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        final OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setProductId(productId);
        onboardedProduct.setStatus(RelationshipState.ACTIVE);
        final UserBinding userBinding = mockInstance(new UserBinding());
        final User user = new DummyUser(institutionId);
        user.setId(userId);
        userBinding.setInstitutionId(institutionId);
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        when(userRegistryConnector.getUserByInternalId(any())).thenReturn(user);
        when(userConnector.findById(userId)).thenReturn(onboardedUser);
        //when
        Executable executable = () -> userEventService.sendUserNotificationToQueue(userId, institutionId, QueueEvent.UPDATE);
        //then
        assertDoesNotThrow(executable);
        ArgumentCaptor<UserNotificationToSend> messageArgumentCaptor = ArgumentCaptor.forClass(UserNotificationToSend.class);
        verify(mapper, times(1)).writeValueAsString(messageArgumentCaptor.capture());
        UserNotificationToSend message = messageArgumentCaptor.getValue();
        checkNotNullFields(message,   "user.fiscalCode");
    }

    @ParameterizedTest
    @EnumSource(value = RelationshipState.class, names = {"ACTIVE", "DELETED", "SUSPENDED"})
    void sendOnboardedUserNotification(RelationshipState state){
        //given
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "product-test";
        when(kafkaTemplateUsers.send(any(), any()))
                .thenReturn(mockFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback callback = invocationOnMock.getArgument(0);
            callback.onSuccess(mockSendResult);
            return null;
        }).when(mockFuture).addCallback(any(ListenableFutureCallback.class));
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        final OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setProductId(productId);
        onboardedProduct.setStatus(state);
        final UserBinding userBinding = mockInstance(new UserBinding());
        final User user = new DummyUser(institutionId);
        user.setId(userId);
        userBinding.setInstitutionId(institutionId);
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        when(userRegistryConnector.getUserByInternalId(any())).thenReturn(user);
        //when
        Executable executable = () ->userEventService.sendOnboardedUserNotification(onboardedUser, productId);
        //then
        assertDoesNotThrow(executable);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
    }

    @Test
    void sendOnboardedUserNotification1(){
        //given
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "product-test";
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        final OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setProductId(productId);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        final UserBinding userBinding = mockInstance(new UserBinding());
        final User user = new DummyUser(institutionId);
        user.setId(userId);
        userBinding.setInstitutionId(institutionId);
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        when(userRegistryConnector.getUserByInternalId(any())).thenReturn(user);
        //when
        Executable executable = () ->userEventService.sendOnboardedUserNotification(onboardedUser, productId);
        //then
        assertDoesNotThrow(executable);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verifyNoInteractions(kafkaTemplateUsers);
    }
    @Test
    void sendOnboardedUserNotification2(){
        //given
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "product-test";
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        final OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setProductId("product");
        onboardedProduct.setStatus(RelationshipState.ACTIVE);
        final UserBinding userBinding = mockInstance(new UserBinding());
        final User user = new DummyUser(institutionId);
        user.setId(userId);
        userBinding.setInstitutionId(institutionId);
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        when(userRegistryConnector.getUserByInternalId(any())).thenReturn(user);
        //when
        Executable executable = () ->userEventService.sendOnboardedUserNotification(onboardedUser, productId);
        //then
        assertDoesNotThrow(executable);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
        verifyNoInteractions(kafkaTemplateUsers);

    }

    @Test
    void sendOnboardedUserNotifications_userNotFound(){
        //given
        final String userId = UUID.randomUUID().toString();
        final String institutionId = UUID.randomUUID().toString();
        final String productId = "product-test";
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        onboardedUser.setId(userId);
        final OnboardedProduct onboardedProduct = mockInstance(new OnboardedProduct());
        onboardedProduct.setProductId(productId);
        final UserBinding userBinding = mockInstance(new UserBinding());
        final User user = new DummyUser(institutionId);
        user.setId(userId);
        userBinding.setInstitutionId(institutionId);
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        when(userRegistryConnector.getUserByInternalId(any())).thenThrow(ResourceNotFoundException.class);
        //when
        Executable executable = () ->userEventService.sendOnboardedUserNotification(onboardedUser, productId);
        //then
        assertDoesNotThrow(executable);
        verify(userRegistryConnector, times(1)).getUserByInternalId(userId);
    }

}