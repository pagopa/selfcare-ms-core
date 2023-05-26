package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.TokenMapper.convertToToken;
import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.TokenMapper.convertToTokenEntity;
import static it.pagopa.selfcare.mscore.constant.CustomError.*;

@Slf4j
@Component
public class TokenConnectorImpl implements TokenConnector {

    private final TokenRepository tokenRepository;

    public TokenConnectorImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<Token> findAll(){
        return tokenRepository.findAll().stream().map(TokenMapper::convertToToken).collect(Collectors.toList());
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
                .map(TokenMapper::convertToToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), institutionId, productId),
                        GET_INSTITUTION_MANAGER_NOT_FOUND.getCode()));
    }

    @Override
    public Token save(Token token, List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        final TokenEntity entity = convertToTokenEntity(token, geographicTaxonomies);
        return convertToToken(tokenRepository.save(entity));
    }

    @Override
    public Token findById(String tokenId) {
        Optional<Token> opt = tokenRepository.findById(tokenId).map(TokenMapper::convertToToken);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(TOKEN_NOT_FOUND.getMessage(), tokenId), TOKEN_NOT_FOUND.getCode());
        }
        return opt.get();
    }

    @Override
    public Token findAndUpdateToken(Token token, RelationshipState status, @Nullable String digest) {
        OffsetDateTime now = OffsetDateTime.now();

        Query query = Query.query(Criteria.where(TokenEntity.Fields.id.name()).is(token.getId()));
        Update updateDefinition = new Update()
                .set(TokenEntity.Fields.status.name(), status)
                .set(TokenEntity.Fields.updatedAt.name(), now);

        if (digest != null) {
            updateDefinition.set(TokenEntity.Fields.checksum.name(), digest);
        }
        if (token.getContractSigned() != null) {
            updateDefinition.set(TokenEntity.Fields.contractSigned.name(), token.getContractSigned());
        }
        if (token.getContentType() != null) {
            updateDefinition.set(TokenEntity.Fields.contentType.name(), token.getContentType());
        }
        if (status == RelationshipState.DELETED) {
            updateDefinition.set(TokenEntity.Fields.closedAt.name(), now);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        return convertToToken(tokenRepository.findAndModify(query, updateDefinition, findAndModifyOptions, TokenEntity.class));
    }

    @Override
    public Token findWithFilter(String institutionId, String productId) {
        Query query = Query.query(Criteria.where(TokenEntity.Fields.productId.name()).is(productId)
                .and(TokenEntity.Fields.institutionId.name()).is(institutionId)
                .and(TokenEntity.Fields.status.name()).is(RelationshipState.ACTIVE)
                .and(TokenEntity.Fields.type.name()).is(TokenType.INSTITUTION));

        return tokenRepository.find(query, TokenEntity.class).stream()
                .map(TokenMapper::convertToToken)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(String.format(CONTRACT_NOT_FOUND.getMessage(), institutionId, productId), CONTRACT_NOT_FOUND.getCode()));
    }

    @Override
    public Token updateTokenCreatedAt(String tokenId, OffsetDateTime createdAt) {
        Query query = Query.query(Criteria.where(TokenEntity.Fields.id.name()).is(tokenId));

        Update update = new Update();
        update.set(TokenEntity.Fields.updatedAt.name(), OffsetDateTime.now())
                .set(TokenEntity.Fields.createdAt.name(), createdAt);

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return TokenMapper.convertToToken(tokenRepository.findAndModify(query, update, findAndModifyOptions, TokenEntity.class));
    }

    @Override
    public List<Token> findByStatusAndProductId(EnumSet<RelationshipState> statuses, String productId, Integer page) {
        Query query = Query.query(Criteria.where(TokenEntity.Fields.status.name()).in(statuses));

        Pageable pageable = PageRequest.of(page, 100, Sort.by(TokenEntity.Fields.createdAt.name()));

        if (productId != null && !productId.isBlank()) {
            query.addCriteria(Criteria.where(TokenEntity.Fields.productId.name()).is(productId));
        }

        return tokenRepository.find(query, pageable, TokenEntity.class)
                .stream()
                .map(TokenMapper::convertToToken)
                .collect(Collectors.toList());
    }

}
