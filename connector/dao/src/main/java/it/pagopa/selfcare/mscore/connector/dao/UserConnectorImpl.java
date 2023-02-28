package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.GET_INSTITUTION_MANAGER_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.RELATIONSHIP_ID_NOT_FOUND;

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
    public void findAndCreate(String id, String institutionId, UserBinding binding) {
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
    public List<OnboardedUser> findAdminWithFilter(String userId, String institutionId, List<PartyRole> roles, List<RelationshipState> states) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(userId)
                .and(constructQuery(UserBinding.Fields.institutionId.name())).is(institutionId));

        query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                .elemMatch(Criteria.where(OnboardedProduct.Fields.role.name()).in(roles)
                        .and(OnboardedProduct.Fields.env.name()).is(EnvEnum.ROOT) //TODO: CAPIRE QUALE ENV CONSIDERARE
                        .and(OnboardedProduct.Fields.status.name()).in(states)));

        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToOnboardedUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<OnboardedUser> findWithFilter(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        List<RelationshipState> stateList = states.isEmpty() ? List.of(RelationshipState.values()) : states;
        List<PartyRole> roleList = roles.isEmpty() ? List.of(PartyRole.values()) : roles;

        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(personId)
                .and(constructQuery(UserBinding.Fields.institutionId.name())).is(institutionId));

        query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                .elemMatch(constructCriteria(roleList, stateList, productRoles, products)));

        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToOnboardedUser)
                .collect(Collectors.toList());
    }

    private Criteria constructCriteria(List<PartyRole> roleList, List<RelationshipState> stateList, List<String> productRoles, List<String> products) {
        Criteria criteria = Criteria.where(OnboardedProduct.Fields.role.name()).in(roleList)
                .and(OnboardedProduct.Fields.status.name()).in(stateList);
        if (!products.isEmpty()) {
            criteria = criteria.and(OnboardedProduct.Fields.productId.name()).in(products);
        }
        if (!productRoles.isEmpty()) {
            criteria = criteria.and(OnboardedProduct.Fields.productRoles.name()).in(productRoles);
        }
        return criteria;
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.bindings.name())
                .elemMatch(Criteria.where(UserBinding.Fields.products.name())
                        .elemMatch(Criteria.where(OnboardedProduct.Fields.relationshipId.name()).in(relationshipId))));

        return repository.find(query, UserEntity.class).stream()
                .findFirst()
                .map(this::convertToOnboardedUser)
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

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserEntity.Fields.bindings.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private OnboardedUser convertToOnboardedUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setId(entity.getId());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setCreatedAt(entity.getCreatedAt());
        user.setBindings(toBindings(entity.getBindings()));
        return user;
    }

    private List<UserBinding> toBindings(List<UserBindingEntity> bindings) {
        List<UserBinding> list = new ArrayList<>();
        for(UserBindingEntity entity : bindings){
            UserBinding binding = new UserBinding();
            binding.setInstitutionId(entity.getInstitutionId());
            binding.setCreatedAt(entity.getCreatedAt());
            binding.setProducts(toOnboardedProduct(entity.getProducts()));
            list.add(binding);
        }
        return list;
    }

    private List<OnboardedProduct> toOnboardedProduct(List<OnboardedProductEntity> products) {
        List<OnboardedProduct> productList = new ArrayList<>();
        for(OnboardedProductEntity entity : products){
            OnboardedProduct product = new OnboardedProduct();
            product.setProductId(entity.getProductId());
            product.setProductRoles(entity.getProductRoles());
            product.setRole(entity.getRole());
            product.setStatus(entity.getStatus());
            product.setEnv(entity.getEnv());
            product.setRelationshipId(entity.getRelationshipId());
            product.setContract(entity.getContract());
            product.setUpdatedAt(entity.getUpdatedAt());
            product.setCreatedAt(entity.getCreatedAt());
            productList.add(product);
        }
        return productList;
    }
}
