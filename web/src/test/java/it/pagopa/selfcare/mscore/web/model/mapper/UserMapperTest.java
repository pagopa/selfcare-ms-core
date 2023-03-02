package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    /**
     * Method under test: {@link UserMapper#toPerson(OnboardedUser)}
     */
    @Test
    void testToPerson() {
        OnboardedUser user = new OnboardedUser();
        user.setId("id");
        assertNotNull(UserMapper.toPerson(user));
    }


}

