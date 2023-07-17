package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.core.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserEventService {
    private final CoreConfig coreConfig;
    private final KafkaTemplate<String, String> kafkaTemplateUsers;
    private final KafkaPropertiesConfig kafkaPropertiesConfig;
    private final ObjectMapper mapper;
    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

    public UserEventService(CoreConfig coreConfig,
                            KafkaTemplate<String, String> kafkaTemplateUsers,
                            KafkaPropertiesConfig kafkaPropertiesConfig,
                            ObjectMapper mapper,
                            UserConnector userConnector, UserRegistryConnector userRegistryConnector) {
        this.coreConfig = coreConfig;
        this.kafkaTemplateUsers = kafkaTemplateUsers;
        this.kafkaPropertiesConfig = kafkaPropertiesConfig;
        this.mapper = mapper;
        this.userConnector = userConnector;
        this.userRegistryConnector = userRegistryConnector;
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
        token.getUsers().forEach(tokenUser -> {
            List<UserToNotify> usersToNotify = toUserToNotify(tokenUser.getUserId(), token.getInstitutionId(), token.getProductId(), Optional.empty(), Optional.of(token.getId()));
            usersToNotify.forEach(user -> {
                UserNotificationToSend notification = setNotificationDetailsFromToken(token, user);
                try {
                    String msg = mapper.writeValueAsString(notification);
                    sendUserNotification(msg, user.getUserId());
                } catch (JsonProcessingException e) {
                    log.warn("error during send dataLake notification for user {}", user.getUserId());
                }
            });

        });
    }

    private List<UserToNotify> toUserToNotify(String userId, String institutionId, String productId, Optional<String> relationshipId, Optional<String> tokenId) {
        User user = userRegistryConnector.getUserByInternalId(userId, EnumSet.allOf(User.Fields.class));
        OnboardedUser onboardedUser = userConnector.findById(userId);
        return onboardedUser.getBindings().stream()
                .filter(userBinding -> institutionId.equals(userBinding.getInstitutionId()))
                .flatMap(userBinding -> userBinding.getProducts().stream())
                .filter(onboardedProduct -> productId.equals(onboardedProduct.getProductId()))
                .filter(onboardedProduct -> relationshipId.map(s -> s.equals(onboardedProduct.getRelationshipId())).orElse(true))
                .filter(onboardedProduct -> tokenId.map(s -> s.equals(onboardedProduct.getTokenId())).orElse(true))
                .map(onboardedProduct -> {
                    UserToNotify userToNotify = new UserToNotify();
                    userToNotify.setUserId(userId);
                    userToNotify.setName(user.getName());
                    userToNotify.setFamilyName(user.getFamilyName());
                    userToNotify.setFiscalCode(user.getFiscalCode());
                    userToNotify.setEmail(user.getWorkContacts().get(institutionId).getEmail());
                    userToNotify.setRole(onboardedProduct.getRole());
                    userToNotify.setRelationshipStatus(onboardedProduct.getStatus());
                    userToNotify.setProductRole(onboardedProduct.getProductRole());
                    return userToNotify;
                })
                .collect(Collectors.toList());
    }

    public void sendOperatorUserNotification(RelationshipInfo relationshipInfo) {
        if (relationshipInfo != null) {
            List<UserToNotify> usersToNotify = toUserToNotify(relationshipInfo.getUserId(),
                    relationshipInfo.getInstitution().getId(),
                    relationshipInfo.getOnboardedProduct().getProductId(), Optional.of(relationshipInfo.getOnboardedProduct().getRelationshipId()), Optional.empty());

            log.debug(LogUtils.CONFIDENTIAL_MARKER, "Notification to send to the data lake, notification: {}", relationshipInfo);
            usersToNotify.forEach(user -> {
                UserNotificationToSend notification = setNotificationDetailsFromRelationship(relationshipInfo, user);
                try {
                    String msg = mapper.writeValueAsString(notification);
                    sendUserNotification(msg, user.getUserId());
                } catch (JsonProcessingException e) {
                    log.warn("error during send dataLake notification for user {}", user.getUserId());
                }
            });
        }
    }

    private UserNotificationToSend setNotificationDetailsFromRelationship(RelationshipInfo relationshipInfo, UserToNotify user) {
        UserNotificationToSend notification = new UserNotificationToSend();
        notification.setId(UUID.randomUUID().toString());
        notification.setInstitutionId(relationshipInfo.getInstitution().getId());
        notification.setProductId(relationshipInfo.getOnboardedProduct().getProductId());
        notification.setCreatedAt(relationshipInfo.getOnboardedProduct().getCreatedAt());
        notification.setUpdatedAt(relationshipInfo.getOnboardedProduct().getUpdatedAt());
        notification.setUser(user);
        return notification;
    }

    private UserNotificationToSend setNotificationDetailsFromToken(Token token, UserToNotify user) {
        UserNotificationToSend notification = new UserNotificationToSend();
        notification.setId(UUID.randomUUID().toString());
        notification.setInstitutionId(token.getInstitutionId());
        notification.setProductId(token.getProductId());
        notification.setCreatedAt(token.getCreatedAt());
        notification.setUpdatedAt(token.getUpdatedAt());
        notification.setOnboardingTokenId(token.getId());
        notification.setUser(user);
        return notification;
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
