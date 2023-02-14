package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
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

import java.util.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OnboardingDao.class})
@ExtendWith(SpringExtension.class)
class OnboardingDaoTest {
    @MockBean
    private InstitutionConnector institutionConnector;

    @Autowired
    private OnboardingDao onboardingDao;

    @MockBean
    private ProductConnector productConnector;

    @MockBean
    private TokenConnector tokenConnector;

    @MockBean
    private UserConnector userConnector;

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        ArrayList<String> stringList = new ArrayList<>();

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
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
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
        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setId("userId1");
        userToOnboard.setName("user");
        userToOnboard.setRole(PartyRole.MANAGER);
        List<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);
        onboardingRequest.setUsers(userToOnboardList);

        Institution institution = new Institution();
        institution.setId("institutionId");

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser("user");
        onboardedUser.setId("userId1");
        Map<String, Map<String, OnboardedProduct>> bindings = new HashMap<>();
        Map<String, OnboardedProduct> onboardedProductMap = new HashMap<>();
        onboardedProductMap.put("productId", new OnboardedProduct());
        bindings.put("institutionId", onboardedProductMap);
        onboardedUser.setBindings(bindings);
        List<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);

        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        when(userConnector.save(any())).thenReturn(onboardedUser);

        assertNull(
                onboardingDao.persist(toUpdate, stringList, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).save(any());
        verify(tokenConnector, atLeast(1)).save(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist2() {
        when(institutionConnector.save(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - persist token, institution and users"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
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
        Institution institution = new Institution();

        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).save(any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist3() {
        when(institutionConnector.save(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - persist token, institution and users"));
        doThrow(new InvalidRequestException("An error occurred", "START - persist token, institution and users"))
                .when(tokenConnector)
                .deleteById(any());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
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
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).save(any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist5() {
        when(institutionConnector.save(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - persist token, institution and users"));
        Token token = mock(Token.class);
        when(token.getId()).thenReturn("42");
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any())).thenReturn(token);
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
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
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest"));
        verify(institutionConnector).save(any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
        verify(token, atLeast(1)).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, List, RelationshipState)}
     */
    @Test
    void testPersistForUpdateInvalid() {
        when(tokenConnector.save(any())).thenReturn(new Token());
        Token token = new Token();
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, institution, new ArrayList<>(), RelationshipState.PENDING));
        verify(tokenConnector, atLeast(1)).save(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, List, RelationshipState)}
     */
    @Test
    void testPersistForUpdate() {

        Token token = new Token();
        token.setId("id");
        token.setInstitutionId("institutionId");
        token.setContract("contract");
        token.setProductId("productId");
        token.setUsers(List.of("user1", "user2"));
        when(tokenConnector.save(any())).thenReturn(new Token());


        Onboarding onboarding = new Onboarding();
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setProductId("productId");

        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(onboardingList);
        when(institutionConnector.save(institution)).thenReturn(institution);

        Map<String, Map<String, OnboardedProduct>> bindings = new HashMap<>();
        Map<String, OnboardedProduct> onboardedProductMap = new HashMap<>();
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setId("productId");
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setName("productName");
        onboardedProductMap.put("productId", onboardedProduct);

        bindings.put("institutionId", onboardedProductMap);
        OnboardedUser user = new OnboardedUser();
        user.setId("idUser1");
        user.setUser("user1");
        user.setBindings(bindings);
        List<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(user);

        when(userConnector.save(any())).thenReturn(user);

        assertDoesNotThrow(() -> onboardingDao.persistForUpdate(token, institution, onboardedUserList, RelationshipState.PENDING));
        verify(tokenConnector, atLeast(1)).save(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, List, RelationshipState)}
     */
    @Test
    void testPersistForUpdate3() {
        when(tokenConnector.save(any())).thenReturn(new Token());
        Token token = mock(Token.class);
        when(token.getChecksum()).thenReturn("Checksum");
        when(token.getContract()).thenReturn("Contract");
        when(token.getExpiringDate()).thenReturn("2020-03-01");
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getProductId()).thenReturn("42");
        when(token.getCreatedAt()).thenReturn(null);
        when(token.getUsers()).thenReturn(new ArrayList<>());
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, institution, new ArrayList<>(), RelationshipState.PENDING));
        verify(tokenConnector, atLeast(1)).save(any());
        verify(token).getChecksum();
        verify(token).getContract();
        verify(token).getExpiringDate();
        verify(token, atLeast(1)).getId();
        verify(token).getInstitutionId();
        verify(token).getProductId();
        verify(token).getCreatedAt();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, List, RelationshipState)}
     */
    @Test
    void testPersistForUpdate5() {
        when(tokenConnector.save(any())).thenReturn(new Token());
        Token token = mock(Token.class);
        when(token.getChecksum()).thenReturn("Checksum");
        when(token.getContract()).thenReturn("Contract");
        when(token.getExpiringDate()).thenReturn("2020-03-01");
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getProductId()).thenReturn("42");
        when(token.getCreatedAt()).thenReturn(null);
        when(token.getUsers()).thenReturn(new ArrayList<>());
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        Institution institution = new Institution("42", "42", "START - persistForUpdate token, institution and users",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "START - persistForUpdate token, institution and users", billing, onboarding, geographicTaxonomies,
                attributes, paymentServiceProvider, new DataProtectionOfficer(), null, null,
                "START - persistForUpdate token, institution and users",
                "START - persistForUpdate token, institution and users",
                "START - persistForUpdate token, institution and users", "jane.doe@example.org", "4105551212", true);

        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, institution, new ArrayList<>(), RelationshipState.PENDING));
        verify(tokenConnector, atLeast(1)).save(any());
        verify(token).getChecksum();
        verify(token).getContract();
        verify(token).getExpiringDate();
        verify(token, atLeast(1)).getId();
        verify(token).getInstitutionId();
        verify(token).getProductId();
        verify(token).getCreatedAt();
        verify(token).getUsers();
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, List, RelationshipState)}
     */
    @Test
    void testPersistForUpdate6() {
        when(tokenConnector.save(any())).thenReturn(new Token());
        Token token = mock(Token.class);
        when(token.getChecksum()).thenReturn("Checksum");
        when(token.getContract()).thenReturn("Contract");
        when(token.getExpiringDate()).thenReturn("2020-03-01");
        when(token.getId()).thenReturn("42");
        when(token.getInstitutionId()).thenReturn("42");
        when(token.getProductId()).thenReturn("42");
        when(token.getCreatedAt()).thenReturn(null);
        when(token.getUsers()).thenReturn(new ArrayList<>());

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.isImported()).thenReturn(true);
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getRea()).thenReturn("Rea");
        when(institution.getShareCapital()).thenReturn("Share Capital");
        when(institution.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institution.getSupportPhone()).thenReturn("4105551212");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getCreatedAt()).thenReturn(null);
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, institution, new ArrayList<>(), RelationshipState.PENDING));
        verify(tokenConnector, atLeast(1)).save(any());
        verify(token).getChecksum();
        verify(token).getContract();
        verify(token).getExpiringDate();
        verify(token, atLeast(1)).getId();
        verify(token).getInstitutionId();
        verify(token).getProductId();
        verify(token).getCreatedAt();
        verify(token).getUsers();
        verify(institution).isImported();
        verify(institution).getBilling();
        verify(institution).getDataProtectionOfficer();
        verify(institution).getInstitutionType();
        verify(institution).getPaymentServiceProvider();
        verify(institution).getAddress();
        verify(institution).getBusinessRegisterPlace();
        verify(institution).getDescription();
        verify(institution).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getId();
        verify(institution).getIpaCode();
        verify(institution).getRea();
        verify(institution).getShareCapital();
        verify(institution).getSupportEmail();
        verify(institution).getSupportPhone();
        verify(institution).getTaxCode();
        verify(institution).getZipCode();
        verify(institution).getCreatedAt();
        verify(institution).getAttributes();
        verify(institution).getGeographicTaxonomies();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, Institution, String)}
     */
    @Test
    void testRollbackSecondStep() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        doNothing().when(tokenConnector).deleteById(any());
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, new Institution(), "42"));
        verify(institutionConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, Institution, String)}
     */
    @Test
    void testRollbackSecondStep2() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        doThrow(new InvalidRequestException("An error occurred",
                "START - rollbackSecondStep - rollback token {}, institution {} and {} users")).when(tokenConnector)
                .deleteById(any());
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, new Institution(), "42"));
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    void testRollbackSecondStepOfUpdate() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(tokenConnector.save(any())).thenReturn(new Token());
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStepOfUpdate(toUpdate, institution, new Token()));
        verify(institutionConnector).save(any());
        verify(tokenConnector).save(any());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    void testRollbackSecondStepOfUpdate2() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(tokenConnector.save(any())).thenThrow(new InvalidRequestException("An error occurred",
                "START - rollbackSecondStep - rollback token {}, institution {} and {} users"));
        ArrayList<OnboardedUser> toUpdate = new ArrayList<>();
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStepOfUpdate(toUpdate, institution, new Token()));
        verify(tokenConnector).save(any());
    }

    /**
     * Method under test: {@link OnboardingDao#findInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testFindInstitutionWithFilter() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(institutionList);
        List<Institution> actualFindInstitutionWithFilterResult = onboardingDao.findInstitutionWithFilter("42", "42",
                new ArrayList<>());
        assertSame(institutionList, actualFindInstitutionWithFilterResult);
        assertTrue(actualFindInstitutionWithFilterResult.isEmpty());
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#findInstitutionWithFilter(String, String, List)}
     */
    @Test
    void testFindInstitutionWithFilter2() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.findInstitutionWithFilter("42", "42", new ArrayList<>()));
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#findInstitutionByExternalId(String)}
     */
    @Test
    void testFindInstitutionByExternalId() {
        Institution institution = new Institution();
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(institution));
        assertSame(institution, onboardingDao.findInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingDao#findInstitutionByExternalId(String)}
     */
    @Test
    void testFindInstitutionByExternalId2() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> onboardingDao.findInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingDao#findInstitutionByExternalId(String)}
     */
    @Test
    void testFindInstitutionByExternalId3() {
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> onboardingDao.findInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingDao#getUserById(String)}
     */
    @Test
    void testGetUserById() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.getById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, onboardingDao.getUserById("foo"));
        verify(userConnector).getById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#getUserById(String)}
     */
    @Test
    void testGetUserById2() {
        when(userConnector.getById(any())).thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> onboardingDao.getUserById("foo"));
        verify(userConnector).getById(any());
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
     * Method under test: {@link OnboardingDao#findInstitutionById(String)}
     */
    @Test
    void testFindInstitutionById() {
        Institution institution = new Institution();
        when(institutionConnector.findById(any())).thenReturn(Optional.of(institution));
        assertSame(institution, onboardingDao.findInstitutionById("42"));
        verify(institutionConnector).findById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#findInstitutionById(String)}
     */
    @Test
    void testFindInstitutionById2() {
        Institution institution = mock(Institution.class);
        when(institution.getExternalId()).thenReturn("42");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findById(any())).thenReturn(ofResult);
        onboardingDao.findInstitutionById("42");
        verify(institutionConnector).findById(any());
        verify(institution).getExternalId();
    }

    /**
     * Method under test: {@link OnboardingDao#findInstitutionById(String)}
     */
    @Test
    void testFindInstitutionById3() {
        when(institutionConnector.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> onboardingDao.findInstitutionById("42"));
        verify(institutionConnector).findById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#findOnboardedManager(String, String)}
     */
    @Test
    void testFindOnboardedManager() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findOnboardedManager(any(), any())).thenReturn(onboardedUserList);
        List<OnboardedUser> actualFindOnboardedManagerResult = onboardingDao.findOnboardedManager("42", "42");
        assertSame(onboardedUserList, actualFindOnboardedManagerResult);
        assertTrue(actualFindOnboardedManagerResult.isEmpty());
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#findOnboardedManager(String, String)}
     */
    @Test
    void testFindOnboardedManager2() {
        when(userConnector.findOnboardedManager(any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> onboardingDao.findOnboardedManager("42", "42"));
        verify(userConnector).findOnboardedManager(any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#getUserByUser(String)}
     */
    @Test
    void testGetUserByUser() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        List<OnboardedUser> actualUserByUser = onboardingDao.getUserByUser("42");
        assertSame(onboardedUserList, actualUserByUser);
        assertTrue(actualUserByUser.isEmpty());
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingDao#getUserByUser(String)}
     */
    @Test
    void testGetUserByUser2() {
        when(userConnector.getByUser(any())).thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> onboardingDao.getUserByUser("42"));
        verify(userConnector).getByUser(any());
    }
}

