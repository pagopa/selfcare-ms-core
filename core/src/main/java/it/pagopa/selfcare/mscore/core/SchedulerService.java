package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.ConfigConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.SchedulerConfig;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Config;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;

@Service
@Slf4j
public class SchedulerService {

    private final ContractService contractService;

    private final TokenConnector tokenConnector;
    private final InstitutionConnector institutionConnector;
    private final ConfigConnector configConnector;

    private final SchedulerConfig schedulerConfig;

    @Autowired
    public SchedulerService(ContractService contractService,
                            SchedulerConfig schedulerConfig,
                            TokenConnector tokenConnector,
                            InstitutionConnector institutionConnector,
                            ConfigConnector configConnector) {
        log.info("Initializing {}...", SchedulerService.class.getSimpleName());
        this.contractService = contractService;
        this.schedulerConfig = schedulerConfig;
        this.tokenConnector = tokenConnector;
        this.institutionConnector = institutionConnector;
        this.configConnector = configConnector;
    }


    @Scheduled(fixedDelayString = "${scheduler.fixed-delay.delay}")
    public void regenerateQueueNotifications() {
        log.trace("regenerateQueueNotifications start");

        Config regenerateQueueConfiguration = null;
        try {
            regenerateQueueConfiguration = configConnector.findById(schedulerConfig.getKafkaRegenerateConfigName());
        } catch (ResourceNotFoundException exception) {
            log.error("Error while retrieving kafka queue regeneration configuration, {}", exception.getMessage());
        }

        //To regenerate all the message on the kafka queue, you need to modify the Config entity, set value of the "enableKafkaScheduler" field to true, directly on mongoDB and if needs be you can also modify the value of "productFilter"
        if (regenerateQueueConfiguration != null && regenerateQueueConfiguration.isEnableKafkaScheduler()) {
            log.debug("Regenerating notification on queue with product filter {}", retrieveProductFilter(regenerateQueueConfiguration.getProductFilter()));

            configConnector.resetConfiguration(schedulerConfig.getKafkaRegenerateConfigName());

            boolean nextPage = true;
            int page = 0;
            do {
                List<Token> tokens = tokenConnector.findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), regenerateQueueConfiguration.getProductFilter(), page);

                sendDataLakeNotifications(tokens);

                page += 1;
                if (tokens.size() < 100)
                    nextPage = false;

            } while (nextPage);
        }

        log.info("Next scheduled check at {}", OffsetDateTime.now().plusSeconds(schedulerConfig.getFixedDelay() / 1000));
        log.trace("regenerateQueueNotifications end");
    }

    private String retrieveProductFilter(String productFilter) {
        return productFilter == null || productFilter.isBlank() ? "null" : productFilter;
    }

    private void sendDataLakeNotifications(List<Token> tokens) {
        tokens.forEach(token -> {
            try {
                Institution institution = institutionConnector.findById(token.getInstitutionId());
                contractService.sendDataLakeNotification(institution, token, QueueEvent.ADD);
                if (!token.getStatus().equals(RelationshipState.ACTIVE)) {
                    contractService.sendDataLakeNotification(institution, token, QueueEvent.UPDATE);
                }
            } catch (ResourceNotFoundException exception) {
                log.error("Error while fetching institution for token {}, {}", token.getId(), exception.getMessage());
            }
        });
    }

}
