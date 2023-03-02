package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class InstitutionServiceImplTest {
    @Mock
    private InstitutionConnector institutionConnector;

    @InjectMocks
    private InstitutionServiceImpl institutionServiceImpl;

    @Mock
    private PartyRegistryProxyConnector partyRegistryProxyConnector;

    @Mock
    private GeoTaxonomiesConnector geoTaxonomiesConnector;

    @Mock
    private UserService userService;

    @Test
    void getUserInstitutionRelationships(){
        OnboardedUser onboardedUser = new OnboardedUser();
        List<UserBinding> userBindings = new ArrayList<>();
        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("id");
        List<OnboardedProduct> onboardedProducts = new ArrayList<>();
        onboardedProducts.add(new OnboardedProduct());
        userBinding.setProducts(onboardedProducts);
        userBindings.add(userBinding);

        onboardedUser.setBindings(userBindings);
        List<OnboardedUser> adminRelationships = new ArrayList<>();
        adminRelationships.add(onboardedUser);
        when(userService.retrieveUsers(any(),any(),any(),any(),any(),any())).thenReturn(adminRelationships);
        Institution institution = new Institution();
        institution.setId("id");
        assertNotNull(institutionServiceImpl.getUserInstitutionRelationships(institution,"userId","personId", new ArrayList<>(), new ArrayList<>(),new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void getUserInstitutionRelationships2(){
        when(userService.retrieveUsers(any(),any(),any(),any(),any(),any())).thenReturn(new ArrayList<>());
        assertNotNull(institutionServiceImpl.getUserInstitutionRelationships(new Institution(),"userId","personId", new ArrayList<>(), new ArrayList<>(),new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void updateInstitution(){
        String institutionId = "instituionId";
        List<String> geographicTaxonomies1 = new ArrayList<>();
        geographicTaxonomies1.add("geo");
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setGeographicTaxonomyCodes(geographicTaxonomies1);
        String userId = "userId";
        when(userService.checkIfAdmin(any(),any())).thenReturn(true);
        when(institutionConnector.findAndUpdate(any(),any(), any())).thenReturn(new Institution());
        assertNotNull(institutionServiceImpl.updateInstitution(institutionId,institutionUpdate,userId));
    }

    @Test
    void updateInstitution2(){
        String institutionId = "instituionId";
        List<String> geographicTaxonomies1 = new ArrayList<>();
        geographicTaxonomies1.add("geo");
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setGeographicTaxonomyCodes(geographicTaxonomies1);
        String userId = "userId";
        when(userService.checkIfAdmin(any(),any())).thenReturn(true);
        InvalidRequestException invalidRequestException = mock(InvalidRequestException.class);
        when(institutionConnector.findAndUpdate(any(),any(), any())).thenThrow(invalidRequestException);
        assertThrows(InvalidRequestException.class, () -> institutionServiceImpl.updateInstitution(institutionId,institutionUpdate,userId));
    }

    @Test
    void retrieveInstitutionGeoTaxonomies(){
        Institution institution = new Institution();
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setCode("code");
        List<GeographicTaxonomies> geographicTaxonomies1 = new ArrayList<>();
        geographicTaxonomies1.add(geographicTaxonomies);
        institution.setGeographicTaxonomies(geographicTaxonomies1);

        assertNotNull(institutionServiceImpl.retrieveInstitutionGeoTaxonomies(institution));
    }

    @Test
    void retrieveInstitutionProducts(){
        Institution institution = new Institution();
        List<RelationshipState> states = new ArrayList<>();
        states.add(RelationshipState.ACTIVE);
        List<Onboarding> onboardings = new ArrayList<>();
        Onboarding onboarding = new Onboarding();
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboardings.add(onboarding);
        institution.setOnboarding(onboardings);

        assertNotNull(institutionServiceImpl.retrieveInstitutionProducts(institution,states));
    }

    @Test
    void retrieveInstitutionProducts2(){
        Institution institution = new Institution();
        List<Onboarding> onboardings = new ArrayList<>();
        Onboarding onboarding = new Onboarding();
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboardings.add(onboarding);
        institution.setOnboarding(onboardings);

        assertNotNull(institutionServiceImpl.retrieveInstitutionProducts(institution,null));
    }

    @Test
    void retrieveInstitutionProducts3(){
        Institution institution = new Institution();
        List<RelationshipState> states = new ArrayList<>();
        states.add(RelationshipState.ACTIVE);
        institution.setOnboarding(null);

        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.retrieveInstitutionProducts(institution,states));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionById(String)}
     */
    @Test
    void testRetrieveInstitutionById() {
        Institution institution = new Institution();
        when(institutionConnector.findById(any())).thenReturn(institution);
        assertSame(institution, institutionServiceImpl.retrieveInstitutionById("42"));
        verify(institutionConnector).findById(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionById(String)}
     */
    @Test
    void testRetrieveInstitutionById2() {
        when(institutionConnector.findById(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.retrieveInstitutionById("42"));
        verify(institutionConnector).findById(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionByExternalId() {
        Institution institution = new Institution();
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(institution));
        assertSame(institution, institutionServiceImpl.retrieveInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionByExternalId2() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.retrieveInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionByExternalId3() {
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.retrieveInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        assertThrows(ResourceConflictException.class, () -> institutionServiceImpl.createInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId2() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionProxyInfo institutionProxyInfo = new InstitutionProxyInfo();
        institutionProxyInfo.setAddress("42 Main St");
        institutionProxyInfo.setAoo("Aoo");
        institutionProxyInfo.setCategory("Category");
        institutionProxyInfo.setDescription("The characteristics of someone or something");
        institutionProxyInfo.setDigitalAddress("42 Main St");
        institutionProxyInfo.setId("42");
        institutionProxyInfo.setO("foo");
        institutionProxyInfo.setOrigin("Origin");
        institutionProxyInfo.setOriginId("42");
        institutionProxyInfo.setOu("Ou");
        institutionProxyInfo.setTaxCode("Tax Code");
        institutionProxyInfo.setZipCode("21654");

        CategoryProxyInfo categoryProxyInfo = new CategoryProxyInfo();
        categoryProxyInfo.setCode("Code");
        categoryProxyInfo.setKind("Kind");
        categoryProxyInfo.setName("Name");
        categoryProxyInfo.setOrigin("Origin");
        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(categoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(institutionProxyInfo);
        assertSame(institution, institutionServiceImpl.createInstitutionByExternalId("42"));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId3() {
        when(institutionConnector.save(any()))
                .thenThrow(new ResourceConflictException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionProxyInfo institutionProxyInfo = new InstitutionProxyInfo();
        institutionProxyInfo.setAddress("42 Main St");
        institutionProxyInfo.setAoo("Aoo");
        institutionProxyInfo.setCategory("Category");
        institutionProxyInfo.setDescription("The characteristics of someone or something");
        institutionProxyInfo.setDigitalAddress("42 Main St");
        institutionProxyInfo.setId("42");
        institutionProxyInfo.setO("foo");
        institutionProxyInfo.setOrigin("Origin");
        institutionProxyInfo.setOriginId("42");
        institutionProxyInfo.setOu("Ou");
        institutionProxyInfo.setTaxCode("Tax Code");
        institutionProxyInfo.setZipCode("21654");

        CategoryProxyInfo categoryProxyInfo = new CategoryProxyInfo();
        categoryProxyInfo.setCode("Code");
        categoryProxyInfo.setKind("Kind");
        categoryProxyInfo.setName("Name");
        categoryProxyInfo.setOrigin("Origin");
        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(categoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(institutionProxyInfo);
        assertThrows(ResourceConflictException.class, () -> institutionServiceImpl.createInstitutionByExternalId("42"));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId4() {
        InstitutionProxyInfo institutionProxyInfo = new InstitutionProxyInfo();
        institutionProxyInfo.setAddress("42 Main St");
        institutionProxyInfo.setAoo("Aoo");
        institutionProxyInfo.setCategory("Category");
        institutionProxyInfo.setDescription("The characteristics of someone or something");
        institutionProxyInfo.setDigitalAddress("42 Main St");
        institutionProxyInfo.setId("42");
        institutionProxyInfo.setO("foo");
        institutionProxyInfo.setOrigin("Origin");
        institutionProxyInfo.setOriginId("42");
        institutionProxyInfo.setOu("Ou");
        institutionProxyInfo.setTaxCode("Tax Code");
        institutionProxyInfo.setZipCode("21654");

        CategoryProxyInfo categoryProxyInfo = new CategoryProxyInfo();
        categoryProxyInfo.setCode("Code");
        categoryProxyInfo.setKind("Kind");
        categoryProxyInfo.setName("Name");
        categoryProxyInfo.setOrigin("Origin");
        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(categoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(institutionProxyInfo);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        assertThrows(ResourceConflictException.class, () -> institutionServiceImpl.createInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId5() {
        InstitutionProxyInfo institutionProxyInfo = new InstitutionProxyInfo();
        institutionProxyInfo.setAddress("42 Main St");
        institutionProxyInfo.setAoo("Aoo");
        institutionProxyInfo.setCategory("Category");
        institutionProxyInfo.setDescription("The characteristics of someone or something");
        institutionProxyInfo.setDigitalAddress("42 Main St");
        institutionProxyInfo.setId("42");
        institutionProxyInfo.setO("foo");
        institutionProxyInfo.setOrigin("Origin");
        institutionProxyInfo.setOriginId("42");
        institutionProxyInfo.setOu("Ou");
        institutionProxyInfo.setTaxCode("Tax Code");
        institutionProxyInfo.setZipCode("21654");

        CategoryProxyInfo categoryProxyInfo = new CategoryProxyInfo();
        categoryProxyInfo.setCode("Code");
        categoryProxyInfo.setKind("Kind");
        categoryProxyInfo.setName("Name");
        categoryProxyInfo.setOrigin("Origin");
        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(categoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(institutionProxyInfo);
        when(institutionConnector.save(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - check institution {} already exists"));
        assertThrows(InvalidRequestException.class, () -> institutionServiceImpl.createInstitutionByExternalId("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId6() {
        InstitutionProxyInfo institutionProxyInfo = new InstitutionProxyInfo();
        institutionProxyInfo.setAddress("42 Main St");
        institutionProxyInfo.setAoo("Aoo");
        institutionProxyInfo.setCategory("Category");
        institutionProxyInfo.setDescription("The characteristics of someone or something");
        institutionProxyInfo.setDigitalAddress("42 Main St");
        institutionProxyInfo.setId("42");
        institutionProxyInfo.setO("foo");
        institutionProxyInfo.setOrigin("Origin");
        institutionProxyInfo.setOriginId("42");
        institutionProxyInfo.setOu("Ou");
        institutionProxyInfo.setTaxCode("Tax Code");
        institutionProxyInfo.setZipCode("21654");

        CategoryProxyInfo categoryProxyInfo = new CategoryProxyInfo();
        categoryProxyInfo.setCode("Code");
        categoryProxyInfo.setKind("Kind");
        categoryProxyInfo.setName("Name");
        categoryProxyInfo.setOrigin("Origin");
        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(categoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(institutionProxyInfo);
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        assertSame(institution, institutionServiceImpl.createInstitutionByExternalId("42"));
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution2() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(new ArrayList<>());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertThrows(InvalidRequestException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, selfCareUser));
        verify(institutionConnector).findByExternalId(any());
        verify(partyRegistryProxyConnector).getInstitutionsByLegal(any());
        verify(selfCareUser).getFiscalCode();
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution3() {
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(new ArrayList<>());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution4() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        ArrayList<InstitutionByLegal> institutionByLegalList = new ArrayList<>();
        institutionByLegalList.add(institutionByLegal);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(institutionByLegalList);
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", true, selfCareUser));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(partyRegistryProxyConnector).getInstitutionsByLegal(any());
        verify(selfCareUser).getFiscalCode();
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution5() {
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(new ArrayList<>());
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "START - check institution {} already exists"));
        assertThrows(ResourceNotFoundException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution6() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        ArrayList<InstitutionByLegal> institutionByLegalList = new ArrayList<>();
        institutionByLegalList.add(institutionByLegal);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(institutionByLegalList);
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", false, selfCareUser));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution7() {
        when(institutionConnector.save(any()))
                .thenThrow(new ResourceConflictException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        ArrayList<InstitutionByLegal> institutionByLegalList = new ArrayList<>();
        institutionByLegalList.add(institutionByLegal);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(institutionByLegalList);
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, selfCareUser));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(partyRegistryProxyConnector).getInstitutionsByLegal(any());
        verify(selfCareUser).getFiscalCode();
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution8() {
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(new ArrayList<>());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertThrows(InvalidRequestException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, selfCareUser));
        verify(partyRegistryProxyConnector).getInstitutionsByLegal(any());
        verify(institutionConnector).findByExternalId(any());
        verify(selfCareUser).getFiscalCode();
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution9() {
        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        ArrayList<InstitutionByLegal> institutionByLegalList = new ArrayList<>();
        institutionByLegalList.add(institutionByLegal);

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setAddress("42 Main St");
        nationalRegistriesProfessionalAddress.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalAddress.setMunicipality("Municipality");
        nationalRegistriesProfessionalAddress.setProvince("Province");
        nationalRegistriesProfessionalAddress.setZip("21654");
        when(partyRegistryProxyConnector.getLegalAddress(any()))
                .thenReturn(nationalRegistriesProfessionalAddress);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(institutionByLegalList);
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", true, selfCareUser));
        verify(partyRegistryProxyConnector).getLegalAddress(any());
        verify(partyRegistryProxyConnector).getInstitutionsByLegal(any());
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(selfCareUser).getFiscalCode();
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution10() {
        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        ArrayList<InstitutionByLegal> institutionByLegalList = new ArrayList<>();
        institutionByLegalList.add(institutionByLegal);

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setAddress("42 Main St");
        nationalRegistriesProfessionalAddress.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalAddress.setMunicipality("Municipality");
        nationalRegistriesProfessionalAddress.setProvince("Province");
        nationalRegistriesProfessionalAddress.setZip("21654");
        when(partyRegistryProxyConnector.getLegalAddress(any()))
                .thenReturn(nationalRegistriesProfessionalAddress);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(institutionByLegalList);
        when(institutionConnector.save(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertThrows(ResourceNotFoundException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, selfCareUser));
        verify(partyRegistryProxyConnector).getLegalAddress(any());
        verify(partyRegistryProxyConnector).getInstitutionsByLegal(any());
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        verify(selfCareUser).getFiscalCode();
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution11() {
        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("START - check institution {} already exists");

        ArrayList<InstitutionByLegal> institutionByLegalList = new ArrayList<>();
        institutionByLegalList.add(institutionByLegal);

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setAddress("42 Main St");
        nationalRegistriesProfessionalAddress.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalAddress.setMunicipality("Municipality");
        nationalRegistriesProfessionalAddress.setProvince("Province");
        nationalRegistriesProfessionalAddress.setZip("21654");
        when(partyRegistryProxyConnector.getLegalAddress(any()))
                .thenReturn(nationalRegistriesProfessionalAddress);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(institutionByLegalList);
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertThrows(InvalidRequestException.class,
                () -> institutionServiceImpl.createPgInstitution("42", true, selfCareUser));
        verify(partyRegistryProxyConnector).getInstitutionsByLegal(any());
        verify(institutionConnector).findByExternalId(any());
        verify(selfCareUser).getFiscalCode();
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution14() {
        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        ArrayList<InstitutionByLegal> institutionByLegalList = new ArrayList<>();
        institutionByLegalList.add(institutionByLegal);

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setAddress("42 Main St");
        nationalRegistriesProfessionalAddress.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalAddress.setMunicipality("Municipality");
        nationalRegistriesProfessionalAddress.setProvince("Province");
        nationalRegistriesProfessionalAddress.setZip("21654");
        when(partyRegistryProxyConnector.getLegalAddress(any()))
                .thenReturn(nationalRegistriesProfessionalAddress);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(institutionByLegalList);
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        when(selfCareUser.getFiscalCode()).thenReturn("Fiscal Code");
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", false, selfCareUser));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        Institution institution = new Institution();
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createInstitutionRaw(institution, "42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw2() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        Institution institution1 = new Institution();
        assertSame(institution, institutionServiceImpl.createInstitutionRaw(institution1, "42"));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        assertEquals(InstitutionType.UNKNOWN, institution1.getInstitutionType());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw3() {
        when(institutionConnector.save(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - check institution {} already exists"));
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> institutionServiceImpl.createInstitutionRaw(institution, "42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw4() {
        when(institutionConnector.save(any())).thenReturn(new Institution());
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        Institution institution = new Institution();
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createInstitutionRaw(institution, "42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw5() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        Institution institution1 = new Institution();
        assertSame(institution, institutionServiceImpl.createInstitutionRaw(institution1, "42"));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
        assertEquals(InstitutionType.UNKNOWN, institution1.getInstitutionType());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw6() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        assertSame(institution, institutionServiceImpl.createInstitutionRaw(new Institution("42", "42",
                "START - check institution {} already exists", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "START - check institution {} already exists",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                null, null, "START - check institution {} already exists", "START - check institution {} already exists",
                "START - check institution {} already exists", "jane.doe@example.org", "4105551212", true), "42"));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw7() {
        when(institutionConnector.save(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - check institution {} already exists"));
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> institutionServiceImpl.createInstitutionRaw(institution, "42"));
        verify(institutionConnector).findByExternalId(any());
    }


    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw9() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        assertSame(institution, institutionServiceImpl.createInstitutionRaw(new Institution("42", "42",
                "START - check institution {} already exists", "The characteristics of someone or something",
                InstitutionType.PA, "42 Main St", "42 Main St", "21654", "START - check institution {} already exists",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                null, null, "START - check institution {} already exists", "START - check institution {} already exists",
                "START - check institution {} already exists", "jane.doe@example.org", "4105551212", true), "42"));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }


    /**
     * Method under test: {@link InstitutionServiceImpl#getInstitutionProduct(String, String)}
     */
    @Test
    void testGetInstitutionProduct() {
        Institution institution = new Institution();
        when(institutionConnector.findInstitutionProduct(any(), any())).thenReturn(institution);
        assertSame(institution, institutionServiceImpl.getInstitutionProduct("42", "42"));
        verify(institutionConnector).findInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#getInstitutionProduct(String, String)}
     */
    @Test
    void testGetInstitutionProduct2() {
        when(institutionConnector.findInstitutionProduct(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.getInstitutionProduct("42", "42"));
        verify(institutionConnector).findInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#checkIfAlreadyExists(String)}
     */
    @Test
    void testCheckIfAlreadyExists() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        assertThrows(ResourceConflictException.class, () -> institutionServiceImpl.checkIfAlreadyExists("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#checkIfAlreadyExists(String)}
     */
    @Test
    void testCheckIfAlreadyExists2() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        institutionServiceImpl.checkIfAlreadyExists("42");
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#checkIfAlreadyExists(String)}
     */
    @Test
    void testCheckIfAlreadyExists3() {
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "START - check institution {} already exists"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.checkIfAlreadyExists("42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#getGeoTaxonomies(String)}
     */
    @Test
    void testGetGeoTaxonomies() {
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setCode("Code");
        geographicTaxonomies.setCountry("GB");
        geographicTaxonomies.setCountryAbbreviation("GB");
        geographicTaxonomies.setDesc("The characteristics of someone or something");
        geographicTaxonomies.setEnable(true);
        geographicTaxonomies.setEndDate("2020-03-01");
        geographicTaxonomies.setProvince("Province");
        geographicTaxonomies.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomies.setRegion("us-east-2");
        geographicTaxonomies.setStartDate("2020-03-01");
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);
        assertSame(geographicTaxonomies, institutionServiceImpl.getGeoTaxonomies("Code"));
        verify(geoTaxonomiesConnector).getExtByCode(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#getGeoTaxonomies(String)}
     */
    @Test
    void testGetGeoTaxonomies2() {
        when(geoTaxonomiesConnector.getExtByCode(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.getGeoTaxonomies("Code"));
        verify(geoTaxonomiesConnector).getExtByCode(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionsWithFilter(String, String, List)}
     */
    @Test
    void testRetrieveInstitutionsWithFilter() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(new ArrayList<>());
        List<RelationshipState> list = List.of(RelationshipState.ACTIVE);
        assertThrows(ResourceNotFoundException.class,
                () -> institutionServiceImpl.retrieveInstitutionsWithFilter("42", "42", list));
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionsWithFilter(String, String, List)}
     */
    @Test
    void testRetrieveInstitutionsWithFilter2() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(institutionList);
        institutionServiceImpl.retrieveInstitutionsWithFilter("42", "42", new ArrayList<>());
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionsWithFilter(String, String, List)}
     */
    @Test
    void testRetrieveInstitutionsWithFilter3() {
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        List<RelationshipState> list = List.of(RelationshipState.ACTIVE);
        assertThrows(ResourceNotFoundException.class,
                () -> institutionServiceImpl.retrieveInstitutionsWithFilter("42", "42", list));
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }
}

