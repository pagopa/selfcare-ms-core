package it.pagopa.selfcare.mscore.web.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;

import java.util.ArrayList;
import java.util.List;

import it.pagopa.selfcare.mscore.web.TestUtils;
import it.pagopa.selfcare.mscore.web.model.institution.BulkPartiesSeed;

import it.pagopa.selfcare.mscore.web.model.institution.BulkPartiesSeed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ManagementController.class})
@ExtendWith(SpringExtension.class)
class ManagementControllerTest {
    @MockBean
    private InstitutionService institutionService;

    @Autowired
    private ManagementController managementController;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link ManagementController#getInstitutionAttributes(String)}
     */
    @Test
    void testGetInstitutionAttributes() throws Exception {
        Institution institution = new Institution();
        institution.setAttributes(List.of(new Attributes()));
        institution.setId("id");
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/{id}/attributes", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ManagementController#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId3() throws Exception {
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/management/external/institutions/{externalId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"externalId\":null,\"origin\":null,\"originId\":null,\"description\":null,\"institutionType\":null"
                                        + ",\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"products\":null,\"geographicTaxonomies"
                                        + "\":null,\"attributes\":null,\"paymentServiceProvider\":null,\"dataProtectionOfficer\":null,\"rea\":null,"
                                        + "\"shareCapital\":null,\"businessRegisterPlace\":null,\"supportEmail\":null,\"supportPhone\":null,\"imported\""
                                        + ":false,\"createdAt\":null,\"updatedAt\":null,\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}"));
    }


    /**
     * Method under test: {@link ManagementController#getInstitutionByGeotaxonomies(String, SearchMode)}
     */
    @Test
    void testGetInstitutionByGeotaxonomies() throws Exception {
        Object[] uriVars = new Object[]{};
        String[] values = new String[]{"foo"};
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/institutions/bygeotaxonomies", uriVars)
                .param("geoTaxonomies", values);
        Object[] controllers = new Object[]{managementController};
        MockMvcBuilders.standaloneSetup(controllers).build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ManagementController#getInstitutionByInternalId(String)}
     */
    @Test
    void testGetInstitutionByInternalId3() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/management/institutions/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"externalId\":null,\"origin\":null,\"originId\":null,\"description\":null,\"institutionType\":null"
                                        + ",\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"products\":null,\"geographicTaxonomies"
                                        + "\":null,\"attributes\":null,\"paymentServiceProvider\":null,\"dataProtectionOfficer\":null,\"rea\":null,"
                                        + "\"shareCapital\":null,\"businessRegisterPlace\":null,\"supportEmail\":null,\"supportPhone\":null,\"imported\""
                                        + ":false,\"createdAt\":null,\"updatedAt\":null,\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}"));
    }


    /**
     * Method under test: {@link ManagementController#getInstitutionByProductId(String)}
     */
    @Test
    void testGetInstitutionByProductId4() throws Exception {
        when(institutionService.findInstitutionsByProductId(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/product/{productId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"items\":[]}"));
    }

    /**
     * Method under test: {@link ManagementController#getInstitutionByProductId(String)}
     */
    @Test
    void testGetInstitutionByProductId5() throws Exception {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        when(institutionService.findInstitutionsByProductId(any())).thenReturn(institutionList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/product/{productId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"items\":[{\"id\":null,\"externalId\":null,\"origin\":null,\"originId\":null,\"description\":null,\"institutionType"
                                        + "\":null,\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"products\":null,\"geographi"
                                        + "cTaxonomies\":null,\"attributes\":null,\"paymentServiceProvider\":null,\"dataProtectionOfficer\":null,\"rea\""
                                        + ":null,\"shareCapital\":null,\"businessRegisterPlace\":null,\"supportEmail\":null,\"supportPhone\":null,\"imported"
                                        + "\":false,\"createdAt\":null,\"updatedAt\":null,\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}]}"));
    }


    /**
     * Method under test: {@link ManagementController#getInstitutionRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testGetInstitutionRelationships2() throws Exception {
        when(institutionService.retrieveUserRelationships(any(), any(), any(),
                any(), any(), any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/relationships");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"items\":[]}"));
    }

    /**
     * Method under test: {@link ManagementController#getToken(String)}
     */
    @Test
    void testGetToken2() throws Exception {
        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum("Checksum");
        tokenRelationships.setInstitutionId("42");
        tokenRelationships.setProductId("42");
        tokenRelationships.setTokenId("42");
        tokenRelationships.setUsers(new ArrayList<>());
        when(tokenService.retrieveToken(any())).thenReturn(tokenRelationships);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tokens/{tokenId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"checksum\":\"Checksum\",\"legals\":[]}"));
    }

    /**
     * Method under test: {@link ManagementController#getUser(String)}
     */
    @Test
    void testGetUser2() throws Exception {
        when(userService.findByUserId(any())).thenReturn(new OnboardedUser());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":null}"));
    }

    /**
     * Method under test: {@link ManagementController#verifyInstitution(String)}
     */
    @Test
    void testVerifyInstitution2() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.head("/institutions/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ManagementController#verifyUser(String)}
     */
    @Test
    void testVerifyUser() throws Exception {
        doNothing().when(userService).verifyUser(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.head("/persons/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ManagementController#verifyUser(String)}
     */
    @Test
    void testVerifyUser2() throws Exception {
        doNothing().when(userService).verifyUser(any());
        MockHttpServletRequestBuilder headResult = MockMvcRequestBuilders.head("/persons/{id}", "42");
        headResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(headResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ManagementController#verifyUser(String)}
     */
    @Test
    void testVerifyUser3() throws Exception {
        doNothing().when(userService).verifyUser(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.head("/persons/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ManagementController#verifyUser(String)}
     */
    @Test
    void testVerifyUser4() throws Exception {
        doNothing().when(userService).verifyUser(any());
        MockHttpServletRequestBuilder headResult = MockMvcRequestBuilders.head("/persons/{id}", "42");
        headResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(headResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ManagementController#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId() throws Exception {
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/management/external/institutions/{externalId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"externalId\":null,\"origin\":null,\"originId\":null,\"description\":null,\"institutionType\":null"
                                        + ",\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"products\":null,\"geographicTaxonomies"
                                        + "\":null,\"attributes\":null,\"paymentServiceProvider\":null,\"dataProtectionOfficer\":null,\"rea\":null,"
                                        + "\"shareCapital\":null,\"businessRegisterPlace\":null,\"supportEmail\":null,\"supportPhone\":null,\"imported\""
                                        + ":false,\"createdAt\":null,\"updatedAt\":null,\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}"));
    }


    /**
     * Method under test: {@link ManagementController#getInstitutionByInternalId(String)}
     */
    @Test
    void testGetInstitutionByInternalId() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/management/institutions/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"externalId\":null,\"origin\":null,\"originId\":null,\"description\":null,\"institutionType\":null"
                                        + ",\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"products\":null,\"geographicTaxonomies"
                                        + "\":null,\"attributes\":null,\"paymentServiceProvider\":null,\"dataProtectionOfficer\":null,\"rea\":null,"
                                        + "\"shareCapital\":null,\"businessRegisterPlace\":null,\"supportEmail\":null,\"supportPhone\":null,\"imported\""
                                        + ":false,\"createdAt\":null,\"updatedAt\":null,\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}"));
    }

    /**
     * Method under test: {@link ManagementController#getInstitutionByInternalId(String)}
     */
    @Test
    void testGetInstitutionByInternalId2() throws Exception {
        Institution institution = TestUtils.createSimpleInstitutionPA();
        when(institutionService.retrieveInstitutionById(any()))
                .thenReturn(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/management/institutions/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of"
                                        + " someone or something\",\"institutionType\":\"PA\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main"
                                        + " St\",\"zipCode\":\"21654\",\"taxCode\":\""+institution.getTaxCode()+"\",\"products\":{},\"geographicTaxonomies\":[],\"attributes\":[],"
                                        + "\"paymentServiceProvider\":{\"abiCode\":\""+institution.getPaymentServiceProvider().getAbiCode()+"\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\","
                                        + "\"legalRegisterName\":\""+institution.getPaymentServiceProvider().getLegalRegisterName()+"\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email"
                                        + "\":\"jane.doe@example.org\",\"pec\":\""+institution.getDataProtectionOfficer().getPec()+"\"},\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\"," +
                                        "\"businessRegisterPlace\":null,\"supportEmail\":null,\"supportPhone\":null,\"imported\":false,\"createdAt\":null,\"updatedAt\":null"
                                        + ",\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}"));
    }

    /**
     * Method under test: {@link ManagementController#getInstitutionByProductId(String)}
     */
    @Test
    void testGetInstitutionByProductId() throws Exception {
        when(institutionService.findInstitutionsByProductId(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/product/{productId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"items\":[]}"));
    }

    /**
     * Method under test: {@link ManagementController#getInstitutionByProductId(String)}
     */
    @Test
    void testGetInstitutionByProductId2() throws Exception {
        ArrayList<Institution> institutionList = new ArrayList<>();
        institutionList.add(new Institution());
        when(institutionService.findInstitutionsByProductId(any())).thenReturn(institutionList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/external/institutions/product/{productId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"items\":[{\"id\":null,\"externalId\":null,\"origin\":null,\"originId\":null,\"description\":null,\"institutionType"
                                        + "\":null,\"digitalAddress\":null,\"address\":null,\"zipCode\":null,\"taxCode\":null,\"products\":null,\"geographi"
                                        + "cTaxonomies\":null,\"attributes\":null,\"paymentServiceProvider\":null,\"dataProtectionOfficer\":null,\"rea\""
                                        + ":null,\"shareCapital\":null,\"businessRegisterPlace\":null,\"supportEmail\":null,\"supportPhone\":null,\"imported"
                                        + "\":false,\"createdAt\":null,\"updatedAt\":null,\"subunitCode\":null,\"subunitType\":null,\"aooParentCode\":null}]}"));
    }


    /**
     * Method under test: {@link ManagementController#getInstitutionRelationships(String, String, List, List, List, List)}
     */
    @Test
    void testGetInstitutionRelationships() throws Exception {
        when(institutionService.retrieveUserRelationships(any(), any(), any(),
                any(), any(), any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/relationships");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"items\":[]}"));
    }

    /**
     * Method under test: {@link ManagementController#getToken(String)}
     */
    @Test
    void testGetToken() throws Exception {
        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum("Checksum");
        tokenRelationships.setInstitutionId("42");
        tokenRelationships.setProductId("42");
        tokenRelationships.setTokenId("42");
        tokenRelationships.setUsers(new ArrayList<>());
        when(tokenService.retrieveToken(any())).thenReturn(tokenRelationships);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tokens/{tokenId}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"checksum\":\"Checksum\",\"legals\":[]}"));
    }

    /**
     * Method under test: {@link ManagementController#getUser(String)}
     */
    @Test
    void testGetUser() throws Exception {
        when(userService.findByUserId(any())).thenReturn(new OnboardedUser());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":null}"));
    }

    /**
     * Method under test: {@link ManagementController#verifyInstitution(String)}
     */
    @Test
    void testVerifyInstitution() throws Exception {
        when(institutionService.retrieveInstitutionById(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.head("/institutions/{id}", "42");
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetInstitutionsByIds() throws Exception {
        BulkPartiesSeed bulkPartiesSeed = new BulkPartiesSeed();
        bulkPartiesSeed.setPartyIdentifiers(List.of("42"));
        ObjectMapper mapper = new ObjectMapper();
        Institution institution = new Institution();
        institution.setId("id");
        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("productId");
        onboarding.setStatus(RelationshipState.ACTIVE);
        institution.setOnboarding(List.of(onboarding));
        when(institutionService.retrieveInstitutionByIds(any())).thenReturn(List.of(institution));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/bulk/institutions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bulkPartiesSeed));
        MockMvcBuilders.standaloneSetup(managementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

