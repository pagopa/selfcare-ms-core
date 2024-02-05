package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.InstitutionToNotify;
import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.RootParent;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ContractEventNotificationServiceImpl implements ContractEventNotificationService {


    static final String DESCRIPTION_TO_REPLACE_REGEX = " - COMUNE";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaPropertiesConfig kafkaPropertiesConfig;
    private final ObjectMapper mapper;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final InstitutionConnector institutionConnector;
    private final CoreConfig coreConfig;

    public ContractEventNotificationServiceImpl(KafkaTemplate<String, String> kafkaTemplate, KafkaPropertiesConfig kafkaPropertiesConfig, PartyRegistryProxyConnector partyRegistryProxyConnector, InstitutionConnector institutionConnector, CoreConfig coreConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaPropertiesConfig = kafkaPropertiesConfig;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionConnector = institutionConnector;
        this.coreConfig = coreConfig;


        this.mapper = new ObjectMapper();
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
    public void sendDataLakeNotification(Institution institution, Token token, QueueEvent queueEvent) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "sendDataLakeNotification institution = {}, token = {}, queueEvent = {}", institution, token, queueEvent);
        if (institution != null) {
            NotificationToSend notification = toNotificationToSend(institution, token, queueEvent);
            log.debug(LogUtils.CONFIDENTIAL_MARKER, "Notification to send to the data lake, notification: {}", notification);
            try {
                String msg = mapper.writeValueAsString(notification);
                sendNotification(msg, token.getId());
            } catch (JsonProcessingException e) {
                log.warn("error during send dataLake notification for token {}", notification.getId());
            }
        }
    }


    @Override
    public NotificationToSend toNotificationToSend(Institution institution, Token token, QueueEvent queueEvent) {
        NotificationToSend notification = new NotificationToSend();
        if (queueEvent.equals(QueueEvent.ADD)) {
            // When Onboarding.complete event id is the onboarding id
            notification.setId(token.getId());
            notification.setState(RelationshipState.ACTIVE.toString());
            // when onboarding complete last update is activated date
            notification.setUpdatedAt(Optional.ofNullable(token.getActivatedAt()).orElse(token.getCreatedAt()));
        } else {
            // New id
            notification.setId(UUID.randomUUID().toString());
            notification.setState(token.getStatus() == RelationshipState.DELETED ? "CLOSED" : token.getStatus().toString());
            // when update last update is updated date
            notification.setUpdatedAt(Optional.ofNullable(token.getUpdatedAt()).orElse(token.getCreatedAt()));
            if (token.getStatus().equals(RelationshipState.DELETED)) {
                // Queue.ClosedAt: if token.deleted show closedAt
                notification.setClosedAt(Optional.ofNullable(token.getDeletedAt()).orElse(token.getUpdatedAt()));
                notification.setUpdatedAt(Optional.ofNullable(token.getDeletedAt()).orElse(token.getUpdatedAt()));
            } else {
                // when update last update is updated date
                notification.setUpdatedAt(Optional.ofNullable(token.getUpdatedAt()).orElse(token.getCreatedAt()));
            }
        }
        // ADD or UPDATE msg event
        notification.setNotificationType(queueEvent);
        return toNotificationToSend(notification, institution, token);
    }

    @Override
    public NotificationToSend toNotificationToSend(NotificationToSend notification, Institution institution, Token token) {
        notification.setInternalIstitutionID(institution.getId());
        notification.setProduct(token.getProductId());
        notification.setFilePath(token.getContractSigned());
        notification.setOnboardingTokenId(token.getId());
        // Queue.CreatedAt: onboarding complete date
        notification.setCreatedAt(Optional.ofNullable(token.getActivatedAt()).orElse(token.getCreatedAt()));

        // ADD or UPDATE msg event
        notification.setFileName(token.getContractSigned() == null ? "" : Paths.get(token.getContractSigned()).getFileName().toString());
        notification.setContentType(token.getContentType() == null ? "" : token.getContentType());

        if (token.getProductId() != null && institution.getOnboarding() != null) {
            Onboarding onboarding = institution.getOnboarding().stream()
                    .filter(o -> token.getProductId().equalsIgnoreCase(o.getProductId()))
                    .findFirst().orElseThrow(() -> new InvalidRequestException(String.format("Product %s not found", token.getProductId()), "0000"));
            notification.setPricingPlan(onboarding.getPricingPlan());
            notification.setBilling(onboarding.getBilling() != null ? onboarding.getBilling() : institution.getBilling());
            notification.setInstitution(toInstitutionToNotify(institution));
        }

        return notification;
    }

    private void sendNotification(String message, String tokenId) {
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(kafkaPropertiesConfig.getDatalakeContractsTopic(), message);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("sent dataLake notification for token : {}", tokenId);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("error during send dataLake notification for token {}: {} ", tokenId, ex.getMessage(), ex);
            }
        });

    }



    private InstitutionToNotify toInstitutionToNotify(Institution institution) {
        InstitutionToNotify toNotify = new InstitutionToNotify();
        toNotify.setInstitutionType(institution.getInstitutionType());
        toNotify.setDescription(institution.getDescription());
        toNotify.setDigitalAddress(institution.getDigitalAddress() == null? coreConfig.getInstitutionAlternativeEmail(): institution.getDigitalAddress());
        toNotify.setAddress(institution.getAddress());
        toNotify.setTaxCode(institution.getTaxCode());
        toNotify.setOrigin(institution.getOrigin());
        toNotify.setOriginId(institution.getOriginId());
        toNotify.setZipCode(institution.getZipCode());
        toNotify.setPaymentServiceProvider(institution.getPaymentServiceProvider());
        if (institution.getSubunitType() != null && !institution.getSubunitType().equals("EC")) {
            try {
                InstitutionPaSubunitType.valueOf(institution.getSubunitType());
                toNotify.setSubUnitType(institution.getSubunitType());
                toNotify.setSubUnitCode(institution.getSubunitCode());
            } catch (IllegalArgumentException ignored) {
            }
        }
        RootParent rootParent = new RootParent();
        rootParent.setDescription(institution.getParentDescription());
        if (StringUtils.hasText(institution.getRootParentId())) {
            rootParent.setId(institution.getRootParentId());
            Institution rootParentInstitution = institutionConnector.findById(institution.getRootParentId());
            rootParent.setOriginId(Objects.nonNull(rootParentInstitution) ? rootParentInstitution.getOriginId() : null);
            toNotify.setRootParent(rootParent);
        }

        if (institution.getAttributes() != null && institution.getAttributes().size() > 0) {
            toNotify.setCategory(institution.getAttributes().get(0).getCode());
        }
        if (institution.getCity() == null) {
            setInstitutionLocation(toNotify, institution);
        } else {
            toNotify.setCounty(institution.getCounty());
            toNotify.setCountry(institution.getCountry());
            toNotify.setIstatCode(institution.getIstatCode());
            toNotify.setCity(institution.getCity().replace(DESCRIPTION_TO_REPLACE_REGEX, ""));
        }
        return toNotify;
    }



    private void setInstitutionLocation(InstitutionToNotify toNotify, Institution institution) {
        try {
            InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(institution.getExternalId());
            toNotify.setIstatCode(institutionProxyInfo.getIstatCode());
            toNotify.setCategory(institutionProxyInfo.getCategory());
            GeographicTaxonomies geographicTaxonomies = partyRegistryProxyConnector.getExtByCode(toNotify.getIstatCode());
            toNotify.setCounty(geographicTaxonomies.getProvinceAbbreviation());
            toNotify.setCountry(geographicTaxonomies.getCountryAbbreviation());
            toNotify.setCity(geographicTaxonomies.getDescription().replace(DESCRIPTION_TO_REPLACE_REGEX, ""));
        } catch (MsCoreException | ResourceNotFoundException e) {
            log.warn("Error while searching institution {} on IPA, {} ", institution.getExternalId(), e.getMessage());
            toNotify.setIstatCode(null);
        }
    }
}
