package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.SchedulerConfig;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService{

    public static final int TOKEN_PAGE_SIZE = 100;
    public static final int USER_PAGE_SIZE = 100;
    private final ContractService contractService;
    private final UserEventService userEventService;
    private Optional<Integer> page_size_api = Optional.empty();
    private Optional<Integer> page = Optional.empty();
    private final TokenConnector tokenConnector;
    private final InstitutionConnector institutionConnector;
    private Optional<List<String>> productsFilter;

    private final UserConnector userConnector;

    private final SchedulerConfig schedulerConfig;

    @Autowired
    public SchedulerServiceImpl(ContractService contractService,
                                UserEventService userEventService, SchedulerConfig schedulerConfig,
                                TokenConnector tokenConnector,
                                InstitutionConnector institutionConnector, UserConnector userConnector) {
        this.userEventService = userEventService;
        this.userConnector = userConnector;
        log.info("Initializing {}...", SchedulerServiceImpl.class.getSimpleName());
        this.contractService = contractService;
        this.schedulerConfig = schedulerConfig;
        this.tokenConnector = tokenConnector;
        this.institutionConnector = institutionConnector;
    }


    @Async
    public void regenerateQueueNotifications() {
        log.trace("regenerateQueueNotifications start");
            if (schedulerConfig.getSendOldEvent() && productsFilter.isPresent()) {
                for (String productId: productsFilter.get()) {
                    log.debug("Regenerating notifications on queue with product filter {}", productId);

                    boolean nextPage = true;
                    int page = 0;
                    do {
                        List<Token> tokens = tokenConnector.findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED), productId, page, page_size_api.orElse(TOKEN_PAGE_SIZE));
                        log.debug("[KAFKA] TOKEN NUMBER {} PAGE {}", tokens.size(), page);

                        sendDataLakeNotifications(tokens);

                        page += 1;
                        if (tokens.size() < TOKEN_PAGE_SIZE) {
                            nextPage = false;
                            log.debug("[KAFKA] TOKEN TOTAL NUMBER {}", page * TOKEN_PAGE_SIZE + tokens.size());
                        }

                    } while (nextPage);
                }
                page_size_api = Optional.empty();
                schedulerConfig.setScheduler(false);
            }

        log.info("Next scheduled check at {}", OffsetDateTime.now().plusSeconds(schedulerConfig.getFixedDelay() / 1000));
        log.trace("regenerateQueueNotifications end");
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

    private void sendDataLakeUserNotifications(List<OnboardedUser> users, String productId){
        users.forEach(onboardedUser -> {
            userEventService.sendOnboardedUserNotification(onboardedUser, productId);
        });
    }

    @Async
    public void regenerateUserNotifications(Optional<String> userId){
        if (productsFilter.isPresent()){
            for (String productId: productsFilter.get()){
                boolean nextPage = true;
                int page = this.page.orElse(0);
                if (userId.isPresent()){
                    OnboardedUser user = userConnector.findById(userId.get());
                    userEventService.sendOnboardedUserNotification(user, productId);
                }
                else {
                    do {
                        List<OnboardedUser> users = userConnector.findAll(page, page_size_api.orElse(USER_PAGE_SIZE), productId);
                        sendDataLakeUserNotifications(users, productId);
                        page += 1;
                        if (users.size() < USER_PAGE_SIZE || this.page.isPresent()) {
                            nextPage = false;
                            log.debug("[KAFKA] USER TOTAL NUMBER {}", page * USER_PAGE_SIZE + users.size());
                        }
                    }while(nextPage);
                }
                page_size_api = Optional.empty();
                schedulerConfig.setScheduler(false);
            }

        }

    }

    @Override
    public void startScheduler(Optional<Integer> size, List<String> productsFilter) {
        this.page_size_api = size;
        this.productsFilter = Optional.of(productsFilter);
        schedulerConfig.setScheduler(true);
        regenerateQueueNotifications();
    }

    @Override
    public void startUsersScheduler(Optional<Integer> size, Optional<Integer> page, List<String> productsFilter, Optional<String> userId) {
        this.page_size_api = size;
        this.productsFilter = Optional.of(productsFilter);
        this.page=page;
        regenerateUserNotifications(userId);
    }
}
