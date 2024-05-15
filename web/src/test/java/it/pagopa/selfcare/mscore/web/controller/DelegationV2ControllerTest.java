package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationWithPagination;
import it.pagopa.selfcare.mscore.model.delegation.GetDelegationParameters;
import it.pagopa.selfcare.mscore.model.delegation.PageInfo;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationWithPaginationResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DelegationV2Controller.class})
@ExtendWith(MockitoExtension.class)
class DelegationV2ControllerTest {

    @InjectMocks
    private DelegationV2Controller delegationController;

    @Mock
    private DelegationService delegationService;

    @Spy
    private DelegationMapper delegationResourceMapper = new DelegationMapperImpl();

    private final ObjectMapper objectMapper = new ObjectMapper();

    final String FROM1 = "from1";
    final String FROM2 = "from2";
    final String TO1 = "to1";

    /**
     * Method under test: {@link InstitutionController#findFromProduct(String, Integer, Integer)}
     */
    @Test
    void getDelegations_shouldInvalidRequest() {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v2/delegations?&productId={productId}", "productId");

        assertThrows(NestedServletException.class, () ->
            MockMvcBuilders.standaloneSetup(delegationController)
                    .build()
                    .perform(requestBuilder));
        
    }

    /**
     * Method under test: {@link DelegationController#getDelegations(String, String, String, String, String, GetDelegationsMode, Optional, Optional, Optional)}
     */
    @Test
    void getDelegations_shouldGetData() throws Exception {
        // Given
        Delegation expectedDelegation = dummyDelegation();
        PageInfo exptectedPageInfo = new PageInfo(10, 0, 1, 1);

        DelegationWithPagination expectedDelegationWithPagination = new DelegationWithPagination(List.of(expectedDelegation), exptectedPageInfo);

        when(delegationService.getDelegationsV2(createDelegationParameters(expectedDelegation.getFrom(), expectedDelegation.getTo(),
                expectedDelegation.getProductId(), null, null, GetDelegationsMode.NORMAL, Order.ASC, 0, 10)))
                .thenReturn(expectedDelegationWithPagination);
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v2/delegations?institutionId={institutionId}&brokerId={brokerId}&productId={productId}&mode={mode}&order={order}&page={page}&size={size}",
                        expectedDelegation.getFrom(), expectedDelegation.getTo(), expectedDelegation.getProductId(), GetDelegationsMode.NORMAL,
                        Order.ASC ,exptectedPageInfo.getPageNo(), exptectedPageInfo.getPageSize());
        MvcResult result = MockMvcBuilders.standaloneSetup(delegationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        DelegationWithPaginationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getDelegations()).isNotNull();
        assertThat(response.getPageInfo()).isNotNull();
        assertThat(response.getDelegations().size()).isEqualTo(1);
        DelegationResponse actualDelegation = response.getDelegations().get(0);
        PageInfo actualPageInfo = response.getPageInfo();
        assertThat(actualDelegation.getId()).isEqualTo(expectedDelegation.getId());
        assertThat(actualDelegation.getInstitutionName()).isEqualTo(expectedDelegation.getInstitutionFromName());
        assertThat(actualDelegation.getBrokerId()).isEqualTo(expectedDelegation.getTo());
        assertThat(actualDelegation.getProductId()).isEqualTo(expectedDelegation.getProductId());
        assertThat(actualDelegation.getInstitutionId()).isEqualTo(expectedDelegation.getFrom());
        assertThat(actualDelegation.getInstitutionRootName()).isEqualTo(expectedDelegation.getInstitutionFromRootName());
        assertThat(actualPageInfo).isEqualTo(exptectedPageInfo);

        verify(delegationService, times(1))
                .getDelegationsV2(createDelegationParameters(expectedDelegation.getFrom(), expectedDelegation.getTo(),
                        expectedDelegation.getProductId(), null, null, GetDelegationsMode.NORMAL, Order.ASC,
                        0, 10));

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

        PageInfo exptectedPageInfo = new PageInfo(10000, 0, 2, 1);

        DelegationWithPagination expectedDelegationWithPagination = new DelegationWithPagination(expectedDelegations, exptectedPageInfo);

        when(delegationService.getDelegationsV2(createDelegationParameters(null, TO1,
                null, null, null, GetDelegationsMode.FULL, Order.DESC, 0, 10000)))
                .thenReturn(expectedDelegationWithPagination);
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v2/delegations?brokerId={brokerId}&mode={mode}&order={order}", TO1, GetDelegationsMode.FULL, Order.DESC);
        MvcResult result = MockMvcBuilders.standaloneSetup(delegationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        DelegationWithPaginationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getDelegations()).isNotNull();
        assertThat(response.getPageInfo()).isNotNull();
        assertThat(response.getDelegations().size()).isEqualTo(2);
        DelegationResponse actualDelegation = response.getDelegations().get(0);
        PageInfo actualPageInfo = response.getPageInfo();
        assertThat(actualDelegation.getId()).isEqualTo(delegation1.getId());
        assertThat(actualDelegation.getInstitutionName()).isEqualTo(delegation1.getInstitutionFromName());
        assertThat(actualDelegation.getBrokerId()).isEqualTo(delegation1.getTo());
        assertThat(actualDelegation.getProductId()).isEqualTo(delegation1.getProductId());
        assertThat(actualDelegation.getInstitutionId()).isEqualTo(delegation1.getFrom());
        assertThat(actualDelegation.getInstitutionRootName()).isEqualTo(delegation1.getInstitutionFromRootName());
        assertThat(actualPageInfo).isEqualTo(exptectedPageInfo);

        verify(delegationService, times(1))
                .getDelegationsV2(createDelegationParameters(null, TO1, null,
                        null, null, GetDelegationsMode.FULL, Order.DESC,
                        0, 10000));
        verifyNoMoreInteractions(delegationService);
    }

    /**
     * Method under test: {@link DelegationController#getDelegations(String, String, String, String, String, GetDelegationsMode, Optional, Optional, Optional)}
     */
    @Test
    void getDelegations_shouldGetData_nullMode() throws Exception {
        // Given
        Delegation expectedDelegation = dummyDelegation();
        PageInfo exptectedPageInfo = new PageInfo(10000, 0, 1, 1);

        DelegationWithPagination expectedDelegationWithPagination = new DelegationWithPagination(List.of(expectedDelegation), exptectedPageInfo);

        when(delegationService.getDelegationsV2(createDelegationParameters(expectedDelegation.getFrom(), expectedDelegation.getTo(),
                expectedDelegation.getProductId(), null, null, null, Order.NONE, 0, 10000)))
                .thenReturn(expectedDelegationWithPagination);
        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v2/delegations?institutionId={institutionId}&brokerId={brokerId}&productId={productId}", expectedDelegation.getFrom(),
                        expectedDelegation.getTo(), expectedDelegation.getProductId());
        MvcResult result = MockMvcBuilders.standaloneSetup(delegationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        DelegationWithPaginationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getDelegations()).isNotNull();
        assertThat(response.getPageInfo()).isNotNull();
        assertThat(response.getDelegations().size()).isEqualTo(1);
        DelegationResponse actualDelegation = response.getDelegations().get(0);
        PageInfo actualPageInfo = response.getPageInfo();
        assertThat(actualDelegation.getId()).isEqualTo(expectedDelegation.getId());
        assertThat(actualDelegation.getInstitutionName()).isEqualTo(expectedDelegation.getInstitutionFromName());
        assertThat(actualDelegation.getBrokerId()).isEqualTo(expectedDelegation.getTo());
        assertThat(actualDelegation.getProductId()).isEqualTo(expectedDelegation.getProductId());
        assertThat(actualDelegation.getInstitutionId()).isEqualTo(expectedDelegation.getFrom());
        assertThat(actualDelegation.getInstitutionRootName()).isEqualTo(expectedDelegation.getInstitutionFromRootName());
        assertThat(actualPageInfo).isEqualTo(exptectedPageInfo);

        verify(delegationService, times(1))
                .getDelegationsV2(createDelegationParameters(expectedDelegation.getFrom(), expectedDelegation.getTo(),
                        expectedDelegation.getProductId(), null, null, null, Order.NONE, 0, 10000));
        verifyNoMoreInteractions(delegationService);
    }

    @Test
    void getDelegations_shouldInvalidRequest_wrongPageSize() {

        Delegation expectedDelegation = dummyDelegation();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v2/delegations?brokerId={brokerId}&size={size}",
                        expectedDelegation.getTo(), 0);

        assertThrows(NestedServletException.class, () ->
                MockMvcBuilders.standaloneSetup(delegationController)
                        .build()
                        .perform(requestBuilder));

    }

    @Test
    void getDelegations_shouldInvalidRequest_wrongPageNumber() {

        Delegation expectedDelegation = dummyDelegation();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v2/delegations?brokerId={brokerId}&page={size}",
                        expectedDelegation.getTo(), -1);

        assertThrows(NestedServletException.class, () ->
                MockMvcBuilders.standaloneSetup(delegationController)
                        .build()
                        .perform(requestBuilder));

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

    private GetDelegationParameters createDelegationParameters(String from, String to, String productId,
                                                               String search, String taxCode, GetDelegationsMode mode,
                                                               Order order, Integer page, Integer size) {
        return GetDelegationParameters.builder()
                .from(from)
                .to(to)
                .productId(productId)
                .search(search)
                .taxCode(taxCode)
                .mode(mode)
                .order(order)
                .page(page)
                .size(size)
                .build();
    }

}