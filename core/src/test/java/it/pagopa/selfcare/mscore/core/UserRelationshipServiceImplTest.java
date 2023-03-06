package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserRelationshipServiceImplTest {
    @Mock
    private InstitutionService institutionService;

    @Mock
    private OnboardingDao onboardingDao;

    @Mock
    private UserConnector userConnector;

    @InjectMocks
    private UserRelationshipServiceImpl userRelationshipServiceImpl;

    /**
     * Method under test: {@link UserRelationshipServiceImpl#findByRelationshipId(String)}
     */
    @Test
    void testFindByRelationshipId() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userRelationshipServiceImpl.findByRelationshipId("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#findByRelationshipId(String)}
     */
    @Test
    void testFindByRelationshipId2() {
        when(userConnector.findByRelationshipId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.findByRelationshipId("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#activateRelationship(String)}
     */
    @Test
    void testActivateRelationship() {
        doNothing().when(onboardingDao)
                .updateUserProductState(any(), any(), any(),
                        any());
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        userRelationshipServiceImpl.activateRelationship("42");
        verify(onboardingDao).updateUserProductState(any(), any(), any(),
                any());
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#
     *
     *
     * (String)}
     */
    @Test
    void testActivateRelationship2() {
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        InvalidRequestException invalidRequestException = mock(InvalidRequestException.class);
        doThrow(invalidRequestException).when(onboardingDao).updateUserProductState(any(),any(),any(),any());
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.activateRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    @Test
    void testActivateRelationship3() {
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        doNothing().when(onboardingDao).updateUserProductState(any(),any(),any(),any());
        userRelationshipServiceImpl.activateRelationship("42");
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#suspendRelationship(String)}
     */
    @Test
    void testSuspendRelationship() {
        doNothing().when(onboardingDao)
                .updateUserProductState(any(), any(), any(),
                        any());
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        userRelationshipServiceImpl.suspendRelationship("42");
        verify(onboardingDao).updateUserProductState(any(), any(),
                any(), any());
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#suspendRelationship(String)}
     */
    @Test
    void testSuspendRelationship2() {
        when(userConnector.findByRelationshipId(any()))
                .thenReturn(new OnboardedUser());
        InvalidRequestException invalidRequestException = mock(InvalidRequestException.class);
        doThrow(invalidRequestException).when(onboardingDao).updateUserProductState(any(),any(),any(),any());
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.suspendRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#deleteRelationship(String)}
     */
    @Test
    void testDeleteRelationship() {
        doNothing().when(onboardingDao)
                .updateUserProductState(any(), any(), any(),
                        any());
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        userRelationshipServiceImpl.deleteRelationship("42");
        verify(onboardingDao).updateUserProductState(any(), any(),
                any(), any());
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#deleteRelationship(String)}
     */
    @Test
    void testDeleteRelationship2() {
        doNothing().when(onboardingDao)
                .updateUserProductState(any(), any(), any(),
                        any());
        when(userConnector.findByRelationshipId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.deleteRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }


    /**
     * Method under test: {@link UserRelationshipServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship4() {
        when(userConnector.findByRelationshipId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }


    /**
     * Method under test: {@link UserRelationshipServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship6() {
        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(new ArrayList<>());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }


    /**
     * Method under test: {@link UserRelationshipServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship8() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
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
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
        RelationshipInfo actualRetrieveRelationshipResult = userRelationshipServiceImpl.retrieveRelationship("42");
        assertSame(institution, actualRetrieveRelationshipResult.getInstitution());
        assertNull(actualRetrieveRelationshipResult.getUserId());
        assertSame(onboardedProduct, actualRetrieveRelationshipResult.getOnboardedProduct());
        verify(userConnector).findByRelationshipId(any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship9() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRelationshipId("Relationship Id");
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
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserRelationshipServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship10() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
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
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        when(institutionService.retrieveInstitutionById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> userRelationshipServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
        verify(institutionService).retrieveInstitutionById(any());
    }
}

