package dz.bououza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dz.bououza.dto.RegistrationRequest;
import dz.bououza.service.IRegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RegistrationController.class,useDefaultFilters = false,
        includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = RegistrationController.class
        )})
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IRegistrationService service;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TOKEN="PbprG3c2FcioN94uYYlDHROkewyLcs1DmvC76bYHNqXrQgAto8VegrgfixBhMMdStvLcaT0YeJynL" +
            "mBcpin2Ssz7HPfLegD0HwV7Zx66gD5jPCjDddawLse9lRLgL9rO";

    @Test
    @WithMockUser
    public void register_newUser_shouldReturnStatus200() throws Exception {
        RegistrationRequest request=new RegistrationRequest();
        request.setFirstName("f_user1");
        request.setLastName("l_user1");
        request.setEmail("user1@email.com");
        request.setPassword("P@ssw0rd");

        when(service.register(request)).thenReturn(TOKEN);
        ResultActions resultActions=mvc.perform(post("/api/v1/registration")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        verify(service,times(1)).register(request);
        verifyNoMoreInteractions(service);
    }
}