package com.gamesUP.gamesUP.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.request.LoginRequest;
import com.gamesUP.gamesUP.dto.request.UserRegisterDTO;
import com.gamesUP.gamesUP.dto.response.LoginResponse;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Inscription réussie : avec le rôle CLIENT")
    void shouldRegisterUserSuccessfully() throws Exception {
        UserRegisterDTO registerDTO = new UserRegisterDTO(
                "Mamadou DIASSY",
                "mamadou@example.com",
                "Password123!"
        );

        mockMvc.perform(post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Mamadou DIASSY"))
                .andExpect(jsonPath("$.email").value("mamadou@example.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));
    }

    @Test
    @DisplayName("Inscription échoue : email déjà utilisé")
    void shouldFailToRegisterUserWithExistingEmail() throws Exception {
        UserRegisterDTO existingUser = new UserRegisterDTO(
                "Mamadou DIASSY",
                "mamadou@example.com",
                "Password123!"
        );
        mockMvc.perform(post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isOk());

        UserRegisterDTO duplicateUser = new UserRegisterDTO(
                "Jean Paul",
                "mamadou@example.com",
                "JeanPassword456!"
        );

        mockMvc.perform(post("/api/public/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cet email est déjà utilisé."));
    }

    @Test
    @DisplayName("Connexion réussie : retourne un token JWT")
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        // Création utilisateur dans la DB avec mot de passe encodé
        User user = new User();
        user.setNom("Client Test");
        user.setEmail("client@gamesup.com");
        user.setPassword(passwordEncoder.encode("clientpass"));
        user.setRole(Role.CLIENT);
        userRepository.save(user);

        LoginRequest request = new LoginRequest("client@gamesup.com", "clientpass");

        String responseJson = mockMvc.perform(post("/api/public/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse response = objectMapper.readValue(responseJson, LoginResponse.class);
        assertThat(response.token()).isNotBlank();
    }
}
