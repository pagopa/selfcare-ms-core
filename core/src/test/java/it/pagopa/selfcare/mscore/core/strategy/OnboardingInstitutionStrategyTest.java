package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.*;
import it.pagopa.selfcare.mscore.core.strategy.factory.OnboardingInstitutionStrategyFactory;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class OnboardingInstitutionStrategyTest {

    private OnboardingInstitutionStrategyFactory strategyFactory;

    @Mock
    private OnboardingDao onboardingDao;
    @Mock
    private ContractService contractService;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private InstitutionService institutionService;
    @Mock
    private CoreConfig coreConfig;

    @BeforeEach
    void beforeAll() {
        strategyFactory = new OnboardingInstitutionStrategyFactory(onboardingDao,
                contractService,userService,emailService,institutionService, coreConfig);
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void shouldOnboardingInstitutionSuccessWhenInstitutionTypeIsPG() {
        Billing billing = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();

        DataProtectionOfficer dataProtectionOfficer = TestUtils.createSimpleDataProtectionOfficer();
        PaymentServiceProvider paymentServiceProvider = TestUtils.createSimplePaymentServiceProvider();

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("description");
        institutionUpdate.setDigitalAddress("digitalAddress");
        institutionUpdate.setGeographicTaxonomies(List.of(new InstitutionGeographicTaxonomies()));
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
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


        InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies();
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

        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);
        when(institutionService.retrieveGeoTaxonomies(any())).thenReturn(new GeographicTaxonomies());
        Token token = new Token();
        token.setId("token");
        when(onboardingDao.persist(any(), any(), any(), any(), any(), any())).thenReturn(new OnboardingRollback());

        strategyFactory.retrieveOnboardingInstitutionStrategy(institutionUpdate.getInstitutionType())
                .onboardingInstitution(onboardingRequest,selfCareUser);

        verify(institutionService).retrieveInstitutionByExternalId(any());
        verify(onboardingDao).persist(any(), any(), any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void shouldThrowExceptionIfExistsPendingToken(){
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
        when(onboardingDao.getTokenById(any())).thenReturn(token);

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setOnboarding(onboardingList);
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);

        Billing billing1 = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();

        InstitutionUpdate institutionUpdate1 = TestUtils.createSimpleInstitutionUpdate();

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate1);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());

        assertThrows(InvalidRequestException.class, () -> strategyFactory.retrieveOnboardingInstitutionStrategy(institutionUpdate.getInstitutionType())
                .onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }



    @Test
    void shouldOnboardInstitutionSuccessWithoutContract() {

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

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing.setVatNumber("42");

        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);

        Billing billing1 = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();

        InstitutionUpdate institutionUpdate1 = new InstitutionUpdate();
        institutionUpdate1.setImported(true);

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate1);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setContractCreatedAt(OffsetDateTime.now());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setId("id");
        userToOnboard.setRole(PartyRole.MANAGER);
        onboardingRequest.setUsers(List.of(userToOnboard));

        OnboardingRollback onboardingRollback = new OnboardingRollback();
        Token token1 =new Token();
        token1.setId("id");
        onboardingRollback.setToken(token1);
        when(onboardingDao.persistComplete(any(), any(), any(), any(), any(), any())).thenReturn(onboardingRollback);

        assertDoesNotThrow(() -> strategyFactory.retrieveOnboardingInstitutionStrategyWithoutContractAndComplete(institutionUpdate.getInstitutionType())
                .onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution4() throws IOException {

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

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}");
        billing.setVatNumber("42");

        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);

        Billing billing1 = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();

        InstitutionUpdate institutionUpdate1 = new InstitutionUpdate();

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate1);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setId("id");
        userToOnboard.setRole(PartyRole.MANAGER);
        onboardingRequest.setUsers(List.of(userToOnboard));
        File file = File.createTempFile("test",".txt");
        when(contractService.createContractPDF(any(), any(), any(), any(), any(), any(), any())).thenReturn(file);
        OnboardingRollback onboardingRollback = new OnboardingRollback();
        Token token1 =new Token();
        token1.setId("id");
        onboardingRollback.setToken(token1);
        when(onboardingDao.persist(any(), any(), any(), any(), any(), any())).thenReturn(onboardingRollback);

        assertDoesNotThrow(() -> strategyFactory.retrieveOnboardingInstitutionStrategy(institutionUpdate.getInstitutionType())
                .onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }


    @Test
    void testOnboardingInstitution5() {
        when(institutionService.retrieveInstitutionByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred",
                        "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}"));

        Billing billing = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

        DataProtectionOfficer dataProtectionOfficer = TestUtils.createSimpleDataProtectionOfficer();
        PaymentServiceProvider paymentServiceProvider = TestUtils.createSimplePaymentServiceProvider();

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setSupportPhone("4105551212");

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

        assertThrows(InvalidRequestException.class, () -> strategyFactory.retrieveOnboardingInstitutionStrategy(institutionUpdate.getInstitutionType())
                        .onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));

        verify(institutionService).retrieveInstitutionByExternalId(any());
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution6() {
        when(institutionService.retrieveInstitutionByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred",
                        "START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}"));

        Billing billing = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

        DataProtectionOfficer dataProtectionOfficer = TestUtils.createSimpleDataProtectionOfficer();
        PaymentServiceProvider paymentServiceProvider = TestUtils.createSimplePaymentServiceProvider();

        InstitutionUpdate institutionUpdate = TestUtils.createSimpleInstitutionUpdate();
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setSupportPhone("4105551212");

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

        assertThrows(InvalidRequestException.class,
                () -> strategyFactory.retrieveOnboardingInstitutionStrategy(institutionUpdate.getInstitutionType())
                        .onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));

        verify(institutionService).retrieveInstitutionByExternalId(any());
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitutionWithProductAlreadyOnboarded() {
        Billing billing = TestUtils.createSimpleBilling();
        Contract contract = TestUtils.createSimpleContract();

        DataProtectionOfficer dataProtectionOfficer = TestUtils.createSimpleDataProtectionOfficer();
        PaymentServiceProvider paymentServiceProvider = TestUtils.createSimplePaymentServiceProvider();

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("address");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("description");
        institutionUpdate.setDigitalAddress("digitalAddress");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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

        assertThrows(NullPointerException.class,
                () -> strategyFactory.retrieveOnboardingInstitutionStrategy(institutionUpdate.getInstitutionType())
                        .onboardingInstitution(onboardingRequest, selfCareUser));
    }
}
