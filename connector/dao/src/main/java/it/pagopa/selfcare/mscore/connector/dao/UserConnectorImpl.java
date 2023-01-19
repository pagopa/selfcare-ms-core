package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public List<OnboardedUser> findForGetOnboardingInfo(String userId, String institutionId, List<RelationshipState> validRelationshipStates) {
        return null;
    }

    @Override
    public OnboardedUser save(OnboardedUser example) {
        return null;
    }

    @Override
    public List<OnboardedUser> findAll() {
        return null;
    }

    @Override
    public List<OnboardedUser> findAll(OnboardedUser example) {
        return null;
    }

    @Override
    public OnboardedUser findById(String id) {
        return null;
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }

    private String constructQuery(String... variables){
        StringBuilder builder = new StringBuilder();
        builder.append(BINDINGS);
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private OnboardedUser convertToUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setUser(entity.getId().toString());
        user.setBindings(entity.getBindings());
        return user;
    }
}
