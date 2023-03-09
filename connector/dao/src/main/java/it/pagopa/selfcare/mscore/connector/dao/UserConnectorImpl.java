package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.page.RelationshipEntityPage;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipPage;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@Component
public class UserConnectorImpl implements UserConnector {

    private static final String CURRENT_PRODUCT = "current.";
    private static final String CURRENT_PRODUCT_REF = "$[current]";
    private static final String CURRENT_USER_BINDING = "currentUserBinding.";
    private static final String CURRENT_USER_BINDING_REF = "$[currentUserBinding]";

    private static final String PROJECT = "$project";
    private static final String FACET = "$facet";
    private static final String SKIP = "$skip";
    private static final String LIMIT = "$limit";
    private static final String COUNT = "$count";
    private static final String ELEM_AT = "$arrayElemAt";

    private final UserRepository repository;
    private final MongoTemplate mongoTemplate;

    public UserConnectorImpl(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.repository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public OnboardedUser findById(String userId) {
        Optional<UserEntity> entityOpt = repository.findById(userId);
        return entityOpt.map(UserMapper::toOnboardedUser).orElse(null);
    }

    @Override
    public List<OnboardedUser> findAllByIds(List<String> users) {
        List<OnboardedUser> userList = new ArrayList<>();
        Set<String> userIds = new HashSet<>(users);
        repository.findAllById(users)
                .forEach(userEntity -> {
                    userList.add(UserMapper.toOnboardedUser(userEntity));
                    userIds.remove(userEntity.getId());
                });
        if (users.size() != userList.size()) {
            throw new ResourceNotFoundException(String.format(USERS_NOT_FOUND_ERROR.getMessage(), String.join(",", userIds)), USERS_NOT_FOUND_ERROR.getCode());
        }
        return userList;
    }

    @Override
    public void findAndUpdateState(String userId, @Nullable String relationshipId, @Nullable String tokenId, RelationshipState state) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));
        Update update = new Update()
                .set(constructQuery(CURRENT_USER_BINDING_REF, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.status.name()), state)
                .set(constructQuery(CURRENT_USER_BINDING_REF, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.updatedAt.name()), OffsetDateTime.now());
        if (relationshipId != null) {
            update.filterArray(Criteria.where(CURRENT_PRODUCT  + OnboardedProduct.Fields.relationshipId.name()).is(relationshipId));
        }
        if (tokenId != null) {
            update.filterArray(Criteria.where(CURRENT_PRODUCT + OnboardedProduct.Fields.tokenId.name()).is(tokenId));
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

    @Override
    public void findAndUpdate(OnboardedUser onboardedUser, String userId, String institutionId, OnboardedProduct product, UserBinding bindings) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));
        Update update = new Update();
        Optional<UserBinding> opt = onboardedUser.getBindings().stream()
                .filter(userBinding -> institutionId.equalsIgnoreCase(userBinding.getInstitutionId()))
                .findFirst();
        if (opt.isPresent()) {
            update.addToSet(constructQuery(CURRENT_USER_BINDING_REF, UserBinding.Fields.products.name()), product);
            update.filterArray(Criteria.where(CURRENT_USER_BINDING + UserBinding.Fields.institutionId.name()).is(institutionId));
        } else {
            update.addToSet(UserEntity.Fields.bindings.name(), bindings);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true).returnNew(true);
        repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

    @Override
    public OnboardedUser findAndCreate(String id, UserBinding binding) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(id));
        Update update = new Update();
        if (binding != null) {
            update.addToSet(UserEntity.Fields.bindings.name(), binding);
        }
        update.set(UserEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        update.set(UserEntity.Fields.createdAt.name(), OffsetDateTime.now());
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true).returnNew(true);
        return UserMapper.toOnboardedUser(repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class));
    }

    @Override
    public OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> state) {

        Query query = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.institutionId.name()).is(institutionId)
                        .and(UserBinding.Fields.products.name())
                        .elemMatch(Criteria.where(OnboardedProduct.Fields.productId.name()).is(productId)
                                .and(OnboardedProduct.Fields.role.name()).is(PartyRole.MANAGER)
                                .and(OnboardedProduct.Fields.env.name()).is(EnvEnum.ROOT)
                                .and(OnboardedProduct.Fields.status.name()).in(state))));

        return repository.find(query, UserEntity.class).stream()
                .findFirst()
                .map(UserMapper::toOnboardedUser)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), institutionId, productId),
                        GET_INSTITUTION_MANAGER_NOT_FOUND.getCode()));
    }

    @Override
    public List<OnboardedUser> findActiveInstitutionAdmin(String userId, String institutionId, List<PartyRole> roles, List<RelationshipState> states) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId))
                .addCriteria(Criteria.where(UserEntity.Fields.bindings.name())
                        .elemMatch(Criteria.where(UserBinding.Fields.institutionId.name()).is(institutionId)
                                .and(constructQuery(UserBinding.Fields.products.name()))
                                .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name())).in(roles)
                                .and(OnboardedProduct.Fields.env.name()).is(EnvEnum.ROOT)
                                .and(OnboardedProduct.Fields.status.name()).in(states)));

        return repository.find(query, UserEntity.class).stream()
                .map(UserMapper::toOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<OnboardedUser> findWithFilter(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        List<RelationshipState> stateList = states.isEmpty() ? List.of(RelationshipState.values()) : states;
        List<PartyRole> roleList = roles.isEmpty() ? List.of(PartyRole.values()) : roles;

        Criteria criteria = Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(constructQuery(UserBinding.Fields.institutionId.name())).is(institutionId))
                .and(constructQuery(UserBinding.Fields.products.name()))
                .elemMatch(constructCriteria("", roleList, stateList, productRoles, products));
        if (personId != null) {
            criteria = criteria.and(UserEntity.Fields.id.name()).is(personId);
        }

        return repository.find(new Query(criteria), UserEntity.class).stream()
                .map(UserMapper::toOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public RelationshipPage findPagedWithFilter(String institutionId,
                                                @Nullable String personId,
                                                @Nullable List<PartyRole> roles,
                                                @Nullable List<RelationshipState> states,
                                                @Nullable List<String> products,
                                                @Nullable List<String> productRoles,
                                                Pageable pageable) {
        /* Step pipeline di aggregazione per query paginata con ordinamento e conteggio totale elementi
        [
            { "$match" : { "bindings" : { "$elemMatch" : { "institutionId" : "7c9c8416-32ad-4f47-834b-b12dc37715d8",
                "products" : { "$elemMatch" : { "role" : { "$in" : ["MANAGER"]}, "status" : { "$in" : ["ACTIVE"]}, "productId" : { "$in" : ["prod-pn-pg"]}, "productRole" : { "$in" : ["referente amministrativo"]}}}},
            }}},
            { "$project" : { "bindings" : 1}},
            { "$unwind" : "$bindings"},
            { "$match" : { "bindings.institutionId" : "7c9c8416-32ad-4f47-834b-b12dc37715d8"}},
            { "$unwind" : "$bindings.products"},
            { "$project" : { "product" : "$bindings.products", "institutionId" : "$bindings.institutionId"}},
            { "$match" : { "product.productId" : { "$in" : ["prod-pn-pg"]}, "product.role" : { "$in" : ["MANAGER"]}, "product.status" : { "$in" : ["ACTIVE"]}, "product.productRole" : { "$in" : ["referente amministrativo"]}}},
            { "$sort" : { "_id" : 1, "institutionId" : 1, "product.productId" : 1}},
            { "$facet" : { "count" : [{ "$count" : "total"}], "data" : [{ "$skip" : 0}, { "$limit" : 1}]}},
            { "$project" : { "data" : 1, "total" : { "$arrayElemAt" : ["$count.total", 0]}}},
        ]
        */
        Criteria bindingCriteria = Criteria.where(UserBindingEntity.Fields.institutionId.name()).is(institutionId)
                .and(UserBindingEntity.Fields.products.name())
                .elemMatch(constructCriteria("", roles, states, productRoles, products));
        Criteria criteria = Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(bindingCriteria);
        if (personId != null) {
            criteria = criteria.and(UserEntity.Fields.id.name()).is(personId);
        }
        AggregationOperation aoMatch1 = match(criteria);
        AggregationOperation aoP1 = c -> new Document(PROJECT, new Document(UserEntity.Fields.bindings.name(), 1L));
        AggregationOperation aoUnwind1 = unwind(UserEntity.Fields.bindings.name());
        AggregationOperation aoMatch2 = match(Criteria.where(constructQuery(UserBindingEntity.Fields.institutionId.name()))
                .is(institutionId));
        AggregationOperation aoUnwind2 = unwind(constructQuery(UserBindingEntity.Fields.products.name()));
        AggregationOperation aoP2 = c -> new Document(PROJECT,
                new Document("product", "$" + constructQuery(UserBindingEntity.Fields.products.name()))
                        .append(UserBindingEntity.Fields.institutionId.name(), "$" + constructQuery(UserBindingEntity.Fields.institutionId.name())));
        AggregationOperation aoMatch3 = match(constructCriteria("product.", roles, states, productRoles, products));
        AggregationOperation aoSort = sort(buildSort("product."));
        AggregationOperation aoFacet = c -> new Document(FACET, new Document("count", List.of(new Document(COUNT, "total")))
                .append("data", Arrays.asList(new Document(SKIP, pageable.getOffset()), new Document(LIMIT, pageable.getPageSize()))));
        AggregationOperation aoP3 = c -> new Document(PROJECT, new Document("data", 1L)
                .append("total", new Document(ELEM_AT, Arrays.asList("$count.total", 0L))));
        Aggregation aggregation = Aggregation.newAggregation(aoMatch1, aoP1, aoUnwind1, aoMatch2, aoUnwind2, aoP2, aoMatch3, aoSort, aoFacet, aoP3);
        AggregationResults<RelationshipEntityPage> result = mongoTemplate.aggregate(aggregation, UserEntity.class, RelationshipEntityPage.class);

        RelationshipEntityPage entityPage = result.getUniqueMappedResult();
        RelationshipPage page = new RelationshipPage();
        if (entityPage != null) {
            page.setTotal(entityPage.getTotal());
            page.setData(UserMapper.toRelationshipElement(entityPage.getData()));
        }
        return page;
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.products.name())
                        .elemMatch(Criteria.where(OnboardedProduct.Fields.relationshipId.name()).in(relationshipId))));

        return repository.find(query, UserEntity.class).stream()
                .findFirst()
                .map(UserMapper::toOnboardedUser)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RELATIONSHIP_ID_NOT_FOUND.getMessage(), relationshipId),
                        RELATIONSHIP_ID_NOT_FOUND.getCode()));
    }

    @Override
    public void findAndRemoveProduct(String userId, String institutionId, OnboardedProduct product) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));
        Update update = new Update();

        update.pull(constructQuery(CURRENT_USER_BINDING_REF, UserBinding.Fields.products.name()), product);
        update.filterArray(Criteria.where(CURRENT_USER_BINDING + UserBinding.Fields.institutionId.name()).is(institutionId));

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true).returnNew(true);
        repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

    private Criteria constructCriteria(String prefix, List<PartyRole> roles, List<RelationshipState> states, List<String> productRoles, List<String> products) {
        return CriteriaBuilder.builder()
                .inIfNotEmpty(prefix + OnboardedProductEntity.Fields.role.name(), roles)
                .inIfNotEmpty(prefix + OnboardedProductEntity.Fields.status.name(), states)
                .inIfNotEmpty(prefix + OnboardedProductEntity.Fields.productId.name(), products)
                .inIfNotEmpty(prefix + OnboardedProductEntity.Fields.productRole.name(), productRoles)
                .build();

    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserEntity.Fields.bindings.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private Sort buildSort(String prefix) {
        return Sort.by(
                Sort.Order.asc(UserEntity.Fields.id.name()),
                Sort.Order.asc(UserBindingEntity.Fields.institutionId.name()),
                Sort.Order.asc(prefix + OnboardedProductEntity.Fields.productId)
        );
    }
}
