package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.GET_INSTITUTION_MANAGER_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.TOKEN_NOT_FOUND;

@Slf4j
@Component
public class TokenConnectorImpl implements TokenConnector {

    private final TokenRepository tokenRepository;

    public TokenConnectorImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void deleteById(String id) {
        tokenRepository.deleteById(id);
    }

    @Override
    public Token findActiveContract(String institutionId, String userId, String productId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where(TokenEntity.Fields.productId.name()).is(productId),
                        Criteria.where(TokenEntity.Fields.institutionId.name()).is(institutionId),
                        Criteria.where(TokenEntity.Fields.status.name()).is(RelationshipState.ACTIVE),
                        Criteria.where(TokenEntity.Fields.users.name()).is(userId)
                )
        );
        return tokenRepository.find(query, TokenEntity.class).stream()
                .findFirst()
                .map(this::convertToToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), institutionId, productId),
                        GET_INSTITUTION_MANAGER_NOT_FOUND.getCode()));
    }

    @Override
    public Token save(Token token) {
        final TokenEntity entity = convertToTokenEntity(token);
        return convertToToken(tokenRepository.save(entity));
    }

    @Override
    public Token findById(String tokenId) {
        Optional<Token> opt = tokenRepository.findById(tokenId).map(this::convertToToken);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(TOKEN_NOT_FOUND.getMessage(), tokenId), TOKEN_NOT_FOUND.getCode());
        }
        return opt.get();
    }

    @Override
    public Token findAndUpdateToken(String tokenId, RelationshipState status, String digest) {
        Query query = Query.query(Criteria.where(TokenEntity.Fields.id.name()).is(tokenId));
        Update updateDefinition = new Update()
                .set(TokenEntity.Fields.status.name(), status)
                .set(TokenEntity.Fields.updatedAt.name(), OffsetDateTime.now());

        if(digest != null){
            updateDefinition.set(TokenEntity.Fields.checksum.name(), digest);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        return convertToToken(tokenRepository.findAndModify(query, updateDefinition, findAndModifyOptions, TokenEntity.class));
    }

    @Override
    public List<Token> findWithFilter(String institutionId, String productId, List<RelationshipState> state) {
        Query query = Query.query(Criteria.where(TokenEntity.Fields.productId.name()).is(productId)
                .and(TokenEntity.Fields.institutionId.name()).is(institutionId)
                .and(TokenEntity.Fields.status.name()).nin(state));

        return tokenRepository.find(query, TokenEntity.class).stream()
                .map(this::convertToToken)
                .collect(Collectors.toList());
    }

    private TokenEntity convertToTokenEntity(Token token) {
        TokenEntity entity = new TokenEntity();
        if (token.getId() != null) {
            entity.setId(token.getId());
        }else{
            entity.setId(UUID.randomUUID().toString());
        }
        entity.setContract(token.getContract());
        entity.setChecksum(token.getChecksum());
        entity.setInstitutionId(token.getInstitutionId());
        entity.setStatus(token.getStatus());
        entity.setUsers(token.getUsers());
        entity.setExpiringDate(token.getExpiringDate());
        entity.setProductId(token.getProductId());
        entity.setCreatedAt(token.getCreatedAt());
        entity.setUpdatedAt(token.getUpdatedAt());
        return entity;
    }

    private Token convertToToken(TokenEntity tokenEntity) {
        Token token = new Token();
        token.setId(tokenEntity.getId());
        token.setContract(tokenEntity.getContract());
        token.setChecksum(tokenEntity.getChecksum());
        token.setInstitutionId(tokenEntity.getInstitutionId());
        token.setStatus(tokenEntity.getStatus());
        token.setUsers(tokenEntity.getUsers());
        token.setExpiringDate(tokenEntity.getExpiringDate());
        token.setCreatedAt(tokenEntity.getCreatedAt());
        token.setUpdatedAt(tokenEntity.getUpdatedAt());
        token.setProductId(tokenEntity.getProductId());
        return token;
    }
}
