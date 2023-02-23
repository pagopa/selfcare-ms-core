package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.Institution;
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
    private final UserService userService;
    private final InstitutionService institutionService;
    private final OnboardingDao onboardingDao;

    public TokenServiceImpl(TokenConnector tokenConnector, UserService userService, InstitutionService institutionService, OnboardingDao onboardingDao) {
        this.tokenConnector = tokenConnector;
        this.userService = userService;
        this.institutionService = institutionService;
        this.onboardingDao = onboardingDao;
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
    public void activateRelationship(String relationshipId) {
        OnboardedUser user = userService.findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.SUSPENDED), RelationshipState.ACTIVE);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_ACTIVABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_ACTIVABLE.getCode());
        }
    }

    @Override
    public void suspendRelationship(String relationshipId) {
        OnboardedUser user = userService.findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.ACTIVE), RelationshipState.SUSPENDED);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_SUSPENDABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_SUSPENDABLE.getCode());
        }
    }

    @Override
    public void deleteRelationship(String relationshipId) {
        OnboardedUser user = userService.findByRelationshipId(relationshipId);
        onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.values()), RelationshipState.DELETED);
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

    @Override
    public RelationshipInfo retrieveRelationship(String relationshipId) {
        OnboardedUser user = userService.findByRelationshipId(relationshipId);
        for (UserBinding userBinding : user.getBindings()) {
            for (OnboardedProduct product : userBinding.getProducts()) {
                if (relationshipId.equalsIgnoreCase(product.getRelationshipId())) {
                    Institution institution = institutionService.retrieveInstitutionById(userBinding.getInstitutionId());
                    return new RelationshipInfo(institution, user.getId(), product);
                }
            }
        }
        throw new InvalidRequestException(String.format(RELATIONSHIP_ID_NOT_FOUND.getMessage(), relationshipId), RELATIONSHIP_ID_NOT_FOUND.getCode());
    }
}
