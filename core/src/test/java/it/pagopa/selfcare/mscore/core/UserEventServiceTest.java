package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.UUID;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    void sendOnboardedUserNotification1() throws JsonProcessingException {
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
        //when
        Executable executable = () ->userEventService.sendOnboardedUserNotification(onboardedUser, productId);
        //then
        assertDoesNotThrow(executable);
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
        //when
        Executable executable = () ->userEventService.sendOnboardedUserNotification(onboardedUser, productId);
        //then
        assertDoesNotThrow(executable);
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