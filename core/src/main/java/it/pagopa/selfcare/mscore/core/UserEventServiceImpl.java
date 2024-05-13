package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
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

@Slf4j
@Service
@ConditionalOnProperty(
        value = "core.user-event-service.type",
        havingValue = "send")
public class UserEventServiceImpl implements UserEventService {
    public static final String ERROR_DURING_SEND_DATA_LAKE_NOTIFICATION_FOR_USER = "error during send dataLake notification for user {}";
    public static final String DONE_SEND_DATA_LAKE_NOTIFICATION_FOR_USER = "done send dataLake notification for user {}";
    private final CoreConfig coreConfig;
    private final KafkaTemplate<String, String> kafkaTemplateUsers;
    private final EnumSet<RelationshipState> ALLOWED_RELATIONSHIP_STATUSES = EnumSet.of(RelationshipState.ACTIVE, RelationshipState.SUSPENDED, RelationshipState.DELETED);
    private final KafkaPropertiesConfig kafkaPropertiesConfig;
    private final ObjectMapper mapper;
    private final UserRegistryConnector userRegistryConnector;

    private final NotificationMapper notificationMapper;

    private final UserNotificationMapper userNotificationMapper;

    public UserEventServiceImpl(CoreConfig coreConfig,
                                KafkaTemplate<String, String> kafkaTemplateUsers,
                                KafkaPropertiesConfig kafkaPropertiesConfig,
                                ObjectMapper mapper,
                                UserRegistryConnector userRegistryConnector,
                                NotificationMapper notificationMapper,
                                UserNotificationMapper userNotificationMapper) {
        this.coreConfig = coreConfig;
        this.kafkaTemplateUsers = kafkaTemplateUsers;
        this.kafkaPropertiesConfig = kafkaPropertiesConfig;
        this.mapper = mapper;
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

    @Override
    public void sendOnboardedUserNotification(OnboardedUser onboardedUser, String productId) {
        try {
            onboardedUser.getBindings().forEach(userBinding -> {
                for (OnboardedProduct onboardedProduct : userBinding.getProducts()) {
                    if (productId.equals(onboardedProduct.getProductId()) && ALLOWED_RELATIONSHIP_STATUSES.contains(onboardedProduct.getStatus())) {
                        sendUserNotificationFromBindings(onboardedUser.getId(), QueueEvent.ADD, userBinding, onboardedProduct);
                    }
                }
            });
        }catch (ResourceNotFoundException e){
            log.error("Error while operating on user: {}", onboardedUser.getId());
        }

    }

    private void sendUserNotificationFromBindings(String userId, QueueEvent eventType, UserBinding userBinding, OnboardedProduct onboardedProduct) {
        User user = userRegistryConnector.getUserByInternalId(userId);
        UserToNotify userToNotify = userNotificationMapper.toUserNotify(user, onboardedProduct, userBinding.getInstitutionId());
        UserNotificationToSend userNotification = notificationMapper.setNotificationDetailsFromOnboardedProduct(userToNotify, onboardedProduct, userBinding.getInstitutionId());
        userNotification.setId(idBuilder(userId, userBinding.getInstitutionId(), onboardedProduct.getProductId(), onboardedProduct.getProductRole()));
        userNotification.setEventType(eventType);
        try {
            String msg = mapper.writeValueAsString(userNotification);
            sendUserNotification(msg, userId);
            log.info(DONE_SEND_DATA_LAKE_NOTIFICATION_FOR_USER, userId);
        } catch (JsonProcessingException e) {
            log.warn(ERROR_DURING_SEND_DATA_LAKE_NOTIFICATION_FOR_USER, userId);
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
