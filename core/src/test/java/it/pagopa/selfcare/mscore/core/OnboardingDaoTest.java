package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(userConnector, times(1))
                .findAndRemoveProduct(captor.capture(), any(), any());
        assertEquals(user.getId(), captor.getValue());
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
        ProductConnector productConnector = mock(ProductConnector.class);
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
        ProductConnector productConnector = mock(ProductConnector.class);
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
        ProductConnector productConnector = mock(ProductConnector.class);
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

        ProductConnector productConnector = mock(ProductConnector.class);
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

        ProductConnector productConnector = mock(ProductConnector.class);
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
        ProductConnector productConnector = mock(ProductConnector.class);
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
        ProductConnector productConnector = mock(ProductConnector.class);
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
        ProductConnector productConnector = mock(ProductConnector.class);
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

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getRelationshipId()).thenReturn("42");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt(any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole(any());
        doNothing().when(onboardedProduct).setStatus(any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt(any());
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
        verify(onboardedProduct).setCreatedAt(any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole(any());
        verify(onboardedProduct).setStatus(any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt(any());
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState20() {
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt(any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole(any());
        doNothing().when(onboardedProduct).setStatus(any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt(any());
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
        verify(onboardedProduct).setCreatedAt(any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole(any());
        verify(onboardedProduct).setStatus(any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt(any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState21() {

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt(any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole(any());
        doNothing().when(onboardedProduct).setStatus(any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt(any());
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
        verify(onboardedProduct).setCreatedAt(any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole(any());
        verify(onboardedProduct).setStatus(any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt(any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState22() {

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt(any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole(any());
        doNothing().when(onboardedProduct).setStatus(any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt(any());
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
        verify(onboardedProduct).setCreatedAt(any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole(any());
        verify(onboardedProduct).setStatus(any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt(any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator() {
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        assertTrue(onboardingDao.onboardOperator(new Institution(), "productId", List.of(user)).isEmpty());
    }

    @Test
    void testOnboardOperatorAndDeleteDifferentRole() {
        Institution institution = TestUtils.dummyInstitution();
        OnboardedUser onboardedUser = TestUtils.dummyOnboardedUser();
        UserBinding userBinding = TestUtils.dummyUserBinding();
        OnboardedProduct onboardedProduct = TestUtils.dummyOnboardedProduct();
        onboardedProduct.setProductId("productId");
        onboardedProduct.setRole(PartyRole.OPERATOR);
        onboardedProduct.setProductRole("api");
        OnboardedProduct onboardedProduct1 = TestUtils.dummyOnboardedProduct();
        userBinding.setProducts(List.of(onboardedProduct, onboardedProduct1));
        userBinding.setInstitutionId("institutionId");
        onboardedUser.setBindings(List.of(userBinding));
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        user.setRole(PartyRole.DELEGATE);
        user.setProductRole("admin");
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertFalse(onboardingDao.onboardOperator(institution, "productId", List.of(user)).isEmpty());
    }

    @Test
    void testOnboardOperatorAndKeepSameRoleWithDifferentProductRole() {
        Institution institution = TestUtils.dummyInstitution();
        OnboardedUser onboardedUser = TestUtils.dummyOnboardedUser();
        UserBinding userBinding = TestUtils.dummyUserBinding();
        OnboardedProduct onboardedProduct = TestUtils.dummyOnboardedProduct();
        onboardedProduct.setProductId("productId");
        onboardedProduct.setRole(PartyRole.OPERATOR);
        onboardedProduct.setProductRole("api");
        OnboardedProduct onboardedProduct1 = TestUtils.dummyOnboardedProduct();
        userBinding.setProducts(List.of(onboardedProduct, onboardedProduct1));
        userBinding.setInstitutionId("institutionId");
        onboardedUser.setBindings(List.of(userBinding));
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        user.setRole(PartyRole.OPERATOR);
        user.setProductRole("security");
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertFalse(onboardingDao.onboardOperator(institution, "productId", List.of(user)).isEmpty());
    }

    @Test
    void testOnboardOperatorAndDeleteSameRole() {
        Institution institution = TestUtils.dummyInstitution();
        OnboardedUser onboardedUser = TestUtils.dummyOnboardedUser();
        UserBinding userBinding = TestUtils.dummyUserBinding();
        OnboardedProduct onboardedProduct = TestUtils.dummyOnboardedProduct();
        onboardedProduct.setProductId("productId");
        onboardedProduct.setRole(PartyRole.OPERATOR);
        onboardedProduct.setProductRole("api");
        OnboardedProduct onboardedProduct1 = TestUtils.dummyOnboardedProduct();
        userBinding.setProducts(List.of(onboardedProduct, onboardedProduct1));
        userBinding.setInstitutionId("institutionId");
        onboardedUser.setBindings(List.of(userBinding));
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        user.setRole(PartyRole.OPERATOR);
        user.setProductRole("api");
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertFalse(onboardingDao.onboardOperator(institution, "productId", List.of(user)).isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator2() {
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null);
        assertTrue(onboardingDao.onboardOperator(new Institution(), "productId", List.of()).isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator5() {
        OnboardedUser onboardedUser = TestUtils.dummyOnboardedUser();
        UserBinding userBinding = TestUtils.dummyUserBinding();
        OnboardedProduct onboardedProduct = TestUtils.dummyOnboardedProduct();
        onboardedProduct.setProductId("42");
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        UserConnector userConnector = mock(UserConnector.class);
        doNothing().when(userConnector)
                .findAndUpdate(any(), any(), any(), any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, userConnector);

        UserToOnboard userToOnboard = TestUtils.dummyUserToOnboard();

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(institution, "42", userToOnboardList);
        assertEquals(1, actualOnboardOperatorResult.size());
        RelationshipInfo getResult = actualOnboardOperatorResult.get(0);
        assertSame(institution, getResult.getInstitution());
        assertEquals("it.pagopa.selfcare.mscore.model.user.UserToOnboard", getResult.getUserId());
        assertEquals(RelationshipState.ACTIVE, onboardedProduct.getStatus());
        assertEquals(PartyRole.DELEGATE, onboardedProduct.getRole());
        assertEquals("productRole", onboardedProduct.getProductRole());
        assertEquals("42", onboardedProduct.getProductId());
        assertEquals(Env.ROOT, onboardedProduct.getEnv());
        verify(userConnector).findById(any());
        verify(userConnector).findAndUpdate(any(), any(), any(),
                any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator6() {
        OnboardedUser onboardedUser = TestUtils.dummyOnboardedUser();
        UserBinding userBinding = TestUtils.dummyUserBinding();
        OnboardedProduct onboardedProduct = TestUtils.dummyOnboardedProduct();
        onboardedProduct.setProductId("42");
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        UserConnector userConnector = mock(UserConnector.class);
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(), any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);

        
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, userConnector);

        UserToOnboard userToOnboard = TestUtils.dummyUserToOnboard();

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        assertTrue(onboardingDao.onboardOperator(new Institution(), "42", userToOnboardList).isEmpty());
        verify(userConnector).findById(any());
        verify(userConnector, times(2)).findAndRemoveProduct(any(), any(), any());
        verify(userConnector).findAndUpdate(any(), any(), any(),
                any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator7() {
        OnboardedUser onboardedUser = TestUtils.dummyOnboardedUser();
        UserBinding userBinding = TestUtils.dummyUserBinding();
        OnboardedProduct onboardedProduct = TestUtils.dummyOnboardedProduct();
        onboardedProduct.setProductId("42");
        userBinding.setProducts(List.of(onboardedProduct));
        onboardedUser.setBindings(List.of(userBinding));
        UserConnector userConnector = mock(UserConnector.class);
        doThrow(new InvalidRequestException("An error occurred", "can not onboard operators")).when(userConnector)
                .findAndRemoveProduct(any(), any(), any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(), any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, userConnector);

        UserToOnboard userToOnboard = TestUtils.dummyUserToOnboard();

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.onboardOperator(new Institution(), "42", userToOnboardList));
        verify(userConnector).findById(any());
        verify(userConnector).findAndRemoveProduct(any(), any(), any());
        verify(userConnector).findAndUpdate(any(), any(), any(),
                any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator8() {

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(), any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, userConnector);

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(institution, "42", userToOnboardList);
        assertEquals(1, actualOnboardOperatorResult.size());
        RelationshipInfo getResult = actualOnboardOperatorResult.get(0);
        assertSame(institution, getResult.getInstitution());
        assertEquals("it.pagopa.selfcare.mscore.model.user.UserToOnboard", getResult.getUserId());
        OnboardedProduct onboardedProduct = getResult.getOnboardedProduct();
        assertEquals(RelationshipState.ACTIVE, onboardedProduct.getStatus());
        assertEquals(PartyRole.SUB_DELEGATE, onboardedProduct.getRole());
        assertEquals("42", onboardedProduct.getProductRole());
        assertEquals("42", onboardedProduct.getProductId());
        assertEquals(Env.COLL, onboardedProduct.getEnv());
        verify(userConnector).findAndCreate(any(), any());
        verify(userConnector).findById(any());
        verify(onboardedUser).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator9() {
        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "users to update: {}"));
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(), any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, userConnector);

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        assertTrue(onboardingDao.onboardOperator(new Institution(), "42", userToOnboardList).isEmpty());
        verify(userConnector).findAndCreate(any(), any());
        verify(userConnector).findById(any());
        verify(onboardedUser).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator10() {
        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(), any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, userConnector);

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(Env.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName("users to update: {}");
        userToOnboard1.setProductRole("users to update: {}");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("users to update: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);

        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(institution, "42", userToOnboardList);
        assertEquals(2, actualOnboardOperatorResult.size());
        assertEquals("42", actualOnboardOperatorResult.get(0).getUserId());
        RelationshipInfo getResult = actualOnboardOperatorResult.get(1);
        assertEquals("it.pagopa.selfcare.mscore.model.user.UserToOnboard", getResult.getUserId());
        assertSame(institution, getResult.getInstitution());
        OnboardedProduct onboardedProduct = getResult.getOnboardedProduct();
        assertEquals(RelationshipState.ACTIVE, onboardedProduct.getStatus());
        assertEquals(PartyRole.SUB_DELEGATE, onboardedProduct.getRole());
        assertEquals("42", onboardedProduct.getProductRole());
        assertEquals("42", onboardedProduct.getProductId());
        assertEquals(Env.COLL, onboardedProduct.getEnv());
        verify(userConnector, atLeast(1)).findAndCreate(any(), any());
        verify(userConnector, atLeast(1)).findById(any());
        verify(onboardedUser, atLeast(1)).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(Institution, String, List)}
     */
    @Test
    void testOnboardOperator11() {

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenReturn("42");
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(), any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, userConnector);
        UserToOnboard userToOnboard = mock(UserToOnboard.class);
        when(userToOnboard.getRole()).thenReturn(PartyRole.MANAGER);
        when(userToOnboard.getEnv()).thenReturn(Env.ROOT);
        when(userToOnboard.getId()).thenReturn("42");
        when(userToOnboard.getProductRole()).thenReturn("Product Role");
        doNothing().when(userToOnboard).setEmail(any());
        doNothing().when(userToOnboard).setEnv(any());
        doNothing().when(userToOnboard).setId(any());
        doNothing().when(userToOnboard).setName(any());
        doNothing().when(userToOnboard).setProductRole(any());
        doNothing().when(userToOnboard).setRole(any());
        doNothing().when(userToOnboard).setSurname(any());
        doNothing().when(userToOnboard).setTaxCode(any());
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        assertTrue(onboardingDao.onboardOperator(new Institution(), "42", userToOnboardList).isEmpty());
        verify(userConnector).findById(any());
        verify(userConnector).findAndRemoveProduct(any(), any(), any());
        verify(userConnector).findAndUpdate(any(), any(), any(),
                any(), any());
        verify(onboardedUser).getId();
        verify(userToOnboard).getRole();
        verify(userToOnboard, atLeast(1)).getEnv();
        verify(userToOnboard, atLeast(1)).getId();
        verify(userToOnboard).getProductRole();
        verify(userToOnboard).setEmail(any());
        verify(userToOnboard).setEnv(any());
        verify(userToOnboard).setId(any());
        verify(userToOnboard).setName(any());
        verify(userToOnboard).setProductRole(any());
        verify(userToOnboard).setRole(any());
        verify(userToOnboard).setSurname(any());
        verify(userToOnboard).setTaxCode(any());
    }
}
