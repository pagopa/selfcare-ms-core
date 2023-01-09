package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.OnboardedUser;

import java.util.List;

public interface UserConnector {

    List<OnboardedUser> findForVerifyOnboardingInfo(String institutionId, List<RelationshipState> validRelationshipStates, String productId);

    List<OnboardedUser> findForGetOnboardingInfo(String userId, String institutionId, List<RelationshipState> validRelationshipStates);

    OnboardedUser save(OnboardedUser example);

    List<OnboardedUser> findAll();

    List<OnboardedUser> findAll(OnboardedUser example);

    OnboardedUser findById(String id);

    boolean existsById(String id);

    void deleteById(String id);

}
