package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.mapper.GameMapper;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // mock Game → GameDTO
        when(gameMapper.toDTO(any(Game.class))).thenAnswer(invocation -> {
            Game g = invocation.getArgument(0);
            return new GameDTO(
                    g.getId(),
                    g.getName(),
                    g.getDescription(),
                    g.getPrice(),
                    g.getCategory() != null ? g.getCategory().getLabel() : null,
                    g.getAuthor() != null ? g.getAuthor().getName() : null,
                    g.getPublisher() != null ? g.getPublisher().getName() : null
            );
        });
    }

    @Test
    void shouldGetAllGames() {
        Game game = new Game();
        game.setId(1L);
        game.setName("FIFA 25");
        game.setDescription("Football game");
        game.setPrice(69.99);

        when(gameRepository.findAll()).thenReturn(List.of(game));

        List<GameDTO> result = gameService.getAllGames();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("FIFA 25");
    }

    @Test
    void shouldGetGameById() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Zelda");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        GameDTO result = gameService.getGameById(1L);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Zelda");
    }

    @Test
    void shouldDeleteGame() {
        // Given
        Game game = new Game();
        game.setId(2L);
        game.setName("GTA 6");

        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));

        // When
        gameService.deleteGame(2L);

        // Then
        verify(gameRepository, times(1)).findById(2L);
        verify(gameRepository, times(1)).delete(game);
    }

}
