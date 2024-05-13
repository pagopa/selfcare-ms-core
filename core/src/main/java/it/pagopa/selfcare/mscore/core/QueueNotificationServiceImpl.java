package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
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
    private Optional<Integer> page_size_api = Optional.empty();
    private Optional<Integer> page = Optional.empty();
    private final InstitutionConnector institutionConnector;
    private Optional<List<String>> productsFilter = Optional.empty();

    private final List<RelationshipState> statesToSend = List.of(RelationshipState.ACTIVE, RelationshipState.DELETED);


    @Autowired
    public QueueNotificationServiceImpl(ContractEventNotificationService contractService,
                                        InstitutionConnector institutionConnector) {
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

}
