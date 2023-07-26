package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequest;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapperImpl;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionResourceMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionResourceMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DelegationController.class})
@ExtendWith(MockitoExtension.class)
class DelegationControllerTest {

    @InjectMocks
    private DelegationController delegationController;

    @Mock
    private DelegationService delegationService;

    @Spy
    private DelegationMapper delegationResourceMapper = new DelegationMapperImpl();

    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Method under test: {@link DelegationController#createDelegation(DelegationRequest)}
     */
    @Test
    void testCreateDelegation() throws Exception {

        Delegation delegation = new Delegation();
        delegation.setId("id");
        delegation.setFrom("from");
        when(delegationService.createDelegation(any())).thenReturn(delegation);

        DelegationRequest delegationRequest = new DelegationRequest();
        delegationRequest.setFrom("111111");
        delegationRequest.setTo("2222222");
        delegationRequest.setInstitutionFromName("Test name");
        delegationRequest.setProductId("productId");
        delegationRequest.setType(DelegationType.PT);
        String content = (new ObjectMapper()).writeValueAsString(delegationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/delegations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result =  MockMvcBuilders.standaloneSetup(delegationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        DelegationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(delegation.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationController#createDelegation(DelegationRequest)}
     */
    @Test
    void testCreateDelegationWithBadRequest() throws Exception {

        DelegationRequest delegationRequest = new DelegationRequest();
        String content = (new ObjectMapper()).writeValueAsString(delegationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/delegations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(delegationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Method under test: {@link InstitutionController#findFromProduct(String, Integer, Integer)}
     */
    @Test
    void getDelegations_shouldGetData() throws Exception {
        // Given
        Delegation expectedDelegation = dummyDelegation();

        when(delegationService.getDelegations(expectedDelegation.getFrom(), expectedDelegation.getTo(), expectedDelegation.getProductId()))
                .thenReturn(List.of(expectedDelegation));
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/delegations?from={from}&to={to}&productId={productId}", expectedDelegation.getFrom(),
                        expectedDelegation.getTo(), expectedDelegation.getProductId());
        MvcResult result = MockMvcBuilders.standaloneSetup(delegationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        List<DelegationResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        // Then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
        DelegationResponse actual = response.get(0);
        assertThat(actual.getId()).isEqualTo(expectedDelegation.getId());
        assertThat(actual.getInstitutionFromName()).isEqualTo(expectedDelegation.getInstitutionFromName());
        assertThat(actual.getTo()).isEqualTo(expectedDelegation.getTo());
        assertThat(actual.getProductId()).isEqualTo(expectedDelegation.getProductId());
        assertThat(actual.getFrom()).isEqualTo(expectedDelegation.getFrom());
        assertThat(actual.getInstitutionFromRootName()).isEqualTo(expectedDelegation.getInstitutionFromRootName());

        verify(delegationService, times(1))
                .getDelegations(expectedDelegation.getFrom(), expectedDelegation.getTo(), expectedDelegation.getProductId());
        verifyNoMoreInteractions(delegationService);
    }

    private Delegation dummyDelegation() {
        Delegation delegation = new Delegation();
        delegation.setFrom("from");
        delegation.setTo("to");
        delegation.setId("setId");
        delegation.setProductId("setProductId");
        delegation.setType(DelegationType.PT);
        delegation.setInstitutionFromName("setInstitutionFromName");
        delegation.setInstitutionFromRootName("setInstitutionFromRootName");
        return delegation;
    }

}