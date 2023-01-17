package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
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
                        Criteria.where(BINDINGS + "." + institutionId).exists(true)
                               /* .elemMatch(Criteria.where(BINDINGS+"."+institutionId + "." + productId).exists(true)
                                        .and(BINDINGS+"."+institutionId + "." + productId+".roles").in(List.of(PartyRole.MANAGER))
                                        .and(BINDINGS+"."+institutionId + "." + productId+".status").is(RelationshipState.ACTIVE))*/
                )
        );
        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());

    }

    private OnboardedUser convertToUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setUser(entity.getId().toString());
        user.setBindings(entity.getBindings());
        return user;
    }
}
