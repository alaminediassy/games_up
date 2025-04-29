package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.AuthorDTO;
import com.gamesUP.gamesUP.dto.request.CategoryDTO;
import com.gamesUP.gamesUP.dto.request.PublisherDTO;
import com.gamesUP.gamesUP.service.GameMetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.MockitoAnnotations.openMocks;

public class AdminMetaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameMetaService gameMetaService;

    @InjectMocks
    private AdminMetaController adminMetaController;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminMetaController).build();
    }

    @Test
    void shouldCreateAuthor() throws Exception {
        mockMvc.perform(post("/api/private/admin/meta/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Auteur Test\"}"))
                .andExpect(status().isOk());

        verify(gameMetaService, times(1)).createAuthor(any(AuthorDTO.class));
    }

    @Test
    void shouldCreateCategory() throws Exception {
        mockMvc.perform(post("/api/private/admin/meta/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Catégorie Test\"}"))
                .andExpect(status().isOk());

        verify(gameMetaService, times(1)).createCategory(any(CategoryDTO.class));
    }

    @Test
    void shouldCreatePublisher() throws Exception {
        mockMvc.perform(post("/api/private/admin/meta/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Éditeur Test\"}"))
                .andExpect(status().isOk());

        verify(gameMetaService, times(1)).createPublisher(any(PublisherDTO.class));
    }
}
