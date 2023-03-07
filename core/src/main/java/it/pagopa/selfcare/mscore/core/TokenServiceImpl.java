package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.VERIFY_TOKEN_RELATIONSHIP_STATES;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;
    private final OnboardingDao onboardingDao;
    private final UserService userService;
    private final InstitutionService institutionService;

    public TokenServiceImpl(TokenConnector tokenConnector,
                            UserService userService,
                            InstitutionService institutionService,
                            OnboardingDao onboardingDao) {
        this.tokenConnector = tokenConnector;
        this.userService = userService;
        this.institutionService = institutionService;
        this.onboardingDao = onboardingDao;
    }

    @Override
    public Token verifyToken(String tokenId) {
        Token token = tokenConnector.findById(tokenId);
        if (!VERIFY_TOKEN_RELATIONSHIP_STATES.contains(token.getStatus()) || token.getUsers() == null || token.getUsers().isEmpty()) {
            throw new ResourceConflictException(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenId), TOKEN_ALREADY_CONSUMED.getCode());
        }
        return token;
    }

    @Override
    public String findActiveContract(String institutionId, String userId, String productId) {
        return tokenConnector.findActiveContract(institutionId, userId, productId).getId();
    }

    @Override
    public TokenRelationships getToken(String tokenId) {
        Token token = tokenConnector.findById(tokenId);
        List<OnboardedUser> users;
        if (token.getUsers() != null) {
            var ids = token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList());
            users = userService.findAllByIds(ids);
        } else {
            users = Collections.emptyList();
        }
        return toTokenRelationships(token, users);
    }

    private TokenRelationships toTokenRelationships(Token token, List<OnboardedUser> users) {
        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum(token.getChecksum());
        tokenRelationships.setTokenId(token.getId());
        tokenRelationships.setInstitutionId(token.getInstitutionId());
        tokenRelationships.setProductId(token.getProductId());
        tokenRelationships.setUsers(users);
        return tokenRelationships;
    }

    @Override
    public void verifyOnboarding(String institutionId, String productId, List<RelationshipState> states) {
        List<Token> tokens = tokenConnector.findWithFilter(institutionId, productId, states);
        if (tokens == null || tokens.isEmpty()) {
            throw new InvalidRequestException(String.format(CONTRACT_NOT_FOUND.getMessage(), institutionId, productId), CONTRACT_NOT_FOUND.getCode());
        }
    }

    @Override
    public void checkAndHandleExpiring(Token token) {
        var now = OffsetDateTime.now();
        if (token.getExpiringDate() != null && (now.isEqual(token.getExpiringDate()) || now.isAfter(token.getExpiringDate()))) {
            log.info("token {} is expired at {} and now is {}", token.getId(), token.getExpiringDate(), now);
            var institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
            onboardingDao.persistForUpdate(token, institution, RelationshipState.DELETED, null);
            throw new InvalidRequestException(String.format(TOKEN_EXPIRED.getMessage(), token.getId(), token.getExpiringDate()), TOKEN_EXPIRED.getCode());
        }
    }
}
