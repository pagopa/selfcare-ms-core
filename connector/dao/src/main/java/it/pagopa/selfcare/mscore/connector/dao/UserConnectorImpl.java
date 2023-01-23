package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UntypedExampleMatcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserConnectorImpl implements UserConnector {
    private static final String BINDINGS = "bindings";
    private final UserRepository repository;

    public UserConnectorImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(new ObjectId(id));
    }
    @Override
    public List<OnboardedUser> findOnboardedManager(String institutionId, String productId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where(BINDINGS).exists(true),
                        Criteria.where(constructQuery(institutionId)).exists(true),
                        Criteria.where(constructQuery(institutionId,productId)).exists(true)
                                .and(constructQuery(institutionId,productId,"roles")).in(PartyRole.MANAGER)
                                .and(constructQuery(institutionId,productId,"status")).is(RelationshipState.ACTIVE))
        );
        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());

    }

    @Override
    public OnboardedUser save(OnboardedUser example) {
        final UserEntity entity = convertToUserEntity(example);
        return convertToOnboardeUser(repository.save(entity));
    }

    @Override
    public List<OnboardedUser> getByUser(String user) {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser(user);
        Example<UserEntity> example = Example.of(convertToUserEntity(onboardedUser), UntypedExampleMatcher.matching());
        return repository.findAll(example).stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
    }

    private UserEntity convertToUserEntity(OnboardedUser example) {
        UserEntity user = new UserEntity();

        user.setUser(example.getUser());
        user.setCreatedAt(example.getCreatedAt());
        user.setBindings(example.getBindings());
        if(example.getId()!=null) {
            user.setId(new ObjectId(example.getId()));
        }
        return user;
    }

    private OnboardedUser convertToOnboardeUser(UserEntity save) {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser(save.getId().toString());
        onboardedUser.setCreatedAt(save.getCreatedAt());
        onboardedUser.setBindings(save.getBindings());
        return onboardedUser;
    }

    private String constructQuery(String... variables){
        StringBuilder builder = new StringBuilder();
        builder.append(BINDINGS);
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private OnboardedUser convertToUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setId(entity.getId().toString());
        user.setUser(entity.getUser());
        user.setCreatedAt(entity.getCreatedAt());
        user.setBindings(entity.getBindings());
        return user;
    }
}
