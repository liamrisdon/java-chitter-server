package com.chitter.server;

import com.chitter.server.controller.SignUpController;
import com.chitter.server.model.User;
import com.chitter.server.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignUpController.class)
public class SignUpControllerTests {

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
    void shouldSuccessfullySignUpUser() throws Exception {
        User testUser = new User("testUsername", "testUser", "test@email.com", "testpassword");

        when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);
        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testUser)))
                .andExpect(content().string("User successfully signed up"))
                .andDo(print());

    }

    @Test
    void shouldReturnErrorIfEmailIsAlreadyTaken() throws Exception {

        User testEmailUser = new User("testEmailUsername", "testEmailUser", "existingtestuser@email.com", "testEmailPassword");

        when(userRepository.existingEmailCheck(testEmailUser.getEmail())).thenReturn(true);
        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testEmailUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already linked to an account"))
                .andDo(print());
    }

    @Test
    void shouldReturnErrorIfUsernameIsAlreadyTaken() throws Exception {

        User testUsernameUser = new User("username", "testUsernameUser", "testusername@email.com", "testUsernamePassword");

        when(userRepository.existingUsernameCheck(testUsernameUser.getUsername())).thenReturn(true);
        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testUsernameUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already linked to an account"))
                .andDo(print());
    }
}
