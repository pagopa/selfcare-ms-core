package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.web.config.WebTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {TokenController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {TokenController.class, WebTestConfig.class})
class TokenControllerTest {


    @MockBean
    private TokenService tokenService;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void verifyToken() throws Exception {
        //given
        String id = UUID.randomUUID().toString();
        Token token = new Token();
        when(tokenService.verifyToken(any())).thenReturn(token);

        //when
        mvc.perform(MockMvcRequestBuilders
                        .post("/tokens/{tokenId}/verify", id)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)));

        //then
        verify(tokenService, times(1)).verifyToken(id);
    }

    @Test
    void getToken() throws Exception {
        //given
        String institutionId = "institutionId";
        String productId = "productId";
        Token token = mockInstance(new Token());
        TokenUser user = mockInstance(new TokenUser());
        token.setUsers(List.of(user));
        when(tokenService.getToken(anyString(), anyString())).thenReturn(token);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/tokens/token")
                        .param("institutionId", institutionId)
                        .param("productId", productId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));
        //then
        verify(tokenService, times(1)).getToken(institutionId, productId);

    }
}

