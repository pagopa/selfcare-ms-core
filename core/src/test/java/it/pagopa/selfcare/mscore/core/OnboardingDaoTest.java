package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class OnboardingDaoTest {

    @Mock
    private InstitutionConnector institutionConnector;
    @Mock
    private TokenConnector tokenConnector;
    @Mock
    private UserConnector userConnector;

    @InjectMocks
    private OnboardingDao onboardingDao;

    UserToOnboard dummyUserToOnboard() {
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        return user;
    }

    @Test
    void rollbackPersistOnboarding() {

        final String institutionId = "institutionId";
        final Onboarding onboarding = new Onboarding();
        onboarding.setProductId("productId");
        final List<UserToOnboard> users = new ArrayList<>();
        UserToOnboard user = dummyUserToOnboard();
        users.add(user);

        onboardingDao.rollbackPersistOnboarding(institutionId, onboarding, users);

        verify(institutionConnector, times(1))
                .findAndRemoveOnboarding(institutionId, onboarding);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING);
        assertNotNull(onboardedUser);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState4() {
        OnboardingDao onboardingDao = new OnboardingDao(null, null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.DELETED);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState5() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.ACTIVE);
        assertNotNull(onboardedUser);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState6() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.SUSPENDED);
        assertNotNull(onboardedUser);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState8() {
        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(new ArrayList<>());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState9() {
        OnboardingDao onboardingDao = new OnboardingDao(null, null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.ACTIVE);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState10() {
        OnboardingDao onboardingDao = new OnboardingDao(null, null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.SUSPENDED);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState11() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class, () -> onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING));
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState12() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.DELETED);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState13() {
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.ACTIVE);
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.SUSPENDED);
        verify(userConnector).findAndUpdateState(any(), any(), any(), any());
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState14() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.TOBEVALIDATED);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState15() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(new ArrayList<>());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState17() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);

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

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState18() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);

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

        OnboardedProduct onboardedProduct1 = new OnboardedProduct();
        onboardedProduct1.setContract("it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct");
        onboardedProduct1.setCreatedAt(null);
        onboardedProduct1.setEnv(Env.DEV);
        onboardedProduct1.setProductId("Product Id");
        onboardedProduct1.setProductRole("it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct");
        onboardedProduct1.setRelationshipId("Relationship Id");
        onboardedProduct1.setRole(PartyRole.DELEGATE);
        onboardedProduct1.setStatus(RelationshipState.ACTIVE);
        onboardedProduct1.setTokenId("ABC123");
        onboardedProduct1.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct1);
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState19() {


        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getRelationshipId()).thenReturn("42");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        verify(onboardedProduct).getRelationshipId();
        verify(onboardedProduct).setContract(any());
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState20() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING));
        verify(onboardedProduct).getStatus();
        verify(onboardedProduct, atLeast(1)).getRelationshipId();
        verify(onboardedProduct).setContract(any());
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState21() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.ACTIVE));
        verify(onboardedProduct).getStatus();
        verify(onboardedProduct, atLeast(1)).getRelationshipId();
        verify(onboardedProduct).setContract(any());
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState22() {

        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.SUSPENDED));
        verify(onboardedProduct).getStatus();
        verify(onboardedProduct, atLeast(1)).getRelationshipId();
        verify(onboardedProduct).setContract(any());
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
    }
}
