package com.gamesUP.gamesUP.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repository.*;
import com.gamesUP.gamesUP.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WishlistControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private GameRepository gameRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private JwtService jwtService;

    private String clientToken;
    private Long gameId;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        userRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();
        publisherRepository.deleteAll();

        // Client
        User client = new User();
        client.setNom("Client Test");
        client.setEmail("client@gamesup.com");
        client.setPassword("password");
        client.setRole(Role.CLIENT);
        userRepository.save(client);
        clientToken = generateClientToken(client);

        // Données pour un jeu
        Author author = authorRepository.save(new Author(null, "Hideo Kojima"));
        Category category = categoryRepository.save(new Category(null, "Action"));
        Publisher publisher = publisherRepository.save(new Publisher(null, "Konami"));

        Game game = new Game();
        game.setName("Metal Gear Solid");
        game.setDescription("Jeu d’infiltration culte.");
        game.setPrice(39.99);
        game.setStock(50);
        game.setAuthor(author);
        game.setCategory(category);
        game.setPublisher(publisher);
        gameRepository.save(game);
        gameId = game.getId();
    }

    @Test
    @DisplayName("Ajouter, lister et supprimer un jeu dans la wishlist")
    void shouldManageWishlistCorrectly() throws Exception {
        // Ajouter
        mockMvc.perform(post("/api/private/client/wishlist/" + gameId)
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Jeu ajouté à la wishlist."));

        // Vérifier présence
        mockMvc.perform(get("/api/private/client/wishlist")
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Metal Gear Solid"));

        // Supprimer
        mockMvc.perform(delete("/api/private/client/wishlist/" + gameId)
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Jeu retiré de la wishlist."));

        // Vérifier que la liste est vide
        mockMvc.perform(get("/api/private/client/wishlist")
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private String generateClientToken(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities()
        );

        return jwtService.generateToken(authentication);
    }
}
