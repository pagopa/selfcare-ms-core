package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.aggregation.QueryCount;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class QueueNotificationServiceImpl implements QueueNotificationService {

    public static final int TOKEN_PAGE_SIZE = 100;
    public static final int USER_PAGE_SIZE = 100;
    private final ContractEventNotificationService contractService;
    private final UserEventService userEventService;
    private Optional<Integer> page_size_api = Optional.empty();
    private Optional<Integer> page = Optional.empty();
    private final InstitutionConnector institutionConnector;
    private Optional<List<String>> productsFilter = Optional.empty();

    private final UserConnector userConnector;

    private final List<RelationshipState> statesToSend = List.of(RelationshipState.ACTIVE, RelationshipState.DELETED);


    @Autowired
    public QueueNotificationServiceImpl(ContractEventNotificationService contractService,
                                        UserEventService userEventService,
                                        InstitutionConnector institutionConnector, UserConnector userConnector) {
        this.userEventService = userEventService;
        this.userConnector = userConnector;
        log.info("Initializing {}...", QueueNotificationServiceImpl.class.getSimpleName());
        this.contractService = contractService;
        this.institutionConnector = institutionConnector;
    }

    @Override
    @Async
    public void sendContractsNotificationsByInstitutionIdAndTokenId(String tokenId, String institutionId) {
        log.trace("regenerateQueueNotifications start");
        log.debug("Regenerating notifications on queue with institutionId {} and tokenId {}", institutionId, tokenId);

        Institution institution = institutionConnector.findById(institutionId);

        List<Onboarding> onboardings = institution.getOnboarding().stream()
                .filter(item -> Objects.nonNull(item.getStatus()) && statesToSend.contains(item.getStatus()))
                .filter(item -> tokenId.equals(item.getTokenId()))
                .toList();

        if(onboardings.isEmpty()) {
            log.trace("Onboarding not found with institutionId {} and tokenId {}", institutionId, tokenId);
            return;
        }

        for(Onboarding onboarding : onboardings) {

            Token token = new Token();
            token.setId(onboarding.getTokenId());
            token.setInstitutionId(institutionId);
            token.setProductId(onboarding.getProductId());
            //token.setUsers(users.stream().map(this::toTokenUser).toList());
            token.setCreatedAt(onboarding.getCreatedAt());
            token.setUpdatedAt(onboarding.getUpdatedAt());
            token.setStatus(onboarding.getStatus());
            token.setContractSigned(onboarding.getContract());
            institution.setOnboarding(List.of(onboarding));
            contractService.sendDataLakeNotification(institution, token, QueueEvent.UPDATE);
        }

        log.trace("regenerateQueueNotifications end");
    }

    private void sendScContractNotifications(List<Token> tokens) {
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
                        List<OnboardedUser> users = userConnector.findAllValidUsers(page, page_size_api.orElse(USER_PAGE_SIZE), productId);
                        sendDataLakeUserNotifications(users, productId);
                        page += 1;
                        if (users.size() < USER_PAGE_SIZE || this.page.isPresent()) {
                            nextPage = false;
                            log.debug("[KAFKA] USER TOTAL NUMBER {}", page * USER_PAGE_SIZE + users.size());
                        }
                    }while(nextPage);
                }
                page_size_api = Optional.empty();
            }

        }

    }

    @Async
    @Override
    public void sendUsers(Optional<Integer> size, Optional<Integer> page, List<String> productsFilter, Optional<String> userId) {
        this.page_size_api = size;
        this.productsFilter = Optional.ofNullable(productsFilter);
        this.page=page;
        regenerateUserNotifications(userId);
    }

    @Override
    public List<QueryCount> countUsers() {
        return userConnector.countUsers();
    }
}
