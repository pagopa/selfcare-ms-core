package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.TokenUserEntity;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.NONE)
public class TokenMapper {

    public static TokenEntity convertToTokenEntity(Token token) {
        TokenEntity entity = new TokenEntity();
        if (token.getId() != null) {
            entity.setId(token.getId());
        }else{
            entity.setId(UUID.randomUUID().toString());
        }
        entity.setContractTemplate(token.getContractTemplate());
        entity.setContractSigned(token.getContractSigned());
        entity.setChecksum(token.getChecksum());
        entity.setInstitutionId(token.getInstitutionId());
        entity.setStatus(token.getStatus());
        entity.setUsers(toTokenUserEntity(token.getUsers()));
        entity.setType(token.getType());
        entity.setExpiringDate(token.getExpiringDate());
        entity.setProductId(token.getProductId());
        entity.setCreatedAt(token.getCreatedAt());
        entity.setUpdatedAt(token.getUpdatedAt());
        return entity;
    }
    public static Token convertToToken(TokenEntity tokenEntity) {
        Token token = new Token();
        token.setId(tokenEntity.getId());
        token.setContractTemplate(tokenEntity.getContractTemplate());
        token.setContractSigned(tokenEntity.getContractSigned());
        token.setChecksum(tokenEntity.getChecksum());
        token.setInstitutionId(tokenEntity.getInstitutionId());
        token.setStatus(tokenEntity.getStatus());
        token.setUsers(toTokenUser(tokenEntity.getUsers()));
        token.setExpiringDate(tokenEntity.getExpiringDate());
        token.setCreatedAt(tokenEntity.getCreatedAt());
        token.setUpdatedAt(tokenEntity.getUpdatedAt());
        token.setProductId(tokenEntity.getProductId());
        return token;
    }

    private static List<TokenUserEntity> toTokenUserEntity(List<TokenUser> users) {
        List<TokenUserEntity> response = new ArrayList<>();
        for(TokenUser user : users){
            TokenUserEntity entity = new TokenUserEntity();
            entity.setRole(user.getRole());
            entity.setUserId(user.getUserId());
            response.add(entity);
        }
        return response;
    }

    private static List<TokenUser> toTokenUser(List<TokenUserEntity> users) {
        List<TokenUser> response = new ArrayList<>();
        for(TokenUserEntity user : users){
            TokenUser tokenUser = new TokenUser();
            tokenUser.setRole(user.getRole());
            tokenUser.setUserId(user.getUserId());
            response.add(tokenUser);
        }
        return response;
    }
}
