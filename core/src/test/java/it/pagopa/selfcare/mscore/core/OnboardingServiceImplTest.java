package it.pagopa.selfcare.mscore.core;

import feign.FeignException;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.pagopa.selfcare.mscore.core.util.TestUtils.dummyInstitutionPa;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OnboardingServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class OnboardingServiceImplTest {

    @Mock
    private ContractService contractService;

    @Mock
    private ContractEventNotificationService contractEventNotificationService;

    @Mock
    private UserService userService;
    @Mock
    private MailNotificationService emailService;

    @Mock
    private UserRelationshipService userRelationshipService;

    @Mock
    private OnboardingDao onboardingDao;

    @InjectMocks
    private OnboardingServiceImpl onboardingServiceImpl;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private UserEventService userEventService;

    @Mock
    private InstitutionConnector institutionConnector;

    @Mock
    private ProductConnector productConnector;

    @Mock
    private UserNotificationService userNotificationService;

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo() {
        doNothing().when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        onboardingServiceImpl.verifyOnboardingInfo("42", "42");
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfoSubunit(String, String, String)}
     */
    @Test
    void shouldNothingWhenVerifyOnboardingInfoSubunit() {
        when(institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(any(), any(), any(), any()))
                .thenReturn(true);
        onboardingServiceImpl.verifyOnboardingInfoSubunit("42", "42", "example");
        verify(institutionConnector).existsByTaxCodeAndSubunitCodeAndProductAndStatusList(any(), any(), any(), any());
    }
    @Test
    void VerifyOnboardingInfoOrigin() {
        when(institutionConnector.existsByOrigin(any(), any(), any(), any()))
                .thenReturn(true);
        onboardingServiceImpl.verifyOnboardingInfoOrigin("42", "42", "example");
        verify(institutionConnector).existsByOrigin(any(), any(), any(), any());
    }

    @Test
    void VerifyOnboardingInfoSubunitResourceNotFound() {
        when(institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(any(), any(), any(), any()))
                .thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfoSubunit("42", "42", "example"));

    }
    @Test
    void VerifyOnboardingInfoOriginResourceNotFound() {
        when(institutionConnector.existsByOrigin(any(), any(), any(), any()))
                .thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfoOrigin("42", "42", "example"));

    }

    @Test
    void testVerifyOnboardingInfoByOrigin() {
        // Arrange
        when(institutionConnector.existsByOrigin(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<List<RelationshipState>>any())).thenReturn(true);

        // Act
        onboardingServiceImpl.verifyOnboardingInfoByFilters("Product", "", "", "Origin", "42", "");

        // Assert that nothing has changed
        verify(institutionConnector).existsByOrigin(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<List<RelationshipState>>any());
    }

    @Test
    void testVerifyOnboardingInfoByTaxCode() {
        // Arrange
        when(institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

        // Act
        onboardingServiceImpl.verifyOnboardingInfoByFilters("Product", "", "taxCode", "", "", "subunitCode");

        // Assert that nothing has changed
        verify(institutionConnector).existsByTaxCodeAndSubunitCodeAndProductAndStatusList(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
    }

    @Test
    void testVerifyOnboardingInfoByExternalId() {
        // Arrange
        doNothing().when(institutionService).retrieveInstitutionsWithFilter(Mockito.any(), Mockito.any(), Mockito.any());

        // Act
        onboardingServiceImpl.verifyOnboardingInfoByFilters("Product", "42", "", "", "", "");

        // Assert that nothing has changed
        verify(institutionService).retrieveInstitutionsWithFilter(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void testVerifyOnboardingInfoByFiltersInvalidRequestException() {
        // Arrange
        doNothing().when(institutionService).retrieveInstitutionsWithFilter(Mockito.any(), Mockito.any(), Mockito.any());

        // Assert that nothing has changed
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.verifyOnboardingInfoByFilters("", "", "", "", "", ""));
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo4() {
        doNothing().when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        onboardingServiceImpl.verifyOnboardingInfo("42", "42");
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo5() {
        doThrow(new InvalidRequestException("An error occurred", "Code")).when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo6() {
        doNothing().when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        onboardingServiceImpl.verifyOnboardingInfo("42", "42");
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo7() {
        doThrow(new InvalidRequestException("An error occurred", "Code")).when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo10() {

        Institution institution = dummyInstitutionPa();

        OnboardedProduct onboardedProduct = getOnboardedProduct();

        UserInstitutionBinding userBinding = new UserInstitutionBinding();
        userBinding.setInstitutionId("42");
        userBinding.setProducts(onboardedProduct);

        UserInstitutionAggregation userInstitutionAggregation = new UserInstitutionAggregation();
        userInstitutionAggregation.setId("42");
        userInstitutionAggregation.setBindings(userBinding);
        userInstitutionAggregation.setInstitutions(List.of(institution));
        when(userService.findUserInstitutionAggregation(any())).thenReturn(List.of(userInstitutionAggregation));
        List<OnboardingInfo> actualOnboardingInfo = onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{},
                "42");
        assertEquals(1, actualOnboardingInfo.size());
        OnboardingInfo getResult = actualOnboardingInfo.get(0);
        Institution institution1 = getResult.getInstitution();
        assertSame(institution, institution1);
        List<Onboarding> onboarding1 = institution1.getOnboarding();
        assertTrue(onboarding1.isEmpty());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfoWithTwoParameters() {

        Institution institution = dummyInstitutionPa();

        OnboardedProduct onboardedProduct = getOnboardedProduct();

        UserInstitutionBinding userBinding = new UserInstitutionBinding();
        userBinding.setInstitutionId("42");
        userBinding.setProducts(onboardedProduct);

        UserInstitutionAggregation userInstitutionAggregation = new UserInstitutionAggregation();
        userInstitutionAggregation.setId("42");
        userInstitutionAggregation.setBindings(userBinding);
        userInstitutionAggregation.setInstitutions(List.of(institution));
        when(userService.findUserInstitutionAggregation(any())).thenReturn(List.of(userInstitutionAggregation));
        var actualOnboardingInfo = onboardingServiceImpl.getOnboardingInfo("42", null, null, "42");
        assertEquals(1, actualOnboardingInfo.size());
        OnboardingInfo getResult = actualOnboardingInfo.get(0);
        Institution institution1 = getResult.getInstitution();
        assertSame(institution, institution1);
        List<Onboarding> onboarding1 = institution1.getOnboarding();
        assertTrue(onboarding1.isEmpty());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingUsers(OnboardingUsersRequest, String, String)}
     */
    @Test
    void onboardingUsers_shouldThrowExceptionIfInstitutionNotFound() {
        OnboardingUsersRequest request = new OnboardingUsersRequest();
        request.setInstitutionTaxCode("taxCode");
        when(institutionService.getInstitutions(request.getInstitutionTaxCode(), null)).thenReturn(List.of());
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.onboardingUsers(request, null, null));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingUsers(OnboardingUsersRequest, String, String)}
     */
    @Test
    void onboardingUsers_shouldThrowExceptionIfProductIsNotValid() {
        OnboardingUsersRequest request = new OnboardingUsersRequest();
        request.setInstitutionTaxCode("taxCode");
        request.setProductId("productId");
        when(institutionService.getInstitutions(request.getInstitutionTaxCode(), null)).thenReturn(List.of(new Institution()));
        when(productConnector.getProductValidById(request.getProductId())).thenReturn(null);
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.onboardingUsers(request, null, null));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingUsers(OnboardingUsersRequest, String, String)}
     */
    @Test
    void onboardingUsers_whenUserExistsOnRegistry() {
        OnboardingUsersRequest request = new OnboardingUsersRequest();
        request.setInstitutionTaxCode("taxCode");
        request.setProductId("productId");
        request.setSendCreateUserNotificationEmail(Boolean.TRUE);
        UserToOnboard userToOnboard = createSimpleUserToOnboard();
        request.setUsers(List.of(userToOnboard));

        when(institutionService.getInstitutions(request.getInstitutionTaxCode(), null)).thenReturn(List.of(new Institution()));
        when(productConnector.getProductValidById(request.getProductId())).thenReturn(new Product());
        when(userService.retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode())).thenReturn(new User());
        when(userService.persistWorksContractToUserRegistry(any(), any(), any())).thenReturn(new User());

        onboardingServiceImpl.onboardingUsers(request, null, null);

        verify(userService, times(1))
                .retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode());
        verifyNoMoreInteractions(userService);
    }

    @Test
    void onboardingUsers_whenUserExistsOnRegistryAndSendMailIsFalse() {
        OnboardingUsersRequest request = new OnboardingUsersRequest();
        request.setInstitutionTaxCode("taxCode");
        request.setProductId("productId");
        request.setSendCreateUserNotificationEmail(Boolean.FALSE);
        UserToOnboard userToOnboard = createSimpleUserToOnboard();
        request.setUsers(List.of(userToOnboard));

        when(institutionService.getInstitutions(request.getInstitutionTaxCode(), null)).thenReturn(List.of(new Institution()));
        when(productConnector.getProductValidById(request.getProductId())).thenReturn(new Product());
        when(userService.retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode())).thenReturn(new User());
        when(userService.persistWorksContractToUserRegistry(any(), any(), any())).thenReturn(new User());

        onboardingServiceImpl.onboardingUsers(request, null, null);

        verify(userService, times(1))
                .retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode());
        verifyNoMoreInteractions(userService);
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingUsers(OnboardingUsersRequest, String, String)}
     */
    @Test
    void onboardingUsers_whenUserNotExistsOnRegistry() {
        OnboardingUsersRequest request = new OnboardingUsersRequest();
        request.setInstitutionTaxCode("taxCode");
        request.setProductId("productId");
        request.setSendCreateUserNotificationEmail(Boolean.TRUE);
        UserToOnboard userToOnboard = createSimpleUserToOnboard();
        User user = new User();
        user.setId("42");
        request.setUsers(List.of(userToOnboard));

        Institution institution = new Institution();
        institution.setId("example");
        when(institutionService.getInstitutions(request.getInstitutionTaxCode(), null)).thenReturn(List.of(institution));
        when(productConnector.getProductValidById(request.getProductId())).thenReturn(new Product());
        when(userService.retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode())).thenThrow(FeignException.NotFound.class);
        when(userService.persistUserRegistry(any(), any(), any(), any(),any())).thenReturn(user);

        onboardingServiceImpl.onboardingUsers(request, null, null);

        verify(userService, times(1))
                .retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode());
        verifyNoMoreInteractions(userService);
    }

    private Institution dummyInstitution() {
        Institution institution = new Institution();
        institution.setId("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setProductId("42");
        institution.setOnboarding(List.of(onboarding));
        return institution;
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(),any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    private UserToOnboard createSimpleUserToOnboard() {
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");
        return userToOnboard;
    }



    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators4() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        UserToOnboard userToOnboard = createSimpleUserToOnboard();
        UserToOnboard userToOnboard1 = createSimpleUserToOnboard();

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setProductTitle("productTitle");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators5() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.DELEGATE, "name", "surname");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators8() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators9() {
        when(institutionService.retrieveInstitutionById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome"));
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators10() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setProductTitle("productTitle");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators11() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(Env.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName("Name");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setProductTitle("productTitle");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators12() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.DELEGATE, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators13() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.SUB_DELEGATE, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators14() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.OPERATOR, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators15() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators16() {
        when(institutionService.retrieveInstitutionById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome"));
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators17() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole("Product Role");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setProductTitle("productTitle");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators19() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.DELEGATE, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators20() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.SUB_DELEGATE, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole, String, String)}
     */
    @Test
    void testOnboardingOperators21() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(dummyInstitution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.OPERATOR, "nome", "cognome");
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#retrieveDocument(String)}
     */
    @Test
    void testRetrieveDocument() {
        when(userRelationshipService.retrieveRelationship(any())).thenReturn(new RelationshipInfo());
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.retrieveDocument("42"));
        verify(userRelationshipService).retrieveRelationship(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#retrieveDocument(String)}
     */
    @Test
    void testRetrieveDocument3() {
        Institution institution = new Institution();
        when(userRelationshipService.retrieveRelationship(any()))
                .thenReturn(new RelationshipInfo(institution, "42", new OnboardedProduct()));
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.retrieveDocument("42"));
        verify(userRelationshipService).retrieveRelationship(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#retrieveDocument(String)}
     */
    @Test
    void testRetrieveDocument4(){
        OnboardedProduct onboardedProduct = getOnboardedProduct();
        RelationshipInfo relationshipInfo = mock(RelationshipInfo.class);
        when(relationshipInfo.getOnboardedProduct()).thenReturn(onboardedProduct);
        when(userRelationshipService.retrieveRelationship(any())).thenReturn(relationshipInfo);

        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        when(contractService.getFile(any())).thenReturn(resourceResponse);
        assertSame(resourceResponse, onboardingServiceImpl.retrieveDocument("42"));
        verify(userRelationshipService).retrieveRelationship(any());
        verify(relationshipInfo, atLeast(1)).getOnboardedProduct();
        verify(contractService).getFile(any());
    }

    @Test
    void persistOnboarding_shouldThrowIfOnboardingExists() {

        Onboarding onboarding = dummyOnboarding();
        onboarding.setStatus(UtilEnumList.VALID_RELATIONSHIP_STATES.get(0));

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(List.of(onboarding, dummyOnboarding()));
        when(institutionConnector.findById(institution.getId())).thenReturn(institution);

        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.persistOnboarding(institution.getId(),
                onboarding.getProductId(), List.of(), new Onboarding()));
    }



    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingUsers(OnboardingUsersRequest, String, String)}
     */
    @Test
    void persistOnboarding_shouldRollback() {

        String pricingPlan = "pricingPlan";
        String productId = "productId";
        Onboarding onboarding = dummyOnboarding();
        onboarding.setStatus(UtilEnumList.VALID_RELATIONSHIP_STATES.get(0));

        Onboarding onboardingToPersist = new Onboarding();
        onboardingToPersist.setPricingPlan(pricingPlan);
        onboardingToPersist.setProductId(productId);
        onboardingToPersist.setBilling(new Billing());

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(List.of(onboarding));

        UserToOnboard userToOnboard = createSimpleUserToOnboard();
        User user = new User();
        user.setFiscalCode("fiscalCode");
        user.setId("42");
        final List<UserToOnboard> userToOnboards = List.of(userToOnboard);


        when(institutionConnector.findById(institution.getId())).thenReturn(institution);
        when(institutionConnector.findAndUpdate(any(), any(), any(), any())).thenReturn(institution);

        when(userService.retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode())).thenReturn(dummyUser());
        when(userService.persistWorksContractToUserRegistry(any(), any(), any())).thenThrow(new RuntimeException());

        Assertions.assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.persistOnboarding(institution.getId(), productId, userToOnboards, onboardingToPersist));

        verify(onboardingDao, times(1))
                .rollbackPersistOnboarding(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingUsers(OnboardingUsersRequest, String, String)}
     */
    @Test
    void persistOnboarding_whenUserNotExistsOnRegistry() {

        String pricingPlan = "pricingPlan";
        String productId = "productId";
        Onboarding onboarding = dummyOnboarding();
        onboarding.setStatus(UtilEnumList.VALID_RELATIONSHIP_STATES.get(0));

        Onboarding onboardingToPersist = new Onboarding();
        onboardingToPersist.setPricingPlan(pricingPlan);
        onboardingToPersist.setProductId(productId);
        onboardingToPersist.setBilling(new Billing());

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(List.of(onboarding));

        UserToOnboard userToOnboard = createSimpleUserToOnboard();
        User user = new User();
        user.setFiscalCode("fiscalCode");
        user.setId("42");
        final List<UserToOnboard> userToOnboards = List.of(userToOnboard);


        when(institutionConnector.findById(institution.getId())).thenReturn(institution);
        when(institutionConnector.findAndUpdate(any(), any(), any(), any())).thenReturn(institution);

        when(userService.retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode())).thenReturn(dummyUser());
        when(userService.persistWorksContractToUserRegistry(userToOnboard.getTaxCode(), userToOnboard.getEmail(), institution.getId())).thenReturn(user);

        onboardingServiceImpl.persistOnboarding(institution.getId(), productId, userToOnboards, onboardingToPersist);

        verify(userService, times(1))
                .retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode());

        verify(userService, times(1))
                .persistWorksContractToUserRegistry(userToOnboard.getTaxCode(), userToOnboard.getEmail(), institution.getId());

        verify(onboardingDao, times(1))
                .onboardOperator(any(), any(), any());

        ArgumentCaptor<Onboarding> captor = ArgumentCaptor.forClass(Onboarding.class);
        verify(institutionConnector, times(1))
                .findAndUpdate(any(), captor.capture(), any(), any());
        Onboarding actual = captor.getValue();
        assertEquals(actual.getCreatedAt().getDayOfYear(), LocalDate.now().getDayOfYear());

        verifyNoMoreInteractions(userService);
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingUsers(OnboardingUsersRequest, String, String)}
     */
    @Test
    void persistOnboarding_whenUserExistsOnRegistryButMailIsEmpty() {

        String pricingPlan = "pricingPlan";
        String productId = "productId";
        Onboarding onboarding = dummyOnboarding();
        onboarding.setStatus(UtilEnumList.VALID_RELATIONSHIP_STATES.get(0));

        Onboarding onboardingToPersist = new Onboarding();
        onboardingToPersist.setPricingPlan(pricingPlan);
        onboardingToPersist.setProductId(productId);
        onboardingToPersist.setBilling(new Billing());
        onboardingToPersist.setCreatedAt(LocalDateTime
                .of(2022,1,1,0,0,0)
                .atZone(ZoneOffset.systemDefault())
                .toOffsetDateTime());

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(List.of(onboarding));

        UserToOnboard userToOnboard = createSimpleUserToOnboard();
        userToOnboard.setEmail(null);
        User user = new User();
        user.setFiscalCode("fiscalCode");
        user.setId("42");
        final List<UserToOnboard> userToOnboards = List.of(userToOnboard);

        when(institutionConnector.findById(institution.getId())).thenReturn(institution);
        when(institutionConnector.findAndUpdate(any(), any(), any(), any())).thenReturn(institution);

        when(userService.retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode())).thenReturn(dummyUser());

        onboardingServiceImpl.persistOnboarding(institution.getId(), productId, userToOnboards, onboardingToPersist);

        verify(userService, times(1))
                .retrieveUserFromUserRegistryByFiscalCode(userToOnboard.getTaxCode());

        verify(userService, times(0))
                .persistWorksContractToUserRegistry(any(), any(), any());

        verify(onboardingDao, times(1))
                .onboardOperator(any(), any(), any());

        ArgumentCaptor<Onboarding> captor = ArgumentCaptor.forClass(Onboarding.class);
        verify(institutionConnector, times(1))
                .findAndUpdate(any(), captor.capture(), any(), any());
        Onboarding actual = captor.getValue();
        assertEquals(actual.getCreatedAt(), onboardingToPersist.getCreatedAt());

        verifyNoMoreInteractions(userService);
    }

    private OnboardedProduct getOnboardedProduct() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("START - getUser with id: {}");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);
        return onboardedProduct;
    }

    private OnboardingRequest getOnboardingRequest() {
        Contract contract = new Contract();
        contract.setPath("Contract Template");
        OnboardingRequest expectedRequest = new OnboardingRequest();
        expectedRequest.setProductId("42");
        expectedRequest.setContract(contract);
        expectedRequest.setPricingPlan("C3");
        expectedRequest.setProductName("42");
        expectedRequest.setInstitutionUpdate(new InstitutionUpdate());
        expectedRequest.setBillingRequest(new Billing());
        expectedRequest.setSignContract(true);
        return expectedRequest;
    }

    private Token getToken(InstitutionUpdate institutionUpdate) {
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        return token;
    }


    private User dummyUser() {
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
        return user;
    }

    private Onboarding dummyOnboarding() {
        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(new Billing());
        onboarding.setTokenId("42");
        onboarding.setPricingPlan("C3");
        onboarding.setProductId("42");
        return onboarding;
    }
}

