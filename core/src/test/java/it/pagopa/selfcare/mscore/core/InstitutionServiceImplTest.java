package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionByLegal;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.NationalRegistriesProfessionalAddress;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        UserServiceImpl userService = new UserServiceImpl(null);
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
        assertEquals(InstitutionType.UNKNOWN, institution1.getInstitutionType());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionRaw(Institution, String)}
     */
    @Test
    void testCreateInstitutionRaw3() { when(institutionConnector.findByExternalId(any()))
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
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        assertSame(institution, institutionServiceImpl.createInstitutionRaw(new Institution("42", "42", Origin.SELC, "",
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
        UserServiceImpl userService = new UserServiceImpl(null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());
        Institution institution = new Institution();
        List<RelationshipState> list = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> institutionServiceImpl.retrieveInstitutionProducts(institution, list));
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
        UserServiceImpl userService = new UserServiceImpl(null);
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
        UserServiceImpl userService = new UserServiceImpl(null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, null, geoTaxonomiesConnector,
                userService, new CoreConfig());

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(new ArrayList<>());
        assertTrue(institutionServiceImpl.retrieveInstitutionGeoTaxonomies(institution).isEmpty());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionGeoTaxonomies(Institution)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomies4() {
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
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);
        UserServiceImpl userService = new UserServiceImpl(null);
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
        assertThrows(InvalidRequestException.class,
                () -> institutionServiceImpl.updateInstitution("42", institutionUpdate, "42"));
        verify(userServiceImpl).checkIfAdmin(any(), any());
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
        UserServiceImpl userService = new UserServiceImpl(null);
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

        Institution institution = new Institution("42", "42", Origin.MOCK, "42",
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
     * Method under test: {@link InstitutionServiceImpl#retrieveGeoTaxonomies(String)}
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
        when(institutionConnector.findWithFilter(any(), any(),  any()))
                .thenReturn(institutionList);
        GeoTaxonomiesConnector geoTaxonomiesConnector = mock(GeoTaxonomiesConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null);
        InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(null, institutionConnector,
                geoTaxonomiesConnector, userService, new CoreConfig());
        institutionServiceImpl.retrieveInstitutionsWithFilter("42", "42", new ArrayList<>());
        verify(institutionConnector).findWithFilter(any(), any(),  any());
    }
}

