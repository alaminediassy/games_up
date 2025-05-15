package com.gamesUP.gamesUP.integration;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PublicMetaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        categoryRepository.deleteAll();
        publisherRepository.deleteAll();

        authorRepository.save(new Author("Miyamoto"));
        categoryRepository.save(new Category("Aventure"));
        publisherRepository.save(new Publisher("Nintendo"));
    }

    @Test
    @DisplayName("GET /meta/authors - retourne la liste des auteurs")
    void shouldReturnListOfAuthors() throws Exception {
        mockMvc.perform(get("/api/public/meta/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Miyamoto"));
    }

    @Test
    @DisplayName("GET /meta/categories - retourne la liste des catégories")
    void shouldReturnListOfCategories() throws Exception {
        mockMvc.perform(get("/api/public/meta/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].label").value("Aventure"));
    }

    @Test
    @DisplayName("GET /meta/publishers - retourne la liste des éditeurs")
    void shouldReturnListOfPublishers() throws Exception {
        mockMvc.perform(get("/api/public/meta/publishers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Nintendo"));
    }
}
