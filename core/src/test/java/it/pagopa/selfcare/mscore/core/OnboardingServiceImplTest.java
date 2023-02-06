package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.User;
import it.pagopa.selfcare.mscore.model.institution.*;

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
    private ContractService contractService;

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
    void testGetOnboardingInfo3() {
        when(userConnector.getByUser(any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo5() {
        when(userConnector.getByUser(any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", null, "42"));
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo6() {
        when(userConnector.getByUser(any())).thenThrow(new ResourceNotFoundException("An error occurred",
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}"));
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo8() {
        when(institutionConnector.findById(any())).thenReturn(Optional.of(new Institution()));

        Product product = new Product();
        product.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                stringProductMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(stringMapMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser
                .setName("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser
                .setTaxCode("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser
                .setUser("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionConnector).findById(any());
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo9() {
        Institution institution = mock(Institution.class);
        when(institution.getId()).thenReturn("42");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findById(any())).thenReturn(ofResult);

        Product product = new Product();
        product.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                stringProductMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(stringMapMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser
                .setName("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser
                .setTaxCode("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser
                .setUser("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionConnector).findById(any());
        verify(institution).getId();
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo10() {
        Institution institution = mock(Institution.class);
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        when(institution.getId())
                .thenReturn("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findById(any())).thenReturn(ofResult);

        Product product = new Product();
        product.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                stringProductMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(stringMapMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser
                .setName("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser
                .setTaxCode("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser
                .setUser("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionConnector).findById(any());
        verify(institution, atLeast(1)).getId();
        verify(institution).getOnboarding();
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo11() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding
                .setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding
                .setPricingPlan("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = mock(Institution.class);
        when(institution.getOnboarding()).thenReturn(onboardingList);
        when(institution.getId())
                .thenReturn("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findById(any())).thenReturn(ofResult);

        Product product = new Product();
        product.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                stringProductMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(stringMapMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser
                .setName("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser
                .setTaxCode("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser
                .setUser("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionConnector).findById(any());
        verify(institution, atLeast(1)).getId();
        verify(institution).getOnboarding();
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo12() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding
                .setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding
                .setPricingPlan("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        billing1.setVatNumber("42");

        Premium premium1 = new Premium();
        premium1
                .setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        premium1.setStatus(RelationshipState.PENDING);

        Onboarding onboarding1 = new Onboarding();
        onboarding1.setBilling(billing1);
        onboarding1
                .setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboarding1.setCreatedAt(null);
        onboarding1.setPremium(premium1);
        onboarding1
                .setPricingPlan("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboarding1.setProductId("42");
        onboarding1.setStatus(RelationshipState.PENDING);
        onboarding1.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding1);
        onboardingList.add(onboarding);
        Institution institution = mock(Institution.class);
        when(institution.getOnboarding()).thenReturn(onboardingList);
        when(institution.getId())
                .thenReturn("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findById(any())).thenReturn(ofResult);

        Product product = new Product();
        product.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                stringProductMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(stringMapMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser
                .setName("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser
                .setTaxCode("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser
                .setUser("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionConnector).findById(any());
        verify(institution, atLeast(1)).getId();
        verify(institution).getOnboarding();
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfo14() {
        when(institutionConnector.findById(any())).thenReturn(Optional.empty());

        Product product = new Product();
        product.setContract("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                + " states {}");
        product.setCreatedAt(null);
        product.setRoles(new ArrayList<>());
        product.setStatus(RelationshipState.PENDING);
        product.setUpdatedAt(null);

        HashMap<String, Product> stringProductMap = new HashMap<>();
        stringProductMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                product);

        HashMap<String, Map<String, Product>> stringMapMap = new HashMap<>();
        stringMapMap.put(
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and" + " states {}",
                stringProductMap);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(stringMapMap);
        onboardedUser.setCreatedAt(null);
        onboardedUser.setEmail("jane.doe@example.org");
        onboardedUser.setId("42");
        onboardedUser
                .setName("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser.setProductRole(new ArrayList<>());
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setSurname("Doe");
        onboardedUser
                .setTaxCode("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");
        onboardedUser
                .setUser("Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}");

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        when(userConnector.getByUser(any())).thenReturn(onboardedUserList);
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(institutionConnector).findById(any());
        verify(userConnector).getByUser(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution19() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        Institution institution = mock(Institution.class);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setExternalId(any());
        institution.setExternalId("Onboarding optionalInstitution having externalId {}");

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
        when(userRegistryConnector.getUserByInternalId(any(),any())).thenReturn(user);

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
        verify(institution).setExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#persist(OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist() {
        when(institutionConnector.save(any())).thenReturn(new Institution());

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContract("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate("2020-03-01");
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenConnector.save(any())).thenReturn(token);

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
        onboardingServiceImpl.persist(onboardingRequest, institution, new ArrayList<>(), "Digest");
        verify(institutionConnector).save(any());
        verify(tokenConnector).save(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#persist(OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist2() {
        when(institutionConnector.save(any())).thenThrow(
                new ResourceNotFoundException("An error occurred", "START - persist token, institution and users"));

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContract("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate("2020-03-01");
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any())).thenReturn(token);

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
        List<GeographicTaxonomies> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.persist(onboardingRequest, institution,list, "Digest"));
        verify(institutionConnector).save(any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#persist(OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist3() {
        when(institutionConnector.save(any())).thenThrow(
                new ResourceNotFoundException("An error occurred", "START - persist token, institution and users"));

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setContract("Contract");
        token.setCreatedAt(null);
        token.setExpiringDate("2020-03-01");
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        doThrow(new ResourceNotFoundException("An error occurred", "START - persist token, institution and users"))
                .when(tokenConnector)
                .deleteById(any());
        when(tokenConnector.save(any())).thenReturn(token);

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
        List<GeographicTaxonomies> list = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.persist(onboardingRequest, institution, list, "Digest"));
        verify(institutionConnector).save(any());
        verify(tokenConnector).save(any());
        verify(tokenConnector).deleteById(any());
    }
}

