package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OnboardingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class OnboardingServiceImplTest {
    @MockBean
    private ExternalService externalService;

    @MockBean
    private GeoTaxonomiesConnector geoTaxonomiesConnector;

    @MockBean
    private InstitutionConnector institutionConnector;

    @Autowired
    private OnboardingServiceImpl onboardingServiceImpl;

    @MockBean
    private TokenConnector tokenConnector;

    @MockBean
    private UserConnector userConnector;

    @MockBean
    private UserRegistryConnector userRegistryConnector;

    @MockBean
    private ContractService contractService;


    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo2() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(institutionList);
        onboardingServiceImpl.verifyOnboardingInfo("42", "42");
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo3() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfoUserWithouthInstitutions() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setId("42");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");
        when(userConnector.getByUser(any())).thenReturn(List.of(onboardedUser));
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfoWhenUserIsNotFound() {
        when(userConnector.getByUser(any())).thenThrow(new ResourceNotFoundException("An error occurred",
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}"));
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfoWithInstitutionNotLinkedToUser() {
        Product product = new Product();
        product.setContract("contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> productMap = new HashMap<>();
        productMap.put("product42", product);

        HashMap<String, Map<String, Product>> institutionMap = new HashMap<>();
        institutionMap.put("institution1", productMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(institutionMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setId("42");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");

        Institution onboardedInstitution = new Institution();
        onboardedInstitution.setId("institution2");

        when(userConnector.getByUser(any())).thenReturn(List.of(onboardedUser));
        when(institutionConnector.findById(any())).thenReturn(Optional.of(onboardedInstitution));

        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "", new String[]{}, "42"));
        verify(institutionConnector).findById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfoWhenUserLooksForInstitutionId() {
        Product product = new Product();
        product.setContract("contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> productMap = new HashMap<>();
        productMap.put("product42", product);

        HashMap<String, Map<String, Product>> institutionMap = new HashMap<>();
        institutionMap.put("institution1", productMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(institutionMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setId("42");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");

        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("product42");
        onboarding.setStatus(RelationshipState.PENDING);
        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution onboardedInstitution = new Institution();
        onboardedInstitution.setId("institution1");
        onboardedInstitution.setOnboarding(onboardingList);

        when(userConnector.getByUser(any())).thenReturn(List.of(onboardedUser));
        when(institutionConnector.findById(any())).thenReturn(Optional.of(onboardedInstitution));

        List<OnboardingInfo> response = onboardingServiceImpl.getOnboardingInfo("42", "", new String[]{"PENDING"}, "42");

        assertEquals(response.size(), 1);
        verify(institutionConnector).findById(any());
    }

    @Test
    void testGetOnboardingInfoWhenUserLooksForInstitutionExternalId() {
        Product product = new Product();
        product.setContract("contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> productMap = new HashMap<>();
        productMap.put("product42", product);

        HashMap<String, Map<String, Product>> institutionMap = new HashMap<>();
        institutionMap.put("institution1", productMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(institutionMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setId("42");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");

        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("product42");
        onboarding.setStatus(RelationshipState.PENDING);
        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution onboardedInstitution = new Institution();
        onboardedInstitution.setId("institution1");
        onboardedInstitution.setExternalId("external1");
        onboardedInstitution.setOnboarding(onboardingList);

        when(userConnector.getByUser(any())).thenReturn(List.of(onboardedUser));
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(onboardedInstitution));

        List<OnboardingInfo> response = onboardingServiceImpl.getOnboardingInfo("", "external1", new String[]{"PENDING"}, "42");

        assertEquals(response.size(), 1);
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfoWhenUserLooksForStates() {
        Product product = new Product();
        product.setContract("contract");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> productMap = new HashMap<>();
        productMap.put("product42", product);

        HashMap<String, Map<String, Product>> institutionMap = new HashMap<>();
        institutionMap.put("institution1", productMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(institutionMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setId("42");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setUser("User");

        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("product42");
        onboarding.setStatus(RelationshipState.PENDING);
        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Institution onboardedInstitution = new Institution();
        onboardedInstitution.setId("institution1");
        onboardedInstitution.setOnboarding(onboardingList);

        when(userConnector.getByUser(any())).thenReturn(List.of(onboardedUser));
        when(institutionConnector.findById(any())).thenReturn(Optional.of(onboardedInstitution));

        List<OnboardingInfo> response = onboardingServiceImpl.getOnboardingInfo("", "", new String[]{"PENDING"}, "42");

        assertEquals(response.size(), 1);
        verify(institutionConnector).findById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitutionWhenInstitutionIsNotFound() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

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
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser));
        verify(institutionConnector).findByExternalId(any());
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


        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.ACTIVE);
        List<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = new Institution();
        institution.setId("id");
        institution.setExternalId("externalId");
        institution.setOnboarding(onboardingList);

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getId()).thenReturn("42");

        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(institution));

        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser));
        verify(institutionConnector).findByExternalId(any());
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

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser("user");
        onboardedUser.setRole(PartyRole.MANAGER);

        OnboardedUser onboardedUser2 = new OnboardedUser();
        onboardedUser2.setUser("user2");
        onboardedUser2.setRole(PartyRole.MANAGER);

        List<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        onboardedUserList.add(onboardedUser2);

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(onboardedUserList);


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

        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(institution));
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);

        Token token = new Token();
        token.setId("token");
        when(tokenConnector.save(any())).thenReturn(token);

        when(userConnector.getByUser(any())).thenReturn(new ArrayList<>());

        OnboardedUser savedUser = new OnboardedUser();
        savedUser.setId("idSaved");
        when(userConnector.save(any())).thenReturn(savedUser);

        onboardingServiceImpl.onboardingInstitution(onboardingRequest, selfCareUser);

        verify(institutionConnector).findByExternalId(any());
        verify(geoTaxonomiesConnector).getExtByCode(any());
        verify(tokenConnector).save(any());
    }


}

