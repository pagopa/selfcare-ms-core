package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.Contract;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingOperatorsRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.constant.InstitutionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OnboardingDaoTest {

    @MockBean
    private ProductConnector productConnector;

    @Mock
    private InstitutionConnector institutionConnector;
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
        when(institutionConnector.findAndUpdate(any(), any(), any(), any()))
                .thenReturn(new Institution());
        when(coreConfig.getOnboardingExpiringDate()).thenReturn(60);
        Token token = new Token();
        token.setStatus(RelationshipState.ACTIVE);
        when(tokenConnector.save(any(), any())).thenReturn(token);
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        List<InstitutionGeographicTaxonomies> list = new ArrayList<>();
        Assertions.assertThrows(InvalidRequestException.class, () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution,
                list, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist() {
        when(institutionConnector.findAndUpdate(any(), any(), any(), any()))
                .thenReturn(new Institution());
        Token token = new Token();
        token.setId("tokenId");
        when(tokenConnector.save(any(), any())).thenReturn(token);
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        Onboarding onboarding = actualPersistResult.getOnboarding();
        assertEquals(RelationshipState.PENDING, onboarding.getStatus());
        assertEquals("42", onboarding.getProductId());
        assertEquals("Pricing Plan", onboarding.getPricingPlan());
        assertEquals("Path", onboarding.getContract());
        assertSame(billing, onboarding.getBilling());
        verify(institutionConnector).findAndUpdate(any(), any(), any(), any());
        verify(tokenConnector).save(any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist2() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> geo = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, geo, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(), any(), any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist3() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"))
                .when(tokenConnector)
                .deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> geo = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, geo, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(), any(), any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist6() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> geo = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, geo, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(), any(), any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist7() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
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
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, list, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(), any(), any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    @Test
    void testPersistLegals() {
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        OnboardingRequest onboardingRequest = new OnboardingRequest();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        onboardingRequest.setBillingRequest(billing);

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        OnboardingRollback onboardingRollback = onboardingDao.persistLegals(toUpdate, toDelete, onboardingRequest, new Institution(), "Digest");
        Assertions.assertNotNull(onboardingRollback);
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate() {
        TokenConnector tokenConnector = mock(TokenConnector.class);
        UserConnector userConnector = mock(UserConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(institutionConnector, tokenConnector, userConnector, productConnector, new CoreConfig());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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
        Institution institution = new Institution();

        assertDoesNotThrow(() -> onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate2() {

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.TOBEVALIDATED);
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, "foo"));
    }

    @Test
    void testPersistForUpdate7() {
        TokenConnector tokenConnector = mock(TokenConnector.class);
        UserConnector userConnector = mock(UserConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(institutionConnector, tokenConnector, userConnector, productConnector, new CoreConfig());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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
        Institution institution = new Institution();
        when(institutionConnector.findAndUpdateStatus(any(), any(), any())).thenThrow(new RuntimeException());
        assertThrows(InvalidRequestException.class, () -> onboardingDao.persistForUpdate(token, institution, RelationshipState.DELETED, "Digest"));
    }

    @Test
    void testPersistForUpdate8() {
        TokenConnector tokenConnector = mock(TokenConnector.class);
        UserConnector userConnector = mock(UserConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(institutionConnector, tokenConnector, userConnector, productConnector, new CoreConfig());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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
        Institution institution = new Institution();
        when(institutionConnector.findAndUpdateInstitutionData(any(), any(), any(), any())).thenThrow(new RuntimeException());
        assertThrows(InvalidRequestException.class, () -> onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate9() {
        TokenConnector tokenConnector = mock(TokenConnector.class);
        UserConnector userConnector = mock(UserConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(institutionConnector, tokenConnector, userConnector, productConnector, new CoreConfig());

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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
        token.setStatus(RelationshipState.TOBEVALIDATED);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        Institution institution = new Institution();

        assertDoesNotThrow(() -> onboardingDao.persistForUpdate(token, institution, RelationshipState.PENDING, "Digest"));
    }

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
        Map<String, OnboardedProduct> map = new HashMap<>();
        Token token = new Token();
        token.setId("id");
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, "42", token, onboarding, map));
    }

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
        Map<String, OnboardedProduct> map = new HashMap<>();
        Token token = new Token();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, "42", token, onboarding, map));
    }

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
        Map<String, OnboardedProduct> map = new HashMap<>();
        Token token = new Token();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(stringList, toDelete, "42", token, onboarding, map));
    }

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
        Map<String, OnboardedProduct> map = new HashMap<>();
        Token token = new Token();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(stringList, toDelete, "42", token, onboarding, map));
    }

    @Test
    void testRollbackSecondStepOfUpdate() {

        ArrayList<String> toUpdate = new ArrayList<>();
        Institution institution = new Institution();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        Assertions.assertThrows(InvalidRequestException.class, () -> onboardingDao.rollbackSecondStepOfUpdate(toUpdate, institution, token));
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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getRelationshipId()).thenReturn("42");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState20() {
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState21() {

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState22() {

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract(any());
        doNothing().when(onboardedProduct).setCreatedAt( any());
        doNothing().when(onboardedProduct).setEnv(any());
        doNothing().when(onboardedProduct).setProductId(any());
        doNothing().when(onboardedProduct).setProductRole(any());
        doNothing().when(onboardedProduct).setRelationshipId(any());
        doNothing().when(onboardedProduct).setRole( any());
        doNothing().when(onboardedProduct).setStatus( any());
        doNothing().when(onboardedProduct).setTokenId(any());
        doNothing().when(onboardedProduct).setUpdatedAt( any());
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
        verify(onboardedProduct).setCreatedAt( any());
        verify(onboardedProduct).setEnv(any());
        verify(onboardedProduct).setProductId(any());
        verify(onboardedProduct).setProductRole(any());
        verify(onboardedProduct).setRelationshipId(any());
        verify(onboardedProduct).setRole( any());
        verify(onboardedProduct).setStatus( any());
        verify(onboardedProduct).setTokenId(any());
        verify(onboardedProduct).setUpdatedAt( any());
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
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
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

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator2() {
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator5() {

        UserConnector userConnector = mock(UserConnector.class);
        doNothing().when(userConnector)
                .findAndUpdate(any(), any(), any(),any(),
                        any());
        when(userConnector.findById(any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).deleteById(any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

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

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(onboardingOperatorsRequest,
                institution);
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
        verify(userConnector).findById(any());
        verify(userConnector).findAndUpdate(any(), any(), any(),
               any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator6() {

        UserConnector userConnector = mock(UserConnector.class);
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(),any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(),any(),
                        any());
        when(userConnector.findById(any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).deleteById(any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

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

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
        verify(userConnector).findById(any());
        verify(userConnector).deleteById(any());
        verify(userConnector).findAndRemoveProduct(any(), any(),any());
        verify(userConnector).findAndUpdate(any(), any(), any(),
               any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator7() {

        UserConnector userConnector = mock(UserConnector.class);
        doThrow(new InvalidRequestException("An error occurred", "can not onboard operators")).when(userConnector)
                .findAndRemoveProduct(any(), any(),any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(),any(),
                        any());
        when(userConnector.findById(any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).deleteById(any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

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

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.onboardOperator(onboardingOperatorsRequest, institution));
        verify(userConnector).findById(any());
        verify(userConnector).findAndRemoveProduct(any(), any(),any());
        verify(userConnector).findAndUpdate(any(), any(), any(),
               any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator8() {

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(),any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(),any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById(any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

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

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(onboardingOperatorsRequest,
                institution);
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
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator9() {
     OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "users to update: {}"));
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(),any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(),any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById(any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

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

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
        verify(userConnector).findAndCreate(any(), any());
        verify(userConnector).findById(any());
        verify(userConnector).deleteById(any());
        verify(onboardedUser).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator10() {
       OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(),any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(),any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById(any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

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

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(onboardingOperatorsRequest,
                institution);
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
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator11() {

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenReturn("42");
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate(any(), any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(),any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate(any(), any(), any(),any(),
                        any());
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById(any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());
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
        doNothing().when(userToOnboard).setRole( any());
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

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
        verify(userConnector).findById(any());
        verify(userConnector).findAndRemoveProduct(any(), any(),any());
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
        verify(userToOnboard).setRole( any());
        verify(userToOnboard).setSurname(any());
        verify(userToOnboard).setTaxCode(any());
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
        ProductConnector productConnector = mock(ProductConnector.class);
        when(productConnector.getProductById(any())).thenReturn(product);
        assertSame(product,
                (new OnboardingDao(null, null, null, productConnector, new CoreConfig())).getProductById("42"));
        verify(productConnector).getProductById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#getProductById(String)}
     */
    @Test
    void testGetProductById2() {
         ProductConnector productConnector = mock(ProductConnector.class);
        when(productConnector.getProductById(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        CoreConfig config = new CoreConfig();
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, config);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.getProductById("42"));
        verify(productConnector).getProductById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#getTokenById(String)}
     */
    @Test
    void testGetTokenById2() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

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
        TokenConnector tokenConnector = mock(TokenConnector.class);
        when(tokenConnector.findById(any())).thenReturn(token);
        ProductConnector productConnector = mock(ProductConnector.class);
        assertSame(token,
                (new OnboardingDao(null, tokenConnector, null, productConnector, new CoreConfig())).getTokenById("42"));
        verify(tokenConnector).findById(any());
    }
}
