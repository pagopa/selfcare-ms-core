package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperTest {

    private final UserEntityMapper userMapper = new UserEntityMapperImpl();
    /**
     * Method under test: {@link UserEntityMapper#toOnboardedUser(UserEntity)}
     */
    @Test
    void testToOnboardedUser() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        OnboardedUser actualToOnboardedUserResult = userMapper.toOnboardedUser(userEntity);
        assertEquals("42", actualToOnboardedUserResult.getId());
        assertNull(actualToOnboardedUserResult.getCreatedAt());
    }

    /**
     * Method under test: {@link UserEntityMapper#toOnboardedUser(UserEntity)}
     */
    @Test
    void testToOnboardedUser2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        userEntity.setBindings(null);
        OnboardedUser actualToOnboardedUserResult = userMapper.toOnboardedUser(userEntity);
        assertEquals("42", actualToOnboardedUserResult.getId());
        assertNull(actualToOnboardedUserResult.getCreatedAt());
    }

    /**
     * Method under test: {@link UserEntityMapper#toOnboardedUser(UserEntity)}
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
        OnboardedUser actualToOnboardedUserResult = userMapper.toOnboardedUser(userEntity);
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
     * Method under test: {@link UserEntityMapper#toOnboardedUser(UserEntity)}
     */
    @Test
    void testToOnboardedUser4() {
        List<UserBindingEntity> userBindingEntityList = new ArrayList<>();

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        userEntity.setBindings(userBindingEntityList);
        OnboardedUser actualToOnboardedUserResult = userMapper.toOnboardedUser(userEntity);
        assertTrue(actualToOnboardedUserResult.getBindings().isEmpty());
        assertEquals("42", actualToOnboardedUserResult.getId());
        assertNull(actualToOnboardedUserResult.getCreatedAt());
    }

    /**
     * Method under test: {@link UserEntityMapper#toUserEntity(OnboardedUser)}
     */
    @Test
    void testToUserEntity() {
        assertNull(userMapper.toUserEntity(new OnboardedUser()).getCreatedAt());
    }

    /**
     * Method under test: {@link UserEntityMapper#toUserEntity(OnboardedUser)}
     */
    @Test
    void testToUserEntity2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        onboardedUser.setBindings(userBindingList);
        onboardedUser.setId(null);
        UserEntity actualToUserEntityResult = userMapper.toUserEntity(onboardedUser);
        assertNull(actualToUserEntityResult.getCreatedAt());
    }

    /**
     * Method under test: {@link UserEntityMapper#toUserEntity(OnboardedUser)}
     */
    @Test
    void testToUserEntity3() {
        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(null);

        UserBinding userBinding1 = new UserBinding();
        userBinding1.setProducts(null);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding2 = new UserBinding();
        userBinding2.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());
        userBindingList.add(userBinding);
        userBindingList.add(userBinding1);
        userBindingList.add(userBinding2);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardedUser.setId(null);
        UserEntity actualToUserEntityResult = userMapper.toUserEntity(onboardedUser);
        List<UserBindingEntity> bindings = actualToUserEntityResult.getBindings();
        assertEquals(4, bindings.size());
        assertNull(actualToUserEntityResult.getCreatedAt());
        UserBindingEntity getResult = bindings.get(3);
        assertEquals(1, getResult.getProducts().size());
        assertNull(bindings.get(2).getInstitutionId());
        assertNull(getResult.getInstitutionId());
        assertNull(bindings.get(0).getInstitutionId());
        assertNull(bindings.get(1).getInstitutionId());
    }

    /**
     * Method under test: {@link UserEntityMapper#toUserEntity(OnboardedUser)}
     */
    @Test
    void testToUserEntity4() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(null);
        onboardedUser.setId("foo");
        UserEntity actualToUserEntityResult = userMapper.toUserEntity(onboardedUser);
        assertEquals("foo", actualToUserEntityResult.getId());
        assertNull(actualToUserEntityResult.getCreatedAt());
    }
}

