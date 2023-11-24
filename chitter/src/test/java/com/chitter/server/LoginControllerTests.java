package com.chitter.server;

import com.chitter.server.controller.LoginController;
import com.chitter.server.model.User;
import com.chitter.server.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;;

@WebMvcTest(LoginController.class)
public class LoginControllerTests {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User existingTestUser;

    @BeforeEach
    void init() {
        existingTestUser = new User("username", "name", "existingtestuser@email.com", "password");
    }

    @AfterEach
    void tearDown() {
        existingTestUser = null;
    }

    @Test
    void shouldSuccessfullyLogin() throws Exception {

        when(userRepository.findByUsername(existingTestUser.getUsername())).thenReturn(Optional.of(existingTestUser));
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(existingTestUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully logged in"))
                .andDo(print());
    }
}
