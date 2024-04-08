package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.aggregation.UserInstitutionAggregationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserInstitutionAggregationMapper;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.aggregation.QueryCount;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserInfo;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.entity.ProductRoleInfo;
import it.pagopa.selfcare.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConnectorImpl implements UserConnector {

    private static final String CURRENT_PRODUCT = "current.";
    private static final String CURRENT_ANY = "$[]";
    private static final String CURRENT_PRODUCT_REF = "$[current]";
    private static final String CURRENT_USER_BINDING = "currentUserBinding.";
    private static final String CURRENT_USER_BINDING_REF = "$[currentUserBinding]";

    private static final List<String> VALID_USER_RELATIONSHIPS = List.of(RelationshipState.ACTIVE.name(), RelationshipState.DELETED.name(), RelationshipState.SUSPENDED.name());
    private final UserRepository repository;

    private final UserEntityMapper userMapper;

    private final UserInstitutionAggregationMapper userInstitutionAggregationMapper;

    private final MongoOperations mongoOperations;

    private final ProductService productService;


    /**
     * This query retrieves all the users having status in VALID_USER_RELATIONSHIPS for the given productId
     *
     * @param page
     * @param size
     * @param productId
     * @return List of onboarded users
     */
    @Override
    public List<OnboardedUser> findAllValidUsers(Integer page, Integer size, String productId) {
        Pageable pageable = PageRequest.of(page, size);
        Query queryMatch = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.products.name())
                        .elemMatch(CriteriaBuilder.builder()
                                .isIfNotNull(OnboardedProductEntity.Fields.productId.name(), productId)
                                .build()
                                .and(OnboardedProductEntity.Fields.status.name()).in(VALID_USER_RELATIONSHIPS))));
        return repository.find(queryMatch, pageable, UserEntity.class)
                .stream()
                .map(userMapper::toOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public OnboardedUser findById(String userId) {
        Optional<UserEntity> entityOpt = repository.findById(userId);
        return entityOpt.map(userMapper::toOnboardedUser)
                .orElseThrow(() -> {
                    log.error(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId));
                    return new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId), USER_NOT_FOUND_ERROR.getCode());
                });
    }

    @Override
    public List<OnboardedUser> findAllByIds(List<String> users) {
        return findAll(users, false);
    }

    @Override
    public List<OnboardedUser> findAllByExistingIds(List<String> users) {
        return findAll(users, true);
    }

    private List<OnboardedUser> findAll(List<String> userIds, boolean existingOnly) {
        List<OnboardedUser> userList = new ArrayList<>();
        Set<String> remainingUserIds = new HashSet<>(userIds);

        repository.findAllById(userIds).forEach(userEntity -> {
            userList.add(userMapper.toOnboardedUser(userEntity));
            remainingUserIds.remove(userEntity.getId());
        });

        if (!existingOnly && !remainingUserIds.isEmpty()) {
            String missingUserIds = String.join(",", remainingUserIds);
            throw new ResourceNotFoundException(String.format(USERS_NOT_FOUND_ERROR.getMessage(), missingUserIds), USERS_NOT_FOUND_ERROR.getCode());
        }
        return userList;
    }


    @Override
    public void findAndUpdateState(String userId, @Nullable String relationshipId, @Nullable Token token, RelationshipState state) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));
        Update update = new Update()
                .set(constructQuery(CURRENT_ANY, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.status.name()), state)
                .set(constructQuery(CURRENT_ANY, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.updatedAt.name()), OffsetDateTime.now());
        if (relationshipId != null) {
            update.filterArray(Criteria.where(CURRENT_PRODUCT + OnboardedProduct.Fields.relationshipId.name()).is(relationshipId));
        }
        if (token != null) {
            update.filterArray(Criteria.where(CURRENT_PRODUCT + OnboardedProduct.Fields.tokenId.name()).is(token.getId()));
            if (token.getContractSigned() != null) {
                update.set(constructQuery(CURRENT_ANY, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.contract.name()), token.getContractSigned());
            }
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

    @Override
    public void findAndUpdateStateWithOptionalFilter(String userId, String institutionId, String productId, PartyRole role, String productRole, RelationshipState state) {

        checkRoles(productId, role, productRole);

        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));
        Update update = new Update();

        boolean productFilterIsPresent = role != null || StringUtils.hasText(productId) || StringUtils.hasText(productRole);

        if (StringUtils.hasText(institutionId) && productFilterIsPresent) {
            filterForInstution(update, institutionId);
            filterForProduct(update, role, productRole, productId);
            setUpdate(update, state, CURRENT_USER_BINDING_REF, CURRENT_PRODUCT_REF);
        } else if (StringUtils.hasText(institutionId)) {
            filterForInstution(update, institutionId);
            setUpdate(update, state, CURRENT_USER_BINDING_REF, CURRENT_ANY);
        } else if (productFilterIsPresent) {
            filterForProduct(update, role, productRole, productId);
            setUpdate(update, state, CURRENT_ANY, CURRENT_PRODUCT_REF);
        } else {
            setUpdate(update, state, CURRENT_ANY, CURRENT_ANY);
        }

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

    private void filterForProduct(Update update, PartyRole role, String productRole, String productId) {
        update.filterArray(constructCriteria(role, productRole, productId));
    }

    private void filterForInstution(Update update, String institutionId) {
        update.filterArray(Criteria.where(CURRENT_USER_BINDING + UserBinding.Fields.institutionId.name()).is(institutionId));
    }

    private void setUpdate(Update update, RelationshipState state, String firstCurrent, String secondCurrent) {
        update.set(constructQuery(firstCurrent, UserBinding.Fields.products.name(), secondCurrent, OnboardedProduct.Fields.status.name()), state)
                .set(constructQuery(firstCurrent, UserBinding.Fields.products.name(), secondCurrent, OnboardedProduct.Fields.updatedAt.name()), OffsetDateTime.now());
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
        return userMapper.toOnboardedUser(repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class));
    }

    @Override
    public OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> state) {

        Query query = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.institutionId.name()).is(institutionId)
                        .and(UserBinding.Fields.products.name())
                        .elemMatch(Criteria.where(OnboardedProduct.Fields.productId.name()).is(productId)
                                .and(OnboardedProduct.Fields.role.name()).is(PartyRole.MANAGER)
                                .and(OnboardedProduct.Fields.env.name()).is(Env.ROOT)
                                .and(OnboardedProduct.Fields.status.name()).in(state))));

        return repository.find(query, UserEntity.class).stream()
                .findFirst()
                .map(userMapper::toOnboardedUser)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), institutionId, productId),
                        GET_INSTITUTION_MANAGER_NOT_FOUND.getCode()));
    }

    @Override
    public List<OnboardedUser> findActiveInstitutionUser(String userId, String institutionId) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId))
                .addCriteria(Criteria.where(UserEntity.Fields.bindings.name())
                        .elemMatch(Criteria.where(UserBinding.Fields.institutionId.name()).is(institutionId)));

        return repository.find(query, UserEntity.class).stream()
                .map(userMapper::toOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findUsersByInstitutionIdAndProductId(String institutionId, String productId) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                        .elemMatch(Criteria.where(UserBinding.Fields.institutionId.name()).is(institutionId)
                                .and(UserBinding.Fields.products.name()).elemMatch(
                                        Criteria.where(OnboardedProductEntity.Fields.productId.name()).is(productId)
                                .and(OnboardedProductEntity.Fields.status.name()).is(RelationshipState.ACTIVE.name())
                        )));

        return repository.find(query, UserEntity.class).stream()
                .map(UserEntity::getId)
                .toList();
    }

    @Override
    public List<OnboardedUser> findWithFilter(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        Criteria criteria = Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(constructElemMatch(institutionId, roles, states, productRoles, products));
        if (personId != null) {
            criteria = criteria.and(UserEntity.Fields.id.name()).is(personId);
        }

        return repository.find(new Query(criteria), UserEntity.class).stream()
                .map(userMapper::toOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.products.name())
                        .elemMatch(Criteria.where(OnboardedProduct.Fields.relationshipId.name()).in(relationshipId))));

        return repository.find(query, UserEntity.class).stream()
                .findFirst()
                .map(userMapper::toOnboardedUser)
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

    @Override
    public List<OnboardedUser> updateUserBindingCreatedAt(String institutionId, String productId, List<String> usersId, OffsetDateTime createdAt) {
        List<OnboardedUser> updatedUsersBindings = new ArrayList<>();

        usersId.forEach(userId -> {
            Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));

            Update update = new Update();
            update.set(constructQuery(CURRENT_USER_BINDING_REF, UserBindingEntity.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProductEntity.Fields.createdAt.name()), createdAt)
                    .set(constructQuery(CURRENT_USER_BINDING_REF, UserBindingEntity.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProductEntity.Fields.updatedAt.name()), OffsetDateTime.now())
                    .filterArray(Criteria.where(CURRENT_USER_BINDING + UserBindingEntity.Fields.institutionId.name()).is(institutionId))
                    .filterArray(Criteria.where(CURRENT_PRODUCT + OnboardedProductEntity.Fields.productId.name()).is(productId));

            Update updateUserEntityUpdatedAt = new Update();
            updateUserEntityUpdatedAt.set(UserEntity.Fields.updatedAt.name(), OffsetDateTime.now());

            FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
            repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
            updatedUsersBindings.add(userMapper.toOnboardedUser(repository.findAndModify(query, updateUserEntityUpdatedAt, findAndModifyOptions, UserEntity.class)));
        });

        return updatedUsersBindings;
    }

    @Override
    public List<UserInfo> findByInstitutionId(String institutionId) {
        Criteria criteria = Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.institutionId.name())
                        .is(institutionId));
        return repository.find(new Query(criteria), UserEntity.class).stream()
                .map(userEntity -> userMapper.toUserInfoByFirstInstitution(userEntity, institutionId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInstitutionAggregation> getUserInfo(String userId, String institutionId, String[] states) {

        List<Criteria> criterias = new ArrayList<>();
        MatchOperation matchUserId = Aggregation.match(Criteria.where("_id").is(userId));

        GraphLookupOperation lookup = Aggregation.graphLookup("Institution")
                .startWith("$bindings.institutionId")
                .connectFrom("institutionId")
                .connectTo("_id").as("institutions");

        UnwindOperation unwindBindings = Aggregation.unwind("$bindings");
        UnwindOperation unwindProducts = Aggregation.unwind("$bindings.products");

        if (Objects.nonNull(states) && states.length > 0) {
            criterias.add(Criteria.where("bindings.products.status").in(states));
        }

        if (Objects.nonNull(institutionId)) {
            criterias.add(Criteria.where("bindings.institutionId").is(institutionId));
        }

        MatchOperation matchInstitutionExist = Aggregation.match(Criteria.where("institutions").size(1));
        MatchOperation matchOperation = new MatchOperation(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));
        Aggregation aggregation = Aggregation.newAggregation(matchUserId, unwindBindings, lookup, matchInstitutionExist, unwindProducts, matchOperation);

        return mongoOperations.aggregate(aggregation, "User", UserInstitutionAggregation.class).getMappedResults();
    }

    private void checkRoles(String productId, PartyRole role, String productRole) {
        if (StringUtils.hasText(productRole)) {
            Assert.notNull(role, ROLE_IS_NULL.getMessage());
            Product product = productService.getProduct(productId);
            ProductRoleInfo productRoleInfo = product.getRoleMappings().get(it.pagopa.selfcare.onboarding.common.PartyRole.valueOf(role.name()));
            if (productRoleInfo == null) {
                throw new InvalidRequestException(ROLE_NOT_FOUND.getMessage(), ROLE_NOT_FOUND.getCode());
            }
            boolean roleExists = productRoleInfo.getRoles().stream()
                    .anyMatch(prodRole -> prodRole.getCode().equals(productRole));
            if (!roleExists) {
                throw new InvalidRequestException(PRODUCT_ROLE_NOT_FOUND.getMessage(), PRODUCT_ROLE_NOT_FOUND.getCode());
            }
        }
    }

    @Override
    public List<UserInstitutionAggregation> findUserInstitutionAggregation(UserInstitutionFilter filter) {
        List<UserInstitutionAggregationEntity> userInstitutionAggregationEntities =
                repository.findUserInstitutionAggregation(filter, UserInstitutionAggregationEntity.class);
        return userInstitutionAggregationEntities.stream()
                .map(userInstitutionAggregationMapper::constructUserInstitutionAggregation)
                .collect(Collectors.toList());
    }

    private Criteria constructElemMatch(String institutionId, List<PartyRole> roles, List<RelationshipState> states, List<String> productRoles, List<String> products) {
        return CriteriaBuilder.builder()
                .isIfNotNull(UserBinding.Fields.institutionId.name(), institutionId)
                .build()
                .and(UserBinding.Fields.products.name())
                .elemMatch(constructCriteria(roles, states, productRoles, products));
    }

    private Criteria constructCriteria(PartyRole role, String productRole, String product) {
        return CriteriaBuilder.builder()
                .isIfNotNull(CURRENT_PRODUCT + OnboardedProductEntity.Fields.role.name(), role != null ? role.name() : null)
                .isIfNotNull(CURRENT_PRODUCT + OnboardedProductEntity.Fields.productId.name(), product)
                .isIfNotNull(CURRENT_PRODUCT + OnboardedProductEntity.Fields.productRole.name(), productRole)
                .build();
    }

    private Criteria constructCriteria(List<PartyRole> roles, List<RelationshipState> states, List<String> productRoles, List<String> products) {
        return CriteriaBuilder.builder()
                .inIfNotEmpty(OnboardedProductEntity.Fields.role.name(), roles)
                .inIfNotEmpty(OnboardedProductEntity.Fields.status.name(), states)
                .inIfNotEmpty(OnboardedProductEntity.Fields.productId.name(), products)
                .inIfNotEmpty(OnboardedProductEntity.Fields.productRole.name(), productRoles)
                .build();

    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserEntity.Fields.bindings.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    @Override
    public List<QueryCount> countUsers() {
        UnwindOperation unwindBindings = Aggregation.unwind("$bindings", "binding", true);
        UnwindOperation unwindProducts = Aggregation.unwind("$bindings.products", "products", true);
        MatchOperation matchStatusId = Aggregation.match(Criteria.where("bindings.products.status").in(VALID_USER_RELATIONSHIPS));
        GroupOperation productCount = group("bindings.products.productId").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(unwindBindings, unwindProducts, matchStatusId, productCount);
        return mongoOperations.aggregate(aggregation, "User", QueryCount.class).getMappedResults();
    }
}
