package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.VERIFY_TOKEN_RELATIONSHIP_STATES;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;

    public TokenServiceImpl(TokenConnector tokenConnector) {
        this.tokenConnector = tokenConnector;
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
    public Token verifyOnboarding(String institutionId, String productId) {
        return tokenConnector.findWithFilter(institutionId, productId);
    }
}
