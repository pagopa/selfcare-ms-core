package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.TestUtils;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.mapper.*;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ContextConfiguration(classes = {InstitutionController.class})
@ExtendWith(MockitoExtension.class)
class InstitutionControllerTest {

    private static final String BASE_URL = "/institutions";

    @InjectMocks
    private InstitutionController institutionController;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private OnboardingService onboardingService;

    @Mock
    private DelegationService delegationService;

    @Spy
    private OnboardingResourceMapper onboardingResourceMapper = new OnboardingResourceMapperImpl();

    @Spy
    private InstitutionResourceMapper institutionResourceMapper = new InstitutionResourceMapperImpl();

    @Spy
    private BrokerMapper brokerMapper = new BrokerMapperImpl();

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    private final static Onboarding onboarding;
    private final static Billing billing;

    private final static Institution institution;

    static {
        billing = new Billing();
        billing.setVatNumber("example");
        billing.setRecipientCode("example");
        billing.setTaxCodeInvoicing("example");

        onboarding = new Onboarding();
        onboarding.setProductId("example");
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setBilling(billing);
        onboarding.setContract("contract");
        onboarding.setTokenId("tokenId");
        onboarding.setPricingPlan("setPricingPlan");
        onboarding.setIsAggregator(true);

        Attributes attribute = new Attributes();
        attribute.setCode("code");
        attribute.setDescription("description");

        institution = new Institution();
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PG);
        institution.setDescription("description");
        institution.setOnboarding(List.of(onboarding));
        institution.setAttributes(List.of(attribute));
        institution.setIstatCode("istatCode");

    }

    @Test
    void shouldGetInstitutionsByTaxCode() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();

        when(institutionService.getInstitutions(any(), any(), any(), any()))
                .thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/?taxCode={taxCode}", "TaxCode");

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutions\":[{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"delegation\":false}]}"));
    }

    @Test
    void shouldGetInstitutionsByTaxCodeAndSubunitCode() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSubunitCode("example");
        institution.setSubunitType(InstitutionPaSubunitType.UO.name());
        institution.setParentDescription("parentDescription");
        institution.setRootParentId("rootParentId");

        when(institutionService.getInstitutions(any(), any(), any(), any()))
                .thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/?taxCode={taxCode}&subunitCode={subunitCode}", "TaxCode", "SubunitCode");

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutions\":[{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rootParent\":{\"description\":\"parentDescription\",\"id\":\"rootParentId\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"subunitCode\":\"example\",\"subunitType\":\"UO\",\"delegation\":false}]}"));
    }

    @Test
    void shouldGetInstitutionsByOriginAndOriginId() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSubunitCode("example");
        institution.setSubunitType(InstitutionPaSubunitType.UO.name());
        institution.setParentDescription("parentDescription");
        institution.setRootParentId("rootParentId");

        when(institutionService.getInstitutions(any(), any(), any(), any()))
                .thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/?origin={origin}&originId={originId}", "origin", "originId");

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutions\":[{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rootParent\":{\"description\":\"parentDescription\",\"id\":\"rootParentId\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"subunitCode\":\"example\",\"subunitType\":\"UO\",\"delegation\":false}]}"));
    }

    @Test
    void shouldGetInstitutionsByOrigin() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSubunitCode("example");
        institution.setSubunitType(InstitutionPaSubunitType.UO.name());
        institution.setParentDescription("parentDescription");
        institution.setRootParentId("rootParentId");

        when(institutionService.getInstitutions(any(), any(), any(), any()))
                .thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/?origin={origin}", "origin");

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutions\":[{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rootParent\":{\"description\":\"parentDescription\",\"id\":\"rootParentId\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"subunitCode\":\"example\",\"subunitType\":\"UO\",\"delegation\":false}]}"));
    }

    @Test
    void shouldGetInstitutionsByOriginId() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSubunitCode("example");
        institution.setSubunitType(InstitutionPaSubunitType.UO.name());
        institution.setParentDescription("parentDescription");
        institution.setRootParentId("rootParentId");

        when(institutionService.getInstitutions(any(), any(), any(), any()))
                .thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/?originId={originId}", "originId");

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutions\":[{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rootParent\":{\"description\":\"parentDescription\",\"id\":\"rootParentId\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"subunitCode\":\"example\",\"subunitType\":\"UO\",\"delegation\":false}]}"));
    }

    @Test
    void shouldGetInstitutionsBySubunitCodeWithoutParam(){
        Assertions.assertThrows(ValidationException.class,
                () -> institutionController.getInstitutions(null, null, null, null),
                "At least one of taxCode, origin or originId must be present");
    }

    @Test
    void shouldGetInstitutionsBySubunitCodeWithoutTaxCode() {
        Assertions.assertThrows(ValidationException.class,
                () -> institutionController.getInstitutions(null, "subunitCode", "origin", null),
                "TaxCode is required if subunitCode is present");
    }

    @Test
    void shouldGetOnboardingsInstitutionByProductId() throws Exception {

        when(institutionService.getOnboardingInstitutionByProductId(any(), any()))
                .thenReturn(List.of(onboarding));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/{institutionId}/onboardings?productId{productId}", "42", onboarding.getProductId());

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"onboardings\":[{\"productId\":\"example\",\"tokenId\":\"tokenId\",\"status\":\"ACTIVE\",\"contract\":\"contract\",\"pricingPlan\":\"setPricingPlan\",\"billing\":{\"vatNumber\":\"example\",\"taxCodeInvoicing\":\"example\",\"recipientCode\":\"example\",\"publicServices\":false},\"createdAt\":null,\"updatedAt\":null,\"closedAt\":null,\"isAggregator\":true}]}"));
    }

    @Test
    void retrieveInstitutionById() throws Exception {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(institutionService.retrieveInstitutionById("42")).thenReturn(institution);
        institution.setId("id");
        MockHttpServletRequestBuilder requestBuilder = get("/institutions/{id}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionById(String)}
     */
    @Test
    void testRetrieveInstitutionById() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}", "42");
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"description\":\"description\",\"institutionType\":\"PG\",\"istatCode\":\"istatCode\",\"attributes\":[{\"origin\":null,\"code\":\"code\",\"description\":\"description\"}],\"onboarding\":[{\"productId\":\"example\",\"status\":\"ACTIVE\",\"billing\":{\"vatNumber\":\"example\",\"taxCodeInvoicing\":\"example\",\"recipientCode\":\"example\",\"publicServices\":false},\"createdAt\":null,\"updatedAt\":null,\"isAggregator\":true}],\"imported\":false,\"delegation\":false}"));
    }

    @Test
    void retrieveInstitutionGeoTaxonomies() throws Exception {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Institution institution = new Institution();
        institution.setId("id");
        GeographicTaxonomyPage page = new GeographicTaxonomyPage();
        page.setData(Collections.emptyList());
        MockHttpServletRequestBuilder requestBuilder = get("/institutions/{id}/geotaxonomies", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionFromIpa(InstitutionFromIpaPost)}}
     */
    @Test
    void shouldCreateInstitutionFromIpa() throws Exception {
        // Given
        InstitutionFromIpaPost institutionFromIpaPost = new InstitutionFromIpaPost();
        institutionFromIpaPost.setTaxCode("123456");
        institutionFromIpaPost.setSubunitType(InstitutionPaSubunitType.AOO);
        institutionFromIpaPost.setSubunitCode("1234");
        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("code");
        geoTaxonomies.setDesc("desc");
        institutionFromIpaPost.setGeographicTaxonomies(List.of(geoTaxonomies));
        String content = objectMapper.writeValueAsString(institutionFromIpaPost);

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSubunitCode(institutionFromIpaPost.getSubunitCode());
        institution.setSubunitType(institutionFromIpaPost.getSubunitType().name());
        institution.setParentDescription("parentDescription");
        institution.setRootParentId("rootParentId");

        when(institutionService.createInstitutionFromIpa(any(), any(), any(), any(), any())).thenReturn(institution);

        //Then
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-ipa/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rootParent\":{\"description\":\"parentDescription\",\"id\":\"rootParentId\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"subunitCode\":\"1234\",\"subunitType\":\"AOO\",\"delegation\":false}"));

        ArgumentCaptor<List<InstitutionGeographicTaxonomies>> captorGeo = ArgumentCaptor.forClass(List.class);
        verify(institutionService, times(1))
                .createInstitutionFromIpa(any(),any(),any(),captorGeo.capture(), any());
        assertEquals(institutionFromIpaPost.getGeographicTaxonomies().size(), captorGeo.getValue().size());
        assertEquals(geoTaxonomies.getCode(), captorGeo.getValue().get(0).getCode());
        assertEquals(geoTaxonomies.getDesc(), captorGeo.getValue().get(0).getDesc());
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionFromAnac(InstitutionRequest)}}
     */
    @Test
    void shouldCreateInstitutionFromAnac() throws Exception {
        // Given
        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setInstitutionType(InstitutionType.SA);
        institutionRequest.setTaxCode("42");
        institutionRequest.setExternalId("42");
        String content = objectMapper.writeValueAsString(institutionRequest);

        Institution institution = TestUtils.createSimpleInstitutionSA();

        when(institutionService.createInstitutionFromAnac(any())).thenReturn(institution);

        //Then
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-anac/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"ANAC\",\"originId\":\"ANAC Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"SA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionFromIvass(InstitutionRequest)}}
     */
    @Test
    void shouldCreateInstitutionFromIvass() throws Exception {
        // Given
        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setInstitutionType(InstitutionType.AS);
        institutionRequest.setTaxCode("42");
        institutionRequest.setExternalId("42");
        String content = objectMapper.writeValueAsString(institutionRequest);

        Institution institution = TestUtils.createSimpleInstitutionAS();

        when(institutionService.createInstitutionFromIvass(any())).thenReturn(institution);

        //Then
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-ivass/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"IVASS\",\"originId\":\"IVASS Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"AS\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionFromInfocamere(InstitutionRequest)}}
     */
    @Test
    void shouldCreateInstitutionFromInfocamere() throws Exception {
        // Given
        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setInstitutionType(InstitutionType.PG);
        institutionRequest.setTaxCode("42");
        institutionRequest.setExternalId("42");
        String content = objectMapper.writeValueAsString(institutionRequest);

        Institution institution = TestUtils.createSimpleInstitutionPA();

        when(institutionService.createInstitutionFromInfocamere(any())).thenReturn(institution);

        //Then
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-infocamere/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionFromIpa(InstitutionFromIpaPost)}}
     */
    @Test
    void shouldThrowValidationExceptionWhenCreateInstitutionFromIpaWithoutTax() throws Exception {

        String content = objectMapper.writeValueAsString(new InstitutionFromIpaPost());

        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-ipa/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void shouldCreateInstitution() throws Exception {

        InstitutionRequest institution = TestUtils.createSimpleInstitutionRequest();
        Institution response = TestUtils.createSimpleInstitutionPA();

        when(institutionService.createInstitution(any())).thenReturn(response);

        String content = objectMapper.writeValueAsString(institution);

        MockHttpServletRequestBuilder requestBuilder = post("/institutions/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId() throws Exception {
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

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOriginId("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitutionByExternalId(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId2() throws Exception {
        when(institutionService.createInstitutionByExternalId(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId3() throws Exception {
        when(institutionService.createInstitutionByExternalId(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId4() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();

        when(institutionService.createInstitutionByExternalId(any()))
                .thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/{externalId}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitutionByExternalId(String)}
     */
    @Test
    void testCreateInstitutionByExternalId5() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(new ArrayList<>());
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false,\"delegation\":false}"));
    }


    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution2() throws Exception {
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

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOriginId("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitution(any())).thenReturn(institution);

        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("?");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("?");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution3() throws Exception {
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

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOriginId("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        when(institutionService.createInstitution(any())).thenReturn(institution);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("?");
        geoTaxonomies.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        geoTaxonomiesList.add(geoTaxonomies);

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(new ArrayList<>());
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"geographicTaxonomies\":[],\"attributes\":[],\"onboarding\":[],\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution4() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(new ArrayList<>());
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution5() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("?");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("?");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution6() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("?");
        geoTaxonomies.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        geoTaxonomiesList.add(geoTaxonomies);

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(new ArrayList<>());
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }


    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution9() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.setCode("?");
        attributesRequest.setDescription("The characteristics of someone or something");
        attributesRequest.setOrigin("?");

        ArrayList<AttributesRequest> attributesRequestList = new ArrayList<>();
        attributesRequestList.add(attributesRequest);

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(attributesRequestList);
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(new ArrayList<>());
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#createInstitution(InstitutionRequest)}
     */
    @Test
    void testCreateInstitution10() throws Exception {
        when(institutionService.createInstitution(any())).thenReturn(new Institution());

        DataProtectionOfficerRequest dataProtectionOfficerRequest = new DataProtectionOfficerRequest();
        dataProtectionOfficerRequest.setAddress("42 Main St");
        dataProtectionOfficerRequest.setEmail("jane.doe@example.org");
        dataProtectionOfficerRequest.setPec("Pec");

        GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
        geoTaxonomies.setCode("?");
        geoTaxonomies.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomies> geoTaxonomiesList = new ArrayList<>();
        geoTaxonomiesList.add(geoTaxonomies);

        PaymentServiceProviderRequest paymentServiceProviderRequest = new PaymentServiceProviderRequest();
        paymentServiceProviderRequest.setAbiCode("Abi Code");
        paymentServiceProviderRequest.setBusinessRegisterNumber("42");
        paymentServiceProviderRequest.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderRequest.setLegalRegisterNumber("42");
        paymentServiceProviderRequest.setVatNumberGroup(true);

        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setAddress("42 Main St");
        institutionRequest.setAttributes(new ArrayList<>());
        institutionRequest.setBusinessRegisterPlace("Business Register Place");
        institutionRequest.setDataProtectionOfficer(dataProtectionOfficerRequest);
        institutionRequest.setDescription("The characteristics of someone or something");
        institutionRequest.setDigitalAddress("42 Main St");
        institutionRequest.setGeographicTaxonomies(geoTaxonomiesList);
        institutionRequest.setInstitutionType(InstitutionType.PA);
        institutionRequest.setPaymentServiceProvider(paymentServiceProviderRequest);
        institutionRequest.setRea("Rea");
        institutionRequest.setShareCapital("Share Capital");
        institutionRequest.setSupportEmail("jane.doe@example.org");
        institutionRequest.setSupportPhone("6625550144");
        institutionRequest.setTaxCode("Tax Code");
        institutionRequest.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institutionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/institutions/insert/{externalId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"imported\":false,\"delegation\":false}"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionProducts(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsEmpty() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionService.retrieveInstitutionProducts(any(), any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}/products", "42");
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"products\":[]}"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionProducts(String, List)}
     */
    @Test
    void testRetrieveInstitutionProducts() throws Exception {

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionService.retrieveInstitutionProducts(any(), any()))
                .thenReturn(onboardingList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}/products", onboarding.getProductId());
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"products\":[{\"id\":\"example\",\"state\":\"ACTIVE\"}]}"));
    }

    /**
     * Method under test: {@link InstitutionController#retrieveInstitutionGeoTaxonomies(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomies() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        when(institutionService.retrieveInstitutionGeoTaxonomies(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/institutions/{id}/geotaxonomies",
                "42");
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void createPgInstitution() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        CreatePgInstitutionRequest request = new CreatePgInstitutionRequest();
        request.setTaxId("taxId");
        request.setExistsInRegistry(true);
        ObjectMapper mapper = new ObjectMapper();
        when(institutionService.createPgInstitution(any(), any(), anyBoolean(), any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/pg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#updateInstitution(String, InstitutionPut, Authentication)} (String, PgInstitutionPut, Authentication)}
     */
    @Test
    void testUpdateInstitutionDescription() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        InstitutionPut pgInstitutionPut = new InstitutionPut();
        pgInstitutionPut.setDescription("desc");
        pgInstitutionPut.setDigitalAddress("digitalAddress");
        pgInstitutionPut.setParentDescription("parentDesc");
        when(institutionService.updateInstitution(any(), any(), any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/institutions/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pgInstitutionPut))
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void testUpdateInstitution() throws Exception {

        InstitutionPut institutionPut = new InstitutionPut();
        institutionPut.setGeographicTaxonomyCodes(new ArrayList<>());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        when(institutionService.updateInstitution(any(), any(), any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/institutions/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(institutionPut))
                .principal(authentication);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void testGetValidInstitutionToOnboard() throws Exception {
        InstitutionToOnboard institution = new InstitutionToOnboard();
        List<InstitutionToOnboard> list = new ArrayList<>();
        list.add(institution);
        List<ValidInstitution> validInstitutions = new ArrayList<>();
        ValidInstitution validInstitution = new ValidInstitution();
        validInstitutions.add(validInstitution);
        when(institutionService.retrieveInstitutionByExternalIds(any(), any())).thenReturn(validInstitutions);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/institutions/onboarded/{productId}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(list));
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link InstitutionController#updateCreatedAt(String, CreatedAtRequest)}
     */
    @Test
    void updateCreatedAt() throws Exception {
        // Given
        String institutionIdMock = "institutionId";
        String productIdMock = "productId";
        OffsetDateTime createdAtMock = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");

        CreatedAtRequest createdAtRequest = new CreatedAtRequest();
        createdAtRequest.setCreatedAt(createdAtMock);
        createdAtRequest.setProductId(productIdMock);
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL + "/{institutionId}/createdAt", institutionIdMock)
                .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(createdAtRequest))
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE);
        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Then
        verify(institutionService, times(1))
                .updateCreatedAt(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        verifyNoMoreInteractions(institutionService);
    }

    /**
     * Method under test: {@link InstitutionController#findFromProduct(String, Integer, Integer)}
     */
    @Test
    void findFromProduct() throws Exception {
        // Given
        String productIdMock = "productId";
        Integer pageMock = 0;
        Integer sizeMock = 2;

        Institution institution = new Institution();
        institution.setId("id");

        // When
        when(institutionService.getInstitutionsByProductId(any(), any(), any())).thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/products/{productId}", productIdMock)
                .param("page", pageMock.toString())
                .param("size", sizeMock.toString());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        // Then
        verify(institutionService, times(1))
                .getInstitutionsByProductId(productIdMock, pageMock, sizeMock);
        verifyNoMoreInteractions(institutionService);
    }

    /**
     * Method under test: {@link InstitutionController#getInstitutionBrokers(String, InstitutionType)}
     */
    @Test
    void getInstitutionBrokers() throws Exception {
        // Given
        final String productId = "test";
        final InstitutionType type = InstitutionType.PT;
        Institution institution = new Institution();
        institution.setId("id");
        institution.setTaxCode("taxCode");

        // When
        when(institutionService.getInstitutionBrokers(any(), any())).thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/{productId}/brokers/{institutionType}", productId, type);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder);
        MvcResult result =  actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        List<BrokerResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        // Then
        assertNotNull(response);
        assertNotNull(response.get(0));
        assertEquals(response.get(0).getId(), institution.getId());
        assertEquals(response.get(0).getTaxCode(), institution.getTaxCode());
        verify(institutionService, times(1))
                .getInstitutionBrokers(productId, type);
        verifyNoMoreInteractions(institutionService);
    }

    /**
     * Method under test: {@link InstitutionController#getInstitutionBrokers(String, InstitutionType)}
     */
    @Test
    void getInstitutionBrokersWithBadRequest() throws Exception {
        // Given
        final String productId = "test";
        final String type = "FAKED-TYPE";

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL + "/{productId}/brokers/{institutionType}", productId, type);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isBadRequest());

    }



    @Test
    void updateCreatedAt_invalidDate() throws Exception {
        // Given


        String institutionIdMock = "institutionId";
        String productIdMock = "productId";
        OffsetDateTime createdAtMock = OffsetDateTime.now().minusHours(10);
        CreatedAtRequest createdAtRequest = new CreatedAtRequest();
        createdAtRequest.setProductId(productIdMock);
        createdAtRequest.setCreatedAt(createdAtMock);
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                        .put(BASE_URL + "/{institutionId}/createdAt", institutionIdMock)
                        .content(objectMapper.writeValueAsString(createdAtRequest))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE);

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link InstitutionController#onboardingInstitution(InstitutionOnboardingRequest, String)}
     */
    @Test
    void institutionOnboarding() throws Exception {


        final String institutionId = "institutionId";
        InstitutionOnboardingRequest request = new InstitutionOnboardingRequest();
        request.setProductId("id");
        request.setUsers(List.of(new Person()));
        request.setIsAggregator(true);

        when(onboardingService.persistOnboarding(any(), any(), any(), any()))
                .thenReturn(new Institution());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BASE_URL + "/{id}/onboarding/", institutionId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
