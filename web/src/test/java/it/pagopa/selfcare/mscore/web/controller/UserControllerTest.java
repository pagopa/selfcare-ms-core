package it.pagopa.selfcare.mscore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.mscore.core.PersonService;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.user.Person;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private PersonService personService;

    @Test
    void infoUser() throws Exception {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser("userId");

        when(personService.findByUserId(any())).thenReturn(onboardedUser);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons/id");

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"userId\":\"userId\"}"));

    }

    @Test
    void createUser() throws Exception {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setUser("userId");

        when(personService.createUser(any())).thenReturn(onboardedUser);

        Person person = new Person();
        person.setId("userId");
        String content = (new ObjectMapper()).writeValueAsString(person);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/persons")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"userId\":\"userId\"}"));


    }

    @Test
    void verifyUser() throws Exception {
        OnboardedUser onboardedUser = new OnboardedUser();

        when(personService.findByUserId(any())).thenReturn(onboardedUser);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .head("/persons/id")
                .contentType(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

