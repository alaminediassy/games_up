package com.gamesUP.gamesUP.unit.controller;

import com.gamesUP.gamesUP.controller.GameController;
import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GameControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameController gameController = new GameController(gameService);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void shouldCreateGame() throws Exception {
        GameDTO responseDTO = new GameDTO(1L, "Test Game", "Test Description", 10.0, null, null, null);

        when(gameService.createGame(any(GameCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/private/admin/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Test Game",
                                "description": "Test Description",
                                "price": 10.0
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Game"));
    }

    @Test
    void shouldGetAllGames() throws Exception {
        when(gameService.getAllGames()).thenReturn(List.of(
                new GameDTO(1L, "Game 1", "Description 1", 10.0, null, null, null)
        ));

        mockMvc.perform(get("/api/public/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Game 1"));
    }

    @Test
    void shouldGetGameById() throws Exception {
        when(gameService.getGameById(1L)).thenReturn(
                new GameDTO(1L, "Game 1", "Description 1", 10.0, null, null, null)
        );

        mockMvc.perform(get("/api/public/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Game 1"));
    }

    @Test
    void shouldUpdateGame() throws Exception {
        when(gameService.updateGame(eq(1L), any(GameCreateDTO.class)))
                .thenReturn(new GameDTO(1L, "Updated Game", "Updated Description", 20.0, null, null, null));

        mockMvc.perform(put("/api/private/admin/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated Game",
                                "description": "Updated Description",
                                "price": 20.0
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Game"));
    }

    @Test
    void shouldDeleteGame() throws Exception {
        doNothing().when(gameService).deleteGame(1L);

        mockMvc.perform(delete("/api/private/admin/games/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Jeu supprimé avec succès."));
    }

    @Test
    void shouldSearchGames() throws Exception {
        when(gameService.searchGamesByName("Test")).thenReturn(Collections.singletonList(
                new GameDTO(1L, "Test Game", "Test Description", 10.0, null, null, null)
        ));

        mockMvc.perform(get("/api/public/games/search")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Game"));
    }

    @Test
    void shouldFilterGames() throws Exception {
        when(gameService.filterGames("Test", 1L, 2L, 3L)).thenReturn(Collections.singletonList(
                new GameDTO(1L, "Filtered Game", "Filtered Description", 15.0, null, null, null)
        ));

        mockMvc.perform(get("/api/public/games/filter")
                        .param("name", "Test")
                        .param("category", "1")
                        .param("author", "2")
                        .param("publisher", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Filtered Game"));
    }
}
