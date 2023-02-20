package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.*;
import java.util.List;

public interface UserConnector {

    void deleteById(String id);
    OnboardedUser save(OnboardedUser example);
    OnboardedUser getById(String userId);
    void findAndUpdate(String id, String institutionId, OnboardedProduct product, UserBinding bindings);
    List<OnboardedUser> findOnboardedManager(String institutionId, String productId, List<RelationshipState> state);
}
