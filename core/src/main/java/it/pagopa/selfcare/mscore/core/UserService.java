package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.OnboardedUser;

public interface UserService {
    boolean verifyPerson(String id);

    OnboardedUser retrievePerson(String id);


    OnboardedUser createPerson(OnboardedUser onboardedUser);
}
