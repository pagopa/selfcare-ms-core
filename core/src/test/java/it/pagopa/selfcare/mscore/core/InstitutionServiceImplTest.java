package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapperImpl;
import it.pagopa.selfcare.mscore.core.strategy.CreateInstitutionStrategy;
import it.pagopa.selfcare.mscore.core.strategy.factory.CreateInstitutionStrategyFactory;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.exception.*;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.mscore.core.util.TestUtils.dummyInstitutionPa;
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
    private CoreConfig coreConfig;

    @Mock
    private ContractService contractService;

    @Mock
    private TokenConnector tokenConnector;

    @Mock
    private UserConnector userConnector;

    @Mock
    private CreateInstitutionStrategyFactory createInstitutionStrategyFactory;

    @Mock
    private CreateInstitutionStrategy createInstitutionStrategy;

    @Spy
    private InstitutionMapper institutionMapper = new InstitutionMapperImpl();


    private static final InstitutionProxyInfo dummyInstitutionProxyInfo;
    private static final CategoryProxyInfo dummyCategoryProxyInfo;

    static {
        dummyInstitutionProxyInfo = new InstitutionProxyInfo();
        dummyInstitutionProxyInfo.setAddress("42 Main St");
        dummyInstitutionProxyInfo.setAoo("Aoo");
        dummyInstitutionProxyInfo.setCategory("Category");
        dummyInstitutionProxyInfo.setDescription("The characteristics of someone or something");
        dummyInstitutionProxyInfo.setDigitalAddress("42 Main St");
        dummyInstitutionProxyInfo.setId("42");
        dummyInstitutionProxyInfo.setO("foo");
        dummyInstitutionProxyInfo.setOrigin("Origin");
        dummyInstitutionProxyInfo.setOriginId("42");
        dummyInstitutionProxyInfo.setOu("Ou");
        dummyInstitutionProxyInfo.setTaxCode("Tax Code");
        dummyInstitutionProxyInfo.setZipCode("21654");

        dummyCategoryProxyInfo = new CategoryProxyInfo();
        dummyCategoryProxyInfo.setCode("Code");
        dummyCategoryProxyInfo.setKind("Kind");
        dummyCategoryProxyInfo.setName("Name");
        dummyCategoryProxyInfo.setOrigin("Origin");
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
    void shouldThrowExceptionOnCreateInstitutionByExternalIdWhenInstitutionFounded() {
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

        InstitutionProxyInfo institutionProxyInfo = dummyInstitutionProxyInfo;
        CategoryProxyInfo categoryProxyInfo = dummyCategoryProxyInfo;

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
    void shouldThrowExceptionOnCreationInstitutionByExternalIdWhenSaveAlreadyExists() {
        when(institutionConnector.save(any()))
                .thenThrow(new ResourceConflictException("An error occurred", "START - check institution {} already exists"));
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.empty());

        InstitutionProxyInfo institutionProxyInfo = dummyInstitutionProxyInfo;
        CategoryProxyInfo categoryProxyInfo = dummyCategoryProxyInfo;

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
    void shouldThrowExceptionOnCreationInstitutionByExternalIdIfAlreadyExists() {
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
        InstitutionProxyInfo institutionProxyInfo = dummyInstitutionProxyInfo;
        CategoryProxyInfo categoryProxyInfo = dummyCategoryProxyInfo;

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

        when(institutionConnector.saveOrRetrievePnPg(any())).thenAnswer(answer -> answer.getArguments()[0]);

        institutionServiceImpl.createPnPgInstitution("42", "The characteristics of someone or something");
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

    @Test
    void testGetOnboardingInstitutionByProductId() {
        List<Onboarding> onboardings = new ArrayList<>();
        when(institutionConnector.findOnboardingByIdAndProductId(any(), any())).thenReturn(onboardings);
        List<Onboarding> onboardingList = institutionServiceImpl.getOnboardingInstitutionByProductId("id", "id");
        assertTrue(onboardingList.isEmpty());
    }

    @Test
    void testGetInstitutions() {
        List<Institution> institutionList = new ArrayList<>();
        when(institutionConnector.findByTaxCodeSubunitCodeAndOrigin(any(), any(), any(), any())).thenReturn(institutionList);
        List<Institution> institutions = institutionServiceImpl.getInstitutions("id", "id", "id", "id");
        assertTrue(institutions.isEmpty());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#createInstitutionFromIpa(String, InstitutionPaSubunitType, String)}
     */
    @Test
    void testCreateInstitutionFromIpa() {
        when(createInstitutionStrategyFactory.createInstitutionStrategyIpa()).thenReturn(createInstitutionStrategy);
        when(createInstitutionStrategy.createInstitution(any())).thenReturn(new Institution());
        Institution institution = institutionServiceImpl.createInstitutionFromIpa("id", InstitutionPaSubunitType.AOO,"id");
        assertNotNull(institution);
    }

    @Test
    void testCreateInstitution() {
        when(createInstitutionStrategyFactory.createInstitutionStrategy(any())).thenReturn(createInstitutionStrategy);
        when(createInstitutionStrategy.createInstitution(any())).thenReturn(new Institution());
        Institution institution = institutionServiceImpl.createInstitution(new Institution());
        assertNotNull(institution);
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#getInstitutionsByProductId(String, Integer, Integer)}
     */
    @Test
    void testInstitutionsInstitutionsByProductId() {
        List<Institution> institutions = new ArrayList<>();
        when(institutionConnector.findInstitutionsByProductId(any(), any(), any())).thenReturn(institutions);
        List<Institution> institutionsResult = institutionServiceImpl.getInstitutionsByProductId("id", 0, 1);
        assertTrue(institutionsResult.isEmpty());
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

        assertSame(institution, institutionServiceImpl.createInstitutionRaw(dummyInstitutionPa(), "example"));
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
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        //InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(partyRegistryProxyConnector, null,
        //        userService, new CoreConfig(), mock(TokenConnector.class), mock(UserConnector.class), contractService);
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
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        //InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(partyRegistryProxyConnector, null,
        //        userService, new CoreConfig(), mock(TokenConnector.class), mock(UserConnector.class), contractService);
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

        Institution institution = new Institution();
        when(institutionConnector.findByExternalIdAndProductId(any(), any())).thenReturn(institution);


        assertSame(institution, institutionServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionConnector).findByExternalIdAndProductId(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionGeoTaxonomies(Institution)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomies2() {
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        //InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(partyRegistryProxyConnector, null,
        //        userService, new CoreConfig(), mock(TokenConnector.class), mock(UserConnector.class), contractService);

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

        when(partyRegistryProxyConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);

        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList.add(new InstitutionGeographicTaxonomies(
                "Retrieving geographic taxonomies for institution {}", "The characteristics of someone or something"));

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        assertEquals(1, institutionServiceImpl.retrieveInstitutionGeoTaxonomies(institution).size());
        verify(partyRegistryProxyConnector).getExtByCode(any());
    }

    /**
     * Method under test: {@link InstitutionService#updateInstitution(String, InstitutionUpdate, String)}
     */
    @Test
    void updateInstitution_shouldThrowExceptionIfGeotaxNotFound() {

        when(partyRegistryProxyConnector.getExtByCode(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));

        when(userService.checkIfInstitutionUser(any(), any())).thenReturn(true);

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
        assertThrows(MsCoreException.class,
                () -> institutionServiceImpl.updateInstitution("42", institutionUpdate, "42"));
        verify(partyRegistryProxyConnector).getExtByCode(any());
        verify(userService).checkIfInstitutionUser(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#updateInstitution(String, InstitutionUpdate, String)}
     */
    @Test
    void testUpdateInstitution7() {
        when(userService.checkIfInstitutionUser(any(), any())).thenReturn(false);

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
        verify(userService).checkIfInstitutionUser(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds2() {

        when(institutionConnector.findByExternalIdsAndProductId(any(), any())).thenReturn(List.of());

        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");

        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertTrue(actualRetrieveInstitutionByExternalIdsResult.isEmpty());
        verify(institutionConnector).findByExternalIdsAndProductId(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds3() {

        when(institutionConnector.findByExternalIdsAndProductId(any(), any())).thenReturn(List.of());

        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");
        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertEquals(1, actualRetrieveInstitutionByExternalIdsResult.size());
        verify(institutionConnector).findByExternalIdsAndProductId(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds4() {


        when(institutionConnector.findByExternalIdsAndProductId(any(), any())).thenReturn(new ArrayList<>());

        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");
        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertEquals(2, actualRetrieveInstitutionByExternalIdsResult.size());
        verify(institutionConnector).findByExternalIdsAndProductId(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByExternalIds(List, String)}
     */
    @Test
    void testRetrieveInstitutionByExternalIds5() {

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("42");

        when(institutionConnector.findByExternalIdsAndProductId(any(), any())).thenReturn(stringList);

        ArrayList<ValidInstitution> validInstitutionList = new ArrayList<>();
        validInstitutionList.add(new ValidInstitution("42", "The characteristics of someone or something"));
        List<ValidInstitution> actualRetrieveInstitutionByExternalIdsResult = institutionServiceImpl
                .retrieveInstitutionByExternalIds(validInstitutionList, "42");
        assertSame(validInstitutionList, actualRetrieveInstitutionByExternalIdsResult);
        assertTrue(actualRetrieveInstitutionByExternalIdsResult.isEmpty());
        verify(institutionConnector).findByExternalIdsAndProductId(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#findInstitutionsByGeoTaxonomies(String, SearchMode)}
     */
    @Test
    void testFindInstitutionsByGeoTaxonomies3() {

        ArrayList<Institution> institutionList = new ArrayList<>();
        when(institutionConnector.findByGeotaxonomies(any(), any()))
                .thenReturn(institutionList);
        List<Institution> actualFindInstitutionsByGeoTaxonomiesResult = institutionServiceImpl
                .findInstitutionsByGeoTaxonomies("Geo Taxonomies", SearchMode.ALL);
        assertSame(institutionList, actualFindInstitutionsByGeoTaxonomiesResult);
        assertTrue(actualFindInstitutionsByGeoTaxonomiesResult.isEmpty());
        verify(institutionConnector).findByGeotaxonomies(any(), any());
    }

    @Test
    void testFindInstitutionsByGeoTaxonomies1() {
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        //InstitutionServiceImpl institutionService = (new InstitutionServiceImpl(partyRegistryProxyConnector,
        //        institutionConnector, userService, new CoreConfig(), mock(TokenConnector.class), mock(UserConnector.class), contractService));
        assertThrows(InvalidRequestException.class, () -> institutionServiceImpl.findInstitutionsByGeoTaxonomies("", SearchMode.ALL));
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#findInstitutionsByProductId(String)}
     */
    @Test
    void testFindInstitutionsByProductId2() {
        when(institutionConnector.findByProductId(any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.findInstitutionsByProductId("42"));
    }


    /**
     * Method under test: {@link InstitutionServiceImpl#findInstitutionsByProductId(String)}
     */
    @Test
    void testFindInstitutionsByProductId3() {
        when(institutionConnector.findByProductId(any())).thenReturn(List.of(new Institution()));
        assertDoesNotThrow(() -> institutionServiceImpl.findInstitutionsByProductId("42"));
    }


    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionByIds(List)}
     */
    @Test
    void testRetrieveInstitutionByIds2() {

        ArrayList<Institution> institutionList = new ArrayList<>();
        when(institutionConnector.findAllByIds(any())).thenReturn(institutionList);

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

        when(userService.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(new ArrayList<>());

        Institution institution = new Institution();
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(institutionServiceImpl
                .retrieveUserInstitutionRelationships(institution, "42", "42", roles, states, products, new ArrayList<>())
                .isEmpty());
        verify(userService, atLeast(1)).retrieveUsers(any(), any(), any(),
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

        when(userService.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);

        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        Institution institution = dummyInstitutionPa();

        assertTrue(institutionServiceImpl
                .retrieveUserInstitutionRelationships(institution, "42", "42", List.of(), List.of(), List.of(), List.of())
                .isEmpty());
        verify(userService, atLeast(1)).retrieveUsers(any(), any(), any(),
                any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserRelationships2() {

        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, null);
        //InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(partyRegistryProxyConnector, null,
        //        userService, new CoreConfig(), mock(TokenConnector.class), mock(UserConnector.class), contractService);
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
    void testRetrieveUserRelationships6() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());

        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        onboardedUserList.add(onboardedUser);

        when(userService.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);

        assertTrue(
                institutionServiceImpl.retrieveUserRelationships("42", "42", List.of(), List.of(), List.of(), List.of())
                        .isEmpty());
        verify(userService).retrieveUsers(any(), any(), any(),
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

        when(userService.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(onboardedUserList);

        assertTrue(
                institutionServiceImpl.retrieveUserRelationships("42", "42", List.of(), List.of(), List.of(), List.of())
                        .isEmpty());
        verify(userService).retrieveUsers(any(), any(), any(), any(), any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveUserRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testRetrieveUserRelationships13() {

        when(institutionConnector.findById(any())).thenReturn(new Institution());
        when(userService.retrieveUsers(any(), any(), any(),
                any(), any(), any())).thenReturn(new ArrayList<>());


        assertTrue(
                institutionServiceImpl.retrieveUserRelationships(null, "42", List.of(), List.of(), List.of(), List.of())
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
        when(institutionConnector.findByExternalIdAndProductId(any(), any())).thenReturn(institution);
        assertSame(institution, institutionServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionConnector).findByExternalIdAndProductId(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testGetInstitutionProduct2() {
        when(institutionConnector.findByExternalIdAndProductId(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> institutionServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionConnector).findByExternalIdAndProductId(any(), any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveAllProduct(String, UserBinding, Institution, List, List, List, List)}
     */
    @Test
    void testRetrieveAllProduct6() {

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

        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        UserServiceImpl userService = new UserServiceImpl(null, mock(UserRegistryConnector.class));

        //InstitutionServiceImpl institutionServiceImpl = new InstitutionServiceImpl(partyRegistryProxyConnector, null,
        //        userService, new CoreConfig(), mock(TokenConnector.class), mock(UserConnector.class), contractService);
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
        assertEquals(2, relationshipInfoList.size());
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
        when(partyRegistryProxyConnector.getExtByCode(any())).thenReturn(geographicTaxonomies);
        Optional<GeographicTaxonomies> optionalGeographicTaxonomies = institutionServiceImpl.retrieveGeoTaxonomies("Code");
        assertTrue(optionalGeographicTaxonomies.isPresent());
        assertSame(geographicTaxonomies,optionalGeographicTaxonomies.get());
        verify(partyRegistryProxyConnector).getExtByCode(any());
    }

    /**
     * Method under test: {@link InstitutionServiceImpl#retrieveGeoTaxonomies(String)}
     */
    @Test
    void getGeoTaxonomies_whenGeoTaxIsEmpty() {
        when(partyRegistryProxyConnector.getExtByCode(any()))
                .thenThrow(new ResourceNotFoundException("",""));
        assertTrue(institutionServiceImpl.retrieveGeoTaxonomies("Code").isEmpty());
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

        when(institutionConnector.findWithFilter(any(), any(), any()))
                .thenReturn(List.of(new Institution()));

        institutionServiceImpl.retrieveInstitutionsWithFilter("42", "42", new ArrayList<>());
        verify(institutionConnector).findWithFilter(any(), any(), any());
    }

    @Test
    void testUpdateInstitutionDescription() {
        when(userService.checkIfInstitutionUser(any(), any())).thenReturn(true);
        when(institutionConnector.findAndUpdate(any(), any(), any(), any())).thenReturn(new Institution());
        assertDoesNotThrow(() -> institutionServiceImpl.updateInstitution("42", new InstitutionUpdate(), "userId"));
    }

    @Test
    void testUpdateInstitutionDescriptionException() {
        when(userService.checkIfInstitutionUser(any(), any())).thenReturn(false);
        assertThrows(ResourceForbiddenException.class, () -> institutionServiceImpl.updateInstitution("42", new InstitutionUpdate(), "userId"));
    }

    @Test
    void updateCreatedAt() {
        // Given
        String institutionIdMock = "institutionIdMock";
        String productIdMock = "productId";
        OffsetDateTime createdAtMock = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");

        Onboarding onboardingMock1 = mockInstance(new Onboarding());
        onboardingMock1.setStatus(RelationshipState.ACTIVE);
        onboardingMock1.setClosedAt(null);
        onboardingMock1.setCreatedAt(OffsetDateTime.parse("2023-11-01T02:15:30+01:00"));
        Onboarding onboardingMock2 = mockInstance(new Onboarding());
        onboardingMock2.setProductId(productIdMock);
        onboardingMock2.setTokenId("222e4444-e99b-11d3-a446-422114890100");
        onboardingMock2.setStatus(RelationshipState.ACTIVE);
        onboardingMock2.setClosedAt(null);
        onboardingMock1.setCreatedAt(OffsetDateTime.parse("2020-11-01T02:15:30+01:00"));
        Onboarding onboardingMock3 = mockInstance(new Onboarding());
        onboardingMock3.setStatus(RelationshipState.ACTIVE);
        onboardingMock3.setClosedAt(null);
        onboardingMock1.setCreatedAt(OffsetDateTime.parse("2022-12-11T02:15:30+01:00"));

        Institution updatedInstitutionMock = mockInstance(new Institution());
        updatedInstitutionMock.setId("123e4567-e89b-12d3-a456-426614174000");
        updatedInstitutionMock.setExternalId("00099991238");
        updatedInstitutionMock.setDigitalAddress("DigitalAddress@example.com");
        updatedInstitutionMock.setInstitutionType(InstitutionType.PA);
        updatedInstitutionMock.setTaxCode(updatedInstitutionMock.getExternalId());
        updatedInstitutionMock.setOnboarding(List.of(onboardingMock1, onboardingMock2, onboardingMock3));
        updatedInstitutionMock.setGeographicTaxonomies(Collections.emptyList());
        updatedInstitutionMock.setPaymentServiceProvider(null);
        updatedInstitutionMock.setDataProtectionOfficer(null);
        updatedInstitutionMock.setImported(false);
        updatedInstitutionMock.setCreatedAt(OffsetDateTime.parse("2019-11-01T02:15:30+01:00"));
        updatedInstitutionMock.setUpdatedAt(OffsetDateTime.now());

        TokenUser tokenUserMock1 = mockInstance(new TokenUser());
        tokenUserMock1.setUserId("999e9999-e89b-12d3-a456-426614174000");
        TokenUser tokenUserMock2 = mockInstance(new TokenUser());
        tokenUserMock2.setUserId("321e9876-e89b-12d3-a456-426614174000");
        tokenUserMock2.setRole(PartyRole.DELEGATE);

        Token updatedTokenMock = mockInstance(new Token());
        updatedTokenMock.setId(updatedInstitutionMock.getOnboarding().get(1).getTokenId());
        updatedTokenMock.setUsers(List.of(tokenUserMock1, tokenUserMock2));

        when(institutionConnector.updateOnboardedProductCreatedAt(institutionIdMock, productIdMock, createdAtMock))
                .thenReturn(updatedInstitutionMock);
        when(tokenConnector.updateTokenCreatedAt(updatedTokenMock.getId(), createdAtMock))
                .thenReturn(updatedTokenMock);

        // When
        institutionServiceImpl.updateCreatedAt(institutionIdMock, productIdMock, createdAtMock);
        // Then
        verify(institutionConnector, times(1))
                .updateOnboardedProductCreatedAt(institutionIdMock, productIdMock, createdAtMock);
        verify(tokenConnector, times(1))
                .updateTokenCreatedAt(updatedTokenMock.getId(), createdAtMock);
        verify(userConnector, times(1))
                .updateUserBindingCreatedAt(institutionIdMock, productIdMock, List.of(tokenUserMock1.getUserId(), tokenUserMock2.getUserId()), createdAtMock);
        verify(contractService, times(1))
                .sendDataLakeNotification(updatedInstitutionMock, updatedTokenMock, QueueEvent.UPDATE);
        verifyNoMoreInteractions(institutionConnector, tokenConnector, userConnector, contractService);
    }

    @Test
    void updateCreatedAt_nullInstitutionId() {
        // Given
        String productIdMock = "productId";
        OffsetDateTime createdAtMock = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");
        // When
        Executable executable = () -> institutionServiceImpl.updateCreatedAt(null, productIdMock, createdAtMock);
        // Then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("An institution ID is required.", illegalArgumentException.getMessage());
        verifyNoInteractions(institutionConnector, tokenConnector, userConnector);
    }

    @Test
    void updateCreatedAt_nullProductId() {
        // Given
        String institutionIdMock = "institutionId";
        OffsetDateTime createdAtMock = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");
        // When
        Executable executable = () -> institutionServiceImpl.updateCreatedAt(institutionIdMock, null, createdAtMock);
        // Then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("A product ID is required.", illegalArgumentException.getMessage());
        verifyNoInteractions(institutionConnector, tokenConnector, userConnector);
    }

    @Test
    void updateCreatedAt_nullCreatedAt() {
        // Given
        String institutionIdMock = "institutionId";
        String productIdMock = "producttId";
        // When
        Executable executable = () -> institutionServiceImpl.updateCreatedAt(institutionIdMock, productIdMock, null);
        // Then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("A createdAt date is required.", illegalArgumentException.getMessage());
        verifyNoInteractions(institutionConnector, tokenConnector, userConnector);

    }

    /**
     * Method under test: {@link InstitutionServiceImpl#getInstitutionBrokers(String, InstitutionType)}
     */
    @Test
    void getInstitutionBrokers() {

        Institution institution = new Institution();
        institution.setId("id");
        when(institutionConnector.findBrokers(any(), any())).thenReturn(List.of(institution));
        List<Institution> institutions = institutionServiceImpl.getInstitutionBrokers("42", InstitutionType.PT);
        assertNotNull(institutions);
        assertFalse(institutions.isEmpty());
        assertNotNull(institutions.get(0));
        assertEquals(institutions.get(0).getId(), institution.getId());
        verify(institutionConnector).findBrokers(any(), any());

    }

    /**
     * Method under test: {@link InstitutionServiceImpl#getInstitutions(String, String)}
     */
    @Test
    void getInstitutionsByTaxCode() {

        Institution institution = new Institution();
        institution.setId("id");
        when(institutionConnector.findByTaxCodeSubunitCode(any(), any())).thenReturn(List.of(institution));
        List<Institution> institutions = institutionServiceImpl.getInstitutions("1111111", null);
        assertNotNull(institutions);
        assertFalse(institutions.isEmpty());
        assertNotNull(institutions.get(0));
        assertEquals(institutions.get(0).getId(), institution.getId());
        verify(institutionConnector).findByTaxCodeSubunitCode(any(), any());

    }

}

