package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionMapperHelper;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.VerifyOnboardingFilters;
import it.pagopa.selfcare.product.entity.ProductStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionMapperHelper.addGeographicTaxonomies;
import static it.pagopa.selfcare.mscore.constant.CustomError.GET_INSTITUTION_BILLING_ERROR;
import static it.pagopa.selfcare.mscore.constant.CustomError.INSTITUTION_NOT_FOUND;

@Slf4j
@Component
public class InstitutionConnectorImpl implements InstitutionConnector {

    private static final String CURRENT_ONBOARDING = "current.";
    private static final String CURRENT_ONBOARDING_REFER = "$[current]";

    private final InstitutionRepository repository;
    private final InstitutionEntityMapper institutionMapper;

    public InstitutionConnectorImpl(InstitutionRepository repository, InstitutionEntityMapper institutionMapper) {
        this.repository = repository;
        this.institutionMapper = institutionMapper;
    }

    @Override
    public List<Institution> findAll() {
        return repository.findAll().stream().map(institutionMapper::convertToInstitution).collect(Collectors.toList());
    }

    @Override
    public Institution save(Institution institution) {
        final InstitutionEntity entity = institutionMapper.convertToInstitutionEntity(institution);
        return institutionMapper.convertToInstitution(repository.save(entity));
    }

    @Override
    public Institution saveOrRetrievePnPg(Institution institution) {
        final InstitutionEntity entity = institutionMapper.convertToInstitutionEntity(institution);
        return findByExternalId(institution.getExternalId())
                .orElse(institutionMapper.convertToInstitution(repository.save(entity)));
    }

    @Override
    public List<String> findByExternalIdsAndProductId(List<ValidInstitution> validInstitutionList, String productId) {
        List<String> externalIds = validInstitutionList.stream().map(ValidInstitution::getId).collect(Collectors.toList());
        Query query = Query.query(Criteria.where(constructQuery(Onboarding.Fields.productId.name())).is(productId)
                .and(InstitutionEntity.Fields.externalId.name()).in(externalIds));
        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionEntity::getExternalId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Institution findById(String id) {
        return repository.findById(id)
                .map(institution -> {
                    log.info("Founded institution {}", institution.getExternalId());
                    return institutionMapper.convertToInstitution(institution);
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), id, "UNDEFINED"), INSTITUTION_NOT_FOUND.getCode()));
    }

    @Override
    public Institution findAndUpdateStatus(String institutionId, String tokenId, RelationshipState status) {
        OffsetDateTime now = OffsetDateTime.now();

        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update()
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.status.name()), status)
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.updatedAt.name()), now)
                .filterArray(Criteria.where(CURRENT_ONBOARDING + Onboarding.Fields.tokenId.name()).is(tokenId));
        if (status == RelationshipState.DELETED) {
            update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.closedAt.name()), now);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return institutionMapper.convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public Institution findAndUpdate(String institutionId, Onboarding onboarding, List<InstitutionGeographicTaxonomies> geographicTaxonomiesList, InstitutionUpdate institutionUpdate) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        if (institutionUpdate != null) {
            Map<String, Object> map = InstitutionMapperHelper.getNotNullField(institutionUpdate);
            map.forEach(update::set);
        }
        addGeographicTaxonomies(geographicTaxonomiesList, update);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return institutionMapper.convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public List<Institution> findByGeotaxonomies(List<String> geo, SearchMode searchMode) {
        Query query = constructQueryWithSearchMode(geo, searchMode);
        return repository.find(query, InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findByProductId(String productId) {
        Query query = Query.query(Criteria.where(constructQuery(Onboarding.Fields.productId.name())).is(productId));
        return repository.find(query, InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findAllByIds(List<String> ids) {
        List<Institution> list = new ArrayList<>();
        repository.findAllById(ids)
                .forEach(entity -> list.add(institutionMapper.convertToInstitution(entity)));
        return list;
    }

    @Override
    public Institution findByExternalIdAndProductId(String externalId, String productId) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)
                .and(constructQuery(Onboarding.Fields.productId.name())).is(productId));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_BILLING_ERROR.getMessage(), externalId, productId),
                        GET_INSTITUTION_BILLING_ERROR.getCode()));
    }

    @Override
    public List<Onboarding> findOnboardingByIdAndProductId(String institutionId, String productId) {

        Optional<InstitutionEntity> optionalInstitution = Objects.nonNull(productId)
                ? Optional
                .ofNullable(repository.findByInstitutionIdAndOnboardingProductId(institutionId, productId))
                : repository.findById(institutionId);
        return optionalInstitution
                .map(institutionMapper::convertToInstitution)
                .map(Institution::getOnboarding)
                .orElse(List.of());
    }

    @Override
    public List<Institution> findInstitutionsByProductId(String productId, Integer page, Integer size) {

        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.onboarding.name()).
                elemMatch(Criteria.where(Onboarding.Fields.productId.name()).is(productId)));

        Pageable pageable = PageRequest.of(Objects.nonNull(page) ? page : 0,
                Objects.nonNull(size) ? size : 100);

        Page<InstitutionEntity> institutionEntities = repository.find(query, pageable, InstitutionEntity.class);
        return institutionEntities.getContent().stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public void findAndRemoveOnboarding(String institutionId, Onboarding onboarding) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.pull(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class);
    }

    @Override
    public List<Institution> findByTaxCodeSubunitCodeAndOrigin(String taxCode, String subunitCode, String origin, String originId) {
        return repository.find(Query.query(CriteriaBuilder.builder()
                                .isIfNotNull(InstitutionEntity.Fields.taxCode.name(), taxCode)
                                .isIfNotNull(InstitutionEntity.Fields.subunitCode.name(), subunitCode)
                                .isIfNotNull(InstitutionEntity.Fields.origin.name(), origin)
                                .isIfNotNull(InstitutionEntity.Fields.originId.name(), originId)
                                .build()
                        ),
                        InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByTaxCodeAndSubunitCodeAndProductAndStatusList(String taxCode, String subunitCode,
                                                                        Optional<String> optProductId, List<RelationshipState> validRelationshipStates) {

        Criteria criteriaInstitution = Criteria.where(InstitutionEntity.Fields.taxCode.name()).is(taxCode)
                .and(InstitutionEntity.Fields.subunitCode.name()).is(subunitCode);

        Criteria criteriaOnboarding = Criteria.where(Onboarding.Fields.status.name()).in(validRelationshipStates);
        optProductId.ifPresent(productId -> criteriaOnboarding.and(Onboarding.Fields.productId.name()).is(productId));

        return repository.exists(Query.query(criteriaInstitution)
                        .addCriteria(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                                .elemMatch(criteriaOnboarding))
                , InstitutionEntity.class);
    }

    @Override
    public Optional<Institution> findByExternalId(String externalId) {
        return repository.find(Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)),
                        InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .findFirst();
    }

    @Override
    public List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId))
                .addCriteria(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                        .elemMatch(Criteria.where(Onboarding.Fields.productId.name()).is(productId)
                                .and(Onboarding.Fields.status.name()).in(validRelationshipStates)));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public Institution updateOnboardedProductCreatedAt(String institutionId, String productId, OffsetDateTime createdAt) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));

        Update update = new Update();
        update.set(constructQuery(CURRENT_ONBOARDING_REFER, OnboardingEntity.Fields.createdAt.name()), createdAt)
                .set(constructQuery(CURRENT_ONBOARDING_REFER, OnboardingEntity.Fields.updatedAt.name()), OffsetDateTime.now())
                .filterArray(Criteria.where(CURRENT_ONBOARDING + OnboardingEntity.Fields.productId.name()).is(productId)
                        .and(CURRENT_ONBOARDING + OnboardingEntity.Fields.status.name()).is(RelationshipState.ACTIVE.name()));

        Update updateInstitutionEntityUpdatedAt = new Update();
        updateInstitutionEntityUpdatedAt.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class);
        return institutionMapper.convertToInstitution(repository.findAndModify(query, updateInstitutionEntityUpdatedAt, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public List<Institution> findBrokers(String productId, InstitutionType type) {

        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.institutionType.name()).is(type)
                .and(InstitutionEntity.Fields.onboarding.name()).elemMatch(Criteria.where(Onboarding.Fields.productId.name()).is(productId)
                        .and(Onboarding.Fields.status.name()).is(ProductStatus.ACTIVE)));

        List<InstitutionEntity> institutionEntities = repository.find(query, InstitutionEntity.class);
        return institutionEntities.stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findByTaxCodeAndSubunitCode(String taxCode, String subunitCode) {
        return repository.find(Query.query(Criteria.where(InstitutionEntity.Fields.taxCode.name()).is(taxCode)
                                .and(InstitutionEntity.Fields.subunitCode.name()).is(subunitCode)
                        ),
                        InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findByOriginAndOriginId(String origin, String originId) {
        return repository.find(Query.query(CriteriaBuilder.builder()
                                .isIfNotNull(InstitutionEntity.Fields.origin.name(), origin)
                                .isIfNotNull(InstitutionEntity.Fields.originId.name(), originId)
                                .build()
                        ),
                        InstitutionEntity.class).stream()
                .map(institutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsOnboardingByFilters(VerifyOnboardingFilters filters) {
        Criteria criteriaInstitution = CriteriaBuilder.builder()
                .isIfNotNull(InstitutionEntity.Fields.externalId.name(), filters.getExternalId())
                .isIfNotNull(InstitutionEntity.Fields.taxCode.name(), filters.getTaxCode())
                .isIfNotNull(InstitutionEntity.Fields.origin.name(), filters.getOrigin())
                .isIfNotNull(InstitutionEntity.Fields.originId.name(), filters.getOriginId())
                .isIfNotNull(InstitutionEntity.Fields.subunitCode.name(), filters.getSubunitCode())
                .build();

        Criteria criteriaOnboarding = Criteria.where(Onboarding.Fields.status.name()).in(filters.getValidRelationshipStates())
                .and(Onboarding.Fields.productId.name()).is(filters.getProductId());

        return repository.exists(Query.query(criteriaInstitution)
                        .addCriteria(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                                .elemMatch(criteriaOnboarding))
                , InstitutionEntity.class);
    }

    private Query constructQueryWithSearchMode(List<String> geo, SearchMode searchMode) {
        String geoQuery = InstitutionEntity.Fields.geographicTaxonomies.name()
                + "." + GeoTaxonomyEntity.Fields.code.name();
        switch (searchMode) {
            case ALL:
                return Query.query(Criteria.where(geoQuery).all(geo));
            case ANY:
                return Query.query(Criteria.where(geoQuery).in(geo));
            case EXACT:
                return Query.query(Criteria.where(geoQuery).all(geo)
                        .and(InstitutionEntity.Fields.geographicTaxonomies.name()).size(geo.size()));
            default:
                throw new InvalidRequestException("Invalid search mode", "0000");
        }
    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(InstitutionEntity.Fields.onboarding.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }
}
