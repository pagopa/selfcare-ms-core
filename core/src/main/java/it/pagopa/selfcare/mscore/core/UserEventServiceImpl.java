package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.core.util.NotificationMapper;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapper;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(
        value = "core.user-event-service.type",
        havingValue = "send")
public class UserEventServiceImpl implements UserEventService {
    public static final String ERROR_DURING_SEND_DATA_LAKE_NOTIFICATION_FOR_USER = "error during send dataLake notification for user {}";
    private final CoreConfig coreConfig;
    private final KafkaTemplate<String, String> kafkaTemplateUsers;
    private final EnumSet<RelationshipState> ALLOWED_RELATIONSHIP_STATUSES = EnumSet.of(RelationshipState.ACTIVE, RelationshipState.SUSPENDED, RelationshipState.DELETED);
    private final KafkaPropertiesConfig kafkaPropertiesConfig;
    private final ObjectMapper mapper;
    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

    private final NotificationMapper notificationMapper;

    private final UserNotificationMapper userNotificationMapper;

    public UserEventServiceImpl(CoreConfig coreConfig,
                                KafkaTemplate<String, String> kafkaTemplateUsers,
                                KafkaPropertiesConfig kafkaPropertiesConfig,
                                ObjectMapper mapper,
                                UserConnector userConnector,
                                UserRegistryConnector userRegistryConnector,
                                NotificationMapper notificationMapper,
                                UserNotificationMapper userNotificationMapper) {
        this.coreConfig = coreConfig;
        this.kafkaTemplateUsers = kafkaTemplateUsers;
        this.kafkaPropertiesConfig = kafkaPropertiesConfig;
        this.mapper = mapper;
        this.userConnector = userConnector;
        this.userRegistryConnector = userRegistryConnector;
        this.notificationMapper = notificationMapper;
        this.userNotificationMapper = userNotificationMapper;

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(OffsetDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime));
            }
        });
        mapper.registerModule(simpleModule);
    }

    public void sendLegalTokenUserNotification(Token token) {
        token.getUsers().forEach(tokenUser -> sendUserNotificationToQueue(tokenUser.getUserId(), token.getInstitutionId(), QueueEvent.ADD));
    }

    @Override
    public void sendOnboardedUserNotification(OnboardedUser onboardedUser, String productId) {
        try {
            User user = userRegistryConnector.getUserByInternalId(onboardedUser.getId());
            onboardedUser.getBindings().forEach(userBinding -> {
                for (OnboardedProduct onboardedProduct : userBinding.getProducts()) {
                    if (productId.equals(onboardedProduct.getProductId()) && ALLOWED_RELATIONSHIP_STATUSES.contains(onboardedProduct.getStatus())) {
                        UserNotificationToSend notification = notificationMapper.setNotificationDetailsFromOnboardedProduct(toUserToNotify(user.getId(), userBinding.getInstitutionId(), user, onboardedProduct), onboardedProduct, userBinding.getInstitutionId());
                        String id = idBuilder(user.getId(), userBinding.getInstitutionId(), onboardedProduct.getProductId(), onboardedProduct.getProductRole());
                        notification.setId(id);
                        try {
                            String msg = mapper.writeValueAsString(notification);
                            sendUserNotification(msg, user.getId());
                        } catch (JsonProcessingException e) {
                            log.warn(ERROR_DURING_SEND_DATA_LAKE_NOTIFICATION_FOR_USER, user.getId());
                        }
                    }
                }
            });
        }catch (ResourceNotFoundException e){
            log.error("Error while operating on user: {}", onboardedUser.getId());
        }

    }

    protected List<UserToNotify> toUserToNotify(String userId, String institutionId, String productId, Optional<String> relationshipId, Optional<String> tokenId) {
        User user = userRegistryConnector.getUserByInternalId(userId);
        OnboardedUser onboardedUser = userConnector.findById(userId);
        return onboardedUser.getBindings().stream()
                .filter(userBinding -> institutionId.equals(userBinding.getInstitutionId()))
                .flatMap(userBinding -> userBinding.getProducts().stream())
                .filter(onboardedProduct -> productId.equals(onboardedProduct.getProductId()))
                .filter(onboardedProduct -> relationshipId.map(s -> s.equals(onboardedProduct.getRelationshipId())).orElse(true))
                .filter(onboardedProduct -> tokenId.map(s -> s.equals(onboardedProduct.getTokenId())).orElse(true))
                .map(onboardedProduct -> toUserToNotify(userId, institutionId, user, onboardedProduct))
                .collect(Collectors.toList());
    }

    @Override
    public void sendUserNotificationToQueue(String userId, String institutionId, QueueEvent eventType) {
        log.trace("sendUpdateUserNotification start");
        log.debug("sendUpdateUserNotification userId = {}, institutionId = {}", userId, institutionId);
        OnboardedUser onboardedUser = userConnector.findById(userId);
        onboardedUser.getBindings().stream()
                .filter(userBinding -> userBinding.getInstitutionId().equals(institutionId))
                .forEach(userBinding -> userBinding.getProducts()
                        .forEach(onboardedProduct -> {
                            User user = userRegistryConnector.getUserByInternalId(userId);
                            UserToNotify userToNotify = userNotificationMapper.toUserNotify(user, onboardedProduct, userBinding.getInstitutionId());
                            UserNotificationToSend userNotification = notificationMapper.setNotificationDetailsFromOnboardedProduct(userToNotify, onboardedProduct, userBinding.getInstitutionId());
                            userNotification.setId(idBuilder(userId, institutionId, onboardedProduct.getProductId(), onboardedProduct.getProductRole()));
                            userNotification.setEventType(eventType);
                            try {
                                String msg = mapper.writeValueAsString(userNotification);
                                sendUserNotification(msg, userId);
                            } catch (JsonProcessingException e) {
                                log.warn(ERROR_DURING_SEND_DATA_LAKE_NOTIFICATION_FOR_USER, userId);
                            }
                        }));
    }


    private UserToNotify toUserToNotify(String userId, String institutionId, User user, OnboardedProduct onboardedProduct) {
        UserToNotify userToNotify = new UserToNotify();
        userToNotify.setUserId(userId);
        userToNotify.setName(user.getName());
        userToNotify.setFamilyName(user.getFamilyName());
        userToNotify.setEmail(user.getWorkContacts().containsKey(institutionId) ? user.getWorkContacts().get(institutionId).getEmail() : user.getEmail());
        userToNotify.setRole(onboardedProduct.getRole());
        userToNotify.setRelationshipStatus(onboardedProduct.getStatus());
        userToNotify.setProductRole(onboardedProduct.getProductRole());
        return userToNotify;
    }

    public void sendOperatorUserNotification(RelationshipInfo relationshipInfo, QueueEvent eventType) {
        if (relationshipInfo != null) {
           sendUserNotificationToQueue(relationshipInfo.getUserId(), relationshipInfo.getInstitution().getId(), eventType);
        }
    }

    private String idBuilder(String userId, String institutionId, String productId, String productRole){
        return String.format("%s_%s_%s_%s", userId, institutionId, productId, productRole);
    }
    private void sendUserNotification(String message, String userId) {
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplateUsers.send(kafkaPropertiesConfig.getScUsersTopic(), message);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("sent dataLake notification for user : {}", userId);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("error during send dataLake notification for user {}: {} ", userId, ex.getMessage(), ex);
            }
        });
    }
}
