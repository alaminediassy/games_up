package com.gamesUP.gamesUP.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.request.AuthorDTO;
import com.gamesUP.gamesUP.dto.request.CategoryDTO;
import com.gamesUP.gamesUP.dto.request.PublisherDTO;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.*;
import com.gamesUP.gamesUP.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminMetaControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private GameRepository gameRepository;

    private String adminToken;

    @BeforeEach
    void setUp() {
        // Supprimer d’abord les entités enfants pour éviter les violations de contrainte
        gameRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();

        // Création d’un admin
        User admin = new User();
        admin.setEmail("admin@gamesup.com");
        admin.setNom("Admin");
        admin.setPassword("1234");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        adminToken = "Bearer " + generateAdminToken(admin);
    }


    private String generateAdminToken(User admin) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        return jwtService.generateToken(authentication);
    }

    @Test
    @DisplayName("Création d’un auteur par un admin")
    void shouldCreateAuthor() throws Exception {
        AuthorDTO dto = new AuthorDTO("Shigeru Miyamoto");

        mockMvc.perform(post("/api/private/admin/meta/authors")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Auteur créé avec succès."));
    }

    @Test
    @DisplayName("Création d’une catégorie par un admin")
    void shouldCreateCategory() throws Exception {
        CategoryDTO dto = new CategoryDTO("Aventure");

        mockMvc.perform(post("/api/private/admin/meta/categories")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Catégorie créée avec succès."));
    }

    @Test
    @DisplayName("Création d’un éditeur par un admin")
    void shouldCreatePublisher() throws Exception {
        PublisherDTO dto = new PublisherDTO("Nintendo");

        mockMvc.perform(post("/api/private/admin/meta/publishers")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Éditeur créé avec succès."));
    }
}
