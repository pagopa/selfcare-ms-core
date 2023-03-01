package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;

import java.util.List;

public interface UserConnector {

    void deleteById(String id);

    OnboardedUser findById(String userId);

    void findAndUpdateState(String userId, String institutionId,  String productId, RelationshipState state);

    void findAndUpdate(OnboardedUser onboardedUser, String id, String institutionId, OnboardedProduct product, UserBinding bindings);

    OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> state);

    OnboardedUser findAndCreate(String id, UserBinding binding);

    List<OnboardedUser> findAdminWithFilter(String userId, String institutionId, List<PartyRole> adminPartyRole, List<RelationshipState> active);

    List<OnboardedUser> findWithFilter(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    OnboardedUser findByRelationshipId(String relationshipId);

    void findAndRemoveProduct(String userId, String institutionId, OnboardedProduct product);

    List<OnboardedUser> findAllByIds(List<String> users);
}
