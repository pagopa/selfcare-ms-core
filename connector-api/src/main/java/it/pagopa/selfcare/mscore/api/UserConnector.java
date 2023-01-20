package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.OnboardedUser;

import java.util.List;

public interface UserConnector {

    List<OnboardedUser> findOnboardedManager(String institutionId, String productId);

    void deleteById(String id);
    OnboardedUser save(OnboardedUser example);

    List<OnboardedUser> getByUser(String user);
}
