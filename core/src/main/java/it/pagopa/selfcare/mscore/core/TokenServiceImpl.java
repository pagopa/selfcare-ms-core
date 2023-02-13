package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.TOKEN_ALREADY_CONSUMED;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.TOKEN_NOT_FOUND;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.verifyTokenRelationshipStates;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;

    public TokenServiceImpl(TokenConnector tokenConnector) {
        this.tokenConnector = tokenConnector;
    }

    @Override
    public Token verifyToken(String tokenId) {
        log.info("START - verifyToken {}", tokenId);
        Optional<Token> token = tokenConnector.findById(tokenId);
        if (token.isEmpty()) {
            throw new ResourceNotFoundException(String.format(TOKEN_NOT_FOUND.getMessage(), tokenId), TOKEN_NOT_FOUND.getCode());
        } else if (!verifyTokenRelationshipStates.contains(token.get().getStatus())) {
            throw new ResourceConflictException(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenId), TOKEN_ALREADY_CONSUMED.getCode());
        }
        log.info("END - verifyToken {}", token.get());
        return token.get();

    }

}
