package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;

    public TokenServiceImpl(TokenConnector tokenConnector) {
        this.tokenConnector = tokenConnector;
    }

    @Override
    public String findActiveContract(String institutionId, String userId, String productId) {
        List<Token> tokenList = tokenConnector.findActiveContract(institutionId, userId, productId);
        if (!tokenList.isEmpty()) {
            log.debug("retrieve first element of tokenList --> id: {}",tokenList.get(0).getId());
            return tokenList.get(0).getId();
        } else {
            throw new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), institutionId, productId),
                    GET_INSTITUTION_MANAGER_NOT_FOUND.getCode());
        }
    }
}
