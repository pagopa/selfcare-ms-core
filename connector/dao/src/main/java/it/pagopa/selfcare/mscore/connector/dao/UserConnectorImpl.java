package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.aggregation.UserInstitutionAggregationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;

@Slf4j
@Component
public class UserConnectorImpl implements UserConnector {

    private static final String CURRENT_PRODUCT = "current.";
    private static final String CURRENT_ANY = "$[]";
    private static final String CURRENT_PRODUCT_REF = "$[current]";
    private static final String CURRENT_USER_BINDING = "currentUserBinding.";
    private static final String CURRENT_USER_BINDING_REF = "$[currentUserBinding]";

    private final UserRepository repository;

    public UserConnectorImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public List<OnboardedUser> findAll() {
        return repository.findAll().stream().map(UserMapper::toOnboardedUser).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public OnboardedUser findById(String userId) {
        Optional<UserEntity> entityOpt = repository.findById(userId);
        return entityOpt.map(UserMapper::toOnboardedUser)
                .orElseThrow(() -> {
                    log.error(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId));
                    throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId), USER_NOT_FOUND_ERROR.getCode());
                });
    }

    @Override
    public OnboardedUser save(OnboardedUser example) {
        final UserEntity entity = UserMapper.toUserEntity(example);
        return UserMapper.toOnboardedUser(repository.save(entity));
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
    public void findAndUpdateState(String userId, @Nullable String relationshipId, @Nullable Token token, RelationshipState state) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));
        Update update = new Update()
                .set(constructQuery(CURRENT_ANY, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.status.name()), state)
                .set(constructQuery(CURRENT_ANY, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.updatedAt.name()), OffsetDateTime.now());
        if (relationshipId != null) {
            update.filterArray(Criteria.where(CURRENT_PRODUCT  + OnboardedProduct.Fields.relationshipId.name()).is(relationshipId));
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
                                .and(OnboardedProduct.Fields.env.name()).is(Env.ROOT)
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
                                .and(UserBinding.Fields.products.name())
                                .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name()).in(roles)
                                .and(OnboardedProduct.Fields.env.name()).is(Env.ROOT)
                                .and(OnboardedProduct.Fields.status.name()).in(states))));

        return repository.find(query, UserEntity.class).stream()
                .map(UserMapper::toOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<OnboardedUser> findWithFilter(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        Criteria criteria = Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(constructElemMatch(institutionId, roles, states, productRoles, products));
        if (personId != null) {
            criteria = criteria.and(UserEntity.Fields.id.name()).is(personId);
        }

        return repository.find(new Query(criteria), UserEntity.class).stream()
                .map(UserMapper::toOnboardedUser)
                .collect(Collectors.toList());
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
            updatedUsersBindings.add(UserMapper.toOnboardedUser(repository.findAndModify(query, updateUserEntityUpdatedAt, findAndModifyOptions, UserEntity.class)));
        });

        return updatedUsersBindings;
    }

    @Override
    public List<UserInstitutionAggregation> findUserInstitutionAggregation(UserInstitutionFilter filter){
        return UserMapper.toUserInstitutionAggregation(repository.findUserInstitutionAggregation(filter, UserInstitutionAggregationEntity.class));
    }

    private Criteria constructElemMatch(String institutionId, List<PartyRole> roles, List<RelationshipState> states, List<String> productRoles, List<String> products) {
        return CriteriaBuilder.builder()
                .isIfNotNull(UserBinding.Fields.institutionId.name(), institutionId)
                .build()
                .and(UserBinding.Fields.products.name())
                .elemMatch(constructCriteria(roles, states, productRoles, products));
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
}
