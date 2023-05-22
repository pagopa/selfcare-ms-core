package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.SchedulerConfig;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
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

    private final SchedulerConfig schedulerConfig;

    @Autowired
    public SchedulerService(ContractService contractService,
                            SchedulerConfig schedulerConfig,
                            TokenConnector tokenConnector,
                            InstitutionConnector institutionConnector) {
        log.info("Initializing {}...", SchedulerService.class.getSimpleName());
        this.contractService = contractService;
        this.schedulerConfig = schedulerConfig;
        this.tokenConnector = tokenConnector;
        this.institutionConnector = institutionConnector;
    }


    @Scheduled(fixedDelayString = "${scheduler.fixed-delay.delay}")
    public void regenerateQueueNotifications() {
        log.trace("regenerateQueueNotifications start");

        //System.out.println("Running the scheduler + " + schedulerConfig.getFixedDelay());
        System.out.println("SCHEDULER RUNNING...");
        System.out.println("ENVIRONMENT VARIABLE: " + System.getenv("SCHEDULER"));

        if (System.getenv("SCHEDULER") != null && System.getenv("SCHEDULER").equals("true")) {
            log.info("Regenerating notification on queue...");
            Boolean nextPage = true;
            Integer page = 0;
            do {
                List<Token> tokens = tokenConnector.findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), System.getenv("SCHEDULER_REGENERATE_QUEUE_NOTIFICATION_PRODUCT_FILTER"), page);

                System.out.println("TOKEN SIZE: " + tokens.size());

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

                page += 1;

                if (tokens.size() < 100)
                    nextPage = false;

            } while (nextPage);
        }

        log.info("Next scheduled check at {}", getNextScheduledCheck());
        log.trace("regenerateQueueNotifications end");
    }

    private OffsetDateTime getNextScheduledCheck() {
        Long seconds = schedulerConfig.getFixedDelay() / 1000;

        if (seconds < 60) {
            return OffsetDateTime.now().plusSeconds(seconds);
        }

        Long minutes = seconds / 60;

        if (minutes < 60) {
            return OffsetDateTime.now().plusMinutes(minutes);
        }

        Long hours = minutes / 60;
        return OffsetDateTime.now().plusHours(hours);
    }

}
