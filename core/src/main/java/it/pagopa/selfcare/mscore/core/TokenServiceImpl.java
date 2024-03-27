package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.RelationshipState.ACTIVE;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;
    private final UserService userService;
    private final OnboardingDao onboardingDao;
    private final InstitutionConnector institutionConnector;
    private final ContractEventNotificationService contractService;

    public TokenServiceImpl(TokenConnector tokenConnector,
                            UserService userService,
                            OnboardingDao onboardingDao,
                            InstitutionConnector institutionConnector,
                            ContractEventNotificationService contractService) {
        this.tokenConnector = tokenConnector;
        this.userService = userService;
        this.onboardingDao = onboardingDao;
        this.institutionConnector = institutionConnector;
        this.contractService = contractService;
    }

    public Token getToken(String institutionId, String productId) {
        log.trace("getToken start");
        log.debug("getToken institutionId = {}, productId = {}", institutionId, productId);
        Token token = tokenConnector.findWithFilter(institutionId, productId);
        log.debug("getToken result = {}", token);
        log.trace("getToken end");
        return token;
    }

    private TokenRelationships retrieveToken(String tokenId, boolean existingOnly){
        Token token = tokenConnector.findById(tokenId);
        List<OnboardedUser> onboardedUsers = Optional.ofNullable(token.getUsers())
                .map(users -> {
                    var ids = users.stream().map(TokenUser::getUserId).collect(Collectors.toList());
                    return existingOnly ? userService.findAllExistingByIds(ids) : userService.findAllByIds(ids);
                })
                .orElse(Collections.emptyList());
        return TokenUtils.toTokenRelationships(token, onboardedUsers);
    }

    @Override
    public List<TokenRelationships> getTokensByProductId(String productId, Integer page, Integer size) {
        log.trace("getTokensByProductId start");
        log.debug("getTokensByProductId productId = {}, page = {}, size = {}", productId, page, size);
        List<Token> tokensByStatusAndProduct = tokenConnector.findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.PENDING),
                productId, page, size);
        List<TokenRelationships> tokens = tokensByStatusAndProduct.stream()
                .map(token -> retrieveToken(token.getId(), true))
                .collect(Collectors.toList());
        log.debug("getTokensByProductId result = {}", tokens);
        log.trace("getTokensByProductId end");
        return tokens;
    }

    @Override
    public PaginatedToken retrieveContractsFilterByStatus(List<RelationshipState> states, Integer page, Integer size, String productId) {
        log.trace("getTokens start");
        log.debug("getTokens productId = {} states = {}, page = {}, size = {}", productId, states, page, size);

        List<Token> tokensByStatusAndProduct = tokenConnector.findByStatusAndProductId(EnumSet.copyOf(states), productId, page, size);
        List<NotificationToSend> notificationToSends = tokensByStatusAndProduct.stream()
                .map(token -> {
                    Institution institution = retrieveRelatedInstitution(token.getInstitutionId());
                    return toNotificationToSend(institution, token);
                })
                .toList();

        log.debug("getTokens result = {}", notificationToSends);
        log.trace("getTokens end");
        return new PaginatedToken(notificationToSends);
    }

    private Institution retrieveRelatedInstitution(String institutionId) {
        return institutionConnector.findById(institutionId);
    }

    public NotificationToSend toNotificationToSend(Institution institution, Token token) {
        NotificationToSend notification = new NotificationToSend();
        notification.setId(token.getId());
        notification.setState(token.getStatus() == RelationshipState.DELETED ? "CLOSED" : token.getStatus().toString());
        if (ACTIVE.equals(token.getStatus())) {
            notification.setUpdatedAt(Optional.ofNullable(token.getActivatedAt()).orElse(token.getCreatedAt()));
        } else {
            notification.setUpdatedAt(Optional.ofNullable(token.getUpdatedAt()).orElse(token.getCreatedAt()));
            if (token.getStatus().equals(RelationshipState.DELETED)) {
                notification.setClosedAt(Optional.ofNullable(token.getDeletedAt()).orElse(token.getUpdatedAt()));
                notification.setUpdatedAt(Optional.ofNullable(token.getDeletedAt()).orElse(token.getUpdatedAt()));
            } else {
                notification.setUpdatedAt(Optional.ofNullable(token.getUpdatedAt()).orElse(token.getCreatedAt()));
            }
        }
        return contractService.toNotificationToSend(notification, institution, token);
    }
}
