package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequest;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequestFromTaxcode;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapperImpl;
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
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    final String FROM1 = "from1";
    final String FROM2 = "from2";
    final String FROM3 = "from3";
    final String TO1 = "to1";
    final String TO2 = "to2";

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
        delegationRequest.setInstitutionToName("Test to name");
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
    void getDelegations_shouldInvalidRequest() {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/delegations?&productId={productId}", "productId");

        assertThrows(NestedServletException.class, () ->
            MockMvcBuilders.standaloneSetup(delegationController)
                    .build()
                    .perform(requestBuilder));
        
    }

    /**
     * Method under test: {@link DelegationController#getDelegations(String, String, String, String, GetDelegationsMode, Optional, Optional)}
     */
    @Test
    void getDelegations_shouldGetData() throws Exception {
        // Given
        Delegation expectedDelegation = dummyDelegation();

        when(delegationService.getDelegations(expectedDelegation.getFrom(), expectedDelegation.getTo(), null,
                expectedDelegation.getProductId(), GetDelegationsMode.NORMAL, Optional.empty(), Optional.empty()))
                .thenReturn(List.of(expectedDelegation));
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/delegations?institutionId={institutionId}&brokerId={brokerId}&productId={productId}&mode={mode}", expectedDelegation.getFrom(),
                        expectedDelegation.getTo(), expectedDelegation.getProductId(), GetDelegationsMode.NORMAL);
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
        assertThat(actual.getInstitutionName()).isEqualTo(expectedDelegation.getInstitutionFromName());
        assertThat(actual.getBrokerId()).isEqualTo(expectedDelegation.getTo());
        assertThat(actual.getProductId()).isEqualTo(expectedDelegation.getProductId());
        assertThat(actual.getInstitutionId()).isEqualTo(expectedDelegation.getFrom());
        assertThat(actual.getInstitutionRootName()).isEqualTo(expectedDelegation.getInstitutionFromRootName());

        verify(delegationService, times(1))
                .getDelegations(expectedDelegation.getFrom(), expectedDelegation.getTo(),
                        expectedDelegation.getProductId(), null, GetDelegationsMode.NORMAL,
                        Optional.empty(), Optional.empty());

        verifyNoMoreInteractions(delegationService);
    }

    @Test
    void getDelegations_shouldGetDataCustom() throws Exception {
        // Given
        List<Delegation> expectedDelegations = new ArrayList<>();
        Delegation delegation1 = createDelegation("1", FROM1, TO1);
        Delegation delegation2 = createDelegation("2", FROM2, TO1);
        expectedDelegations.add(delegation1);
        expectedDelegations.add(delegation2);

        when(delegationService.getDelegations(null, TO1,
                null, null, GetDelegationsMode.FULL, Optional.empty(), Optional.empty()))
                .thenReturn(expectedDelegations);
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/delegations?brokerId={brokerId}&mode={mode}", TO1, GetDelegationsMode.FULL);
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
        assertThat(response.size()).isEqualTo(2);
        DelegationResponse actual = response.get(0);
        assertThat(actual.getId()).isEqualTo(delegation1.getId());
        assertThat(actual.getInstitutionName()).isEqualTo(delegation1.getInstitutionFromName());
        assertThat(actual.getBrokerId()).isEqualTo(delegation1.getTo());
        assertThat(actual.getProductId()).isEqualTo(delegation1.getProductId());
        assertThat(actual.getInstitutionId()).isEqualTo(delegation1.getFrom());
        assertThat(actual.getInstitutionRootName()).isEqualTo(delegation1.getInstitutionFromRootName());

        verify(delegationService, times(1))
                .getDelegations(null, TO1,
                        null, null, GetDelegationsMode.FULL,
                        Optional.empty(), Optional.empty());
        verifyNoMoreInteractions(delegationService);
    }

    /**
     * Method under test: {@link DelegationController#getDelegations(String, String, String, String, GetDelegationsMode, Optional, Optional)}
     */
    @Test
    void getDelegations_shouldGetData_nullMode() throws Exception {
        // Given
        Delegation expectedDelegation = dummyDelegation();

        when(delegationService.getDelegations(expectedDelegation.getFrom(), expectedDelegation.getTo(),
                expectedDelegation.getProductId(), null, null, Optional.empty(), Optional.empty()))
                .thenReturn(List.of(expectedDelegation));
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/delegations?institutionId={institutionId}&brokerId={brokerId}&productId={productId}", expectedDelegation.getFrom(),
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
        assertThat(actual.getInstitutionName()).isEqualTo(expectedDelegation.getInstitutionFromName());
        assertThat(actual.getBrokerId()).isEqualTo(expectedDelegation.getTo());
        assertThat(actual.getProductId()).isEqualTo(expectedDelegation.getProductId());
        assertThat(actual.getInstitutionId()).isEqualTo(expectedDelegation.getFrom());
        assertThat(actual.getInstitutionRootName()).isEqualTo(expectedDelegation.getInstitutionFromRootName());

        verify(delegationService, times(1))
                .getDelegations(expectedDelegation.getFrom(), expectedDelegation.getTo(),
                        expectedDelegation.getProductId(), null, null, Optional.empty(), Optional.empty());
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

    private Delegation createDelegation(String pattern, String from, String to) {
        Delegation delegation = new Delegation();
        delegation.setId("id_" + pattern);
        delegation.setProductId("productId");
        delegation.setType(DelegationType.PT);
        delegation.setTo(to);
        delegation.setFrom(from);
        delegation.setInstitutionFromName("name_" + from);
        delegation.setInstitutionFromRootName("name_" + to);
        return delegation;
    }

    /**
     * Method under test: {@link DelegationController#createDelegationFromInstitutionsTaxCode(DelegationRequestFromTaxcode)}
     */
    @Test
    void testCreateDelegationUsingTaxCode() throws Exception {

        Delegation delegation = new Delegation();
        delegation.setId("id");
        delegation.setTo("to");
        delegation.setFrom("from");
        when(delegationService.createDelegationFromInstitutionsTaxCode(any())).thenReturn(delegation);

        DelegationRequestFromTaxcode delegationRequest = new DelegationRequestFromTaxcode();
        delegationRequest.setFromTaxCode("111111");
        delegationRequest.setToTaxCode("2222222");
        delegationRequest.setInstitutionFromName("Test name");
        delegationRequest.setInstitutionToName("Test to name");
        delegationRequest.setProductId("productId");
        delegationRequest.setType(DelegationType.PT);
        String content = (new ObjectMapper()).writeValueAsString(delegationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/delegations/from-taxcode")
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

    @Test
    void testDeleteDelegation() throws Exception {
        doNothing().when(delegationService).deleteDelegationByDelegationId(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/delegations/{delegationId}",
                "42");
        MockMvcBuilders.standaloneSetup(delegationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}