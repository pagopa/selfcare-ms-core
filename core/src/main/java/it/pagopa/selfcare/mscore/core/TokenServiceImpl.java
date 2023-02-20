package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.ProductRelationship;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.verifyTokenRelationshipStates;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;
    private final InstitutionService institutionService;
    private final OnboardingDao onboardingDao;

    public TokenServiceImpl(TokenConnector tokenConnector, UserConnector userConnector, InstitutionService institutionService, OnboardingDao onboardingDao) {
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
        this.institutionService = institutionService;
        this.onboardingDao = onboardingDao;
    }

    @Override
    public Token verifyToken(String tokenId) {
        Token token = tokenConnector.findById(tokenId);
        if (!verifyTokenRelationshipStates.contains(token.getStatus())) {
            throw new ResourceConflictException(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenId), TOKEN_ALREADY_CONSUMED.getCode());
        }
        return token;
    }

    @Override
    public void activateRelationship(String id) {
        Token token = tokenConnector.findById(id);
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        List<OnboardedUser> userList = new ArrayList<>();
        token.getUsers().forEach(s -> {
            OnboardedUser user = userConnector.getById(id);
            log.info("founded user {}", s);
            if (user != null) {
                userList.add(user);
            }
        });
        if (RelationshipState.SUSPENDED == token.getStatus()) {
            onboardingDao.persistForUpdate(token, institution, userList, RelationshipState.ACTIVE);
        }else {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_ACTIVABLE.getMessage(), id, token.getStatus()), RELATIONSHIP_NOT_ACTIVABLE.getCode());
        }
    }

    @Override
    public void suspendRelationship(String id) {
        Token token = tokenConnector.findById(id);
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        List<OnboardedUser> userList = new ArrayList<>();
        token.getUsers().forEach(s -> {
            OnboardedUser user = userConnector.getById(id);
            log.info("founded user {}", s);
            if (user != null) {
                userList.add(user);
            }
        });
        if (RelationshipState.ACTIVE == token.getStatus()) {
            onboardingDao.persistForUpdate(token, institution, userList, RelationshipState.SUSPENDED);
        } else {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_SUSPENDABLE.getMessage(), id, token.getStatus()), RELATIONSHIP_NOT_SUSPENDABLE.getCode());
        }
    }

    @Override
    public Token retrieveToken(String institutionId, String productId) {
        List<Token> tokens = tokenConnector.findWithFilter(institutionId, productId, List.of(RelationshipState.values()));
        if(tokens!=null && !tokens.isEmpty()){
            return tokens.get(0);
        }
        throw new InvalidRequestException(String.format(CONTRACT_NOT_FOUND.getMessage(), institutionId, productId), CONTRACT_NOT_FOUND.getCode());
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

    @Override
    public ProductRelationship retrieveRelationship(String tokenId) {
        return new ProductRelationship();
    }

    @Override
    public Token getToken(String id){
        return tokenConnector.findById(id);
    }
}
