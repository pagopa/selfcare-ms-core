package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class OnboardingDaoTest {

    @Mock
    private InstitutionConnector institutionConnector;
    @Mock
    private ProductConnector productConnector;
    @Mock
    private TokenConnector tokenConnector;
    @Mock
    private UserConnector userConnector;
    @Mock
    private CoreConfig coreConfig;

    @InjectMocks
    private OnboardingDao onboardingDao;

    @Test
    void testPersist0() {
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenReturn(new Institution());
        when(coreConfig.getExpiringDate()).thenReturn(60);
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        UserToOnboard user = new UserToOnboard();
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingRequest.setUsers(users);
        when(userConnector.findById(any())).thenReturn(null);
        Institution institution = new Institution();
        OnboardingRollback actualPersistResult = onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution,
                new ArrayList<>(), "Digest");
        assertNull(actualPersistResult.getTokenId());
        Onboarding onboarding = actualPersistResult.getOnboarding();
        assertEquals(RelationshipState.PENDING, onboarding.getStatus());
        assertEquals("42", onboarding.getProductId());
        assertEquals("Pricing Plan", onboarding.getPricingPlan());
        assertEquals("Path", onboarding.getContract());
        assertSame(billing, onboarding.getBilling());
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(tokenConnector).save(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist() {
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenReturn(new Institution());
        Token token = new Token();
        token.setId("tokenId");
        when(tokenConnector.save(any())).thenReturn(token);
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        UserToOnboard user = new UserToOnboard();
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingRequest.setUsers(users);
        when(userConnector.findById(any())).thenReturn(new OnboardedUser());
        Institution institution = new Institution();
        OnboardingRollback actualPersistResult = onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution,
                new ArrayList<>(), "Digest");
        assertEquals("tokenId", actualPersistResult.getTokenId());
        Onboarding onboarding = actualPersistResult.getOnboarding();
        assertEquals(RelationshipState.PENDING, onboarding.getStatus());
        assertEquals("42", onboarding.getProductId());
        assertEquals("Pricing Plan", onboarding.getPricingPlan());
        assertEquals("Path", onboarding.getContract());
        assertSame(billing, onboarding.getBilling());
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(tokenConnector).save(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist2() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist3() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"))
                .when(tokenConnector)
                .deleteById(any());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist6() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        institutionUpdate.setInstitutionType(InstitutionType.PG);
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
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist7() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
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
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }


    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate2() {
        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.PENDING, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate3() {
        Token token = new Token();
        token.setStatus(RelationshipState.ACTIVE);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.PENDING, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate5() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "update token {} from state {} to {}"));

        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.ACTIVE, "Digest"));
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate6() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());

        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, null, RelationshipState.ACTIVE, "Digest"));
        verify(tokenConnector, atLeast(1)).findAndUpdateToken(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate7() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "update token {} from state {} to {}"));

        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.SUSPENDED, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate8() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "update token {} from state {} to {}"));

        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.DELETED, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate9() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());

        Token token = new Token();
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.PENDING);
        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.ACTIVE, "Digest");
        verify(institutionConnector).findAndUpdateStatus(any(), any(), any());
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate10() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "update token {} from state {} to {}"));

        Token token = new Token();
        token.setStatus(RelationshipState.TOBEVALIDATED);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.ACTIVE, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate11() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "update token {} from state {} to {}"));

        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.TOBEVALIDATED, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate12() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "update token {} from state {} to {}"));

        Token token = new Token();
        token.setStatus(RelationshipState.PENDING);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.REJECTED, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate13() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId("userId");

        Token token = new Token();
        token.setUsers(List.of(tokenUser));
        token.setStatus(RelationshipState.PENDING);

        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.ACTIVE, "Digest");

        verify(institutionConnector).findAndUpdateStatus(any(), any(), any());
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
        verify(userConnector).findAndUpdateState(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate14() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        TokenUser tokenUser1 = new TokenUser();
        tokenUser1.setUserId("userId1");
        TokenUser tokenUser2 = new TokenUser();
        tokenUser2.setUserId("userId2");

        Token token = new Token();
        token.setUsers(List.of(tokenUser1, tokenUser2));
        token.setStatus(RelationshipState.PENDING);

        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.ACTIVE, "Digest");

        verify(institutionConnector).findAndUpdateStatus(any(), any(), any());
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
        verify(userConnector, atLeast(1)).findAndUpdateState(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUsers(Institution, Token, RelationshipState)}
     */
    @Test
    void testUpdateUsers() {
        Institution institution = new Institution();
        Token token = new Token();
        token.setUsers(Collections.emptyList());
        assertDoesNotThrow(() -> onboardingDao.updateUsers(institution, token, RelationshipState.PENDING));
    }

    /**
     * Method under test: {@link OnboardingDao#updateUsers(Institution, Token, RelationshipState)}
     */
    @Test
    void testUpdateUsers2() {
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId("userId");

        Token token = new Token();
        token.setUsers(List.of(tokenUser));

        Institution institution = new Institution();
        onboardingDao.updateUsers(institution, token, RelationshipState.PENDING);
        verify(userConnector).findAndUpdateState(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUsers(Institution, Token, RelationshipState)}
     */
    @Test
    void testUpdateUsers3() {
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        TokenUser tokenUser1 = new TokenUser();
        tokenUser1.setUserId("userId1");
        TokenUser tokenUser2 = new TokenUser();
        tokenUser2.setUserId("userId2");

        Token token = new Token();
        token.setUsers(List.of(tokenUser1, tokenUser2));

        Institution institution = new Institution();
        onboardingDao.updateUsers(institution, token, RelationshipState.PENDING);
        verify(userConnector, atLeast(1)).findAndUpdateState(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUsers(Institution, Token, RelationshipState)}
     */
    @Test
    void testUpdateUsers5() {
        when(tokenConnector.findAndUpdateToken(any(), any(), any())).thenThrow(
                new InvalidRequestException("An error occurred", "update {} users state from {} to {} for product {}"));
        doThrow(new RuntimeException()).when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId("userId");

        Token token = new Token();
        token.setUsers(List.of(tokenUser));

        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUsers(null, token, RelationshipState.PENDING));
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, String, Onboarding, Map)}
     */
    @Test
    void testRollbackSecondStep() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doNothing().when(tokenConnector).deleteById(any());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, "42", "42", onboarding, new HashMap<>()));
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, String, Onboarding, Map)}
     */
    @Test
    void testRollbackSecondStep2() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doThrow(new InvalidRequestException("An error occurred", "rollback second step completed")).when(tokenConnector)
                .deleteById(any());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, "42", "42", onboarding, new HashMap<>()));
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, String, Onboarding, Map)}
     */
    @Test
    void testRollbackSecondStep3() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doNothing().when(tokenConnector).deleteById(any());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("rollback second step completed");
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(stringList, toDelete, "42", "42", onboarding, new HashMap<>()));
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).deleteById(any());
        verify(userConnector).findAndRemoveProduct(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, String, Onboarding, Map)}
     */
    @Test
    void testRollbackSecondStep4() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doNothing().when(tokenConnector).deleteById(any());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("rollback second step completed");
        stringList.add("rollback second step completed");
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(stringList, toDelete, "42", "42", onboarding, new HashMap<>()));
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).deleteById(any());
        verify(userConnector, atLeast(1)).findAndRemoveProduct(any(), any(), any());
    }


    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    void testRollbackSecondStepOfUpdate() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStepOfUpdate(toUpdate, institution, new Token()));
        verify(institutionConnector).findAndUpdateStatus(any(), any(), any());
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    void testRollbackSecondStepOfUpdate2() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "rollback second step completed"));
        ArrayList<String> toUpdate = new ArrayList<>();
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStepOfUpdate(toUpdate, institution, new Token()));
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    void testRollbackSecondStepOfUpdate3() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("rollback second step completed");
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStepOfUpdate(stringList, institution, new Token()));
        verify(institutionConnector).findAndUpdateStatus(any(), any(), any());
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
        verify(userConnector).findAndUpdateState(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    void testRollbackSecondStepOfUpdate4() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("rollback second step completed");
        stringList.add("rollback second step completed");
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStepOfUpdate(stringList, institution, new Token()));
        verify(institutionConnector).findAndUpdateStatus(any(), any(), any());
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
        verify(userConnector, atLeast(1)).findAndUpdateState(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    void testRollbackSecondStepOfUpdate7() {
        doNothing().when(institutionConnector)
                .findAndUpdateStatus(any(), any(), any());
        when(tokenConnector.findAndUpdateToken(any(), any(), any()))
                .thenReturn(new Token());
        doThrow(new InvalidRequestException("An error occurred", "rollback second step completed")).when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("rollback second step completed");
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStepOfUpdate(stringList, institution, new Token()));
        verify(institutionConnector).findAndUpdateStatus(any(), any(), any());
        verify(tokenConnector).findAndUpdateToken(any(), any(), any());
        verify(userConnector).findAndUpdateState(any(), any(), any(), any());
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
    void testUpdateUserProductState11() {
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
        assertThrows(InvalidRequestException.class, () -> onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING));
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
        onboardedProduct.setEnv(EnvEnum.ROOT);
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
     * Method under test: {@link OnboardingDao#getProductById(String)}
     */
    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(productConnector.getProductById(any())).thenReturn(product);
        assertSame(product, onboardingDao.getProductById("42"));
        verify(productConnector).getProductById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#getProductById(String)}
     */
    @Test
    void testGetProductById2() {
        when(productConnector.getProductById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> onboardingDao.getProductById("42"));
        verify(productConnector).getProductById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator() {
        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingOperatorsRequest.setUsers(users);
        assertFalse(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
    }


    @Test
    void testOnboardOperator1() {
        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingOperatorsRequest.setUsers(users);
        when(userConnector.findById(any())).thenReturn(new OnboardedUser());
        assertFalse(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
    }
}
