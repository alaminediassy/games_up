package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.UserRegisterDTO;
import com.gamesUP.gamesUP.dto.response.UserResponseDTO;
import com.gamesUP.gamesUP.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Important : ouvrir le contexte de mocks
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build(); // ➔ Juste le controller isolé
    }

    @Test
    void shouldReturnHelloMessage() throws Exception {
        mockMvc.perform(get("/api/private/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bienvenue sur votre espace personnel privé !"));
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        UserResponseDTO responseDTO = new UserResponseDTO("John Doe", "john@example.com", "CLIENT");
        when(userService.register(any(UserRegisterDTO.class))).thenReturn(responseDTO);

        // Act + Assert
        mockMvc.perform(post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nom": "John Doe",
                                "email": "john@example.com",
                                "password": "password123"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));
    }

    @Test
    void shouldHandleRegistrationError() throws Exception {
        // Arrange
        when(userService.register(any(UserRegisterDTO.class)))
                .thenThrow(new IllegalArgumentException("Email already used"));

        // Act + Assert
        mockMvc.perform(post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nom": "John Doe",
                                "email": "alreadyused@example.com",
                                "password": "password123"
                            }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already used"));
    }
}
