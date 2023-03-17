package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class TokenMapperTest {
    /**
     * Method under test: {@link TokenMapper#toTokenResponse(TokenRelationships)}
     */
    @Test
    void testToTokenResponse() {
        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum("Checksum");
        tokenRelationships.setInstitutionId("42");
        tokenRelationships.setProductId("42");
        tokenRelationships.setTokenId("42");
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        tokenRelationships.setUsers(onboardedUserList);
        TokenResponse actualToTokenResponseResult = TokenMapper.toTokenResponse(tokenRelationships);
        assertEquals("Checksum", actualToTokenResponseResult.getChecksum());
        assertEquals("42", actualToTokenResponseResult.getId());
    }

    /**
     * Method under test: {@link TokenMapper#toTokenResponse(TokenRelationships)}
     */
    @Test
    void testToTokenResponse3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);

        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum("Checksum");
        tokenRelationships.setInstitutionId("42");
        tokenRelationships.setProductId("42");
        tokenRelationships.setTokenId("42");
        tokenRelationships.setUsers(onboardedUserList);
        TokenResponse actualToTokenResponseResult = TokenMapper.toTokenResponse(tokenRelationships);
        assertEquals("Checksum", actualToTokenResponseResult.getChecksum());
        assertEquals("42", actualToTokenResponseResult.getId());
    }

    /**
     * Method under test: {@link TokenMapper#toTokenResponse(TokenRelationships)}
     */
    @Test
    void testToTokenResponse5() {
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);

        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum("Checksum");
        tokenRelationships.setInstitutionId("42");
        tokenRelationships.setProductId("42");
        tokenRelationships.setTokenId("42");
        tokenRelationships.setUsers(onboardedUserList);
        TokenResponse actualToTokenResponseResult = TokenMapper.toTokenResponse(tokenRelationships);
        assertEquals("Checksum", actualToTokenResponseResult.getChecksum());
        assertTrue(actualToTokenResponseResult.getLegals().isEmpty());
        assertEquals("42", actualToTokenResponseResult.getId());
    }

    /**
     * Method under test: {@link TokenMapper#toTokenResponse(TokenRelationships)}
     */
    @Test
    void testToTokenResponse6() {
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);

        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum("Checksum");
        tokenRelationships.setInstitutionId("42");
        tokenRelationships.setProductId("42");
        tokenRelationships.setTokenId("42");
        tokenRelationships.setUsers(onboardedUserList);
        TokenResponse actualToTokenResponseResult = TokenMapper.toTokenResponse(tokenRelationships);
        assertEquals("Checksum", actualToTokenResponseResult.getChecksum());
        assertTrue(actualToTokenResponseResult.getLegals().isEmpty());
        assertEquals("42", actualToTokenResponseResult.getId());
    }
}

