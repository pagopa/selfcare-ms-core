package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.constant.*;
import it.pagopa.selfcare.mscore.core.strategy.OnboardingInstitutionStrategy;
import it.pagopa.selfcare.mscore.core.strategy.factory.OnboardingInstitutionStrategyFactory;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OnboardingServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class OnboardingServiceImplTest {

    @Mock
    private ContractService contractService;
    @Mock
    private EmailService emailService;

    @Mock
    private UserRelationshipService userRelationshipService;

    @Mock
    private OnboardingDao onboardingDao;

    @InjectMocks
    private OnboardingServiceImpl onboardingServiceImpl;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private UserService userService;

    @Mock
    private PagoPaSignatureConfig pagoPaSignatureConfig;

    @Mock
    private OnboardingInstitutionStrategyFactory institutionStrategyFactory;

    @Mock
    private InstitutionConnector institutionConnector;

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
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", Origin.SELC.name(), "START - getUser with id: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - getUser with id: {}", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null, "START - getUser with id: {}", "START - getUser with id: {}",
                "true", true, OffsetDateTime.now(), OffsetDateTime.now(), null, null);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("START - getUser with id: {}");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

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
     * Method under test: {@link OnboardingServiceImpl#completeOboarding(Token, MultipartFile)}
     */
    @Test
    void shouldThrowExceptionCompleteOnboarding() {

        Token token = TestUtils.dummyToken();

        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionConnector.findWithFilter(any(), any(), any())).thenReturn(List.of(new Institution()));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.completeOboarding(token,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8)))));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#completeOboarding(Token, MultipartFile)}
     */
    @Test
    void testCompleteOnboarding() {

        Token token = TestUtils.dummyToken();
        token.setInstitutionUpdate(TestUtils.createSimpleInstitutionUpdate());

        when(onboardingDao.persistForUpdate(any(), any(), any(), any())).thenReturn(new OnboardingUpdateRollback());
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionConnector.findWithFilter(any(), any(), any())).thenReturn(List.of());

        Assertions.assertDoesNotThrow(() -> onboardingServiceImpl.completeOboarding(token,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes(StandardCharsets.UTF_8)))));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#approveOnboarding(Token, SelfCareUser)}
     */
    @Test
    void testApproveOnboarding() throws IOException {

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
        User onboardedUser = new User();
        onboardedUser.setEmail(certifiedField);
        onboardedUser.setFamilyName(certifiedField1);
        onboardedUser.setFiscalCode("Fiscal Code3");
        onboardedUser.setId("42");
        onboardedUser.setName(certifiedField2);
        onboardedUser.setWorkContacts(new HashMap<>());
        User manager = new User();
        manager.setEmail(certifiedField);
        manager.setFamilyName(certifiedField1);
        manager.setFiscalCode("Fiscal Code3");
        manager.setId("42");
        manager.setName(certifiedField2);
        manager.setWorkContacts(new HashMap<>());

        List<User> delegate = new ArrayList<>();
        when(userService.retrieveUserFromUserRegistry(any(), any())).thenReturn(user).thenReturn(manager).thenReturn(onboardedUser);
        when(userService.findAllByIds(any())).thenReturn(new ArrayList<>());

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Contract contract = new Contract();
        contract.setPath("Contract Template");
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId("42");
        request.setContract(contract);
        request.setPricingPlan("C3");
        request.setProductName("42");
        request.setInstitutionUpdate(new InstitutionUpdate());
        request.setBillingRequest(new Billing());

        Institution institution = new Institution();
        institution.setBilling(new Billing());
        List<Onboarding> onboardingList = new ArrayList<>();
        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(new Billing());
        onboarding.setTokenId("42");
        onboarding.setPricingPlan("C3");
        onboarding.setProductId("42");
        onboardingList.add(onboarding);
        institution.setOnboarding(onboardingList);
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId("id");
        tokenUser.setRole(PartyRole.MANAGER);
        token.setUsers(List.of(tokenUser));

        List<String> validManagerList = OnboardingInstitutionUtils.getOnboardedValidManager(token);

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");
        File file = File.createTempFile("file", ".txt");
        when(contractService.extractTemplate(any())).thenReturn(token.getContractTemplate());
        when(contractService.createContractPDF(any(), any(), any(), any(), any(), any(), any())).thenReturn(file);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
        Assertions.assertDoesNotThrow(() -> onboardingServiceImpl.approveOnboarding(token, selfCareUser));
        verify(userService, times(1)).retrieveUserFromUserRegistry(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));
        verify(userService, times(1)).findAllByIds(List.of(tokenUser.getUserId()));
        verify(userService, times(1)).retrieveUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));
        verify(userService, times(1)).retrieveUserFromUserRegistry(onboardedUser.getId(), EnumSet.allOf(User.Fields.class));
        verify(institutionService, times(1)).retrieveInstitutionById(token.getInstitutionId());
        verify(contractService, times(1)).extractTemplate(token.getContractTemplate());
        verify(contractService, times(1)).createContractPDF(token.getContractTemplate(), manager, delegate, institution, request, null, null);
        verify(onboardingDao, times(1)).persistForUpdate(token, institution, RelationshipState.PENDING, "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=");
        verifyNoMoreInteractions(onboardingDao);
        verify(emailService, times(1)).sendMail(file, institution, user, request, token.getId(), true, null);
    }

    @Test
    void testApproveOnboarding2() throws IOException {

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
        User delegate = new User();
        delegate.setEmail(certifiedField);
        delegate.setFamilyName(certifiedField1);
        delegate.setFiscalCode("Fiscal Code3");
        delegate.setId("43");
        delegate.setName(certifiedField2);
        delegate.setWorkContacts(new HashMap<>());
        User manager = new User();
        manager.setEmail(certifiedField);
        manager.setFamilyName(certifiedField1);
        manager.setFiscalCode("Fiscal Code3");
        manager.setId("44");
        manager.setName(certifiedField2);
        manager.setWorkContacts(new HashMap<>());
        List<User> delegateList = new ArrayList<>();
        delegateList.add(delegate);
        delegateList.add(delegate);

        OnboardedUser onboardedUser1 = new OnboardedUser();
        OnboardedUser onboardedUser2 = new OnboardedUser();
        onboardedUser1.setId(manager.getId());
        onboardedUser2.setId(delegate.getId());

        when(userService.retrieveUserFromUserRegistry(any(), any())).thenReturn(user).thenReturn(manager).thenReturn(delegate);
        when(userService.findAllByIds(any())).thenReturn(List.of(onboardedUser1, onboardedUser2));

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Contract contract = new Contract();
        contract.setPath("Contract Template");
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId("42");
        request.setContract(contract);
        request.setPricingPlan("C3");
        request.setProductName("42");
        request.setInstitutionUpdate(new InstitutionUpdate());
        request.setBillingRequest(new Billing());

        Institution institution = new Institution();
        institution.setBilling(new Billing());
        List<Onboarding> onboardingList = new ArrayList<>();
        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(new Billing());
        onboarding.setTokenId("42");
        onboarding.setPricingPlan("C3");
        onboarding.setProductId("42");
        onboardingList.add(onboarding);
        institution.setOnboarding(onboardingList);
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId("id");
        tokenUser.setRole(PartyRole.MANAGER);
        token.setUsers(List.of(tokenUser));

        List<String> validManagerList = OnboardingInstitutionUtils.getOnboardedValidManager(token);

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");
        File file = File.createTempFile("file", ".txt");
        when(contractService.extractTemplate(any())).thenReturn(token.getContractTemplate());
        when(contractService.createContractPDF(any(), any(), any(), any(), any(), any(), any())).thenReturn(file);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);

        doThrow(RuntimeException.class).when(emailService).sendMail(any(), any(), any(), any(), any(), anyBoolean(), any());
        Assertions.assertDoesNotThrow(() -> onboardingServiceImpl.approveOnboarding(token, selfCareUser));
        verify(userService, times(1)).retrieveUserFromUserRegistry(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));
        verify(userService, times(1)).findAllByIds(List.of(tokenUser.getUserId()));
        verify(userService, times(1)).retrieveUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));
        verify(userService, times(1)).retrieveUserFromUserRegistry(delegate.getId(), EnumSet.allOf(User.Fields.class));
        verify(institutionService, times(1)).retrieveInstitutionById(token.getInstitutionId());
        verify(contractService, times(1)).extractTemplate(token.getContractTemplate());
        verify(contractService, times(1)).createContractPDF(token.getContractTemplate(), manager, delegateList, institution, request, null, null);
        verify(onboardingDao, times(1)).persistForUpdate(token, institution, RelationshipState.PENDING, "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=");
        verify(onboardingDao, times(1)).rollbackSecondStepOfUpdate((List.of(tokenUser.getUserId())), institution, token);
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#invalidateOnboarding}
     */
    @Test
    void testInvalidateOnboarding() {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        onboardingServiceImpl.invalidateOnboarding(token);
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(institutionService).retrieveInstitutionById(any());
        assertEquals("Checksum", token.getChecksum());
        assertEquals(TokenType.INSTITUTION, token.getType());
        assertEquals(RelationshipState.PENDING, token.getStatus());
        assertEquals("42", token.getProductId());
        assertSame(institutionUpdate, token.getInstitutionUpdate());
        assertEquals("42", token.getInstitutionId());
        assertEquals("Contract Template", token.getContractTemplate());
        assertEquals("Contract Signed", token.getContractSigned());
        assertEquals("42", token.getId());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#invalidateOnboarding}
     */
    @Test
    void testInvalidateOnboarding2() {
        when(institutionService.retrieveInstitutionById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - invalidate token {}"));

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.invalidateOnboarding(token));
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingReject}
     */
    @Test
    void testOnboardingReject() {
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(onboardingDao.getProductById(any())).thenReturn(product);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(contractService.getLogoFile())
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        onboardingServiceImpl.onboardingReject(token);
        verify(onboardingDao).getProductById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(institutionService).retrieveInstitutionById(any());
        verify(contractService).getLogoFile();
        verify(emailService).sendRejectMail(any(), any(), any());
        assertEquals("Checksum", token.getChecksum());
        assertEquals(TokenType.INSTITUTION, token.getType());
        assertEquals(RelationshipState.PENDING, token.getStatus());
        assertEquals("42", token.getProductId());
        assertSame(institutionUpdate, token.getInstitutionUpdate());
        assertEquals("42", token.getInstitutionId());
        assertEquals("Contract Template", token.getContractTemplate());
        assertEquals("Contract Signed", token.getContractSigned());
        assertEquals("42", token.getId());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingReject}
     */
    @Test
    void testOnboardingReject2() {
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        doNothing().when(onboardingDao)
                .rollbackSecondStepOfUpdate(any(), any(), any());
        when(onboardingDao.getProductById(any())).thenReturn(product);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(contractService.getLogoFile())
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
        doThrow(new InvalidRequestException("An error occurred", "START - invalidate token {}")).when(emailService)
                .sendRejectMail(any(), any(), any());

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        onboardingServiceImpl.onboardingReject(token);
        verify(onboardingDao).getProductById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(onboardingDao).rollbackSecondStepOfUpdate(any(), any(), any());
        verify(institutionService).retrieveInstitutionById(any());
        verify(contractService).getLogoFile();
        verify(emailService).sendRejectMail(any(), any(), any());
        assertEquals("Checksum", token.getChecksum());
        assertEquals(TokenType.INSTITUTION, token.getType());
        assertEquals(RelationshipState.PENDING, token.getStatus());
        assertEquals("42", token.getProductId());
        assertSame(institutionUpdate, token.getInstitutionUpdate());
        assertEquals("42", token.getInstitutionId());
        assertEquals("Contract Template", token.getContractTemplate());
        assertEquals("Contract Signed", token.getContractSigned());
        assertEquals("42", token.getId());
    }

    @Test
    void testOnboardingOperators() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators4() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

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
        userToOnboard1.setProductRole("");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators5() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.DELEGATE);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators8() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
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
                () -> onboardingServiceImpl.onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER));
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators10() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

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
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators11() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

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
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators12() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.DELEGATE);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators13() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.SUB_DELEGATE);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators14() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.OPERATOR);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators15() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
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
                () -> onboardingServiceImpl.onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER));
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators17() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

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
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.MANAGER);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators19() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.DELEGATE);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators20() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.SUB_DELEGATE);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators21() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator(any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        List<RelationshipInfo> actualOnboardingOperatorsResult = onboardingServiceImpl
                .onboardingOperators(onboardingOperatorsRequest, PartyRole.OPERATOR);
        assertSame(relationshipInfoList, actualOnboardingOperatorsResult);
        assertTrue(actualOnboardingOperatorsResult.isEmpty());
        verify(onboardingDao).onboardOperator(any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingLegals(OnboardingLegalsRequest, SelfCareUser, Token)}
     */
    @Test
    void testOnboardingLegals() throws IOException {
        Institution institution = new Institution();
        institution.setInstitutionType(InstitutionType.PA);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
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
        when(userService.retrieveUserFromUserRegistry(any(),  any())).thenReturn(user);

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        OnboardingLegalsRequest onboardingLegalsRequest = new OnboardingLegalsRequest();
        onboardingLegalsRequest.setContract(contract);
        onboardingLegalsRequest.setInstitutionExternalId("42");
        onboardingLegalsRequest.setInstitutionId("42");
        onboardingLegalsRequest.setProductId("42");
        onboardingLegalsRequest.setProductName("Product Name");
        onboardingLegalsRequest.setSignContract(true);
        onboardingLegalsRequest.setTokenType(TokenType.INSTITUTION);
        onboardingLegalsRequest.setUsers(new ArrayList<>());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId("id");
        tokenUser.setRole(PartyRole.MANAGER);
        token.setUsers(List.of(tokenUser));

        File file = File.createTempFile("test",".txt");
        when(contractService.createContractPDF(any(), any(), any(), any(), any(), any(), any())).thenReturn(file);

        OnboardingRollback onboardingRollback = new OnboardingRollback();
        onboardingRollback.setToken(new Token());
        when(onboardingDao.persistLegals(any(), any(), any(), any(), any())).thenReturn(onboardingRollback);
        Assertions.assertDoesNotThrow(() -> onboardingServiceImpl.onboardingLegals(onboardingLegalsRequest, selfCareUser, token));
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

    /**
     * Method under test: {@link OnboardingServiceImpl#checkAndHandleExpiring}
     */
    @Test
    void testCheckAndHandleExpiring() {
        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        onboardingServiceImpl.checkAndHandleExpiring(token);
        assertEquals("Checksum", token.getChecksum());
        assertEquals(TokenType.INSTITUTION, token.getType());
        assertEquals(RelationshipState.PENDING, token.getStatus());
        assertEquals("42", token.getProductId());
        assertSame(institutionUpdate, token.getInstitutionUpdate());
        assertEquals("42", token.getInstitutionId());
        assertEquals("Contract Template", token.getContractTemplate());
        assertEquals("Contract Signed", token.getContractSigned());
        assertEquals("42", token.getId());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#checkAndHandleExpiring}
     */
    @Test
    void testCheckAndHandleExpiring2() {
        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();

        Token token = mock(Token.class);
        when(token.getExpiringDate()).thenReturn(null);
        doNothing().when(token).setChecksum(any());
        doNothing().when(token).setClosedAt(any());
        doNothing().when(token).setContractSigned(any());
        doNothing().when(token).setContractTemplate(any());
        doNothing().when(token).setCreatedAt(any());
        doNothing().when(token).setExpiringDate(any());
        doNothing().when(token).setId(any());
        doNothing().when(token).setInstitutionId(any());
        doNothing().when(token).setInstitutionUpdate(any());
        doNothing().when(token).setProductId(any());
        doNothing().when(token).setStatus(any());
        doNothing().when(token).setType(any());
        doNothing().when(token).setUpdatedAt(any());
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        onboardingServiceImpl.checkAndHandleExpiring(token);
        verify(token).getExpiringDate();
        verify(token).setChecksum(any());
        verify(token).setClosedAt(any());
        verify(token).setContractSigned(any());
        verify(token).setContractTemplate(any());
        verify(token).setCreatedAt(any());
        verify(token).setExpiringDate(any());
        verify(token).setId(any());
        verify(token).setInstitutionId(any());
        verify(token).setInstitutionUpdate(any());
        verify(token).setProductId(any());
        verify(token).setStatus(any());
        verify(token).setType(any());
        verify(token).setUpdatedAt(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#checkAndHandleExpiring}
     */
    @Test
    void testCheckAndHandleExpiring_expired() {
        // Given
        List<TokenUser> users = new ArrayList<>();
        users.add(new TokenUser("UserId1", PartyRole.MANAGER));
        users.add(new TokenUser("UserId2", PartyRole.DELEGATE));
        users.add(new TokenUser("UserId3", PartyRole.DELEGATE));

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setDescription("InstitutionUpdateDescription");
        institutionUpdate.setDigitalAddress("email@example.com");
        institutionUpdate.setAddress("InstitutionUpdateAddress");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        institutionUpdate.setGeographicTaxonomies(List.of(new InstitutionGeographicTaxonomies("100", "Italia")));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setImported(false);

        Token token = new Token();
        token.setId("TokenId");
        token.setType(TokenType.INSTITUTION);
        token.setStatus(RelationshipState.PENDING);
        token.setInstitutionId("InstitutionId");
        token.setProductId("ProductId");
        token.setExpiringDate(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        token.setChecksum("Checksum");
        token.setContractVersion("1.0.1");
        token.setContractSigned("ContractSigned");
        token.setContractTemplate("ContractTemplate");
        token.setUsers(users);
        token.setInstitutionUpdate(institutionUpdate);
        token.setCreatedAt(token.getExpiringDate().minusDays(150));
        token.setUpdatedAt(null);
        token.setClosedAt(null);

        Institution institution = new Institution();
        institution.setId("InstitutionId");
        institution.setDescription("InstitutionUpdateDescription");
        institution.setDigitalAddress("email@example.com");
        institution.setAddress("InstitutionUpdateAddress");
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        institution.setGeographicTaxonomies(List.of(new InstitutionGeographicTaxonomies("100", "Italia")));
        institution.setRea("Rea");
        institution.setShareCapital("Share Capital");
        institution.setBusinessRegisterPlace("Business Register Place");
        institution.setSupportEmail("jane.doe@example.org");
        institution.setSupportPhone("6625550144");
        institution.setImported(false);

        when(institutionService.retrieveInstitutionById(any()))
                .thenReturn(institution);
        when(onboardingDao.persistForUpdate(any(), any(), any(), any()))
                .thenReturn(null);

        // When
        Executable executable = () -> onboardingServiceImpl.checkAndHandleExpiring(token);

        // Then
        InvalidRequestException e = assertThrows(InvalidRequestException.class, executable);
        assertEquals(String.format(CustomError.TOKEN_EXPIRED.getMessage(), token.getId(), token.getExpiringDate()), e.getMessage());
        verify(institutionService, times(1))
                .retrieveInstitutionById(token.getInstitutionId());
        verify(onboardingDao, times(1))
                .persistForUpdate(token, institution, RelationshipState.REJECTED, null);
        verifyNoMoreInteractions(institutionService, onboardingDao);
        verifyNoInteractions(contractService, emailService, userRelationshipService, userService);
    }

    @Test
    void shouldOnboardingInstitution() {
        OnboardingInstitutionStrategy mockInstitutionStrategy = mock(OnboardingInstitutionStrategy.class);
        when(institutionStrategyFactory.retrieveOnboardingInstitutionStrategy(any()))
                .thenReturn(mockInstitutionStrategy);
        doNothing().when(mockInstitutionStrategy).onboardingInstitution(any(),any());

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setInstitutionUpdate(TestUtils.createSimpleInstitutionUpdate());

        assertDoesNotThrow(() -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }

    @Test
    void shouldOnboardingInstitutionComplete() {
        OnboardingInstitutionStrategy mockInstitutionStrategy = mock(OnboardingInstitutionStrategy.class);
        when(institutionStrategyFactory.retrieveOnboardingInstitutionStrategyWithoutContractAndComplete(any()))
                .thenReturn(mockInstitutionStrategy);
        doNothing().when(mockInstitutionStrategy).onboardingInstitution(any(),any());

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setInstitutionUpdate(TestUtils.createSimpleInstitutionUpdate());

        assertDoesNotThrow(() -> onboardingServiceImpl.onboardingInstitutionComplete(onboardingRequest, mock(SelfCareUser.class)));
    }

}

