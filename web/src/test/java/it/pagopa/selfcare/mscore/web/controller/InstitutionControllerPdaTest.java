package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.InstitutionService;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.PdaInstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionResourceMapper;
import it.pagopa.selfcare.mscore.web.model.mapper.InstitutionResourceMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ContextConfiguration(classes = {InstitutionController.class})
@ExtendWith(MockitoExtension.class)
public class InstitutionControllerPdaTest {
    @InjectMocks
    private InstitutionController institutionController;

    @Mock
    private InstitutionService institutionService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Spy
    private InstitutionResourceMapper institutionResourceMapper = new InstitutionResourceMapperImpl();


    /**
     * Method under test: {@link InstitutionController#createInstitutionFromPda(PdaInstitutionRequest)}}
     */
    @Test
    void shouldCreateInstitutionFromPda() throws Exception {
        // Given
        PdaInstitutionRequest institutionRequest = new PdaInstitutionRequest();
        institutionRequest.setInjectionInstitutionType("EC");
        institutionRequest.setDescription("test ec");
        institutionRequest.setTaxCode("taxCode");
        String content = objectMapper.writeValueAsString(institutionRequest);

        Institution institution = createSimpleInstitutionPda();

        when(institutionService.createInstitutionFromPda(any(), any())).thenReturn(institution);

        //Then
        MockHttpServletRequestBuilder requestBuilder = post("/institutions/from-pda/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(institutionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":\"42\",\"externalId\":\"42\",\"origin\":\"MOCK\",\"originId\":\"Ipa Code\",\"description\":\"The characteristics of someone or something\",\"institutionType\":\"PA\",\"address\":\"42 Main St\",\"zipCode\":\"21654\",\"taxCode\":\"Tax Code\",\"attributes\":[],\"imported\":true}"));
    }

    public static Institution createSimpleInstitutionPda() {
        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(createSimpleBilling());

        institution.setDescription("The characteristics of someone or something");
        institution.setExternalId("42");
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setOriginId("Ipa Code");
        institution.setOrigin(Origin.MOCK.name());

        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        institution.setImported(true);

        return institution;
    }

    public static Billing createSimpleBilling() {
        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        return billing;
    }
}
