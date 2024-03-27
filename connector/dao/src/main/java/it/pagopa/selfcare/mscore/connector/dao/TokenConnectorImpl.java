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
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.TokenMapper.convertToToken;
import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.TokenMapper.convertToTokenEntity;
import static it.pagopa.selfcare.mscore.constant.CustomError.CONTRACT_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.CustomError.TOKEN_NOT_FOUND;

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
    public Token updateTokenCreatedAt(String tokenId, OffsetDateTime createdAt, OffsetDateTime activatedAt) {
        Query query = Query.query(Criteria.where(TokenEntity.Fields.id.name()).is(tokenId));

        Update update = new Update();
        update.set(TokenEntity.Fields.updatedAt.name(), OffsetDateTime.now())
                .set(TokenEntity.Fields.createdAt.name(), createdAt);

        if(activatedAt != null) {
            update.set(TokenEntity.Fields.activatedAt.name(), activatedAt);
        }

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return TokenMapper.convertToToken(tokenRepository.findAndModify(query, update, findAndModifyOptions, TokenEntity.class));
    }

    @Override
    public List<Token> findByStatusAndProductId(EnumSet<RelationshipState> statuses, String productId, Integer page, Integer size) {
        Query query = Query.query(Criteria.where(TokenEntity.Fields.status.name()).in(statuses))
                .with(Sort.by(Sort.Direction.ASC, TokenEntity.Fields.id.name()));

        Pageable pageable = PageRequest.of(page, size);

        if (productId != null && !productId.isBlank()) {
            query.addCriteria(Criteria.where(TokenEntity.Fields.productId.name()).is(productId));
        }

        return tokenRepository.find(query, pageable, TokenEntity.class)
                .stream()
                .map(TokenMapper::convertToToken)
                .toList();
    }

}
