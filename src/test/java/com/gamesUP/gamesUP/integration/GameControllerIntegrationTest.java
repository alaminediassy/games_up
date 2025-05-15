package com.gamesUP.gamesUP.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.model.*;
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

import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GameControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private GameRepository gameRepository;
    @Autowired private JwtService jwtService;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();

        // Création de l’admin
        User admin = new User();
        admin.setNom("Admin");
        admin.setEmail("admin@gamesup.com");
        admin.setPassword("1234");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }

    @Test
    @DisplayName("Quand il n’y a aucun jeu, l’API retourne une liste vide")
    void shouldReturnEmptyListWhenNoGames() throws Exception {
        mockMvc.perform(get("/api/public/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Récupération d’un jeu par ID (public)")
    void shouldReturnGameById() throws Exception {
        Author author = authorRepository.save(new Author(null, "Shigeru Miyamoto"));
        Category category = categoryRepository.save(new Category(null, "Aventure"));
        Publisher publisher = publisherRepository.save(new Publisher(null, "Nintendo"));

        GameCreateDTO createDTO = new GameCreateDTO(
                "Zelda",
                "Aventure épique",
                59.99,
                100,
                category.getId(),
                author.getId(),
                publisher.getId()
        );

        String response = mockMvc.perform(post("/api/private/admin/games")
                        .header("Authorization", "Bearer " + generateAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GameDTO created = objectMapper.readValue(response, GameDTO.class);

        mockMvc.perform(get("/api/public/games/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zelda"))
                .andExpect(jsonPath("$.description").value("Aventure épique"))
                .andExpect(jsonPath("$.price").value(closeTo(59.99, 0.01)))
                .andExpect(jsonPath("$.author").value("Shigeru Miyamoto"))
                .andExpect(jsonPath("$.category").value("Aventure"))
                .andExpect(jsonPath("$.publisher").value("Nintendo"));
    }

    @Test
    @DisplayName("Mise à jour d’un jeu existant")
    void shouldUpdateGame() throws Exception {
        Author oldAuthor = authorRepository.save(new Author(null, "Auteur 1"));
        Category oldCategory = categoryRepository.save(new Category(null, "Catégorie 1"));
        Publisher oldPublisher = publisherRepository.save(new Publisher(null, "Éditeur 1"));

        GameCreateDTO createDTO = new GameCreateDTO(
                "Jeu original",
                "Desc",
                30.00,
                10,
                oldCategory.getId(),
                oldAuthor.getId(),
                oldPublisher.getId()
        );

        String token = generateAdminToken();

        String response = mockMvc.perform(post("/api/private/admin/games")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andReturn().getResponse().getContentAsString();

        GameDTO created = objectMapper.readValue(response, GameDTO.class);

        Author newAuthor = authorRepository.save(new Author(null, "Auteur 2"));
        Category newCategory = categoryRepository.save(new Category(null, "Catégorie 2"));
        Publisher newPublisher = publisherRepository.save(new Publisher(null, "Éditeur 2"));

        GameCreateDTO updateDTO = new GameCreateDTO(
                "Jeu MAJ",
                "Nouvelle desc",
                49.99,
                25,
                newCategory.getId(),
                newAuthor.getId(),
                newPublisher.getId()
        );

        mockMvc.perform(put("/api/private/admin/games/" + created.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jeu MAJ"))
                .andExpect(jsonPath("$.description").value("Nouvelle desc"))
                .andExpect(jsonPath("$.price").value(closeTo(49.99, 0.01)))
                .andExpect(jsonPath("$.author").value("Auteur 2"))
                .andExpect(jsonPath("$.category").value("Catégorie 2"))
                .andExpect(jsonPath("$.publisher").value("Éditeur 2"));
    }

    @Test
    @DisplayName("Suppression d’un jeu existant")
    void shouldDeleteGame() throws Exception {
        Author author = authorRepository.save(new Author(null, "Auteur"));
        Category category = categoryRepository.save(new Category(null, "Catégorie"));
        Publisher publisher = publisherRepository.save(new Publisher(null, "Éditeur"));

        GameCreateDTO createDTO = new GameCreateDTO(
                "À supprimer",
                "Suppression",
                15.0,
                5,
                category.getId(),
                author.getId(),
                publisher.getId()
        );

        String token = generateAdminToken();

        String response = mockMvc.perform(post("/api/private/admin/games")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andReturn().getResponse().getContentAsString();

        GameDTO created = objectMapper.readValue(response, GameDTO.class);

        mockMvc.perform(delete("/api/private/admin/games/" + created.id())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Jeu supprimé avec succès."));

        mockMvc.perform(get("/api/public/games/" + created.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Suppression d’un jeu inexistant")
    void deleteNonExistingGame_shouldReturnNotFound() throws Exception {
        long nonExistentId = 9999L;
        mockMvc.perform(delete("/api/private/admin/games/" + nonExistentId)
                        .header("Authorization", "Bearer " + generateAdminToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Jeu non trouvé avec l'identifiant : " + nonExistentId));
    }

    private String generateAdminToken() {
        User admin = userRepository.findByEmail("admin@gamesup.com").orElseThrow();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        return jwtService.generateToken(authentication);
    }
}
