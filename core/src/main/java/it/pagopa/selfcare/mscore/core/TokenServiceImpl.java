package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.TOKEN_ALREADY_CONSUMED;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.VERIFY_TOKEN_RELATIONSHIP_STATES;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;
    private final UserService userService;
    private final OnboardingDao onboardingDao;
    private final InstitutionConnector institutionConnector;

    public TokenServiceImpl(TokenConnector tokenConnector,
                            UserService userService,
                            OnboardingDao onboardingDao,
                            InstitutionConnector institutionConnector) {
        this.tokenConnector = tokenConnector;
        this.userService = userService;
        this.onboardingDao = onboardingDao;
        this.institutionConnector = institutionConnector;
    }

    @Override
    public Token verifyToken(String tokenId) {
        Token token = tokenConnector.findById(tokenId);

        if (!VERIFY_TOKEN_RELATIONSHIP_STATES.contains(token.getStatus()) || token.getUsers() == null || token.getUsers().isEmpty()) {
            throw new ResourceConflictException(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenId), TOKEN_ALREADY_CONSUMED.getCode());
        }

        if (hasTokenExpired(token, OffsetDateTime.now())) {
            log.debug("Token {} has expired on {} and the current date is {}", token.getId(), token.getExpiringDate(), OffsetDateTime.now());
            var institution = institutionConnector.findById(token.getInstitutionId());
            onboardingDao.persistForUpdate(token, institution, RelationshipState.REJECTED, null);
            throw new ResourceConflictException(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenId), TOKEN_ALREADY_CONSUMED.getCode());
        }

        return token;
    }

    @Override
    public Token verifyOnboarding(String institutionId, String productId) {
        return tokenConnector.findWithFilter(institutionId, productId);
    }

    public Token getToken(String institutionId, String productId){
        log.trace("getToken start");
        log.debug("getToken institutionId = {}, productId = {}", institutionId, productId);
        Token token = tokenConnector.findWithFilter(institutionId, productId);
        log.debug("getToken result = {}", token);
        log.trace("getToken end");
        return token;
    }

    @Override
    public TokenRelationships retrieveToken(String tokenId, boolean existingOnly) {
        log.trace("retrieveToken start");
        log.debug("retrieveToken tokenId = {}, existingOnly = {}", tokenId, existingOnly);
        Token token = tokenConnector.findById(tokenId);
        List<OnboardedUser> users;
        if (token.getUsers() != null) {
            var ids = token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList());
                users = userService.findAllByIds(ids, existingOnly);
        } else {
            users = Collections.emptyList();
        }
        return TokenUtils.toTokenRelationships(token, users);
    }

    private boolean hasTokenExpired(Token token, OffsetDateTime now) {
        return token.getExpiringDate() != null && (now.isEqual(token.getExpiringDate()) || now.isAfter(token.getExpiringDate()));
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
}
