package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.page.GeographicTaxonomyEntityPage;
import it.pagopa.selfcare.mscore.connector.dao.model.page.OnboardingEntityPage;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionMapper.addGeographicTaxonomies;
import static it.pagopa.selfcare.mscore.constant.CustomError.GET_INSTITUTION_BILLING_ERROR;
import static it.pagopa.selfcare.mscore.constant.CustomError.INSTITUTION_NOT_FOUND;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@Component
public class InstitutionConnectorImpl implements InstitutionConnector {

    private static final String CURRENT_ONBOARDING = "current.";
    private static final String CURRENT_ONBOARDING_REFER = "$[current]";

    private static final String PROJECT = "$project";
    private static final String FACET = "$facet";
    private static final String SKIP = "$skip";
    private static final String LIMIT = "$limit";
    private static final String COUNT = "$count";
    private static final String ELEM_AT = "$arrayElemAt";
    private static final String TOTAL = "total";

    private final InstitutionRepository repository;
    private final MongoTemplate mongoTemplate;

    public InstitutionConnectorImpl(InstitutionRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Institution save(Institution institution) {
        final InstitutionEntity entity = InstitutionMapper.convertToInstitutionEntity(institution);
        return InstitutionMapper.convertToInstitution(repository.save(entity));
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
                    return InstitutionMapper.convertToInstitution(institution);
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), id, "UNDEFINED"), INSTITUTION_NOT_FOUND.getCode()));
    }

    @Override
    public InstitutionGeographicTaxonomyPage findGeographicTaxonomies(String institutionId, Pageable pageable) {
        /* Step pipeline di aggregazione per query paginata con ordinamento e conteggio totale elementi
        [
            { "$match" : { "_id" : "462f2565-4244-4f30-bb0b-52d71e166aaf"}},
            { "$project" : { "geographicTaxonomies" : 1, "_id" : 0}},
            { "$unwind" : "$geographicTaxonomies"},
            { "$sort" : { "geographicTaxonomies.code" : -1}},
            { "$facet" : { "count" : [{ "$count" : "total"}], "data" : [{ "$skip" : 0}, { "$limit" : 1}]}},
            { "$project" : { "data" : "$$ROOT.data.geographicTaxonomies", "total" : { "$arrayElemAt" : ["$count.total", 0]}}},
        ]
        */
        AggregationOperation aoMatch = match(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        AggregationOperation aoP1 = c -> new Document(PROJECT, new Document(InstitutionEntity.Fields.geographicTaxonomies.name(), 1L)
                .append(InstitutionEntity.Fields.id.name(), 0L));
        AggregationOperation aoUnwind = unwind(InstitutionEntity.Fields.geographicTaxonomies.name());
        AggregationOperation aoSort = sort(buildSortForGeographicTaxonomies(pageable));
        AggregationOperation aoFacet = c -> new Document(FACET, new Document("count", List.of(new Document(COUNT, TOTAL)))
                .append("data", Arrays.asList(new Document(SKIP, pageable.getOffset()), new Document(LIMIT, pageable.getPageSize()))));
        AggregationOperation aoP2 = c -> new Document(PROJECT, new Document("data", "$$ROOT.data." + InstitutionEntity.Fields.geographicTaxonomies.name())
                .append(TOTAL, new Document(ELEM_AT, Arrays.asList("$count.total", 0L))));
        Aggregation aggregation = Aggregation.newAggregation(aoMatch, aoP1, aoUnwind, aoSort, aoFacet, aoP2);
        AggregationResults<GeographicTaxonomyEntityPage> result = mongoTemplate.aggregate(aggregation, InstitutionEntity.class, GeographicTaxonomyEntityPage.class);

        GeographicTaxonomyEntityPage entityPage = result.getUniqueMappedResult();
        InstitutionGeographicTaxonomyPage page = new InstitutionGeographicTaxonomyPage();
        if (entityPage != null) {
            page.setTotal(entityPage.getTotal());
            page.setData(InstitutionMapper.toInstitutionGeographicTaxonomies(entityPage.getData()));
        }
        return page;
    }

    @Override
    public OnboardingPage findOnboarding(String institutionId, List<RelationshipState> states, Pageable pageable) {
        /* Step pipeline di aggregazione per query paginata con ordinamento e conteggio totale elementi
        [
            { "$match" : { "_id" : "6735a33d-17bd-46a3-9dc6-8862f9b9af04", "$and" : [{ "onboarding" : { "$elemMatch" : { "status" : { "$in" : ["ACTIVE"]}}}}]}},
            { "$project" : { "onboarding" : 1, "id" : 0}},
            { "$unwind" : "$onboarding"},
            { "$sort" : { "onboarding.code" : -1}},
            { "$facet" : { "count" : [{ "$count" : "total"}], "data" : [{ "$skip" : 0}, { "$limit" : 1}]}},
            { "$project" : { "data" : "$$ROOT.data.onboarding", "total" : { "$arrayElemAt" : ["$count.total", 0]}}},
        ]
        */
        Criteria criteria = Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId);
        if (states != null && !states.isEmpty()) {
            criteria.andOperator(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                    .elemMatch(Criteria.where(OnboardingEntity.Fields.status.name()).in(states)));
        }
        AggregationOperation aoMatch = match(criteria);
        AggregationOperation aoP1 = c -> new Document(PROJECT, new Document(InstitutionEntity.Fields.onboarding.name(), 1L)
                .append(InstitutionEntity.Fields.id.name(), 0L));
        AggregationOperation aoUnwind = unwind(InstitutionEntity.Fields.onboarding.name());
        AggregationOperation aoSort = sort(buildSortForOnboarding(pageable));
        AggregationOperation aoFacet = c -> new Document(FACET, new Document("count", List.of(new Document(COUNT, TOTAL)))
                .append("data", Arrays.asList(new Document(SKIP, pageable.getOffset()), new Document(LIMIT, pageable.getPageSize()))));
        AggregationOperation aoP2 = c -> new Document(PROJECT, new Document("data", "$$ROOT.data." + InstitutionEntity.Fields.onboarding.name())
                .append(TOTAL, new Document(ELEM_AT, Arrays.asList("$count.total", 0L))));
        Aggregation aggregation = Aggregation.newAggregation(aoMatch, aoP1, aoUnwind, aoSort, aoFacet, aoP2);
        AggregationResults<OnboardingEntityPage> result = mongoTemplate.aggregate(aggregation, InstitutionEntity.class, OnboardingEntityPage.class);

        OnboardingEntityPage entityPage = result.getUniqueMappedResult();
        OnboardingPage page = new OnboardingPage();
        if (entityPage != null) {
            page.setTotal(entityPage.getTotal());
            page.setData(InstitutionMapper.toOnboarding(entityPage.getData()));
        }
        return page;
    }

    @Override
    public void findAndUpdateStatus(String institutionId, String tokenId, RelationshipState status) {
        OffsetDateTime now = OffsetDateTime.now();

        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update()
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.status.name()), status)
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.updatedAt.name()), now)
                .filterArray(Criteria.where(CURRENT_ONBOARDING + Onboarding.Fields.tokenId.name()).is(tokenId));
        if (status == RelationshipState.DELETED) {
            update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.closedAt.name()), now);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class);
    }

    @Override
    public Institution findAndUpdate(String institutionId, Onboarding onboarding, List<InstitutionGeographicTaxonomies> geographicTaxonomiesList) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        if (geographicTaxonomiesList != null && !geographicTaxonomiesList.isEmpty()) {
            List<GeoTaxonomyEntity> list = geographicTaxonomiesList.stream().map(geographicTaxonomies -> {
                GeoTaxonomyEntity entity = new GeoTaxonomyEntity();
                entity.setCode(geographicTaxonomies.getCode());
                entity.setDesc(geographicTaxonomies.getDesc());
                return entity;
            }).collect(Collectors.toList());
            list.forEach(geoTaxonomyEntity -> update.addToSet(InstitutionEntity.Fields.geographicTaxonomies.name(), geoTaxonomyEntity));
        }

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return InstitutionMapper.convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public void findAndUpdateInstitutionData(String institutionId, Token token, Onboarding onboarding, RelationshipState state) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        if (state != null) {
            update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.status.name()), state)
                    .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.updatedAt.name()), OffsetDateTime.now())
                    .filterArray(Criteria.where(CURRENT_ONBOARDING + Onboarding.Fields.tokenId.name()).is(token.getId()));
            if (state == RelationshipState.DELETED) {
                update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.closedAt.name()), OffsetDateTime.now());
            }
        }
        InstitutionUpdate institutionUpdate = token.getInstitutionUpdate();
        if (institutionUpdate != null) {
            Map<String, Object> map = InstitutionMapper.getNotNullField(institutionUpdate);
            map.forEach(update::set);
            addGeographicTaxonomies(institutionUpdate, update);
        }

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class);
    }

    @Override
    public List<Institution> findByGeotaxonomies(List<String> geo, String searchMode) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.geographicTaxonomies.name()).in(geo));
        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findByProductId(String productId) {
        Query query = Query.query(Criteria.where(constructQuery(Onboarding.Fields.productId.name())).is(productId));
        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public Institution findInstitutionProduct(String externalId, String productId) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)
                .and(constructQuery(Onboarding.Fields.productId.name())).is(productId));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_BILLING_ERROR.getMessage(), externalId, productId),
                        GET_INSTITUTION_BILLING_ERROR.getCode()));
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
    public Optional<Institution> findByExternalId(String externalId) {
        return repository.find(Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)),
                        InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .findFirst();
    }

    @Override
    public List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId))
                .addCriteria(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                        .elemMatch(Criteria.where(Onboarding.Fields.productId.name()).is(productId)
                                .and(Onboarding.Fields.status.name()).in(validRelationshipStates)));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(InstitutionEntity.Fields.onboarding.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private Sort buildSortForGeographicTaxonomies(Pageable pageable) {
        String prefix = InstitutionEntity.Fields.geographicTaxonomies.name() + ".";
        return buildSort(pageable, prefix, GeoTaxonomyEntity.Fields.code.name());
    }

    private Sort buildSortForOnboarding(Pageable pageable) {
        String prefix = InstitutionEntity.Fields.onboarding.name() + ".";
        return buildSort(pageable, prefix, OnboardingEntity.Fields.productId.name());
    }

    private Sort buildSort(Pageable pageable, String prefix, String defaultField) {
        Sort sort;
        if (pageable.getSort().isUnsorted()) {
            sort = Sort.by(Sort.Order.desc(prefix + defaultField));
        } else {
            List<Sort.Order> sorts = pageable.getSort().get()
                    .map(s -> {
                        if (s.isAscending()) {
                            return Sort.Order.asc(prefix + s.getProperty());
                        }
                        return Sort.Order.desc(prefix + s.getProperty());
                    })
                    .collect(Collectors.toList());
            sort = Sort.by(sorts);
        }
        return sort;
    }
}
