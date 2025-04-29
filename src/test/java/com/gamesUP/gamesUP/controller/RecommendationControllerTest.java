package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.PurchaseLineCreateDTO;
import com.gamesUP.gamesUP.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.MockitoAnnotations.openMocks;

public class RecommendationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    @Test
    void shouldReturnRecommendations() throws Exception {
        when(recommendationService.getRecommendations(eq(1L), anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/private/client/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "1")
                        .content("[]"))
                .andExpect(status().isOk());

        verify(recommendationService, times(1)).getRecommendations(eq(1L), anyList());
    }
}
