package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
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

@Slf4j
@Component
public class UserConnectorImpl implements UserConnector {
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
    public void findAndUpdate(String id, String institutionId, OnboardedProduct product, UserBinding bindings) {
        Query query = Query.query(Criteria.where(UserEntity.Fields.id.name()).is(id));
        UpdateDefinition updateDefinition = new Update()
                .set(UserEntity.Fields.updatedAt.name(), OffsetDateTime.now())
                .addToSet(constructQuery(UserBinding.Fields.institutionId.name()), institutionId)
                .addToSet(constructQuery(CURRENT_USER_BINDING_REF, UserBinding.Fields.products.name()), product)
                .filterArray(Criteria.where(CURRENT_USER_BINDING + UserBinding.Fields.institutionId.name()).is(institutionId))
                .setOnInsert(UserEntity.Fields.bindings.name(), bindings)
                .setOnInsert(UserEntity.Fields.updatedAt.name(), OffsetDateTime.now())
                .setOnInsert(UserEntity.Fields.createdAt.name(), OffsetDateTime.now());

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true).returnNew(true);
        repository.findAndModify(query, updateDefinition, findAndModifyOptions, UserEntity.class);
    }

    @Override
    public List<OnboardedUser> findOnboardedManager(String institutionId, String productId, List<RelationshipState> state) {
        Query query = Query.query(Criteria.where(constructQuery(UserBinding.Fields.institutionId.name())).is(institutionId));

        query.addCriteria(Criteria.where(constructQuery(UserBinding.Fields.products.name()))
                .elemMatch(Criteria.where(OnboardedProduct.Fields.productId.name()).is(productId)
                        .and(OnboardedProduct.Fields.role.name()).is(PartyRole.MANAGER)
                        .and(OnboardedProduct.Fields.status.name()).in(state)));

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
