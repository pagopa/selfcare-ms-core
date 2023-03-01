package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

import java.util.UUID;

public class TokenMapper {

    public static TokenEntity convertToTokenEntity(Token token) {
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

    public static Token convertToToken(TokenEntity tokenEntity) {
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
