package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    /**
     * Method under test: {@link UserMapper#toOnboardedUser(UserEntity)}
     */
    @Test
    void testToOnboardedUser() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        OnboardedUser actualToOnboardedUserResult = UserMapper.toOnboardedUser(userEntity);
        assertEquals("42", actualToOnboardedUserResult.getId());
        assertNull(actualToOnboardedUserResult.getCreatedAt());
    }

    /**
     * Method under test: {@link UserMapper#toOnboardedUser(UserEntity)}
     */
    @Test
    void testToOnboardedUser2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        userEntity.setBindings(null);
        OnboardedUser actualToOnboardedUserResult = UserMapper.toOnboardedUser(userEntity);
        assertEquals("42", actualToOnboardedUserResult.getId());
        assertNull(actualToOnboardedUserResult.getCreatedAt());
    }

    /**
     * Method under test: {@link UserMapper#toOnboardedUser(UserEntity)}
     */
    @Test
    void testToOnboardedUser3() {
        UserBindingEntity userBindingEntity = new UserBindingEntity();
        userBindingEntity.setProducts(null);

        UserBindingEntity userBindingEntity1 = new UserBindingEntity();
        userBindingEntity1.setProducts(null);

        OnboardedProductEntity onboardedProductEntity = new OnboardedProductEntity();
        onboardedProductEntity.setContract("Contract");
        onboardedProductEntity.setCreatedAt(null);
        onboardedProductEntity.setEnv(Env.ROOT);
        onboardedProductEntity.setProductId("42");
        onboardedProductEntity.setProductRole("Product Role");
        onboardedProductEntity.setRelationshipId("42");
        onboardedProductEntity.setRole(PartyRole.MANAGER);
        onboardedProductEntity.setStatus(RelationshipState.PENDING);
        onboardedProductEntity.setTokenId("42");
        onboardedProductEntity.setUpdatedAt(null);

        ArrayList<OnboardedProductEntity> onboardedProductEntityList = new ArrayList<>();
        onboardedProductEntityList.add(onboardedProductEntity);

        UserBindingEntity userBindingEntity2 = new UserBindingEntity();
        userBindingEntity2.setProducts(onboardedProductEntityList);

        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userBindingEntityList.add(new UserBindingEntity());
        userBindingEntityList.add(userBindingEntity);
        userBindingEntityList.add(userBindingEntity1);
        userBindingEntityList.add(userBindingEntity2);

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        userEntity.setBindings(userBindingEntityList);
        OnboardedUser actualToOnboardedUserResult = UserMapper.toOnboardedUser(userEntity);
        List<UserBinding> bindings = actualToOnboardedUserResult.getBindings();
        assertEquals(4, bindings.size());
        assertEquals("42", actualToOnboardedUserResult.getId());
        assertNull(actualToOnboardedUserResult.getCreatedAt());
        UserBinding getResult = bindings.get(3);
        assertEquals(1, getResult.getProducts().size());
        assertNull(bindings.get(0).getInstitutionId());
        assertNull(getResult.getInstitutionId());
        assertNull(bindings.get(1).getInstitutionId());
        assertNull(bindings.get(2).getInstitutionId());
    }

    /**
     * Method under test: {@link UserMapper#toOnboardedUser(UserEntity)}
     */
    @Test
    void testToOnboardedUser4() {
        List<UserBindingEntity> userBindingEntityList = new ArrayList<>();

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        userEntity.setBindings(userBindingEntityList);
        OnboardedUser actualToOnboardedUserResult = UserMapper.toOnboardedUser(userEntity);
        assertTrue(actualToOnboardedUserResult.getBindings().isEmpty());
        assertEquals("42", actualToOnboardedUserResult.getId());
        assertNull(actualToOnboardedUserResult.getCreatedAt());
    }
}

