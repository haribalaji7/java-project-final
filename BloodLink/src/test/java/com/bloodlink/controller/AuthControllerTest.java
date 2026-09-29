package com.bloodlink.controller;

import com.bloodlink.dto.AuthResponse;
import com.bloodlink.dto.LoginRequest;
import com.bloodlink.dto.RegisterRequest;
import com.bloodlink.dto.UserDTO;
import com.bloodlink.entity.Role;
import com.bloodlink.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterEndpoint() throws Exception {
        RegisterRequest request = new RegisterRequest("Test", "test@example.com", "Password@123");
        UserDTO user = new UserDTO(1L, "Test", "test@example.com", Role.USER);
        AuthResponse response = new AuthResponse(null, "Registration successful", user);

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registration successful"));
    }

    @Test
    void testLoginEndpoint() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "Password@123");
        UserDTO user = new UserDTO(1L, "Test", "test@example.com", Role.USER);
        AuthResponse response = new AuthResponse("fake-jwt-token", null, user);

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void testProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/donors"))
                .andExpect(status().isForbidden()); // or 401 based on filter logic
    }

    @Test
    @WithMockUser(roles = "USER")
    void testProtectedEndpointWithValidToken() throws Exception {
        mockMvc.perform(get("/api/donors"))
                .andExpect(status().isOk()); 
    }
}
