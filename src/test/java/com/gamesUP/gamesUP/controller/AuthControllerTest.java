package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.LoginRequest;
import com.gamesUP.gamesUP.dto.response.LoginResponse;
import com.gamesUP.gamesUP.exception.BadCredentialsException;
import com.gamesUP.gamesUP.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthController authController = new AuthController(jwtService, authenticationManager);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new com.gamesUP.gamesUP.exception.GlobalExceptionHandler()) // Important pour attraper BadCredentialsException
                .build();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(authentication))
                .thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "email": "test@example.com",
                                "password": "password123"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void shouldReturnBadRequestOnInvalidCredentials() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException());

        mockMvc.perform(post("/api/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "email": "wrong@example.com",
                                "password": "wrongpassword"
                            }
                        """))
                .andExpect(status().isUnauthorized());
    }
}
