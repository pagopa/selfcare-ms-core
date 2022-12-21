package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.OnboardedUser;

import java.util.List;
import java.util.Optional;

public interface UserConnector {

    List<OnboardedUser> find(OnboardedUser user, List<RelationshipState> validRelationshipStates, String productId);

    OnboardedUser save(OnboardedUser example);

    List<OnboardedUser> findAll();

    List<OnboardedUser> findAll(OnboardedUser example);

    Optional<OnboardedUser> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);

}
