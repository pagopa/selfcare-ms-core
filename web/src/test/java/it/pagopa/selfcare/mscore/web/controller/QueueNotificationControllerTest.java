package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.core.QueueNotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {QueueNotificationController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {QueueNotificationController.class})
class QueueNotificationControllerTest {
    private static final String BASE_URL = "/notification-event";
    @Autowired
    protected MockMvc mvc;
    @MockBean
    private QueueNotificationService queueNotificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendContracts() throws Exception {
        Integer size = 1;
        String productId = "product";
        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"/contracts")
                        .param("size", String.valueOf(size))
                        .param("productsFilter", productId))
                .andExpect(status().isOk());

        Mockito.verify(queueNotificationService, Mockito.times(1)).sendContracts(Optional.of(size), List.of(productId));
    }

    @Test
    void sendUsers() throws Exception{
        Integer size = 1;
        Integer page = 0;
        String productId = "product";
        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL+"/users")
                        .param("size", String.valueOf(size))
                        .param("page",String.valueOf(page))
                        .param("productsFilter", productId))
                .andExpect(status().isOk());

        Mockito.verify(queueNotificationService, Mockito.times(1)).sendUsers(Optional.of(size),Optional.of(page), List.of(productId), Optional.empty());
    }
}