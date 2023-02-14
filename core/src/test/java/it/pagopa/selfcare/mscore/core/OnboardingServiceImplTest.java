package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;

import java.io.File;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {OnboardingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class OnboardingServiceImplTest {
    @MockBean
    private ContractService contractService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private GeoTaxonomiesConnector geoTaxonomiesConnector;

    @MockBean
    private OnboardingDao onboardingDao;

    @Autowired
    private OnboardingServiceImpl onboardingServiceImpl;

    @MockBean
    private UserRegistryConnector userRegistryConnector;

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo() {
        when(onboardingDao.findInstitutionWithFilter(any(), any(), any()))
                .thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(onboardingDao).findInstitutionWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo2() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        when(onboardingDao.findInstitutionWithFilter(any(), any(), any()))
                .thenReturn(institutionList);
        onboardingServiceImpl.verifyOnboardingInfo("42", "42");
        verify(onboardingDao).findInstitutionWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("START - getUser with id: {}");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setId("product1");
        onboardedProduct.setName("START - getUser with id: {}");
        onboardedProduct.setRoles(new ArrayList<>());
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        HashMap<String, OnboardedProduct> stringOnboardedProductMap = new HashMap<>();
        stringOnboardedProductMap.put("product1", onboardedProduct);

        HashMap<String, Map<String, OnboardedProduct>> institutionMap = new HashMap<>();
        institutionMap.put("institution1", stringOnboardedProductMap);
        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getBindings()).thenReturn(institutionMap);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);

        Institution institution = new Institution();
        institution.setId("institution1");
        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("product1");
        onboarding.setStatus(RelationshipState.PENDING);
        institution.setOnboarding(List.of(onboarding));

        when(onboardingDao.findInstitutionById(any())).thenReturn(institution);
        when(onboardingDao.getUserByUser(any())).thenReturn(onboardedUserList);

        List<OnboardingInfo> result = onboardingServiceImpl.getOnboardingInfo("institution1", "", new String[]{}, "42");
        assertEquals(1, result.size());
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).getUserByUser(any());
        verify(onboardedUser).getBindings();
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

        when(onboardingDao.findInstitutionByExternalId(any())).thenReturn(institution);

        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser));
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

        when(onboardingDao.findInstitutionByExternalId(any())).thenReturn(institution);
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);

        Token token = new Token();
        token.setId("token");
        when(onboardingDao.persist(any(), any(), any(), any(), any(), any())).thenReturn("token");

        onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser);

        verify(onboardingDao).findInstitutionByExternalId(any());
        verify(geoTaxonomiesConnector).getExtByCode(any());
        verify(onboardingDao).persist(any(), any(), any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitutionSuccessWhenInstitutionTypeIsPA() throws IOException {
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
        institution.setInstitutionType(InstitutionType.PA);
        institution.setDescription("description");
        institution.setTaxCode("taxCode");
        institution.setDigitalAddress("digitalAddress");
        institution.setZipCode("zipCode");
        institution.setAddress("address");
        institution.setOnboarding(onboardingList);

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");

        when(onboardingDao.findInstitutionByExternalId(any())).thenReturn(institution);
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);

        when(contractService.extractTemplate(any())).thenReturn("template");
        File pdf = File.createTempFile("pdf","pdf");
        when(contractService.createContractPDF(any(), any(), any(), any(), any(), any())).thenReturn(pdf);
        when(onboardingDao.persist(any(), any(), any(), any(), any(), any())).thenReturn("token");
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser);

        verify(onboardingDao).findInstitutionByExternalId(any());
        verify(geoTaxonomiesConnector).getExtByCode(any());
        verify(onboardingDao).persist(any(), any(), any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#completeOboarding(Token, MultipartFile)}
     */
    @Test
    void testCompleteOboarding() {

        Token token = new Token();
        token.setInstitutionId("institutionId");
        token.setProductId("productId");
        token.setUsers(List.of("user1"));

        Map<String, Map<String, OnboardedProduct>> bindings = new HashMap<>();
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setId("productId");
        onboardedProduct.setRoles(List.of("MANAGER"));
        productMap.put("productId", onboardedProduct);
        bindings.put("institutionId", productMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId("user1");
        onboardedUser.setBindings(bindings);

        User user = new User();
        user.setId("user1");

        Institution institution = new Institution();
        institution.setId("institutionId");

        Product product = new Product();
        product.setId("productId");

        when(onboardingDao.getUserById(any())).thenReturn(onboardedUser);
        when(userRegistryConnector.getUserByInternalId(any(), any())).thenReturn(user);
        when(onboardingDao.findInstitutionById(any())).thenReturn(institution);
        when(onboardingDao.getProductById(any())).thenReturn(product);
        onboardingServiceImpl.completeOboarding(token, new MockMultipartFile("Name", "AAAAAAAA".getBytes(StandardCharsets.UTF_8)));
        verify(onboardingDao).getUserById(any());
        verify(userRegistryConnector).getUserByInternalId(any(), any());
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
        user.setId("user1");
        user.setName(certifiedField2);
        user.setWorkContacts(new HashMap<>());

        Token token = new Token();
        token.setInstitutionId("institutionId");
        token.setProductId("productId");
        token.setUsers(List.of("user1"));
        token.setContract("contract");

        Map<String, Map<String, OnboardedProduct>> bindings = new HashMap<>();
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setId("productId");
        onboardedProduct.setRoles(List.of("MANAGER"));
        productMap.put("productId", onboardedProduct);
        bindings.put("institutionId", productMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId("user1");
        onboardedUser.setBindings(bindings);
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setDescription("description");

        Product product = new Product();
        product.setId("productId");

        when(onboardingDao.getUserById(any())).thenReturn(onboardedUser);
        when(userRegistryConnector.getUserByInternalId(any(), any())).thenReturn(user);
        when(onboardingDao.findInstitutionById(any())).thenReturn(institution);
        when(onboardingDao.getProductById(any())).thenReturn(product);
        when(contractService.extractTemplate(any())).thenReturn("template");
        when(contractService.createContractPDF(any(), any(), any(), any(), any(), any())).thenReturn(File.createTempFile("pdf","pdf"));

        assertDoesNotThrow(() -> onboardingServiceImpl.approveOnboarding(token, selfCareUser));

        verify(onboardingDao).getUserById(any());
        verify(onboardingDao).findInstitutionById(any());
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#invalidateOnboarding(Token)}
     */
    @Test
    void testInvalidateOnboarding3() {
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getUsers()).thenReturn(new ArrayList<>());
        onboardingServiceImpl.invalidateOnboarding(token);
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#invalidateOnboarding(Token)}
     */
    @Test
    void testInvalidateOnboarding4() {
        when(onboardingDao.getUserById(any())).thenReturn(new OnboardedUser());
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("START - invalidate token {}");
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getUsers()).thenReturn(stringList);
        onboardingServiceImpl.invalidateOnboarding(token);
        verify(onboardingDao).getUserById(any());
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#invalidateOnboarding(Token)}
     */
    @Test
    void testInvalidateOnboarding5() {
        when(onboardingDao.getUserById(any())).thenReturn(null);
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("START - invalidate token {}");
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getUsers()).thenReturn(stringList);
        onboardingServiceImpl.invalidateOnboarding(token);
        verify(onboardingDao).getUserById(any());
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#invalidateOnboarding(Token)}
     */
    @Test
    void testInvalidateOnboarding6() {
        when(onboardingDao.getUserById(any())).thenReturn(new OnboardedUser());
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("START - invalidate token {}");
        stringList.add("START - invalidate token {}");
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getUsers()).thenReturn(stringList);
        onboardingServiceImpl.invalidateOnboarding(token);
        verify(onboardingDao, atLeast(1)).getUserById(any());
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).persistForUpdate(any(), any(),any(), any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingReject(Token)}
     */
    @Test
    void testOnboardingReject3() {
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
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());
        when(contractService.getLogoFile())
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
        doNothing().when(emailService).sendRejectMail(any(), any(), any());
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getProductId()).thenReturn("42");
        when(token.getUsers()).thenReturn(new ArrayList<>());
        onboardingServiceImpl.onboardingReject(token);
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).getProductById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(contractService).getLogoFile();
        verify(emailService).sendRejectMail(any(), any(), any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getProductId();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingReject(Token)}
     */
    @Test
    void testOnboardingReject4() {
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(onboardingDao.getUserById(any())).thenReturn(new OnboardedUser());
        when(onboardingDao.getProductById(any())).thenReturn(product);
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());
        when(contractService.getLogoFile())
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
        doNothing().when(emailService).sendRejectMail(any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("START - invalidate token {}");
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getProductId()).thenReturn("42");
        when(token.getUsers()).thenReturn(stringList);
        onboardingServiceImpl.onboardingReject(token);
        verify(onboardingDao).getUserById(any());
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).getProductById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(contractService).getLogoFile();
        verify(emailService).sendRejectMail(any(), any(), any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getProductId();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingReject(Token)}
     */
    @Test
    void testOnboardingReject5() {
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(onboardingDao.getUserById(any())).thenReturn(null);
        when(onboardingDao.getProductById(any())).thenReturn(product);
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());
        when(contractService.getLogoFile())
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
        doNothing().when(emailService).sendRejectMail(any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("START - invalidate token {}");
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getProductId()).thenReturn("42");
        when(token.getUsers()).thenReturn(stringList);
        onboardingServiceImpl.onboardingReject(token);
        verify(onboardingDao).getUserById(any());
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).getProductById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(contractService).getLogoFile();
        verify(emailService).sendRejectMail(any(), any(), any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getProductId();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingReject(Token)}
     */
    @Test
    void testOnboardingReject6() {
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(onboardingDao.getUserById(any())).thenReturn(new OnboardedUser());
        when(onboardingDao.getProductById(any())).thenReturn(product);
        doNothing().when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        when(onboardingDao.findInstitutionById(any())).thenReturn(new Institution());
        when(contractService.getLogoFile())
                .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
        doNothing().when(emailService).sendRejectMail(any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("START - invalidate token {}");
        stringList.add("START - invalidate token {}");
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getProductId()).thenReturn("42");
        when(token.getUsers()).thenReturn(stringList);
        onboardingServiceImpl.onboardingReject(token);
        verify(onboardingDao, atLeast(1)).getUserById(any());
        verify(onboardingDao).findInstitutionById(any());
        verify(onboardingDao).getProductById(any());
        verify(onboardingDao).persistForUpdate(any(), any(), any(),
                any());
        verify(contractService).getLogoFile();
        verify(emailService).sendRejectMail(any(), any(), any());
        verify(token).getId();
        verify(token).getInstitutionId();
        verify(token).getProductId();
        verify(token).getUsers();
    }
}

