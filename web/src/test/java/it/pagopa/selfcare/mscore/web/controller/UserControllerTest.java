package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.UserRelationshipService;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapper;
import it.pagopa.selfcare.mscore.core.util.UserNotificationMapperImpl;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.UserToNotify;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.web.TestUtils;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapperImpl;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedInstitutionResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.user.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRelationshipService userRelationshipService;

    @Mock
    private UserService userService;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @Spy
    private UserNotificationMapper userNotificationMapper = new UserNotificationMapperImpl();

    @BeforeEach
    void resetContext() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    /**
     * Method under test: {@link UserController#activateRelationship(String, org.springframework.security.core.Authentication)}
     */
    @Test
    void testActivateRelationship() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);

        doNothing().when(userRelationshipService).activateRelationship(org.mockito.Mockito.any(), any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/relationships/{relationshipId}/activate", "42")
                .principal(authentication);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#activateRelationship(String, org.springframework.security.core.Authentication)}
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
     * Method under test: {@link UserController#activateRelationship(String, org.springframework.security.core.Authentication)}
     */
    @Test
    void testActivateRelationship3() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        doNothing().when(userRelationshipService).activateRelationship(org.mockito.Mockito.any(), any(), any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/relationships/{relationshipId}/activate",
                "42").principal(authentication);
        postResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(postResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#suspendRelationship(String, org.springframework.security.core.Authentication)}
     */
    @Test
    void testSuspendRelationship() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        doNothing().when(userRelationshipService).suspendRelationship(org.mockito.Mockito.any(), any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/relationships/{relationshipId}/suspend", "42")
                .principal(authentication);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#suspendRelationship(String, org.springframework.security.core.Authentication)}
     */
    @Test
    void testSuspendRelationship2() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        doNothing().when(userRelationshipService).suspendRelationship(org.mockito.Mockito.any(), any(), any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/relationships/{relationshipId}/suspend",
                "42").principal(authentication);
        postResult.characterEncoding("Encoding");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(postResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#deleteRelationship(String, org.springframework.security.core.Authentication)}
     */
    @Test
    void testDeleteRelationship() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        doNothing().when(userRelationshipService).deleteRelationship(org.mockito.Mockito.any(), any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/relationships/{relationshipId}",
                "42").principal(authentication);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserController#deleteRelationship(String, org.springframework.security.core.Authentication)}
     */
    @Test
    void testDeleteRelationship2() throws Exception {
        SelfCareUser selfCareUser = SelfCareUser.builder("id").name("nome").surname("cognome").build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(selfCareUser);
        doNothing().when(userRelationshipService).deleteRelationship(org.mockito.Mockito.any(), any(), any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/relationships/{relationshipId}",
                "42").principal(authentication);
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
     * Method under test: {@link UserController#getInstitutionProductsInfo(String, String, String[])}
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

        when(userService.getUserInfo(anyString(), anyString(), any())).thenReturn(List.of(onboardingInfo));

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

    /**
     * Method under test: {@link UserController#getUserProductsInfo(String, String, String[])}
     */
    @Test
    void getUserProductsInfo_shouldNotFound() throws Exception {

        final String userId = "userId";
        final String institutionId = "institutionId";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{id}/products?institutionId={test}", userId, institutionId)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserController#getUserProductsInfo(String, String, String[])}
     */
    @Test
    void getUserProductsInfo_shouldGetData() throws Exception {

        final String institutionId = "institutionId";

        OnboardedUser expected  = dummyOnboardedUser(institutionId);

        when(userService.retrieveBindings(anyString(), anyString(), any(), any()))
                .thenReturn(expected.getBindings());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{id}/products?institutionId={test}", expected.getId(), institutionId)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UserProductsResponse actual = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(), UserProductsResponse.class);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertNotNull(actual.getBindings());
        assertEquals(1, actual.getBindings().size());

        InstitutionProducts actualInstitutionProducts = actual.getBindings().get(0);
        UserBinding expectedUserBinding = expected.getBindings().get(0);

        assertThat( actualInstitutionProducts.getInstitutionId()).isEqualTo(expectedUserBinding.getInstitutionId());

        Product actualProduct = actualInstitutionProducts.getProducts().get(0);
        OnboardedProduct expectedProduct = expectedUserBinding.getProducts().get(0);

        assertThat( actualProduct.getProductRole()).isEqualTo(expectedProduct.getProductRole());
        assertThat( actualProduct.getProductId()).isEqualTo(expectedProduct.getProductId());
        assertThat( actualProduct.getContract()).isEqualTo(expectedProduct.getContract());
        assertThat( actualProduct.getRole()).isEqualTo(expectedProduct.getRole());
        assertThat( actualProduct.getEnv()).isEqualTo(expectedProduct.getEnv());
        assertThat( actualProduct.getTokenId()).isEqualTo(expectedProduct.getTokenId());
        assertThat( actualProduct.getStatus()).isEqualTo(expectedProduct.getStatus());

    }


    /**
     * Method under test: {@link UserController#getUserProductsInfo(String, String, String[])}
     */
    @Test
    void getUserProductsInfo_shouldGetDataWithStates() throws Exception {
        //Given

        final String institutionId = "institutionId";
        final String state = "ACTIVE";

        OnboardedUser expected  = dummyOnboardedUser(institutionId);

        when(userService.retrieveBindings(anyString(), anyString(), any(), any()))
                .thenReturn(expected.getBindings());

        //When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{id}/products?institutionId={test}&states={state}", expected.getId(), institutionId, state)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        //Then
        UserProductsResponse actual = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(), UserProductsResponse.class);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertNotNull(actual.getBindings());
        assertEquals(1, actual.getBindings().size());

        InstitutionProducts actualInstitutionProducts = actual.getBindings().get(0);
        UserBinding expectedUserBinding = expected.getBindings().get(0);

        assertThat( actualInstitutionProducts.getInstitutionId()).isEqualTo(expectedUserBinding.getInstitutionId());

        Product actualProduct = actualInstitutionProducts.getProducts().get(0);
        OnboardedProduct expectedProduct = expectedUserBinding.getProducts().get(0);

        assertThat( actualProduct.getProductRole()).isEqualTo(expectedProduct.getProductRole());
        assertThat( actualProduct.getProductId()).isEqualTo(expectedProduct.getProductId());
        assertThat( actualProduct.getContract()).isEqualTo(expectedProduct.getContract());
        assertThat( actualProduct.getRole()).isEqualTo(expectedProduct.getRole());
        assertThat( actualProduct.getEnv()).isEqualTo(expectedProduct.getEnv());
        assertThat( actualProduct.getTokenId()).isEqualTo(expectedProduct.getTokenId());
        assertThat( actualProduct.getStatus()).isEqualTo(expectedProduct.getStatus());

    }


    /**
     * Method under test: {@link UserController#deleteProducts(String, String, String)}
     */
    @Test
    void deleteProducts() throws Exception {

        final String userId = "userId";
        final String institutionId = "institutionId";
        final String productId = "productId";

        doNothing().when(userService).updateUserStatus(userId, institutionId, productId, null, null, RelationshipState.DELETED);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/{userId}/institutions/{institutionId}/products/{productId}", userId, institutionId, productId)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private UserBinding dummyUserBinding(String institutionId) {

        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId(institutionId);
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("productId");
        onboardedProduct.setProductRole("admin");
        onboardedProduct.setStatus(RelationshipState.ACTIVE);
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setEnv(Env.PROD);
        onboardedProduct.setContract("contract");
        onboardedProduct.setRelationshipId("setRelationshipId");
        onboardedProduct.setTokenId("setTokenId");

        userBinding.setProducts(List.of(onboardedProduct));
        return userBinding;
    }

    private OnboardedUser dummyOnboardedUser(String institutionId) {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId("id");
        UserBinding userBinding = dummyUserBinding(institutionId);
        onboardedUser.setBindings(List.of(userBinding));
        return onboardedUser;
    }

    @Test
    void testGetPersonOk() throws Exception {

        User user = new User();
        user.setFiscalCode("taxCode");
        CertifiedField<String> certName = new CertifiedField<>();
        certName.setValue("nome");
        user.setName(certName);
        CertifiedField<String> certSurname = new CertifiedField<>();
        certSurname.setValue("cognome");
        user.setFamilyName(certSurname);
        Map<String, WorkContact> map = new HashMap<>();
        WorkContact contact = new WorkContact();
        CertifiedField<String> mail = new CertifiedField<>();
        mail.setValue("mail@test.it");
        contact.setEmail(mail);
        map.put("id", contact);
        user.setWorkContacts(map);
        when(userService.retrievePerson("userId", "prod-pn", "id")).thenReturn(user);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/userId?productId=prod-pn&institutionId=id")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UserResponse response = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        Assertions.assertEquals("nome", response.getName());
        Assertions.assertEquals("cognome", response.getSurname());
        Assertions.assertEquals("mail@test.it", response.getEmail());
        Assertions.assertEquals("taxCode", response.getTaxCode());

    }

    @Test
    void testGetPersonOkWithoutMail() throws Exception {

        User user = new User();
        user.setFiscalCode("taxCode");
        CertifiedField<String> certName = new CertifiedField<>();
        certName.setValue("nome");
        user.setName(certName);
        CertifiedField<String> certSurname = new CertifiedField<>();
        certSurname.setValue("cognome");
        user.setFamilyName(certSurname);
        Map<String, WorkContact> map = new HashMap<>();
        WorkContact contact = new WorkContact();
        CertifiedField<String> mail = new CertifiedField<>();
        mail.setValue("mail@test.it");
        contact.setEmail(mail);
        map.put("id", contact);
        user.setWorkContacts(map);
        when(userService.retrievePerson("userId", "prod-pn", null)).thenReturn(user);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/userId?productId=prod-pn")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UserResponse response = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        Assertions.assertEquals("nome", response.getName());
        Assertions.assertEquals("cognome", response.getSurname());
        Assertions.assertEquals("taxCode", response.getTaxCode());

    }

    /**
     * Method under test: {@link UserController#getUsers(Optional, Optional, String)}}
     */
    @Test
    void testFindAllUsers() throws Exception {

        UserNotificationToSend user =  new UserNotificationToSend();
        user.setInstitutionId("institutionId");
        UserToNotify userToNotify = new UserToNotify();
        userToNotify.setName("name");
        userToNotify.setFamilyName("surname");
        user.setUser(userToNotify);
        user.setProductId("prod-io");

        when(userService.findAll(any(), any(), anyString())).thenReturn(List.of(user));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users?productId=prod-io")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UsersNotificationResponse response = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                UsersNotificationResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUsers());
        Assertions.assertFalse(response.getUsers().isEmpty());
        Assertions.assertNotNull(response.getUsers().get(0));
        Assertions.assertNotNull(response.getUsers().get(0).getBindings().get(0).getUser());
        Assertions.assertEquals("name", response.getUsers().get(0).getBindings().get(0).getUser().getName());
        Assertions.assertEquals("surname", response.getUsers().get(0).getBindings().get(0).getUser().getFamilyName());
        Assertions.assertEquals("prod-io", response.getUsers().get(0).getBindings().get(0).getProductId());
        Assertions.assertEquals("institutionId", response.getUsers().get(0).getBindings().get(0).getInstitutionId());

    }

    @Test
    void updateUserStatus() throws Exception {
        final String userId = "userId";

        doNothing().when(userService).updateUserStatus(userId, null, null, null, null, RelationshipState.DELETED);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/{id}/status", userId)
                .queryParam("status","DELETED")
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void onboardedUsers() throws Exception {
        final OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId("userId");
        when(userService.findAllByIds(any())).thenReturn(List.of(onboardedUser));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/onboarded-users", "userId")
                .queryParam("ids","DELETED")
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

