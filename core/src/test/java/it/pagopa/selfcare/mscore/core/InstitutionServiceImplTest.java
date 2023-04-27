package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.exception.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceImplTest {
    @Mock
    private InstitutionConnector institutionConnector;

    @InjectMocks
    private InstitutionServiceImpl institutionServiceImpl;

    @Mock
    private PartyRegistryProxyConnector partyRegistryProxyConnector;

    @Mock
    private UserService userService;

    @Mock
    private GeoTaxonomiesConnector geoTaxonomiesConnector;

    @Mock
    private CoreConfig coreConfig;

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
        assertThrows(MsCoreException.class, () -> institutionServiceImpl.createInstitutionByExternalId("42"));
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
     * Method under test: {@link InstitutionServiceImpl#createPnPgInstitution(String, String)}
     */
    @Test
    void testCreatePnPgInstitution2() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        Institution institution = new Institution();
        when(institutionConnector.saveOrRetrievePnPg(any())).thenReturn(institution);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        assertSame(institution, (new InstitutionServiceImpl(null, institutionConnector, geoTaxonomiesConnector,
                userService, new CoreConfig())).createPnPgInstitution("42", "The characteristics of someone or something"));
        verify(institutionConnector).saveOrRetrievePnPg(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createPgInstitution("42", "42", true, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution3() {
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createPgInstitution("42", "42", true, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution4() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        Institution institutionResult = institutionServiceImpl.createPgInstitution("42", "42", false, selfCareUser);
        assertSame(institution, institutionResult);
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution2() {
        Institution institution = new Institution();
        when(coreConfig.isInfoCamereEnable()).thenReturn(true);
        List<InstitutionByLegal> list = new ArrayList<>();
        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");
        list.add(institutionByLegal);
        when(partyRegistryProxyConnector.getInstitutionsByLegal(any())).thenReturn(list);
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", "42", true, selfCareUser));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution5() {
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "START - check institution {} already exists"));
        assertThrows(ResourceNotFoundException.class,
                () -> institutionServiceImpl.createPgInstitution("42", "42", true, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution6() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", "42", false, selfCareUser));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution7() {
        when(institutionConnector.save(any()))
                .thenThrow(new ResourceConflictException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        assertThrows(ResourceConflictException.class,
                () -> institutionServiceImpl.createPgInstitution("42", "42", true, selfCareUser));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution9() {
        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setAddress("42 Main St");
        nationalRegistriesProfessionalAddress.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalAddress.setMunicipality("Municipality");
        nationalRegistriesProfessionalAddress.setProvince("Province");
        nationalRegistriesProfessionalAddress.setZip("21654");
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", "42", true, selfCareUser));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createPgInstitution(String, String, boolean, SelfCareUser)}
     */
    @Test
    void testCreatePgInstitution14() {
        InstitutionByLegal institutionByLegal = new InstitutionByLegal();
        institutionByLegal.setBusinessName("START - check institution {} already exists");
        institutionByLegal.setBusinessTaxId("42");

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setAddress("42 Main St");
        nationalRegistriesProfessionalAddress.setDescription("The characteristics of someone or something");
        nationalRegistriesProfessionalAddress.setMunicipality("Municipality");
        nationalRegistriesProfessionalAddress.setProvince("Province");
        nationalRegistriesProfessionalAddress.setZip("21654");
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());
        SelfCareUser selfCareUser = mock(SelfCareUser.class);
        assertSame(institution, institutionServiceImpl.createPgInstitution("42", "42", false, selfCareUser));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw() {
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
        assertNull(institution1.getInstitutionType());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw3() {
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
        assertNull(institution1.getInstitutionType());
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
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        assertSame(institution, institutionServiceImpl.createInstitutionRaw(new Institution("42", "42", Origin.SELC.name(), "",
                "START - check institution {} already exists", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "START - check institution {} already exists",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider, new DataProtectionOfficer(),
                null, null, "START - check institution {} already exists", "START - check institution {} already exists",
                "START - check institution {} already exists", true, OffsetDateTime.now(), OffsetDateTime.now()), "42"));
        verify(institutionConnector).save(any());
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw7() {
        when(institutionConnector.findByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "START - check institution {} already exists"));
        Institution institution = new Institution();
        assertThrows(InvalidRequestException.class,
                () -> institutionServiceImpl.createInstitutionRaw(institution, "42"));
        verify(institutionConnector).findByExternalId(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionProducts(Institution, List)}
     */
    @Test
    void testRetrieveInstitutionProducts() {
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());
        Institution institution = new Institution();
        Onboarding onboarding = new Onboarding();
        onboarding.setStatus(RelationshipState.PENDING);
        List<RelationshipState> list = new ArrayList<>();
        list.add(RelationshipState.PENDING);
        institution.setOnboarding(List.of(onboarding));
        assertEquals(1, institutionServiceImpl.retrieveInstitutionProducts(institution, list).size());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionProducts(Institution, List)}
     */
    @Test
    void testRetrieveInstitutionProducts3() {
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());
        Institution institution = new Institution();
        Onboarding onboarding = new Onboarding();
        onboarding.setStatus(RelationshipState.PENDING);
        institution.setOnboarding(List.of(onboarding));
        List<RelationshipState> list = new ArrayList<>();
        assertEquals(1, institutionServiceImpl.retrieveInstitutionProducts(institution, list).size());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testRetrieveInstitutionProduct2() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        Institution institution = new Institution();
        when(institutionConnector.findInstitutionProduct(any(), any())).thenReturn(institution);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        assertSame(institution, (new InstitutionServiceImpl(null, institutionConnector, geoTaxonomiesConnector,
                userService, new CoreConfig())).retrieveInstitutionProduct("42", "42"));
        verify(institutionConnector).findInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionGeoTaxonomies(Institution)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomies2() {
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(new ArrayList<>());
        Assertions.assertThrows(MsCoreException.class, () -> institutionServiceImpl.retrieveInstitutionGeoTaxonomies(institution));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionGeoTaxonomies(Institution)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomies4() {
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setGeotaxId("Code");
        geographicTaxonomies.setCountry("GB");
        geographicTaxonomies.setCountryAbbreviation("GB");
        geographicTaxonomies.setDescription("The characteristics of someone or something");
        geographicTaxonomies.setEnable(true);
        geographicTaxonomies.setIstatCode("");
        geographicTaxonomies.setProvinceId("Province");
        geographicTaxonomies.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomies.setRegionId("us-east-2");
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());

        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList.add(new InstitutionGeographicTaxonomies(
                "Retrieving geographic taxonomies for institution {}", "The characteristics of someone or something"));

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        assertEquals(1, institutionServiceImpl.retrieveInstitutionGeoTaxonomies(institution).size());
        verify(geoTaxonomiesConnector).getExtByCode(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#updateInstitution(String, InstitutionUpdate, String)}
     */
    @Test
    void testUpdateInstitution6() {
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        when(geoTaxonomiesConnector.getExtByCode(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        when(userServiceImpl.checkIfAdmin(any(), any())).thenReturn(true);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userServiceImpl, new CoreConfig());

        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
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
        assertThrows(ResourceNotFoundException.class,
                () -> institutionServiceImpl.updateInstitution("42", institutionUpdate, "42"));
        verify(geoTaxonomiesConnector).getExtByCode(any());
        verify(userServiceImpl).checkIfAdmin(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#updateInstitution(String, InstitutionUpdate, String)}
     */
    @Test
    void testUpdateInstitution7() {
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        when(userServiceImpl.checkIfAdmin(any(), any())).thenReturn(false);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userServiceImpl, new CoreConfig());

        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
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
        assertThrows(ResourceForbiddenException.class,
                () -> institutionServiceImpl.updateInstitution("42", institutionUpdate, "42"));
        verify(userServiceImpl).checkIfAdmin(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds2() {

        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        when(institutionConnector.findByExternalIdAndProductId(any(),
                any()))
                .thenReturn(new ArrayList<>());
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());
        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");
        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertTrue(actualRetrieveInstitutionByExternalIdsResult.isEmpty());
        verify(institutionConnector).findByExternalIdAndProductId(any(),
                any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds3() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        when(institutionConnector.findByExternalIdAndProductId(any(),
                any()))
                .thenReturn(new ArrayList<>());
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());

        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");
        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertEquals(1, actualRetrieveInstitutionByExternalIdsResult.size());
        verify(institutionConnector).findByExternalIdAndProductId(any(),
                any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds4() {

        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        when(institutionConnector.findByExternalIdAndProductId(any(),
                any()))
                .thenReturn(new ArrayList<>());
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());

        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");
        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertEquals(2, actualRetrieveInstitutionByExternalIdsResult.size());
        verify(institutionConnector).findByExternalIdAndProductId(any(),
                any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds5() {

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("42");
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        when(institutionConnector.findByExternalIdAndProductId(any(),
                any()))
                .thenReturn(stringList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());

        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");
        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertTrue(actualRetrieveInstitutionByExternalIdsResult.isEmpty());
        verify(institutionConnector).findByExternalIdAndProductId(any(),
                any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#findInstitutionsByGeoTaxonomies(String, SearchMode)}
     */
    @Test
    void testFindInstitutionsByGeoTaxonomies3() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ArrayList<Institution> institutionList = new ArrayList<>();
        when(institutionConnector.findByGeotaxonomies(any(), any()))
                .thenReturn(institutionList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        List<Institution> actualFindInstitutionsByGeoTaxonomiesResult = (new InstitutionServiceImpl(null,
                institutionConnector, geoTaxonomiesConnector, userService, new CoreConfig()))
                .findInstitutionsByGeoTaxonomies("Geo Taxonomies", SearchMode.ALL);
        assertSame(institutionList, actualFindInstitutionsByGeoTaxonomiesResult);
        assertTrue(actualFindInstitutionsByGeoTaxonomiesResult.isEmpty());
        verify(institutionConnector).findByGeotaxonomies(any(), any());
    }

    @Test
    void testFindInstitutionsByGeoTaxonomies1() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionService = (new InstitutionServiceImpl(null,
                institutionConnector, geoTaxonomiesConnector, userService, new CoreConfig()));
        assertThrows(InvalidRequestException.class, () -> institutionService.findInstitutionsByGeoTaxonomies("", SearchMode.ALL));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#findInstitutionsByProductId(String)}
     */
    @Test
    void testFindInstitutionsByProductId2() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        when(institutionConnector.findByProductId(any())).thenReturn(new ArrayList<>());
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionService = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, coreConfig);
        assertThrows(ResourceNotFoundException.class, () -> institutionService.findInstitutionsByProductId("42"));
    }


    /**
     * Method under test: {@link InstitutionServiceImpl#findInstitutionsByProductId(String)}
     */
    @Test
    void testFindInstitutionsByProductId3() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);

        when(institutionConnector.findByProductId(any())).thenReturn(List.of(new Institution()));
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        assertDoesNotThrow(() -> (new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, coreConfig)).findInstitutionsByProductId("42"));
    }


    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByIds(List)}
     */
    @Test
    void testRetrieveInstitutionByIds2() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ArrayList<Institution> institutionList = new ArrayList<>();
        when(institutionConnector.findAllByIds(any())).thenReturn(institutionList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());
        List<Institution> actualRetrieveInstitutionByIdsResult = institutionServiceImpl
                .retrieveInstitutionByIds(new ArrayList<>());
        assertSame(institutionList, actualRetrieveInstitutionByIdsResult);
        assertTrue(actualRetrieveInstitutionByIdsResult.isEmpty());
        verify(institutionConnector).findAllByIds(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserInstitutionRelationships(Institution, String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserInstitutionRelationships3() {
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        when(userServiceImpl.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(new ArrayList<>());
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userServiceImpl, new CoreConfig());
        Institution institution = new Institution();
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(institutionServiceImpl
                .retrieveUserInstitutionRelationships(institution, "42", "42", roles, states, products, new ArrayList<>())
                .isEmpty());
        verify(userServiceImpl, atLeast(1)).retrieveUsers(any(), any(), any(),
                any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserInstitutionRelationships(Institution, String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserInstitutionRelationships9() {
        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        when(userServiceImpl.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userServiceImpl, new CoreConfig());
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        Institution institution = new Institution("42", "42", Origin.MOCK.name(), "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "Tax Code", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"), "Rea", "Share Capital",
                "Business Register Place", "jane.doe@example.org", "6625550144", true, null, null);

        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(institutionServiceImpl
                .retrieveUserInstitutionRelationships(institution, "42", "42", roles, states, products, new ArrayList<>())
                .isEmpty());
        verify(userServiceImpl, atLeast(1)).retrieveUsers(any(), any(), any(),
                any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserRelationships2() {

        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        List<String> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class, () -> institutionServiceImpl.retrieveUserRelationships(null, null,
                roles, states, products, list));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserRelationships4() {
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        when(userServiceImpl.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(new ArrayList<>());
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userServiceImpl, new CoreConfig());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(
                institutionServiceImpl.retrieveUserRelationships("42", "42", roles, states, products, new ArrayList<>())
                        .isEmpty());
        verify(userServiceImpl).retrieveUsers(any(), any(), any(),
                any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserRelationships6() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        when(userServiceImpl.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userServiceImpl, new CoreConfig());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(
                institutionServiceImpl.retrieveUserRelationships("42", "42", roles, states, products, new ArrayList<>())
                        .isEmpty());
        verify(userServiceImpl).retrieveUsers(any(), any(), any(),
                any(), any(), any());
    }

    @Test
    void testRetrieveUserRelationships9() {
        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(new ArrayList<>());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        when(userServiceImpl.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userServiceImpl, new CoreConfig());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(
                institutionServiceImpl.retrieveUserRelationships("42", "42", roles, states, products, new ArrayList<>())
                        .isEmpty());
        verify(userServiceImpl).retrieveUsers(any(), any(), any(),
                any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserRelationships13() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        when(institutionConnector.findById(any())).thenReturn(new Institution());
        UserService userService = mock(UserService.class);
        when(userService.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(new ArrayList<>());
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(
                institutionServiceImpl.retrieveUserRelationships(null, "42", roles, states, products, new ArrayList<>())
                        .isEmpty());
        verify(institutionConnector).findById(any());
        verify(userService).retrieveUsers(any(), any(), any(),
                any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testGetInstitutionProduct() {
        Institution institution = new Institution();
        when(institutionConnector.findInstitutionProduct(any(), any())).thenReturn(institution);
        assertSame(institution, institutionServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionConnector).findInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testGetInstitutionProduct2() {
        when(institutionConnector.findInstitutionProduct(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.retrieveInstitutionProduct("42", "42"));
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
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct5() {

        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        binding.setProducts(List.of(product));
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        Institution institution = new Institution("42", "42", Origin.MOCK.name(), "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                "Tax Code", billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                dataProtectionOfficer, "Rea", "Share Capital", "Business Register Place", "jane.doe@example.org",
                "6625550144", true, null, null);

        assertEquals("42 Main St", institution.getAddress());
        assertTrue(institution.isImported());
        assertEquals("21654", institution.getZipCode());
        assertEquals("Tax Code", institution.getTaxCode());
        assertEquals("6625550144", institution.getSupportPhone());
        assertEquals("jane.doe@example.org", institution.getSupportEmail());
        assertEquals("Share Capital", institution.getShareCapital());
        assertEquals("Rea", institution.getRea());
        assertSame(paymentServiceProvider, institution.getPaymentServiceProvider());
        assertEquals("42", institution.getOriginId());
        assertEquals("Business Register Place", institution.getBusinessRegisterPlace());
        assertEquals("42 Main St", institution.getDigitalAddress());
        assertEquals("42", institution.getExternalId());
        assertEquals(Origin.MOCK.name(), institution.getOrigin());
        assertEquals(InstitutionType.PA, institution.getInstitutionType());
        assertEquals("42", institution.getId());
        assertSame(billing, institution.getBilling());
        assertSame(dataProtectionOfficer, institution.getDataProtectionOfficer());
        assertEquals("The characteristics of someone or something", institution.getDescription());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct6() {

        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector, geoTaxonomiesConnector,
                userService, new CoreConfig());
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("43");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        binding.setProducts(List.of(product));
        when(institutionConnector.findById(any())).thenReturn(new Institution());

        Assertions.assertDoesNotThrow(() -> institutionServiceImpl.retrieveAllProduct("42", binding, null, null, null, null, null));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct_filterProduct_allEmpty() {

        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());
        List<RelationshipInfo> relationshipInfoList;
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        binding.setProducts(List.of(product));

        Institution institution = new Institution();
        institution.setId("42");

        List<PartyRole> roles = Collections.emptyList();
        List<RelationshipState> states = Collections.emptyList();
        List<String> products = Collections.emptyList();
        List<String> productRoles = Collections.emptyList();

        relationshipInfoList = institutionServiceImpl.retrieveAllProduct("42", binding, institution, roles, states, products, productRoles);

        assertFalse(relationshipInfoList.isEmpty());
        assertEquals(relationshipInfoList.get(0).getOnboardedProduct().getProductId(), binding.getProducts().get(0).getProductId());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct_filterProduct_noRoleFound() {
        // Given
        List<RelationshipInfo> relationshipInfoList;
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        product.setRole(PartyRole.MANAGER);
        binding.setProducts(List.of(product));

        Institution institution = new Institution();
        institution.setId("42");

        List<PartyRole> roles = new ArrayList<>();
        roles.add(PartyRole.DELEGATE);
        List<RelationshipState> states = Collections.emptyList();
        List<String> products = Collections.emptyList();
        List<String> productRoles = Collections.emptyList();

        // When
        relationshipInfoList = institutionServiceImpl.retrieveAllProduct("42", binding, institution, roles, states, products, productRoles);

        // Then
        assertTrue(relationshipInfoList.isEmpty());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct_filterProduct_noStatesFound() {
        // Given
        List<RelationshipInfo> relationshipInfoList;
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        product.setRole(PartyRole.DELEGATE);
        product.setStatus(RelationshipState.SUSPENDED);
        binding.setProducts(List.of(product));

        Institution institution = new Institution();
        institution.setId("42");

        List<PartyRole> roles = new ArrayList<>();
        roles.add(PartyRole.DELEGATE);
        List<RelationshipState> states = new ArrayList<>();
        states.add(RelationshipState.ACTIVE);
        List<String> products = Collections.emptyList();
        List<String> productRoles = Collections.emptyList();

        // When
        relationshipInfoList = institutionServiceImpl.retrieveAllProduct("42", binding, institution, roles, states, products, productRoles);

        // Then
        assertTrue(relationshipInfoList.isEmpty());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct_filterProduct_noProductsFound() {
        // Given
        List<RelationshipInfo> relationshipInfoList;
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        product.setRole(PartyRole.DELEGATE);
        product.setStatus(RelationshipState.ACTIVE);
        binding.setProducts(List.of(product));

        Institution institution = new Institution();
        institution.setId("42");

        List<PartyRole> roles = new ArrayList<>();
        roles.add(PartyRole.DELEGATE);
        List<RelationshipState> states = new ArrayList<>();
        states.add(RelationshipState.ACTIVE);
        List<String> products = new ArrayList<>();
        products.add("productIdNotFound");
        List<String> productRoles = Collections.emptyList();

        // When
        relationshipInfoList = institutionServiceImpl.retrieveAllProduct("42", binding, institution, roles, states, products, productRoles);

        // Then
        assertTrue(relationshipInfoList.isEmpty());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct_filterProduct_noProductRolesFound() {
        // Given
        List<RelationshipInfo> relationshipInfoList;
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        product.setRole(PartyRole.DELEGATE);
        product.setStatus(RelationshipState.ACTIVE);
        product.setProductRole("Operator API");
        binding.setProducts(List.of(product));

        Institution institution = new Institution();
        institution.setId("42");

        List<PartyRole> roles = new ArrayList<>();
        roles.add(PartyRole.DELEGATE);
        List<RelationshipState> states = new ArrayList<>();
        states.add(RelationshipState.ACTIVE);
        List<String> products = new ArrayList<>();
        products.add("productId");
        List<String> productRoles = new ArrayList<>();
        productRoles.add("Role not found");

        // When
        relationshipInfoList = institutionServiceImpl.retrieveAllProduct("42", binding, institution, roles, states, products, productRoles);

        // Then
        assertTrue(relationshipInfoList.isEmpty());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct_institutionNull_filterProduct_noProductsFound() {
        // Given
        List<RelationshipInfo> relationshipInfoList;
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        product.setRole(PartyRole.DELEGATE);
        product.setStatus(RelationshipState.ACTIVE);
        product.setProductRole("Operator API");
        binding.setProducts(List.of(product));

        Institution institution = new Institution();
        institution.setId("42");

        List<PartyRole> roles = new ArrayList<>();
        roles.add(PartyRole.DELEGATE);
        List<RelationshipState> states = new ArrayList<>();
        states.add(RelationshipState.ACTIVE);
        List<String> products = new ArrayList<>();
        products.add("productId");
        List<String> productRoles = new ArrayList<>();
        productRoles.add("Role not found");

        // When
        relationshipInfoList = institutionServiceImpl.retrieveAllProduct("42", binding, institution, roles, states, products, productRoles);

        // Then
        assertTrue(relationshipInfoList.isEmpty());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct_filterProduct_found() {
        // Given
        List<RelationshipInfo> relationshipInfoList;
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        OnboardedProduct product1 = new OnboardedProduct();
        product1.setProductId("productId1");
        product1.setRole(PartyRole.DELEGATE);
        product1.setStatus(RelationshipState.ACTIVE);
        product1.setProductRole("Operator API");
        OnboardedProduct product2 = new OnboardedProduct();
        product2.setProductId("productId2");
        product2.setRole(PartyRole.OPERATOR);
        product2.setStatus(RelationshipState.SUSPENDED);
        product2.setProductRole("Operatore API");
        OnboardedProduct product3 = new OnboardedProduct();
        product3.setProductId("productId3");
        product3.setRole(PartyRole.DELEGATE);
        product3.setStatus(RelationshipState.ACTIVE);
        product3.setProductRole("Operator API");
        OnboardedProduct product4 = new OnboardedProduct();
        product4.setProductId("productId4");
        product4.setRole(PartyRole.DELEGATE);
        product4.setStatus(RelationshipState.ACTIVE);
        product4.setProductRole("Amministratore");
        binding.setProducts(List.of(product1, product2, product3, product4));

        Institution institution = new Institution();
        institution.setId("42");

        List<PartyRole> roles = new ArrayList<>();
        roles.add(PartyRole.OPERATOR);
        roles.add(PartyRole.DELEGATE);
        List<RelationshipState> states = new ArrayList<>();
        states.add(RelationshipState.ACTIVE);
        states.add(RelationshipState.SUSPENDED);
        List<String> products = new ArrayList<>();
        products.add("productId2");
        products.add("productId4");
        List<String> productRoles = new ArrayList<>();
        productRoles.add("Operatore API");
        productRoles.add("Amministratore");

        // When
        relationshipInfoList = institutionServiceImpl.retrieveAllProduct("42", binding, institution, roles, states, products, productRoles);

        // Then
        assertTrue(relationshipInfoList.size() == 2);
        assertEquals(binding.getProducts().get(1).getRole(), relationshipInfoList.get(0).getOnboardedProduct().getRole());
        assertEquals(binding.getProducts().get(1).getStatus(), relationshipInfoList.get(0).getOnboardedProduct().getStatus());
        assertEquals(binding.getProducts().get(1).getProductId(), relationshipInfoList.get(0).getOnboardedProduct().getProductId());
        assertEquals(binding.getProducts().get(1).getProductRole(), relationshipInfoList.get(0).getOnboardedProduct().getProductRole());
        assertEquals(binding.getProducts().get(3).getRole(), relationshipInfoList.get(1).getOnboardedProduct().getRole());
        assertEquals(binding.getProducts().get(3).getStatus(), relationshipInfoList.get(1).getOnboardedProduct().getStatus());
        assertEquals(binding.getProducts().get(3).getProductId(), relationshipInfoList.get(1).getOnboardedProduct().getProductId());
        assertEquals(binding.getProducts().get(3).getProductRole(), relationshipInfoList.get(1).getOnboardedProduct().getProductRole());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveGeoTaxonomies(String)}
     */
    @Test
    void testGetGeoTaxonomies() {
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setGeotaxId("Code");
        geographicTaxonomies.setCountry("GB");
        geographicTaxonomies.setCountryAbbreviation("GB");
        geographicTaxonomies.setDescription("The characteristics of someone or something");
        geographicTaxonomies.setEnable(true);
        geographicTaxonomies.setIstatCode("");
        geographicTaxonomies.setProvinceId("Province");
        geographicTaxonomies.setProvinceAbbreviation("Province Abbreviation");
        geographicTaxonomies.setRegionId("us-east-2");
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);
        assertSame(geographicTaxonomies, institutionServiceImpl.retrieveGeoTaxonomies("Code"));
        verify(geoTaxonomiesConnector).getExtByCode(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveGeoTaxonomies(String)}
     */
    @Test
    void testGetGeoTaxonomies2() {
        when(geoTaxonomiesConnector.getExtByCode(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.retrieveGeoTaxonomies("Code"));
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

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionsWithFilter(String, String, List)}
     */
    @Test
    void testRetrieveInstitutionsWithFilter6() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(institutionList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());
        institutionServiceImpl.retrieveInstitutionsWithFilter("42", "42", new ArrayList<>());
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    @Test
    void testUpdateInstitutionDescription(){
        when(userService.checkIfAdmin(any(), any())).thenReturn(true);
        when(institutionConnector.findAndUpdate(any(), any(), any(), any())).thenReturn(new Institution());
        assertDoesNotThrow(() -> institutionServiceImpl.updateInstitutionDescription("42", "42", "userId"));
    }

    @Test
    void testUpdateInstitutionDescriptionException(){
        when(userService.checkIfAdmin(any(), any())).thenReturn(false);
        assertThrows(ResourceForbiddenException.class, () -> institutionServiceImpl.updateInstitutionDescription("42", "42", "userId"));
    }

    private Institution initializeInstitution(Integer bias) {
        Institution institution = new Institution();

        return institution;
    }
}

