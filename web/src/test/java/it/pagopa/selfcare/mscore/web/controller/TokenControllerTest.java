package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.TokenService;
import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.web.config.WebTestConfig;
import it.pagopa.selfcare.mscore.web.model.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.web.model.token.ScContractResponse;
import it.pagopa.selfcare.mscore.web.model.token.TokenResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {TokenController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {TokenController.class, WebTestConfig.class,})
class TokenControllerTest {

    @Autowired
    private TokenController tokenController;

    @MockBean
    private TokenMapper tokenMapper;

    @MockBean
    private TokenService tokenService;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * Method under test:
     * {@link TokenController#getAllTokens(List, Integer, Integer, String)}
     */
    @Test
    void testFindAllTokens() throws Exception {
        // Arrange
        PaginatedToken tokenList = new PaginatedToken();

        NotificationToSend notification = mockInstance(new NotificationToSend());
        tokenList.setItems(List.of(notification));

        ScContractResponse scContractResponse = mockInstance(new ScContractResponse());

        when(tokenService.retrieveContractsFilterByStatus(Mockito.<List<RelationshipState>>any(), Mockito.<Integer>any(),
                Mockito.<Integer>any(), Mockito.<String>any())).thenReturn(tokenList);
        when(tokenMapper.toScContractResponse(any())).thenReturn(scContractResponse);
        // Act and Assert
        mvc.perform(MockMvcRequestBuilders.get("/tokens")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        verify(tokenService, times(1)).retrieveContractsFilterByStatus(Mockito.<List<RelationshipState>>any(), Mockito.<Integer>any(),
                Mockito.<Integer>any(), Mockito.<String>any());
    }

    @Test
    void getToken() throws Exception {
        //given
        String institutionId = "institutionId";
        String productId = "productId";
        Token token = mockInstance(new Token());
        TokenUser user = mockInstance(new TokenUser());
        token.setUsers(List.of(user));
        TokenResource tokenResource = mockInstance(new TokenResource());
        tokenResource.setUsers(List.of(user));
        when(tokenService.getToken(anyString(), anyString())).thenReturn(token);
        when(tokenMapper.toTokenResponse(token)).thenReturn(tokenResource);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/tokens/token")
                .param("institutionId", institutionId)
                .param("productId", productId)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()));
        //then
        verify(tokenService, times(1)).getToken(institutionId, productId);

    }

    /**
     * Method under test:
     * {@link TokenController#findFromProduct(String, Integer, Integer)}
     */
    @Test
    void findFromProduct() throws Exception {
        // Given
        String productIdMock = "productId";
        Integer pageMock = 0;
        Integer sizeMock = 100;

        TokenRelationships token = new TokenRelationships();
        token.setTokenId("id");
        token.setProductId("productId");
        token.setInstitutionId("institutionId");
        OnboardedUser user = mockInstance(new OnboardedUser());
        OnboardedProduct product = mockInstance(new OnboardedProduct());
        product.setProductId("productId");
        UserBinding binding = mockInstance(new UserBinding());
        binding.setInstitutionId("institutionId");
        binding.setProducts(List.of(product));
        user.setBindings(List.of(binding));
        token.setUsers(List.of(user));
        // When
        when(tokenService.getTokensByProductId(any(), any(), any())).thenReturn(List.of(token));

        mvc.perform(MockMvcRequestBuilders.get("/tokens/products/{productId}", productIdMock)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));

        // Then
        verify(tokenService, times(1)).getTokensByProductId(productIdMock, pageMock, sizeMock);
        verifyNoMoreInteractions(tokenService);
    }
}

