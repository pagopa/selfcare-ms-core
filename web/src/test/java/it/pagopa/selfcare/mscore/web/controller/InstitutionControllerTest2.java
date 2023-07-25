package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionResourceMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionResourceMapperImpl;
import it.pagopa.selfcare.mscore.web.model.mapper.OnboardingResourceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@WebMvcTest(value = {InstitutionController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {InstitutionController.class})
class InstitutionControllerTest2 {

    private static final String BASE_URL = "/institutions";

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private InstitutionService institutionService;

    @MockBean
    private DelegationService delegationService;

    @MockBean
    private OnboardingResourceMapper onboardingResourceMapper;

    @MockBean
    private InstitutionResourceMapper institutionResourceMapper;

    @MockBean
    private DelegationMapper delegationMapper;

    @Test
    void updateCreatedAt_invalidDate() throws Exception {
        // Given
        String institutionIdMock = "institutionId";
        String productIdMock = "productId";
        OffsetDateTime createdAtMock = OffsetDateTime.now().minusHours(10);
        String createdAtString = createdAtMock.toString();
        // When
        mvc.perform(MockMvcRequestBuilders
                        .put(BASE_URL + "/{institutionId}/products/{productId}/createdAt", institutionIdMock, productIdMock)
                        .param("createdAt", createdAtString)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

}
