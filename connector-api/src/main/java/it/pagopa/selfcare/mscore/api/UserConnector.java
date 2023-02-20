package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;
import java.util.List;

public interface UserConnector {

    void deleteById(String id);
    OnboardedUser save(OnboardedUser example);
    OnboardedUser getById(String userId);
    void findAndUpdateState(String userId, String institutionId,  String productId, RelationshipState state);
    void findAndUpdate(String id, String institutionId, OnboardedProduct product, UserBinding bindings);
    List<OnboardedUser> findOnboardedManager(String institutionId, String productId, List<RelationshipState> state);
    List<OnboardedUser> findAdminWithFilter(EnvEnum env, String userId, String institutionId, List<String> adminPartyRole, List<RelationshipState> active);

    List<OnboardedUser> findWithFilter(EnvEnum env, String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);
}
