package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.UserBinding;

import java.util.List;

public interface UserConnector {

    void deleteById(String id);
    OnboardedUser save(OnboardedUser example);
    OnboardedUser getById(String userId);
    void findAndUpdate(OnboardedUser onboardedUser, String id, String institutionId, OnboardedProduct product, UserBinding bindings);
    OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> state);
    void findAndCreate(String id, String institutionId, OnboardedProduct product, UserBinding binding);
}
