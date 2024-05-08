package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.core.OnboardingService;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.*;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OnboardingController.class})
@ExtendWith(MockitoExtension.class)
class OnboardingControllerTest {
    @InjectMocks
    private OnboardingController onboardingController;

    @Mock
    private OnboardingService onboardingService;

    @Spy
    @InjectMocks
    OnboardingResourceMapper onboardingResourceMapper = new OnboardingResourceMapperImpl();

    @Spy
    InstitutionUpdateMapper institutionUpdateMapper = new InstitutionUpdateMapperImpl();

    @Spy
    InstitutionResourceMapper institutionResourceMapper = new InstitutionResourceMapperImpl();

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void resetContext() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    /**
     * Method under test: {@link OnboardingController#getOnboardingDocument(String)}
     */
    @Test
    void testGetOnboardingDocument() throws Exception {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData("AXAXAXAX".getBytes(StandardCharsets.UTF_8));
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        when(onboardingService.retrieveDocument(org.mockito.Mockito.any())).thenReturn(resourceResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/onboarding/relationship/{relationshipId}/document", "42");
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"))
                .andExpect(MockMvcResultMatchers.content().string("AXAXAXAX"));
    }

    /**
     * Method under test: {@link OnboardingController#getOnboardingDocument(String)}
     */
    @Test
    void testGetOnboardingDocument2() throws Exception {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setData(null);
        resourceResponse.setFileName("foo.txt");
        resourceResponse.setMimetype("Mimetype");
        when(onboardingService.retrieveDocument(org.mockito.Mockito.any())).thenReturn(resourceResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/onboarding/relationship/{relationshipId}/document", "42");
        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String, String)}
     */
    @Test
    void shouldVerifyOnboardingInfoBySubunit() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfoSubunit(any(), any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/")
                .queryParam("taxCode", "42")
                .queryParam("productId", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfoByFilters(String, String, String, String, String, String)}
     */
    @Test
    void shouldVerifyOnboardingInfoByFiltersSuccess() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfoByFilters(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/verify")
                .queryParam("origin", "42")
                .queryParam("originId", "42")
                .queryParam("productId", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    @Test
    void shouldVerifyOnboardingInfoByExternalId() throws Exception {
        doNothing().when(onboardingService).verifyOnboardingInfo(any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/institution/{externalId}/products/{productId}", "42", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }



    /**
     * Method under test: {@link OnboardingController#verifyOnboardingInfo(String, String)}
     */
    //@Test
    void shouldNotFoundVerifyOnboardingInfoByExternalId() throws Exception {
        doThrow(new ResourceNotFoundException("","")).when(onboardingService).verifyOnboardingInfo(any(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/onboarding/institution/{externalId}/products/{productId}", "42", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link OnboardingController#onboardingInfo(String, String, String[], Authentication)}
     */
    @Test
    void testOnboardingInfo() throws Exception {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(SelfCareUser.builder("id").build());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/onboarding/info")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(onboardingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    }

