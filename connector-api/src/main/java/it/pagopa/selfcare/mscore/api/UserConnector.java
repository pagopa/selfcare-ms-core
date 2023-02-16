package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.OnboardedUser;

import java.util.List;

public interface UserConnector {

    List<OnboardedUser> findOnboardedManager(String institutionId, String productId);
    List<OnboardedUser> findRelationship(String institutionId, String userId, List<String> roles, List<String> states);
    void deleteById(String id);
    OnboardedUser save(OnboardedUser example);

    List<OnboardedUser> getByUser(String user);

    OnboardedUser getById(String id);
}
