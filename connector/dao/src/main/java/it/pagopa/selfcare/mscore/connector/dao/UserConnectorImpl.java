package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.GET_INSTITUTION_MANAGER_NOT_FOUND;

@Slf4j
@Component
public class UserConnectorImpl implements UserConnector {
    private static final String CURRENT_PRODUCT = "current.";
    private static final String CURRENT_PRODUCT_REF = "$[current]";
    private static final String CURRENT_USER_BINDING = "currentUserBinding.";
    private static final String CURRENT_USER_BINDING_REF = "$[currentUserBinding]";
    private final UserRepository repository;

    public UserConnectorImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public OnboardedUser save(OnboardedUser example) {
        final UserEntity entity = convertToUserEntity(example);
        return convertToOnboardedUser(repository.save(entity));
    }

    @Override
    public OnboardedUser getById(String userId) {
        Optional<UserEntity> entityOpt = repository.findById(userId);
        return entityOpt.map(this::convertToOnboardedUser).orElse(null);
    }

    @Override
    public void findAndUpdateState(String userId, String institutionId, String productId, RelationshipState state) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId));
        UpdateDefinition updateDefinition = new Update()
                .set(constructQuery(CURRENT_USER_BINDING_REF, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.status.name()), state)
                .set(constructQuery(CURRENT_USER_BINDING_REF, UserBinding.Fields.products.name(), CURRENT_PRODUCT_REF, OnboardedProduct.Fields.updatedAt.name()), OffsetDateTime.now())
                .filterArray(Criteria.where(CURRENT_USER_BINDING + UserBinding.Fields.institutionId.name()).is(institutionId))
                .filterArray(Criteria.where(CURRENT_PRODUCT + OnboardedProduct.Fields.productId.name()).is(productId));
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, updateDefinition, findAndModifyOptions, UserEntity.class);
    }

    @Override
    public void findAndUpdate(OnboardedUser onboardedUser, String id, String institutionId, OnboardedProduct product, UserBinding bindings) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(id));
        Update update = new Update();
        Optional<UserBinding> opt = onboardedUser.getBindings().stream().filter(userBinding -> institutionId.equalsIgnoreCase(userBinding.getInstitutionId()))
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
    public void findAndCreate(String id, String institutionId, OnboardedProduct product, UserBinding binding) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(id));
        Update update = new Update();
        update.set(UserEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        update.addToSet(UserEntity.Fields.bindings.name(), binding);
        update.set(UserEntity.Fields.createdAt.name(), OffsetDateTime.now());
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true).returnNew(true);
        repository.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

    @Override
    public OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> state) {

        Query query = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.institutionId.name()).is(institutionId)
                        .and(UserBinding.Fields.products.name())
                                .elemMatch(Criteria.where(OnboardedProduct.Fields.productId.name()).is(productId)
                                        .and(OnboardedProduct.Fields.role.name()).is(PartyRole.MANAGER)
                                        .and(OnboardedProduct.Fields.status.name()).in(state))));

        return repository.find(query, UserEntity.class).stream()
                .findFirst()
                .map(this::convertToOnboardedUser)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), institutionId, productId),
                        GET_INSTITUTION_MANAGER_NOT_FOUND.getCode()));
    }

    @Override
    public List<OnboardedUser> findAdminWithFilter(EnvEnum env, String userId, String institutionId, List<PartyRole> roles, List<RelationshipState> states) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId)
                .and(constructQuery(UserBinding.Fields.institutionId.name())).is(institutionId));

        query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name()).in(roles)
                        .and(OnboardedProduct.Fields.env.name()).is(env)
                        .and(OnboardedProduct.Fields.status.name()).in(states)));

        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<OnboardedUser> findWithFilter(EnvEnum env, String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        List<RelationshipState> stateList = states.isEmpty() ? List.of(RelationshipState.values()) : states;
        List<PartyRole> roleList = roles.isEmpty() ? List.of(PartyRole.values()) : roles;

        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(personId)
                .and(constructQuery(UserBinding.Fields.institutionId.name())).is(institutionId));

        if(productRoles.isEmpty() && products.isEmpty()) {
            query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                    .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name()).in(roleList)
                            .and(OnboardedProduct.Fields.env.name()).is(env)
                            .and(OnboardedProduct.Fields.status.name()).in(stateList)));
        }else if(productRoles.isEmpty()){
            query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                    .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name()).in(roleList)
                            .and(OnboardedProduct.Fields.env.name()).is(env)
                            .and(OnboardedProduct.Fields.productId.name()).in(products)
                            .and(OnboardedProduct.Fields.status.name()).in(stateList)));
        }else if(products.isEmpty()){
            query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                    .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name()).in(roleList)
                            .and(OnboardedProduct.Fields.env.name()).is(env)
                            .and(OnboardedProduct.Fields.productRoles.name()).in(productRoles)
                            .and(OnboardedProduct.Fields.status.name()).in(stateList)));
        }else {
            query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                    .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name()).in(roleList)
                            .and(OnboardedProduct.Fields.productRoles.name()).in(productRoles)
                            .and(OnboardedProduct.Fields.productId.name()).in(products)
                            .and(OnboardedProduct.Fields.env.name()).is(env)
                            .and(OnboardedProduct.Fields.status.name()).in(stateList)));
        }

        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToOnboardedUser)
                .collect(Collectors.toList());
    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserEntity.Fields.bindings.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private UserEntity convertToUserEntity(OnboardedUser example) {
        UserEntity user = new UserEntity();
        user.setUpdatedAt(example.getUpdatedAt());
        user.setCreatedAt(example.getCreatedAt());
        user.setBindings(example.getBindings());
        user.setId(example.getId());
        return user;
    }

    private OnboardedUser convertToOnboardedUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setId(entity.getId());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setCreatedAt(entity.getCreatedAt());
        user.setBindings(entity.getBindings());
        return user;
    }
}
