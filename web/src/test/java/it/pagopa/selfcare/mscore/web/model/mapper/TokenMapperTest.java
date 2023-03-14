package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("42");
        List<OnboardedProduct> onboardedProductList = new ArrayList<>();
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("42");
        onboardedProductList.add(onboardedProduct);
        userBinding.setProducts(onboardedProductList);
        userBindingList.add(userBinding);
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId("42");
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

}

