package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @Mock
    private InstitutionService institutionService;

    @Mock
    private OnboardingDao onboardingDao;

    @Mock
    private UserRegistryConnector userRegistryConnector;

    @Mock
    private UserConnector userConnector;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager3() {
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        List<RelationshipState> states = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findOnboardedManager("42", "42", states));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager4() {
        when(userConnector.findOnboardedManager(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findOnboardedManager("42", "42", new ArrayList<>()));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.getById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByUserId("42"));
        verify(userConnector).getById(any());
    }

    @Test
    void verifyUser() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.getById(any())).thenReturn(onboardedUser);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.verifyUser("42"));
        verify(userConnector).getById(any());
    }
    @Test
    void verifyUser2() {
        when(userConnector.getById(any())).thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.verifyUser("42"));
        verify(userConnector).getById(any());
    }

    @Test
    void suspendRelationship(){
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        Assertions.assertDoesNotThrow(() -> userServiceImpl.suspendRelationship("42"));
    }

    @Test
    void suspendRelationship2(){
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        doThrow(new InvalidRequestException("An error occurred", "Code")).when(onboardingDao)
                .updateUserProductState(any(),any(),any(),any());
        Assertions.assertThrows(InvalidRequestException.class, () -> userServiceImpl.suspendRelationship("42"));
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId2() {
        when(userConnector.getById(any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByUserId("42"));
        verify(userConnector).getById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId(String)}
     */
    @Test
    void testFindByUserId3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.getById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByUserId("42"));
        verify(userConnector).getById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId(String)}
     */
    @Test
    void testFindByUserId4() {
        when(userConnector.getById(any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByUserId("42"));
        verify(userConnector).getById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveAdminUsers(String, String)}
     */
    @Test
    void testRetrieveAdminUsers() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findWithFilter(any(), any(), any(),
                any(),any(),any())).thenReturn(onboardedUserList);
        List<OnboardedUser> actualRetrieveAdminUsersResult = userServiceImpl.retrieveAdminUsers("42", "42");
        assertSame(onboardedUserList, actualRetrieveAdminUsersResult);
        assertTrue(actualRetrieveAdminUsersResult.isEmpty());
        verify(userConnector).findWithFilter(any(), any(), any(),
                any(),any(),any());
    }

    @Test
    void retrieveUsers() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findWithFilter(any(), any(), any(),
                any(),any(),any())).thenReturn(onboardedUserList);
        List<OnboardedUser> actualRetrieveAdminUsersResult = userServiceImpl.retrieveUsers("42", "42", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertSame(onboardedUserList, actualRetrieveAdminUsersResult);
        assertTrue(actualRetrieveAdminUsersResult.isEmpty());
        verify(userConnector).findWithFilter(any(), any(), any(),
                any(),any(),any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveAdminUsers(String, String)}
     */
    @Test
    void testRetrieveAdminUsers2() {
        when(userConnector.findWithFilter(any(), any(), any(),
                any(),any(),any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.retrieveAdminUsers("42", "42"));
        verify(userConnector).findWithFilter(any(), any(), any(),
                any(),any(),any());
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfAdmin(String, String)}
     */
    @Test
    void testCheckIfAdmin() {
        when(userConnector.findAdminWithFilter(any(), any(), any(),
                any())).thenReturn(new ArrayList<>());
        assertFalse(userServiceImpl.checkIfAdmin("42", "42"));
        verify(userConnector).findAdminWithFilter(any(), any(), any(),
                any());
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfAdmin(String, String)}
     */
    @Test
    void testCheckIfAdmin2() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(new OnboardedUser());
        when(userConnector.findAdminWithFilter(any(), any(), any(),
                any())).thenReturn(onboardedUserList);
        assertTrue(userServiceImpl.checkIfAdmin("42", "42"));
        verify(userConnector).findAdminWithFilter(any(), any(), any(),
                any());
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfAdmin(String, String)}
     */
    @Test
    void testCheckIfAdmin3() {
        when(userConnector.findAdminWithFilter(any(), any(), any(),
                any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.checkIfAdmin("42", "42"));
        verify(userConnector).findAdminWithFilter(any(), any(), any(),
                any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByRelationshipId(String)}
     */
    @Test
    void testFindByRelationshipId() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByRelationshipId("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByRelationshipId(String)}
     */
    @Test
    void testFindByRelationshipId2() {
        when(userConnector.findByRelationshipId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByRelationshipId("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#activateRelationship(String)}
     */
    @Test
    void testActivateRelationship() {
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        doNothing().when(onboardingDao)
                .updateUserProductState(any(), any(), any(),
                        any());
        userServiceImpl.activateRelationship("42");
        verify(userConnector).findByRelationshipId(any());
        verify(onboardingDao).updateUserProductState(any(), any(),
                any(), any());
    }


    /**
     * Method under test: {@link UserServiceImpl#activateRelationship(String)}
     */
    @Test
    void testActivateRelationship3() {
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        doThrow(new InvalidRequestException("An error occurred", "Code")).when(onboardingDao)
                .updateUserProductState(any(), any(), any(),
                        any());
        assertThrows(InvalidRequestException.class, () -> userServiceImpl.activateRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
        verify(onboardingDao).updateUserProductState(any(), any(),
                any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteRelationship(String)}
     */
    @Test
    void testDeleteRelationship() {
        when(userConnector.findByRelationshipId(any())).thenReturn(new OnboardedUser());
        doNothing().when(onboardingDao)
                .updateUserProductState(any(), any(), any(),
                        any());
        userServiceImpl.deleteRelationship("42");
        verify(userConnector).findByRelationshipId(any());
        verify(onboardingDao).updateUserProductState(any(), any(),
                any(), any());
    }


    /**
     * Method under test: {@link UserServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        when(userConnector.findByRelationshipId(any())).thenReturn(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> userServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship4() {
        when(userConnector.findByRelationshipId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveRelationship(String)}
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
        assertThrows(InvalidRequestException.class, () -> userServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }


    /**
     * Method under test: {@link UserServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship8() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRoles(new ArrayList<>());
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
        RelationshipInfo actualRetrieveRelationshipResult = userServiceImpl.retrieveRelationship("42");
        assertSame(institution, actualRetrieveRelationshipResult.getInstitution());
        assertNull(actualRetrieveRelationshipResult.getUserId());
        assertSame(onboardedProduct, actualRetrieveRelationshipResult.getOnboardedProduct());
        verify(userConnector).findByRelationshipId(any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship9() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRoles(new ArrayList<>());
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
        assertThrows(InvalidRequestException.class, () -> userServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveRelationship(String)}
     */
    @Test
    void testRetrieveRelationship10() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRoles(new ArrayList<>());
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
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.retrieveRelationship("42"));
        verify(userConnector).findByRelationshipId(any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserFromUserRegistry(String, EnumSet)}
     */
    @Test
    void testGetUserFromUserRegistry() {
        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.NONE);
        certifiedField.setValue("42");

        CertifiedField<String> certifiedField1 = new CertifiedField<>();
        certifiedField1.setCertification(Certification.NONE);
        certifiedField1.setValue("42");

        CertifiedField<String> certifiedField2 = new CertifiedField<>();
        certifiedField2.setCertification(Certification.NONE);
        certifiedField2.setValue("42");

        User user = new User();
        user.setEmail(certifiedField);
        user.setFamilyName(certifiedField1);
        user.setFiscalCode("Fiscal Code");
        user.setId("42");
        user.setName(certifiedField2);
        user.setWorkContacts(new HashMap<>());
        when(userRegistryConnector.getUserByInternalId(any(), any())).thenReturn(user);
        assertSame(user, userServiceImpl.getUserFromUserRegistry("42", null));
        verify(userRegistryConnector).getUserByInternalId(any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserFromUserRegistry(String, EnumSet)}
     */
    @Test
    void testGetUserFromUserRegistry2() {
        when(userRegistryConnector.getUserByInternalId(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUserFromUserRegistry("42", null));
        verify(userRegistryConnector).getUserByInternalId(any(), any());
    }
}

