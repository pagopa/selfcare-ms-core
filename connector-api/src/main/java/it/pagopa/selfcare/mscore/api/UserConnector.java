package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.User;

import java.util.List;
import java.util.Optional;

public interface UserConnector {

    List<User> find(User user, List<RelationshipState> validRelationshipStates, String productId);

    User save(User example);

    List<User> findAll();

    List<User> findAll(User example);

    Optional<User> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);

}
