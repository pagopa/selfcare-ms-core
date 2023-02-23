package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.CONTRACT_NOT_FOUND;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
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
        if (!VERIFY_TOKEN_RELATIONSHIP_STATES.contains(token.getStatus())) {
            throw new ResourceConflictException(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenId), TOKEN_ALREADY_CONSUMED.getCode());
        }
        return token;
    }

    @Override
    public String findActiveContract(String institutionId, String userId, String productId) {
        return tokenConnector.findActiveContract(institutionId, userId, productId).getId();
    }

    @Override
    public Token getToken(String id) {
        return tokenConnector.findById(id);
    }

    @Override
    public Token retrieveToken(String institutionId, String productId) {
        List<Token> tokens = tokenConnector.findWithFilter(institutionId, productId, List.of(RelationshipState.values()));
        if (tokens != null && !tokens.isEmpty()) {
            return tokens.get(0);
        }
        throw new InvalidRequestException(String.format(CONTRACT_NOT_FOUND.getMessage(), institutionId, productId), CONTRACT_NOT_FOUND.getCode());
    }
}
