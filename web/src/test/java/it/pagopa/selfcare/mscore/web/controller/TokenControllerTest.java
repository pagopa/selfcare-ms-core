package it.pagopa.selfcare.mscore.web.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TokenController.class})
@ExtendWith(SpringExtension.class)
class TokenControllerTest {
    @Autowired
    private TokenController tokenController;

    @MockBean
    private TokenService tokenService;

    /**
     * Method under test: {@link TokenController#verifyToken(String)}
     */
    @Test
    void testVerifyToken() throws Exception {
        when(tokenService.verifyToken(any())).thenReturn(new Token());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/tokens/{tokenId}/verify", "42");
        MockMvcBuilders.standaloneSetup(tokenController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link TokenController#verifyToken(String)}
     */
    @Test
    void testVerifyToken2() throws Exception {
        when(tokenService.verifyToken(any())).thenReturn(new Token());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(tokenController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

