package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.UserRelationshipService;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRelationshipService userRelationshipService;

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
                                "{\"institutionUpdate\":{\"paymentServiceProvider\":{\"abiCode\":null,\"businessRegisterNumber\":null,"
                                        + "\"legalRegisterNumber\":null,\"legalRegisterName\":null,\"vatNumberGroup\":false},\"dataProtectionOfficer\":"
                                        + "{\"address\":null,\"email\":null,\"pec\":null},\"imported\":false}}"));
    }

    /**
     * Method under test: {@link UserController#getRelationship(String)}
     */
    @Test
    void testGetRelationship2() throws Exception {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("?", "42", "?", "42", true);

        Institution institution = new Institution("42", "42", Origin.MOCK, "42",
                "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654", "?",
                billing, onboarding, geographicTaxonomies, attributes, paymentServiceProvider,
                new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "?"), "?", "?", "?", "jane.doe@example.org",
                "6625550144", true, null, null);
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
                                        + "\":\"?\",\"businessRegisterNumber\":\"42\",\"legalRegisterNumber\":\"42\",\"legalRegisterName\":\"?\",\"vatNumberGroup"
                                        + "\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"?\"},"
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
}

