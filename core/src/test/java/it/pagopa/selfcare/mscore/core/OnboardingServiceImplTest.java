package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
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

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo() {
        doNothing().when(externalService)
                .getInstitutionWithFilter(any(), any(), any());
        onboardingServiceImpl.verifyOnboardingInfo("42", "42");
        verify(externalService).getInstitutionWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo2() {
        doThrow(new ResourceNotFoundException("An error occurred", "Code")).when(externalService)
                .getInstitutionWithFilter(any(), any(), any());
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(externalService).getInstitutionWithFilter(any(), any(), any());
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
        when(userConnector.getById((String) any())).thenReturn(onboardedUser);
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userConnector).getById((String) any());
    }


    /**
     * Method under test: {@link OnboardingServiceImpl#getOnboardingInfo(String, String, String[], String)}
     */
    @Test
    void testGetOnboardingInfoWhenUserIsNotFound() {
        when(userConnector.getById((String) any())).thenThrow(new ResourceNotFoundException("An error occurred",
                "Getting onboarding info for institution having institutionId {} institutionExternalId {} and"
                        + " states {}"));
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "42", new String[]{}, "42"));
        verify(userConnector).getById((String) any());
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

        when(userConnector.getById((String) any())).thenReturn(onboardedUser);
        when(institutionConnector.findById((String) any())).thenReturn(Optional.of(onboardedInstitution));

        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.getOnboardingInfo("42", "", new String[]{}, "42"));
        verify(institutionConnector).findById((String) any());
        verify(userConnector).getById((String) any());
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

        when(userConnector.getById((String) any())).thenReturn(onboardedUser);
        when(institutionConnector.findById((String) any())).thenReturn(Optional.of(onboardedInstitution));

        List<OnboardingInfo> response = onboardingServiceImpl.getOnboardingInfo("42", "", new String[]{ "PENDING" }, "42");

        assertEquals(response.size(), 1);
        verify(institutionConnector).findById((String) any());
        verify(userConnector).getById((String) any());
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

        when(userConnector.getById((String) any())).thenReturn(onboardedUser);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(Optional.of(onboardedInstitution));

        List<OnboardingInfo> response = onboardingServiceImpl.getOnboardingInfo("", "external1", new String[]{ "PENDING" }, "42");

        assertEquals(response.size(), 1);
        verify(institutionConnector).findByExternalId((String) any());
        verify(userConnector).getById((String) any());
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

        when(userConnector.getById((String) any())).thenReturn(onboardedUser);
        when(institutionConnector.findById((String) any())).thenReturn(Optional.of(onboardedInstitution));

        List<OnboardingInfo> response = onboardingServiceImpl.getOnboardingInfo("", "", new String[]{ "PENDING" }, "42");

        assertEquals(response.size(), 1);
        verify(institutionConnector).findById((String) any());
        verify(userConnector).getById((String) any());
    }

    @Test
    void testOnboardingInstitution1() {
        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(institution));

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
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
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

        List<OnboardedUser> list = new ArrayList<>();

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser("id");
        onboardedUser.setRole(PartyRole.MANAGER);
        onboardedUser.setCreatedAt(OffsetDateTime.now());
        onboardedUser.setId("535");
        list.add(onboardedUser);

        OnboardedUser onboardedUser1 = new OnboardedUser();
        onboardedUser1.setUser("id");
        onboardedUser1.setRole(PartyRole.MANAGER);
        onboardedUser1.setCreatedAt(OffsetDateTime.now());
        onboardedUser1.setId("536");
        list.add(onboardedUser1);

        OnboardedUser onboardedUser2 = new OnboardedUser();
        onboardedUser2.setUser("id");
        onboardedUser2.setRole(PartyRole.MANAGER);
        onboardedUser2.setCreatedAt(OffsetDateTime.now());
        list.add(onboardedUser2);

        onboardingRequest.setUsers(list);

        Token token = new Token();
        token.setId("id");
        when(tokenConnector.save(any())).thenReturn(token);
        Assertions.assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution() {
        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(institution));

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
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
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
        onboardingRequest.setUsers(new ArrayList<>());

        Token token = new Token();
        token.setId("id");
        when(tokenConnector.save(any())).thenReturn(token);
        Assertions.assertDoesNotThrow(() -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution2() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(institutionConnector.findByExternalId(any()))
                .thenReturn(Optional.of(new Institution("42", "42", "Onboarding institution having externalId {}",
                        "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                        "Onboarding institution having externalId {}", billing, onboarding, geographicTaxonomies, attributes,
                        paymentServiceProvider, new DataProtectionOfficer(), null, null)));

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution3() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(institution).getBilling();
        verify(institution).getDataProtectionOfficer();
        verify(institution).getPaymentServiceProvider();
        verify(institution, atLeast(1)).getAddress();
        verify(institution, atLeast(1)).getDescription();
        verify(institution, atLeast(1)).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getId();
        verify(institution).getIpaCode();
        verify(institution, atLeast(1)).getTaxCode();
        verify(institution, atLeast(1)).getZipCode();
        verify(institution).getAttributes();
        verify(institution).getGeographicTaxonomies();
        verify(institution, atLeast(1)).getOnboarding();
        verify(tokenConnector).save(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution5() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
        verify(institution).getAddress();
        verify(institution).getDescription();
        verify(institution).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution).getTaxCode();
        verify(institution).getZipCode();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution6() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        when(institutionConnector.findByExternalId((String) any()))
                .thenReturn(Optional.of(new Institution("42", "42", "Onboarding institution having externalId {}",
                        "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                        "Onboarding institution having externalId {}", billing, onboarding, geographicTaxonomies, attributes,
                        paymentServiceProvider, new DataProtectionOfficer(), null, null)));

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId((String) any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution7() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
        verify(institution).getDescription();
        verify(institution).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution).getTaxCode();
        verify(institution).getZipCode();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution8() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
        verify(institutionConnector).save((Institution) any());
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getBilling();
        verify(institution).getDataProtectionOfficer();
        verify(institution, atLeast(1)).getInstitutionType();
        verify(institution).getPaymentServiceProvider();
        verify(institution, atLeast(1)).getAddress();
        verify(institution, atLeast(1)).getDescription();
        verify(institution, atLeast(1)).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getId();
        verify(institution).getIpaCode();
        verify(institution, atLeast(1)).getTaxCode();
        verify(institution, atLeast(1)).getZipCode();
        verify(institution).getAttributes();
        verify(institution).getGeographicTaxonomies();
        verify(institution, atLeast(1)).getOnboarding();
        verify(tokenConnector).save((Token) any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution9() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
        verify(institution).getDescription();
        verify(institution).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution).getTaxCode();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution11() {

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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        Assertions.assertDoesNotThrow(() -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution12() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
        verify(institution).getDescription();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution13() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Onboarding institution having externalId {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Onboarding institution having externalId {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Onboarding institution having externalId {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Onboarding institution having externalId {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

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
        when(institution.getBilling()).thenReturn(billing1);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(onboardingList);
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing2);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(institution).getBilling();
        verify(institution).getDataProtectionOfficer();
        verify(institution).getPaymentServiceProvider();
        verify(institution, atLeast(1)).getAddress();
        verify(institution, atLeast(1)).getDescription();
        verify(institution, atLeast(1)).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getId();
        verify(institution).getIpaCode();
        verify(institution, atLeast(1)).getTaxCode();
        verify(institution, atLeast(1)).getZipCode();
        verify(institution).getAttributes();
        verify(institution).getGeographicTaxonomies();
        verify(institution, atLeast(1)).getOnboarding();
        verify(tokenConnector).save(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution14() {

        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

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
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
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
        onboardingRequest.setUsers(new ArrayList<>());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution15() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

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
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
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
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution16() {

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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(ofResult);

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        DataProtectionOfficer dataProtectionOfficer2 = new DataProtectionOfficer();
        dataProtectionOfficer2.setAddress("42 Main St");
        dataProtectionOfficer2.setEmail("jane.doe@example.org");
        dataProtectionOfficer2.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider2 = new PaymentServiceProvider();
        paymentServiceProvider2.setAbiCode("Abi Code");
        paymentServiceProvider2.setBusinessRegisterNumber("42");
        paymentServiceProvider2.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider2.setLegalRegisterNumber("42");
        paymentServiceProvider2.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate1 = new InstitutionUpdate();
        institutionUpdate1.setAddress("42 Main St");
        institutionUpdate1.setDataProtectionOfficer(dataProtectionOfficer2);
        institutionUpdate1.setDescription("The characteristics of someone or something");
        institutionUpdate1.setDigitalAddress("42 Main St");
        institutionUpdate1.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate1.setInstitutionType(InstitutionType.PA);
        institutionUpdate1.setPaymentServiceProvider(paymentServiceProvider2);
        institutionUpdate1.setTaxCode("Tax Code");
        institutionUpdate1.setZipCode("21654");
        OnboardingRequest onboardingRequest = mock(OnboardingRequest.class);
        when(onboardingRequest.getContract())
                .thenThrow(new InvalidRequestException("An error occurred", "Onboarding institution having externalId {}"));
        when(onboardingRequest.getBillingRequest())
                .thenThrow(new InvalidRequestException("An error occurred", "Onboarding institution having externalId {}"));
        when(onboardingRequest.getPricingPlan())
                .thenThrow(new InvalidRequestException("An error occurred", "Onboarding institution having externalId {}"));
        when(onboardingRequest.getProductId())
                .thenThrow(new InvalidRequestException("An error occurred", "Onboarding institution having externalId {}"));
        when(onboardingRequest.getUsers())
                .thenThrow(new InvalidRequestException("An error occurred", "Onboarding institution having externalId {}"));
        when(onboardingRequest.getInstitutionUpdate()).thenReturn(institutionUpdate1);
        when(onboardingRequest.getInstitutionExternalId()).thenReturn("42");
        doNothing().when(onboardingRequest).setBillingRequest(any());
        doNothing().when(onboardingRequest).setContract(any());
        doNothing().when(onboardingRequest).setInstitutionExternalId(any());
        doNothing().when(onboardingRequest).setInstitutionUpdate(any());
        doNothing().when(onboardingRequest).setPricingPlan(any());
        doNothing().when(onboardingRequest).setProductId(any());
        doNothing().when(onboardingRequest).setProductName(any());
        doNothing().when(onboardingRequest).setUsers(any());
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        Assertions.assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution17() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(null);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
        verify(institutionConnector).save((Institution) any());
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getBilling();
        verify(institution).getDataProtectionOfficer();
        verify(institution, atLeast(1)).getInstitutionType();
        verify(institution).getPaymentServiceProvider();
        verify(institution, atLeast(1)).getAddress();
        verify(institution, atLeast(1)).getDescription();
        verify(institution, atLeast(1)).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getId();
        verify(institution).getIpaCode();
        verify(institution, atLeast(1)).getTaxCode();
        verify(institution, atLeast(1)).getZipCode();
        verify(institution).getAttributes();
        verify(institution).getGeographicTaxonomies();
        verify(institution, atLeast(1)).getOnboarding();
        verify(tokenConnector).save((Token) any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution18() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PG);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
        verify(institutionConnector).save((Institution) any());
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getBilling();
        verify(institution).getDataProtectionOfficer();
        verify(institution, atLeast(1)).getInstitutionType();
        verify(institution).getPaymentServiceProvider();
        verify(institution, atLeast(1)).getAddress();
        verify(institution, atLeast(1)).getDescription();
        verify(institution, atLeast(1)).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getId();
        verify(institution).getIpaCode();
        verify(institution, atLeast(1)).getTaxCode();
        verify(institution, atLeast(1)).getZipCode();
        verify(institution).getAttributes();
        verify(institution).getGeographicTaxonomies();
        verify(institution, atLeast(1)).getOnboarding();
        verify(tokenConnector).save((Token) any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution19() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getAddress();
        verify(institution).getDescription();
        verify(institution).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution).getTaxCode();
        verify(institution).getZipCode();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution21() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getDescription();
        verify(institution).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution).getTaxCode();
        verify(institution).getZipCode();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution23() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getDescription();
        verify(institution).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution).getTaxCode();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution26() {
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
        when(institution.getBilling()).thenReturn(billing);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("Onboarding institution having externalId {}");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(InvalidRequestException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getDescription();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getOnboarding();
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution27() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Onboarding institution having externalId {}");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Onboarding institution having externalId {}");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Onboarding institution having externalId {}");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Onboarding institution having externalId {}");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

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
        when(institution.getBilling()).thenReturn(billing1);
        when(institution.getDataProtectionOfficer()).thenReturn(dataProtectionOfficer);
        when(institution.getPaymentServiceProvider()).thenReturn(paymentServiceProvider);
        when(institution.getExternalId()).thenReturn("42");
        when(institution.getIpaCode()).thenReturn("Ipa Code");
        when(institution.getAttributes()).thenReturn(new ArrayList<>());
        when(institution.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institution.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institution.getId()).thenReturn("42");
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(onboardingList);
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing2);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
        verify(institutionConnector).save((Institution) any());
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).getBilling();
        verify(institution).getDataProtectionOfficer();
        verify(institution, atLeast(1)).getInstitutionType();
        verify(institution).getPaymentServiceProvider();
        verify(institution, atLeast(1)).getAddress();
        verify(institution, atLeast(1)).getDescription();
        verify(institution, atLeast(1)).getDigitalAddress();
        verify(institution).getExternalId();
        verify(institution, atLeast(1)).getId();
        verify(institution).getIpaCode();
        verify(institution, atLeast(1)).getTaxCode();
        verify(institution, atLeast(1)).getZipCode();
        verify(institution).getAttributes();
        verify(institution).getGeographicTaxonomies();
        verify(institution, atLeast(1)).getOnboarding();
        verify(tokenConnector).save((Token) any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution29() {
        when(institutionConnector.save((Institution) any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId((String) any())).thenReturn(Optional.empty());

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
        when(tokenConnector.save((Token) any())).thenReturn(token);

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
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
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
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId((String) any());
    }
}

