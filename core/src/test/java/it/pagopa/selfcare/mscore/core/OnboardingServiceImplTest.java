package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.core.util.MailParametersMapper;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class OnboardingServiceImplTest {

    @Mock
    private ContractService contractService;

    @Mock
    private EmailService emailService;

    @Mock
    private OnboardingDao onboardingDao;

    @InjectMocks
    private OnboardingServiceImpl onboardingServiceImpl;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private UserService userService;


    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OnboardingServiceImpl#OnboardingServiceImpl(OnboardingDao, InstitutionService, UserService, ContractService, EmailService)}
     *   <li>{@link OnboardingServiceImpl#onboardingLegals(OnboardingLegalsRequest, SelfCareUser)}
     * </ul>
     */
    @Test
    void testConstructor() {
        OnboardingDao onboardingDao = new OnboardingDao(mock(InstitutionConnector.class), mock(TokenConnector.class),
                mock(UserConnector.class), mock(ProductConnector.class));

        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserConnector userConnector = mock(UserConnector.class);
        OnboardingDao onboardingDao1 = new OnboardingDao(mock(InstitutionConnector.class), mock(TokenConnector.class),
                mock(UserConnector.class), mock(ProductConnector.class));

        PartyRegistryProxyConnector partyRegistryProxyConnector1 = mock(PartyRegistryProxyConnector.class);
        InstitutionConnector institutionConnector1 = mock(InstitutionConnector.class);
        GeoTaxonomiesConnector geoTaxonomiesConnector1 = mock(GeoTaxonomiesConnector.class);
        InstitutionServiceImpl institutionService = new InstitutionServiceImpl(partyRegistryProxyConnector,
                institutionConnector, geoTaxonomiesConnector,
                new UserServiceImpl(userConnector, onboardingDao1,
                        new InstitutionServiceImpl(partyRegistryProxyConnector1, institutionConnector1, geoTaxonomiesConnector1,
                                new UserServiceImpl(mock(UserConnector.class), mock(OnboardingDao.class),
                                        mock(InstitutionService.class), mock(UserRegistryConnector.class))),
                        mock(UserRegistryConnector.class)));

        UserConnector userConnector1 = mock(UserConnector.class);
        OnboardingDao onboardingDao2 = new OnboardingDao(mock(InstitutionConnector.class), mock(TokenConnector.class),
                mock(UserConnector.class), mock(ProductConnector.class));

        PartyRegistryProxyConnector partyRegistryProxyConnector2 = mock(PartyRegistryProxyConnector.class);
        InstitutionConnector institutionConnector2 = mock(InstitutionConnector.class);
        GeoTaxonomiesConnector geoTaxonomiesConnector2 = mock(GeoTaxonomiesConnector.class);
        UserConnector userConnector2 = mock(UserConnector.class);
        OnboardingDao onboardingDao3 = new OnboardingDao(mock(InstitutionConnector.class), mock(TokenConnector.class),
                mock(UserConnector.class), mock(ProductConnector.class));

        UserServiceImpl userService = new UserServiceImpl(userConnector1, onboardingDao2,
                new InstitutionServiceImpl(partyRegistryProxyConnector2, institutionConnector2, geoTaxonomiesConnector2,
                        new UserServiceImpl(userConnector2, onboardingDao3,
                                new InstitutionServiceImpl(mock(PartyRegistryProxyConnector.class), mock(InstitutionConnector.class),
                                        mock(GeoTaxonomiesConnector.class), mock(UserService.class)),
                                mock(UserRegistryConnector.class))),
                mock(UserRegistryConnector.class));

        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        FileStorageConnector fileStorageConnector = mock(FileStorageConnector.class);
        CoreConfig coreConfig = new CoreConfig();
        Pkcs7HashSignService pkcs7HashSignService = mock(Pkcs7HashSignService.class);
        ContractService contractService = new ContractService(pagoPaSignatureConfig, fileStorageConnector, coreConfig,
                pkcs7HashSignService, new SignatureService());

        EmailConnector emailConnector = mock(EmailConnector.class);
        FileStorageConnector fileStorageConnector1 = mock(FileStorageConnector.class);
        MailParametersMapper mailParametersMapper = new MailParametersMapper();
        OnboardingServiceImpl actualOnboardingServiceImpl = new OnboardingServiceImpl(onboardingDao, institutionService,
                userService, contractService,
                new EmailService(emailConnector, fileStorageConnector1, mailParametersMapper, new CoreConfig()));
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
        onboardingLegalsRequest.setUsers(new ArrayList<>());
        actualOnboardingServiceImpl.onboardingLegals(onboardingLegalsRequest, mock(SelfCareUser.class));
    }

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
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo2() {
        Assertions.assertDoesNotThrow(() -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo3() {
        doThrow(new InvalidRequestException("An error occurred", "Code")).when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }


    @Test
    void testGetOnboardingInfo4() {
        when(userService.findByUserId(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userService).findByUserId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo5() {
        when(userService.findByUserId(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userService).findByUserId(any());
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo7() {
        when(userService.findByUserId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - getUser with id: {}"));
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userService).findByUserId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo9() {
        when(userService.findByUserId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - getUser with id: {}"));
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userService).findByUserId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo10() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", "START - getUser with id: {}",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - getUser with id: {}", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer(), null, null, "START - getUser with id: {}", "START - getUser with id: {}",
                "START - getUser with id: {}", "jane.doe@example.org", "4105551212", true);

        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("START - getUser with id: {}");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        ArrayList<String> stringList = new ArrayList<>();
        onboardedProduct.setProductRoles(stringList);
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setCreatedAt(null);
        userBinding.setInstitutionId("42");
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        when(userService.findByUserId(any())).thenReturn(onboardedUser);
        List<OnboardingInfo> actualOnboardingInfo = onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{},
                "42");
        assertEquals(1, actualOnboardingInfo.size());
        OnboardingInfo getResult = actualOnboardingInfo.get(0);
        Institution institution1 = getResult.getInstitution();
        assertSame(institution, institution1);
        assertEquals(1, getResult.getOnboardedProducts().size());
        List<Onboarding> onboarding1 = institution1.getOnboarding();
        assertTrue(onboarding1.isEmpty());
        verify(institutionService).retrieveInstitutionById(any());
        verify(userService).findByUserId(any());
    }

    @Test
    void testGetOnboardingInfo13() {
        when(institutionService.retrieveInstitutionById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - getUser with id: {}"));

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("START - getUser with id: {}");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRoles(new ArrayList<>());
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setCreatedAt(null);
        userBinding.setInstitutionId("42");
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        when(userService.findByUserId(any())).thenReturn(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionService).retrieveInstitutionById(any());
        verify(userService).findByUserId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo16() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution("42", "42",
                "START - getUser with id: {}", "The characteristics of someone or something", InstitutionType.PA,
                "42 Main St", "42 Main St", "21654", "START - getUser with id: {}", billing, onboarding, geographicTaxonomies,
                attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null, "START - getUser with id: {}",
                "START - getUser with id: {}", "START - getUser with id: {}", "jane.doe@example.org", "4105551212", true));

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("START - getUser with id: {}");
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
        when(userService.findByUserId(any())).thenReturn(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionService).retrieveInstitutionById(any());
        verify(userService).findByUserId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo18() {
        when(institutionService.retrieveInstitutionById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - getUser with id: {}"));

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("START - getUser with id: {}");
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
        when(userService.findByUserId(any())).thenReturn(onboardedUser);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionService).retrieveInstitutionById(any());
        verify(userService).findByUserId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitutionWithProductAlreadyOnboarded() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("address");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("description");
        institutionUpdate.setDigitalAddress("digitalAddress");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("taxCode");
        institutionUpdate.setZipCode("zipCode");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());


        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.ACTIVE);
        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = new Institution();
        institution.setId("id");
        institution.setExternalId("externalId");
        institution.setOnboarding(onboardingList);
        institution.setDescription("description");
        institution.setTaxCode("taxCode");
        institution.setDigitalAddress("digitalAddress");
        institution.setZipCode("zipCode");
        institution.setAddress("address");

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");

        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);

        assertThrows(NullPointerException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser));
    }

    @Test
    void testOnboardingInstitution5() {
        when(institutionService.retrieveInstitutionByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred",
                        "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}"));

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setContractImported(contractImported);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionService).retrieveInstitutionByExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitutionSuccessWhenInstitutionTypeIsPG() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("address");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("description");
        institutionUpdate.setDigitalAddress("digitalAddress");
        institutionUpdate.setGeographicTaxonomyCodes(List.of("code1"));
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("taxCode");
        institutionUpdate.setZipCode("zipCode");

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setId("user");
        userToOnboard.setRole(PartyRole.MANAGER);

        List<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(userToOnboardList);


        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setEnable(true);
        geographicTaxonomies.setDesc("desc");
        geographicTaxonomies.setCode("code");

        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("43");
        onboarding.setStatus(RelationshipState.PENDING);

        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setId("id");
        institution.setExternalId("externalId");
        institution.setInstitutionType(InstitutionType.PG);
        institution.setDescription("description");
        institution.setTaxCode("taxCode");
        institution.setDigitalAddress("digitalAddress");
        institution.setZipCode("zipCode");
        institution.setAddress("address");
        institution.setOnboarding(onboardingList);

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");

        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);

        Token token = new Token();
        token.setId("token");
        when(onboardingDao.persist(any(), any(), any(), any(), any(), any())).thenReturn(new OnboardingRollback());

        onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser);

        verify(institutionService).retrieveInstitutionByExternalId(any());
        verify(onboardingDao).persist(any(), any(), any(), any(), any(), any());
    }

    @Test
    void testInvalidateOnboarding() {
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        onboardingServiceImpl.invalidateOnboarding(new Token());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#invalidateOnboarding(Token)}
     */
    @Test
    void testInvalidateOnboarding2() {
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(institutionService.retrieveInstitutionById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - invalidate token {}"));
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.invalidateOnboarding(new Token()));
        verify(institutionService).retrieveInstitutionById(any());
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingReject(Token)}
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
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(contractService.getLogoFile())
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
        doNothing().when(emailService).sendRejectMail(any(), any(), any());
        onboardingServiceImpl.onboardingReject(new Token());
        verify(onboardingDao).getProductById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(institutionService).retrieveInstitutionById(any());
        verify(contractService).getLogoFile();
        verify(emailService).sendRejectMail(any(), any(), any());
    }

    @Test
    void testOnboardingOperators() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator( any(), any()))
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
        verify(onboardingDao).onboardOperator( any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators2() {
        when(onboardingDao.onboardOperator( any(), any()))
                .thenReturn(new ArrayList<>());
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
    void testOnboardingOperators3() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator( any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
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
        verify(onboardingDao).onboardOperator( any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators4() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator( any(), any()))
                .thenReturn(relationshipInfoList);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(EnvEnum.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("Name");
        userToOnboard.setProductRole(new ArrayList<>());
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("Tax Code");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(EnvEnum.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName("Name");
        userToOnboard1.setProductRole(new ArrayList<>());
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
        verify(onboardingDao).onboardOperator( any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators5() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator( any(), any()))
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
        verify(onboardingDao).onboardOperator( any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators6() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator( any(), any()))
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
        verify(onboardingDao).onboardOperator( any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingOperators(OnboardingOperatorsRequest, PartyRole)}
     */
    @Test
    void testOnboardingOperators7() {
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(onboardingDao.onboardOperator( any(), any()))
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
        verify(onboardingDao).onboardOperator( any(), any());
        verify(institutionService).retrieveInstitutionById(any());
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#retrieveDocument(String)}
     */
    @Test
    void testRetrieveDocument() {
        when(userService.retrieveRelationship(any())).thenReturn(new RelationshipInfo());
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.retrieveDocument("42"));
        verify(userService).retrieveRelationship(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#retrieveDocument(String)}
     */
    @Test
    void testRetrieveDocument3() {
        Institution institution = new Institution();
        when(userService.retrieveRelationship(any()))
                .thenReturn(new RelationshipInfo(institution, "42", new OnboardedProduct()));
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.retrieveDocument("42"));
        verify(userService).retrieveRelationship(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#retrieveDocument(String)}
     */
    @Test
    void testRetrieveDocument4() {
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

        RelationshipInfo relationshipInfo = new RelationshipInfo();
        relationshipInfo.setOnboardedProduct(onboardedProduct);
        when(userService.retrieveRelationship(any())).thenReturn(relationshipInfo);

        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData("AAAAAAAA".getBytes(StandardCharsets.UTF_8));
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        when(contractService.getFile(any())).thenReturn(resourceResponse);
        assertSame(resourceResponse, onboardingServiceImpl.retrieveDocument("42"));
        verify(userService).retrieveRelationship(any());
        verify(contractService).getFile(any());
    }

}

