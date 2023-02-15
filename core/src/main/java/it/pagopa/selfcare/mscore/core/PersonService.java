package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardedUser;

public interface PersonService {
    OnboardedUser findByUserId(String id);
    OnboardedUser createUser(OnboardedUser onboardedUser);

}
