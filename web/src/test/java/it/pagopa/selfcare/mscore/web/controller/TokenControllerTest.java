package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TokenControllerTest {

    @InjectMocks
    private TokenController tokenController;

    @Mock
    private TokenService tokenService;

    @Test
    void verifyToken() throws Exception {
        when(tokenService.verifyToken(any())).thenReturn(new Token());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/tokens/{tokenId}/verify","42")
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(tokenController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getToken() throws Exception {
        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setUsers(new ArrayList<>());
        when(tokenService.retrieveToken(any())).thenReturn(tokenRelationships);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/tokens/{tokenId}","42")
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(tokenController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

