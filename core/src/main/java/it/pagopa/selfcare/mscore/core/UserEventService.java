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
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserEventService {
    private final CoreConfig coreConfig;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaPropertiesConfig kafkaPropertiesConfig;
    private final ObjectMapper mapper;
    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

    public UserEventService(CoreConfig coreConfig,
                            KafkaTemplate<String, String> kafkaTemplate,
                            KafkaPropertiesConfig kafkaPropertiesConfig,
                            ObjectMapper mapper,
                            UserConnector userConnector, UserRegistryConnector userRegistryConnector) {
        this.coreConfig = coreConfig;
        this.kafkaTemplate = kafkaTemplate;
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

    public void sendLegalUserNotification(Token token) {
        token.getUsers().forEach(tokenUser -> {
            UserToNotify userToNotify = toUserToNotify(tokenUser, token.getInstitutionId(), token.getProductId());
            try {
                String msg = mapper.writeValueAsString(userToNotify);
                sendUserNotification(msg, userToNotify.getUserId());
            } catch (JsonProcessingException e) {
                log.warn("error during send dataLake notification for user {}", userToNotify.getUserId());
            }

        });
    }

    private UserToNotify toUserToNotify(TokenUser tokenUser, String institutionId, String productId) {
        UserToNotify userToNotify = new UserToNotify();
        User user = userRegistryConnector.getUserByInternalId(tokenUser.getUserId(), EnumSet.allOf(User.Fields.class));
        OnboardedUser onboardedUser = userConnector.findById(tokenUser.getUserId());
        List<String> userProductRoles = onboardedUser.getBindings().stream()
                .filter(userBinding -> institutionId.equals(userBinding.getInstitutionId()))
                .flatMap(userBinding -> userBinding.getProducts().stream())
                .filter(onboardedProduct -> productId.equals(onboardedProduct.getProductId()))
                .map(OnboardedProduct::getProductRole)
                .collect(Collectors.toList());
        userToNotify.setUserId(tokenUser.getUserId());
        userToNotify.setName(user.getName());
        userToNotify.setFamilyName(user.getFamilyName());
        userToNotify.setFiscalCode(user.getFiscalCode());
        userToNotify.setEmail(user.getWorkContacts().get(institutionId).getEmail());
        userToNotify.setRole(tokenUser.getRole());
        userToNotify.setProductRoles(userProductRoles);

        return userToNotify;
    }

    public void sendCreateUserNotification(RelationshipInfo relationshipInfo) {
        if (relationshipInfo != null) {
            log.debug(LogUtils.CONFIDENTIAL_MARKER, "Notification to send to the data lake, notification: {}", relationshipInfo);
            try {
                String msg = mapper.writeValueAsString(relationshipInfo);
                sendUserNotification(msg, relationshipInfo.getUserId());
            } catch (JsonProcessingException e) {
                log.warn("error during send dataLake notification for user {}", relationshipInfo.getUserId());
            }
        }
    }


    private void sendUserNotification(String message, String userId) {
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(kafkaPropertiesConfig.getScUsersTopic(), message);

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
