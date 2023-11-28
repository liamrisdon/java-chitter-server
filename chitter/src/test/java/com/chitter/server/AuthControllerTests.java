package com.chitter.server;

import com.chitter.server.controller.AuthController;
import com.chitter.server.model.User;
import com.chitter.server.repository.RoleRepository;
import com.chitter.server.repository.UserRepository;
import com.chitter.server.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;;


@WebMvcTest(AuthController.class)
public class AuthControllerTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

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

//        when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);
        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testUser))).with(jwt())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.message").value("Registration successful"))
//                .andExpect(jsonPath("$.user.username").value(testUser.getUsername()))
//                .andExpect(jsonPath("$.user.name").value(testUser.getName()))
//                .andExpect(jsonPath("$.user.email").value(testUser.getEmail()))
//                .andExpect(jsonPath("$.user.password").value(testUser.getPassword()))
                .andDo(print());

    }

    @Test
    void shouldReturnErrorIfEmailIsAlreadyTaken() throws Exception {

        User testEmailUser = new User("testEmailUsername", "testEmailUser", "existingtestuser@email.com", "testEmailPassword");

        when(userRepository.existsByEmail(testEmailUser.getEmail())).thenReturn(true);
        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testEmailUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already linked to an account"))
                .andDo(print());
    }

    @Test
    void shouldReturnErrorIfUsernameIsAlreadyTaken() throws Exception {

        User testUsernameUser = new User("username", "testUsernameUser", "testusername@email.com", "testUsernamePassword");

        when(userRepository.existsByUsername(testUsernameUser.getUsername())).thenReturn(true);
        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testUsernameUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already linked to an account"))
                .andDo(print());
    }

    @Test
    void shouldSuccessfullyLogin() throws Exception {

        when(userRepository.findByUsername(existingTestUser.getUsername())).thenReturn(Optional.of(existingTestUser));
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(existingTestUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully logged in"))
                .andExpect(jsonPath("$.user.username").value(existingTestUser.getUsername()))
                .andExpect(jsonPath("$.user.name").value(existingTestUser.getName()))
                .andExpect(jsonPath("$.user.email").value(existingTestUser.getEmail()))
                .andExpect(jsonPath("$.user.password").value(existingTestUser.getPassword()))
                .andDo(print());
    }

    @Test
    void shouldReturnErrorIfPasswordIsInvalid() throws Exception {

        User loginUser = new User(existingTestUser.getUsername(), existingTestUser.getName(), existingTestUser.getEmail(), "incorrectPassword");

        when(userRepository.findByUsername(existingTestUser.getUsername())).thenReturn(Optional.of(existingTestUser));
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unable to find login details"))
                .andDo(print());

    }

    @Test
    void shouldReturnErrorIfUsernameIsInvalid() throws Exception {

        User loginUser = new User("incorrectUsername", existingTestUser.getName(), existingTestUser.getEmail(), existingTestUser.getPassword());

        when(userRepository.findByUsername("incorrectUsername")).thenReturn(Optional.empty());
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unable to find login details"))
                .andDo(print());

    }


}
