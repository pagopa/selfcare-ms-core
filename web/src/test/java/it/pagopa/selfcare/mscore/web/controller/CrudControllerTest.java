package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.migration.MigrationService;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CrudController.class})
@ExtendWith(SpringExtension.class)
class CrudControllerTest {
    @Autowired
    private CrudController crudController;

    @MockBean
    private MigrationService migrationService;

    @Test
    void testCreateToken() throws Exception {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(migrationService.createToken(any())).thenReturn(token);

        InstitutionUpdate institutionUpdate1 = new InstitutionUpdate();
        institutionUpdate1.setAddress("42 Main St");
        institutionUpdate1.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate1
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate1.setDescription("The characteristics of someone or something");
        institutionUpdate1.setDigitalAddress("42 Main St");
        institutionUpdate1.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate1.setImported(true);
        institutionUpdate1.setInstitutionType(InstitutionType.PA);
        institutionUpdate1
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate1.setRea("Rea");
        institutionUpdate1.setShareCapital("Share Capital");
        institutionUpdate1.setSupportEmail("jane.doe@example.org");
        institutionUpdate1.setSupportPhone("6625550144");
        institutionUpdate1.setTaxCode("Tax Code");
        institutionUpdate1.setZipCode("21654");

        Token token1 = new Token();
        token1.setChecksum("Checksum");
        token1.setDeletedAt(null);
        token1.setContractSigned("Contract Signed");
        token1.setContractTemplate("Contract Template");
        token1.setCreatedAt(null);
        token1.setExpiringDate(null);
        token1.setId("42");
        token1.setInstitutionId("42");
        token1.setInstitutionUpdate(institutionUpdate1);
        token1.setProductId("42");
        token1.setStatus(RelationshipState.PENDING);
        token1.setType(TokenType.INSTITUTION);
        token1.setUpdatedAt(null);
        token1.setUsers(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(token1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/migration/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"type\":\"INSTITUTION\",\"status\":\"PENDING\",\"institutionId\":\"42\",\"productId\":\"42\",\"checksum\":\"Checksum\",\"contractTemplate\":\"Contract Template\",\"contractSigned\":\"Contract Signed\",\"users\":[],\"institutionUpdate\":{\"institutionType\":\"PA\",\"description\":\"The characteristics of someone or something\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"taxCode\":\"Tax Code\",\"zipCode\":\"21654\",\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"legalRegisterNumber\":\"42\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"geographicTaxonomies\":[],\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"businessRegisterPlace\":\"Business Register Place\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"6625550144\",\"imported\":true}}"));
    }

    @Test
    void testCreateInstitution() throws Exception {
        when(migrationService.createInstitution(any())).thenReturn(new Institution());

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setBusinessRegisterPlace("Business Register Place");
        institution.setCreatedAt(null);
        institution.setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setImported(true);
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOnboarding(new ArrayList<>());
        institution.setOrigin(Origin.MOCK.name());
        institution.setOriginId("42");
        institution
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institution.setRea("Rea");
        institution.setShareCapital("Share Capital");
        institution.setSupportEmail("jane.doe@example.org");
        institution.setSupportPhone("6625550144");
        institution.setTaxCode("Tax Code");
        institution.setUpdatedAt(null);
        institution.setZipCode("21654");
        String content = (new ObjectMapper()).writeValueAsString(institution);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/migration/institution")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"imported\":false}"));
    }

    @Test
    void testCreateUser() throws Exception {
        when(migrationService.createUser(any())).thenReturn(new OnboardedUser());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardedUser.setCreatedAt(null);
        onboardedUser.setId("42");
        String content = (new ObjectMapper()).writeValueAsString(onboardedUser);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/migration/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":null,\"bindings\":null,\"createdAt\":null}"));
    }

    /**
     * Method under test: {@link CrudController#findTokenById(String)}
     */
    @Test
    void testFindTokenById() throws Exception {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(migrationService.findTokenById(any())).thenReturn(token);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/migration/token/{id}", "42");
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"type\":\"INSTITUTION\",\"status\":\"PENDING\",\"institutionId\":\"42\",\"productId\":\"42\",\"checksum\":\"Checksum\",\"contractTemplate\":\"Contract Template\",\"contractSigned\":\"Contract Signed\",\"users\":[],\"institutionUpdate\":{\"institutionType\":\"PA\",\"description\":\"The characteristics of someone or something\",\"digitalAddress\":\"42 Main St\",\"address\":\"42 Main St\",\"taxCode\":\"Tax Code\",\"zipCode\":\"21654\",\"paymentServiceProvider\":{\"abiCode\":\"Abi Code\",\"businessRegisterNumber\":\"42\",\"legalRegisterName\":\"Legal Register Name\",\"legalRegisterNumber\":\"42\",\"vatNumberGroup\":true},\"dataProtectionOfficer\":{\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\",\"pec\":\"Pec\"},\"geographicTaxonomies\":[],\"rea\":\"Rea\",\"shareCapital\":\"Share Capital\",\"businessRegisterPlace\":\"Business Register Place\",\"supportEmail\":\"jane.doe@example.org\",\"supportPhone\":\"6625550144\",\"imported\":true}}"));
    }

    /**
     * Method under test: {@link CrudController#findInstitutionById(String)}
     */
    @Test
    void testFindInstitutionById() throws Exception {
        when(migrationService.findInstitutionById(any())).thenReturn(new Institution());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/migration/institution/{id}", "42");
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"imported\":false}"));
    }

    /**
     * Method under test: {@link CrudController#findUserById(String)}
     */
    @Test
    void testFindUserById() throws Exception {
        when(migrationService.findUserById(any())).thenReturn(new OnboardedUser());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/migration/user/{id}", "42");
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":null,\"bindings\":null,\"createdAt\":null}"));
    }

    /**
     * Method under test: {@link CrudController#deleteToken(String)}
     */
    @Test
    void testDeleteToken() throws Exception {
        doNothing().when(migrationService).deleteToken(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/migration/token/{id}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CrudController#deleteToken(String)}
     */
    @Test
    void testDeleteToken2() throws Exception {
        doNothing().when(migrationService).deleteToken(any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/migration/token/{id}", "42");
        deleteResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(crudController).build().perform(deleteResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CrudController#deleteInstitution(String)}
     */
    @Test
    void testDeleteInstitution() throws Exception {
        doNothing().when(migrationService).deleteInstitution(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/migration/institution/{id}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CrudController#deleteInstitution(String)}
     */
    @Test
    void testDeleteInstitution2() throws Exception {
        doNothing().when(migrationService).deleteInstitution(any());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link CrudController#deleteInstitution(String)}
     */
    @Test
    void testDeleteInstitution3() throws Exception {
        doNothing().when(migrationService).deleteInstitution(any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/migration/institution/{id}", "42");
        deleteResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(crudController).build().perform(deleteResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CrudController#deleteUser(String)}
     */
    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(migrationService).deleteUser(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/migration/user/{id}", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CrudController#deleteUser(String)}
     */
    @Test
    void testDeleteUser2() throws Exception {
        doNothing().when(migrationService).deleteUser(any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/migration/user/{id}", "42");
        deleteResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(crudController).build().perform(deleteResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CrudController#findInstitutions()}
     */
    @Test
    void testFindInstitutions() throws Exception {
        when(migrationService.findInstitution()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/migration/institutions");
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link CrudController#findInstitutions()}
     */
    @Test
    void testFindInstitutions2() throws Exception {
        when(migrationService.findInstitution()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/migration/institutions");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link CrudController#findUsers()}
     */
    @Test
    void testFindUsers() throws Exception {
        when(migrationService.findUser()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/migration/users");
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link CrudController#findUsers()}
     */
    @Test
    void testFindUsers2() throws Exception {
        when(migrationService.findUser()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/migration/users");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(crudController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

