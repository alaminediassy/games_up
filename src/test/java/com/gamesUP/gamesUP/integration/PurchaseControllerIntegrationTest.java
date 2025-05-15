package com.gamesUP.gamesUP.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.request.PurchaseCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseLineCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseLineDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PurchaseControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtService jwtService;

    @Autowired private UserRepository userRepository;
    @Autowired private GameRepository gameRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private PurchaseRepository purchaseRepository;

    private String clientToken;

    @BeforeEach
    void setUp() {
        purchaseRepository.deleteAll();
        userRepository.deleteAll();
        gameRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();
        publisherRepository.deleteAll();

        User client = new User(null, "Client Test", "client@gamesup.com", "pass", Role.CLIENT);
        userRepository.save(client);

        clientToken = generateTokenFor(client);
    }

    @Test
    @DisplayName("Création et récupération d’un achat avec un token valide")
    void shouldCreateAndGetPurchaseSuccessfully() throws Exception {
        // Préparer les métadonnées du jeu
        Author author = authorRepository.save(new Author("Auteur"));
        Category category = categoryRepository.save(new Category("Catégorie"));
        Publisher publisher = publisherRepository.save(new Publisher("Éditeur"));

        // Créer un jeu
        Game game = new Game();
        game.setName("Jeu de test");
        game.setDescription("Description");
        game.setPrice(49.99);
        game.setStock(10);
        game.setAuthor(author);
        game.setCategory(category);
        game.setPublisher(publisher);
        gameRepository.save(game);

        // Préparer la commande
        PurchaseLineCreateDTO line = new PurchaseLineCreateDTO(game.getId(), 2);
        PurchaseCreateDTO purchaseCreateDTO = new PurchaseCreateDTO(List.of(line));

        // Effectuer l’achat
        mockMvc.perform(post("/api/private/client/purchases")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lines", hasSize(1)))
                .andExpect(jsonPath("$.lines[0].gameId").value(game.getId()))
                .andExpect(jsonPath("$.lines[0].quantity").value(2));

        // Vérifier que l’achat est listé
        mockMvc.perform(get("/api/private/client/purchases")
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private String generateTokenFor(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        return jwtService.generateToken(authentication);
    }
}
