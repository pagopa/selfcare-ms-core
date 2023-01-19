package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenConnectorImpl implements TokenConnector {

    private final TokenRepository tokenRepository;

    public TokenConnectorImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<Token> findActiveContract(String institutionId, String userId, String productId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("productId").is(productId),
                        Criteria.where("institutionId").is(institutionId),
                        Criteria.where("status").is(RelationshipState.ACTIVE),
                        Criteria.where("users").in(userId)
                )
        );
        return tokenRepository.find(query, TokenEntity.class).stream()
                .map(this::convertToToken)
                .collect(Collectors.toList());
    }

    @Override
    public Token save(Token token) {
        final TokenEntity entity = convertToTokenEntity(token);
        return convertToToken(tokenRepository.save(entity));
    }

    private TokenEntity convertToTokenEntity(Token token) {
        TokenEntity entity = new TokenEntity();
        entity.setContract(token.getContract());
        entity.setChecksum(token.getChecksum());
        entity.setInstitutionId(token.getInstitutionId());
        entity.setStatus(token.getStatus());
        entity.setUsers(token.getUsers());
        entity.setExpiringDate(token.getExpiringDate());
        entity.setProductId(token.getProductId());
        return entity;
    }

    private Token convertToToken(TokenEntity tokenEntity) {
        Token token = new Token();
        token.setId(tokenEntity.getId().toString());
        token.setContract(tokenEntity.getContract());
        token.setChecksum(tokenEntity.getChecksum());
        token.setInstitutionId(tokenEntity.getInstitutionId());
        token.setStatus(tokenEntity.getStatus());
        token.setUsers(tokenEntity.getUsers());
        token.setExpiringDate(token.getExpiringDate());
        token.setProductId(token.getProductId());
        return token;
    }
}
