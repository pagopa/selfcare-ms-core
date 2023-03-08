package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.VERIFY_TOKEN_RELATIONSHIP_STATES;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;
    private final UserService userService;

    public TokenServiceImpl(TokenConnector tokenConnector,
                            UserService userService) {
        this.tokenConnector = tokenConnector;
        this.userService = userService;
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

    @Override
    public Token verifyOnboarding(String institutionId, String productId) {
        return tokenConnector.findWithFilter(institutionId, productId);
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

}
