package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.core.UserRelationshipService;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.web.TestUtils;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedInstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRelationshipService userRelationshipService;

    @Mock
    private OnboardingService onboardingService;

    /**
     * Method under test: {@link UserController#activateRelationship(String)}
     */
    @Test
    void testActivateRelationship() throws Exception {
        doNothing().when(userRelationshipService).activateRelationship(org.mockito.Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/relationships/{relationshipId}/activate", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#activateRelationship(String)}
     */
    @Test
    void testActivateRelationship2() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserController#activateRelationship(String)}
     */
    @Test
    void testActivateRelationship3() throws Exception {
        doNothing().when(userRelationshipService).activateRelationship(org.mockito.Mockito.any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/relationships/{relationshipId}/activate",
                "42");
        postResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(postResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#suspendRelationship(String)}
     */
    @Test
    void testSuspendRelationship() throws Exception {
        doNothing().when(userRelationshipService).suspendRelationship(org.mockito.Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/relationships/{relationshipId}/suspend", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#suspendRelationship(String)}
     */
    @Test
    void testSuspendRelationship2() throws Exception {
        doNothing().when(userRelationshipService).suspendRelationship(org.mockito.Mockito.any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/relationships/{relationshipId}/suspend",
                "42");
        postResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(postResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#deleteRelationship(String)}
     */
    @Test
    void testDeleteRelationship() throws Exception {
        doNothing().when(userRelationshipService).deleteRelationship(org.mockito.Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/relationships/{relationshipId}",
                "42");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#deleteRelationship(String)}
     */
    @Test
    void testDeleteRelationship2() throws Exception {
        doNothing().when(userRelationshipService).deleteRelationship(org.mockito.Mockito.any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/relationships/{relationshipId}",
                "42");
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#getRelationship(String)}
     */
    @Test
    void testGetRelationship() throws Exception {
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());

        RelationshipInfo relationshipInfo = new RelationshipInfo();
        relationshipInfo.setInstitution(institution);
        when(userRelationshipService.retrieveRelationship(org.mockito.Mockito.any()))
                .thenReturn(relationshipInfo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/relationships/{relationshipId}",
                "42");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"institutionUpdate\":{\"imported\":false}}"));
    }

    /**
     * Method under test: {@link UserController#getRelationship(String)}
     */
    @Test
    void testGetRelationship2() throws Exception {

        Institution institution = TestUtils.createSimpleInstitutionPA();
        institution.setSupportPhone("8605550118");
        institution.setSupportEmail("john.smith@example.org");
        institution.setBusinessRegisterPlace("U");
        institution.setShareCapital("U");
        institution.setRea("U");
        institution.setTaxCode("U");
        institution.setZipCode("OX1 1PT");
        institution.setAddress("17 High St");
        institution.setDigitalAddress("17 High St");
        institution.setInstitutionType(InstitutionType.PG);
        institution.setDescription("?");
        institution.setId("?");

        RelationshipInfo relationshipInfo = new RelationshipInfo();
        relationshipInfo.setUserId("?");
        relationshipInfo.setInstitution(institution);
        when(userRelationshipService.retrieveRelationship(org.mockito.Mockito.any()))
                .thenReturn(relationshipInfo);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/relationships/{relationshipId}",
                "42");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"to\":\"?\",\"from\":\"?\",\"institutionUpdate\":{\"institutionType\":\"PG\",\"description\":\"?\",\"digitalAddress\":\"17"
                                        + " High St\",\"address\":\"17 High St\",\"taxCode\":\"U\",\"zipCode\":\"OX1 1PT\",\"paymentServiceProvider\":{\"abiCode"
                                        + "\":\""+institution.getPaymentServiceProvider().getAbiCode()+"\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\"," +
                                        "\"legalRegisterName\":\""+institution.getPaymentServiceProvider().getLegalRegisterName()+"\",\"vatNumberGroup"
                                        + "\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\""+institution.getDataProtectionOfficer().getPec()+"\"},"
                                        + "\"geographicTaxonomyCodes\":[],\"rea\":\"U\",\"shareCapital\":\"U\",\"businessRegisterPlace\":\"U\",\"supportEmail\""
                                        + ":\"john.smith@example.org\",\"supportPhone\":\"8605550118\",\"imported\":false}}"));
    }

    @Test
    void verifyUser() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/persons/{id}", "42")
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    /**
     * Method under test: {@link UserController#getInstitutionProductsInfo(String, String)}
     */
    @Test
    void testGetInstitutionProductsInfo() throws Exception {

        OnboardingInfo onboardingInfo = new OnboardingInfo();
        onboardingInfo.setUserId("id");
        UserInstitutionBinding userInstitutionBinding = new UserInstitutionBinding();
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId("productId");
        product.setStatus(RelationshipState.ACTIVE);
        userInstitutionBinding.setProducts(product);
        onboardingInfo.setBinding(userInstitutionBinding);
        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setInstitutionType(InstitutionType.PA);
        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("productId");
        onboarding.setStatus(RelationshipState.ACTIVE);
        institution.setOnboarding(List.of(onboarding));
        onboardingInfo.setInstitution(institution);

        when(onboardingService.getOnboardingInfo(anyString(), anyString())).thenReturn(List.of(onboardingInfo));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/id/institution-products?institutionId=test")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        OnboardingInfoResponse response = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals(response.getUserId(), onboardingInfo.getUserId());
        assertNotNull(response.getInstitutions());
        assertEquals(1, response.getInstitutions().size());

        OnboardedInstitutionResponse institutionResponse = response.getInstitutions().get(0);

        assertEquals(institutionResponse.getInstitutionType(), onboardingInfo.getInstitution().getInstitutionType());
        assertEquals(institutionResponse.getId(), onboardingInfo.getInstitution().getId());
        assertEquals(institutionResponse.getProductInfo().getId(), onboardingInfo.getBinding().getProducts().getProductId());
        assertEquals(institutionResponse.getInstitutionType(), onboardingInfo.getInstitution().getInstitutionType());

    }
}

