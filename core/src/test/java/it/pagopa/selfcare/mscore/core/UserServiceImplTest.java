package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.NotificationMapper;
import it.pagopa.selfcare.mscore.core.util.NotificationMapperImpl;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapper;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapperImpl;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRegistryConnector userRegistryConnector;

    @Mock
    private UserConnector userConnector;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Spy
    private NotificationMapper notificationMapper = new NotificationMapperImpl();

    @Spy
    private UserNotificationMapper userNotificationMapper = new UserNotificationMapperImpl();

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
        List<RelationshipState> states = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.findOnboardedManager("42", "42", states));
        verify(userConnector).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    @Test
    void verifyUser() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.verifyUser("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#retrieveUserFromUserRegistry(String)}
     */
    @Test
    void testRetrieveUserFromUserRegistry() {
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
        when(userRegistryConnector.getUserByInternalIdWithFiscalCode( any())).thenReturn(user);
        assertSame(user, userServiceImpl.retrieveUserFromUserRegistry("42"));
        verify(userRegistryConnector).getUserByInternalIdWithFiscalCode( user.getId());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId2() {
        when(userConnector.findById(any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId3() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByUserId}
     */
    @Test
    void testFindByUserId4() {
        when(userConnector.findById(any())).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByUserId("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link UserService#findAllByIds(List)}
     */
    @Test
    void testFindAllByIds() {
        assertTrue(userServiceImpl.findAllByIds(new ArrayList<>()).isEmpty());
    }
    @Test
    void testFindAllExistingByIds_empty() {
        assertTrue(userServiceImpl.findAllExistingByIds(new ArrayList<>()).isEmpty());
    }
    @Test
    void testFindAllExistingByIds_null() {
        assertTrue(userServiceImpl.findAllExistingByIds(null).isEmpty());
    }
    @Test
    void testFindAllExistingByIds() {
        List<OnboardedUser> onboardedUserList = List.of(mockInstance(new OnboardedUser()));
        when(userConnector.findAllByExistingIds(any())).thenReturn(onboardedUserList);

        List<String> stringList = new ArrayList<>();
        stringList.add("foo");
        List<OnboardedUser> actualFindAllByIdsResult = userServiceImpl.findAllExistingByIds(stringList);
        assertSame(onboardedUserList, actualFindAllByIdsResult);
        verify(userConnector).findAllByExistingIds(stringList);
    }

    /**
     * Method under test: {@link UserService#findAllByIds(List)}
     */
    @Test
    void testFindAllByIds2() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findAllByIds(any())).thenReturn(onboardedUserList);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        List<OnboardedUser> actualFindAllByIdsResult = userServiceImpl.findAllByIds(stringList);
        assertSame(onboardedUserList, actualFindAllByIdsResult);
        assertTrue(actualFindAllByIdsResult.isEmpty());
        verify(userConnector).findAllByIds(any());
    }

    @Test
    void retrieveUsers() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findWithFilter(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);
        List<OnboardedUser> actualRetrieveAdminUsersResult = userServiceImpl.retrieveUsers("42", "42", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertSame(onboardedUserList, actualRetrieveAdminUsersResult);
        assertTrue(actualRetrieveAdminUsersResult.isEmpty());
        verify(userConnector).findWithFilter(any(), any(), any(),
                any(), any(), any());
    }

    @Test
    void retrieveBindings_shouldReturnEmpty() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findWithFilter(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);

        List<UserBinding> actualRetrieveAdminUsersResult = userServiceImpl.retrieveBindings("42", "42", null, new ArrayList<>());
        assertTrue(actualRetrieveAdminUsersResult.isEmpty());
    }

    @Test
    void retrieveBindings_shouldReturnData() {
        String institutionId = "institutionId";
        OnboardedUser onboardedUser = dummyOnboardedUser("institutionId");
        when(userConnector.findWithFilter(any(), any(), any(),
                any(), any(), any())).thenReturn(List.of(onboardedUser));

        List<UserBinding> actualRetrieveAdminUsersResult = userServiceImpl.retrieveBindings(onboardedUser.getId(), institutionId, null, new ArrayList<>());
        assertFalse(actualRetrieveAdminUsersResult.isEmpty());
        assertSame(onboardedUser.getBindings().get(0), actualRetrieveAdminUsersResult.get(0));
    }

    @Test
    void retrieveBindings_shouldReturnDataFilteringOnStatus() {
        //Given
        RelationshipState filterStatus = RelationshipState.DELETED;
        UserBinding userBindingDeleted = dummyUserBinding("institutionA");
        OnboardedProduct productDeleted = dummyOnboardedProduct(filterStatus);
        userBindingDeleted.setProducts(List.of(productDeleted));

        OnboardedUser expectedUser = new OnboardedUser();
        expectedUser.setId("id");
        expectedUser.setBindings(List.of(dummyUserBinding("institutionA"), userBindingDeleted));

        when(userConnector.findWithFilter(anyString(), any(), any(),
                any(), any(), any())).thenReturn(List.of(expectedUser));

        //When
        List<UserBinding> actualRetrieveAdminUsersResult = userServiceImpl.retrieveBindings(expectedUser.getId(),
                null, new String[]{filterStatus.name()}, new ArrayList<>());

        //Then
        assertFalse(actualRetrieveAdminUsersResult.isEmpty());
        assertEquals(1, actualRetrieveAdminUsersResult.size());
        UserBinding actualUserBinding = actualRetrieveAdminUsersResult.get(0);
        assertEquals(userBindingDeleted.getInstitutionId(), actualUserBinding.getInstitutionId());



        ArgumentCaptor<List<RelationshipState>> argumentCaptorStates = ArgumentCaptor.forClass(List.class);
        verify(userConnector).findWithFilter(anyString(), any(), any(), argumentCaptorStates.capture(), any(), any());
        assertThat( argumentCaptorStates.getValue().get(0)).isEqualTo(filterStatus);
    }

    private OnboardedProduct dummyOnboardedProduct(RelationshipState state) {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("productId");
        onboardedProduct.setProductRole("admin");
        onboardedProduct.setStatus(state);
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setEnv(Env.PROD);
        onboardedProduct.setContract("contract");
        onboardedProduct.setRelationshipId("setRelationshipId");
        onboardedProduct.setTokenId("setTokenId");
        return onboardedProduct;
    }

    private UserBinding dummyUserBinding(String institutionId) {
        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId(institutionId);
        userBinding.setProducts(List.of(dummyOnboardedProduct(RelationshipState.ACTIVE)));
        return userBinding;
    }

    private OnboardedUser dummyOnboardedUser(String institutionId) {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId("id");
        UserBinding userBinding = dummyUserBinding(institutionId);
        onboardedUser.setBindings(List.of(userBinding));
        return onboardedUser;
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin() {
        when(userConnector.findActiveInstitutionUser(any(), any()
        )).thenReturn(new ArrayList<>());
        assertFalse(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser(any(), any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin2() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(new OnboardedUser());
        when(userConnector.findActiveInstitutionUser(any(), any()
        )).thenReturn(onboardedUserList);
        assertTrue(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser(any(), any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin3() {
        when(userConnector.findActiveInstitutionUser(any(), any()
        )).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser(any(), any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin4() {
        when(userConnector.findActiveInstitutionUser( any(),  any()
        )).thenReturn(new ArrayList<>());
        assertFalse(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser( any(),  any()
        );
    }

    /**
     * Method under test: {@link UserServiceImpl#checkIfInstitutionUser(String, String)}
     */
    @Test
    void testCheckIfAdmin5() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(new OnboardedUser());
        when(userConnector.findActiveInstitutionUser( any(),  any()
        )).thenReturn(onboardedUserList);
        assertTrue(userServiceImpl.checkIfInstitutionUser("42", "42"));
        verify(userConnector).findActiveInstitutionUser( any(),  any()
        );
    }

    @Test
    void findUserInstitutionAggregation() {
        UserInstitutionAggregation userInstitutionAggregation = new UserInstitutionAggregation();
        userInstitutionAggregation.setId("id");
        when(userConnector.findUserInstitutionAggregation(any())).thenReturn(List.of(userInstitutionAggregation));
        UserInstitutionFilter filter = new UserInstitutionFilter();
        filter.setUserId("id");
        Assertions.assertDoesNotThrow(() -> userServiceImpl.findUserInstitutionAggregation(filter));
    }

    @Test
    void findAndUpdateStateByInstitutionAndProduct() {

        doNothing().when(userConnector).findAndUpdateStateByInstitutionAndProduct(anyString(),anyString(),anyString(),any());
        Assertions.assertDoesNotThrow(() -> userServiceImpl
                .findAndUpdateStateByInstitutionAndProduct("userId","institutionId","productId",RelationshipState.DELETED));
    }

    @Test
    void retrievePersonOk(){
        OnboardedUser onboardedUser = new OnboardedUser();
        UserBinding binding = new UserBinding();
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("prod-pn");
        binding.setProducts(List.of(product));
        onboardedUser.setBindings(List.of(binding));
        when(userConnector.findById(any())).thenReturn(onboardedUser);

        User user = new User();
        user.setFiscalCode("taxCode");
        CertifiedField<String> certName = new CertifiedField<>();
        certName.setValue("nome");
        user.setName(certName);
        CertifiedField<String> certSurname = new CertifiedField<>();
        certSurname.setValue("cognome");
        user.setFamilyName(certSurname);
        CertifiedField<String> certMail = new CertifiedField<>();
        certMail.setValue("mail@test.it");
        user.setEmail(certMail);
        when(userRegistryConnector.getUserByInternalIdWithFiscalCode(any())).thenReturn(user);
        Map<String, WorkContact> map = new HashMap<>();
        WorkContact contact = new WorkContact();
        CertifiedField<String> mail = new CertifiedField<>();
        mail.setValue("mail@test.it");
        contact.setEmail(mail);
        map.put("id", contact);
        user.setWorkContacts(map);
        User response = userServiceImpl.retrievePerson("userId","prod-pn", "id");

        Assertions.assertEquals("nome", response.getName());
        Assertions.assertEquals("cognome", response.getFamilyName());
        Assertions.assertEquals("mail@test.it", response.getEmail());
        Assertions.assertEquals("taxCode", response.getFiscalCode());
    }

    @Test
    void retrievePersonOkWithoutProductFilter(){
        OnboardedUser onboardedUser = new OnboardedUser();
        UserBinding binding = new UserBinding();
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("prod-pn");
        binding.setProducts(List.of(product));
        onboardedUser.setBindings(List.of(binding));
        when(userConnector.findById(any())).thenReturn(onboardedUser);

        User user = new User();
        user.setFiscalCode("taxCode");
        CertifiedField<String> certName = new CertifiedField<>();
        certName.setValue("nome");
        user.setName(certName);
        CertifiedField<String> certSurname = new CertifiedField<>();
        certSurname.setValue("cognome");
        user.setFamilyName(certSurname);
        Map<String, WorkContact> map = new HashMap<>();
        WorkContact contact = new WorkContact();
        CertifiedField<String> mail = new CertifiedField<>();
        mail.setValue("mail@test.it");
        contact.setEmail(mail);
        map.put("id", contact);
        user.setWorkContacts(map);
        when(userRegistryConnector.getUserByInternalIdWithFiscalCode(any())).thenReturn(user);

        User response = userServiceImpl.retrievePerson("userId",null, null);

        Assertions.assertEquals("nome", response.getName());
        Assertions.assertEquals("cognome", response.getFamilyName());
        Assertions.assertEquals("taxCode", response.getFiscalCode());
    }

    @Test
    void retrievePersonProductNotFound(){
        OnboardedUser onboardedUser = new OnboardedUser();
        UserBinding binding = new UserBinding();
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("prod-pn");
        binding.setProducts(List.of(product));
        onboardedUser.setBindings(List.of(binding));
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.retrievePerson("userId","prod-io", null));
    }

    @Test
    void retrievePersonNotFound(){
        OnboardedUser onboardedUser = new OnboardedUser();
        UserBinding binding = new UserBinding();
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("prod-pn");
        binding.setProducts(List.of(product));
        onboardedUser.setBindings(List.of(binding));
        when(userConnector.findById(any())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.retrievePerson("userId","prod-io", "id"));
    }

    @Test
    void retrieveUsersFromRegistry() {
        User user = new User();
        user.setId("fiscalCode");
        when(userRegistryConnector.getUserByFiscalCode(any())).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.retrieveUserFromUserRegistryByFiscalCode("fiscalCode"));
        assertEquals("fiscalCode", user.getId());
    }

    @Test
    void persistUsersFromRegistry() {
        User user = new User();
        user.setId("fiscalCode");
        when(userRegistryConnector.persistUserUsingPatch(any(), any(), any(), any(), any())).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.persistUserRegistry("name", "familyName", "fiscalCode", "email", "institutionId"));
    }

    @Test
    void persistWorksContractToUserRegistry() {
        User user = new User();
        user.setId("fiscalCode");
        when(userRegistryConnector.persistUserWorksContractUsingPatch("fiscalCode", "email", "institutionId")).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userServiceImpl.persistWorksContractToUserRegistry( "fiscalCode", "email", "institutionId"));
    }

    @Test
    void getUserInfo() {
        UserInstitutionAggregation userInstitution = new UserInstitutionAggregation();
        userInstitution.setId("userId");
        UserInstitutionBinding binding = new UserInstitutionBinding();
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("prod-pn");
        binding.setProducts(product);
        userInstitution.setBindings(binding);
        Institution institution = new Institution();
        institution.setId("id");
        userInstitution.setInstitutions(List.of(institution));
        when(userConnector.getUserInfo(any(), any(), any())).thenReturn(List.of(userInstitution));
        List<OnboardingInfo> response = userServiceImpl.getUserInfo("userId",null, null);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("userId", response.get(0).getUserId());
        assertNotNull(response.get(0).getBinding());
        assertEquals("prod-pn", response.get(0).getBinding().getProducts().getProductId());
    }

    /**
     * Method under test: {@link UserServiceImpl#findAll(Optional, Optional, String)}
     */
    @Test
    void findAllUsers() {
        OnboardedUser user = new OnboardedUser();
        user.setId("userId");
        UserBinding binding = new UserBinding();
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("prod-pn");
        binding.setProducts(List.of(product));
        binding.setInstitutionId("institutionId");
        user.setBindings(List.of(binding));
        when(userConnector.findAllValidUsers(any(), any(), any())).thenReturn(List.of(user));
        User pdvUser = new User();
        pdvUser.setId("userId");
        pdvUser.setWorkContacts(Map.of("institutionId", new WorkContact()));
        when(userRegistryConnector.getUserByInternalId("userId")).thenReturn(pdvUser);
        List<UserNotificationToSend> response = userServiceImpl.findAll(Optional.empty(),Optional.empty(), "prod-pn");
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertNotNull(response.get(0));
        assertNotNull(response.get(0).getUser());
        assertEquals("userId", response.get(0).getUser().getUserId());
        assertEquals("institutionId", response.get(0).getInstitutionId());
        assertEquals("prod-pn", response.get(0).getProductId());
    }
}

