package com.gamesUP.gamesUP.controller;


import com.gamesUP.gamesUP.service.GameMetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.MockitoAnnotations.openMocks;

public class PublicMetaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameMetaService gameMetaService;

    @InjectMocks
    private PublicMetaController publicMetaController;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(publicMetaController).build();
    }

    @Test
    void shouldGetAllAuthors() throws Exception {
        when(gameMetaService.getAllAuthors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/public/meta/authors"))
                .andExpect(status().isOk());

        verify(gameMetaService, times(1)).getAllAuthors();
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        when(gameMetaService.getAllCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/public/meta/categories"))
                .andExpect(status().isOk());

        verify(gameMetaService, times(1)).getAllCategories();
    }

    @Test
    void shouldGetAllPublishers() throws Exception {
        when(gameMetaService.getAllPublishers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/public/meta/publishers"))
                .andExpect(status().isOk());

        verify(gameMetaService, times(1)).getAllPublishers();
    }
}
