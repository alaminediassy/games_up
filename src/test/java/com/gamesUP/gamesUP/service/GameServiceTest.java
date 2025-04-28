package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.mapper.GameMapper;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameServiceImpl gameService;

    private GameCreateDTO createDTO;
    private Game game;
    private GameDTO gameDTO;

    @BeforeEach
    void setUp() {
        createDTO = new GameCreateDTO("Catan Junior", "Simplified version", 25.0, 10, 1L, 1L, 1L);

        game = new Game();
        game.setId(1L);
        game.setName("Catan Junior");
        game.setDescription("Simplified version");
        game.setPrice(25.0);
        game.setStock(10);

        gameDTO = new GameDTO(1L, "Catan Junior", "Simplified version", 25.0, 10, "Stratégie", "Klaus Teuber", "Kosmos");
    }

    @Test
    void shouldCreateGameSuccessfully() {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        category.setLabel("Stratégie");

        Author author = new Author();
        author.setId(1L);
        author.setName("Klaus Teuber");

        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Kosmos");

        when(gameMapper.fromCreateDTO(createDTO)).thenReturn(new Game());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(gameRepository.save(org.mockito.ArgumentMatchers.any(Game.class))).thenReturn(game);
        when(gameMapper.toDTO(org.mockito.ArgumentMatchers.any(Game.class))).thenReturn(gameDTO);

        // Act
        GameDTO result = gameService.createGame(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Catan Junior", result.name());
        assertEquals("Stratégie", result.category());
        assertEquals("Klaus Teuber", result.author());
        assertEquals("Kosmos", result.publisher());
    }

    @Test
    void shouldGetAllGamesSuccessfully() {
        // Arrange
        when(gameRepository.findAll()).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        // Act
        List<GameDTO> games = gameService.getAllGames();

        // Assert
        assertEquals(1, games.size());
        assertEquals("Catan Junior", games.get(0).name());
    }

    @Test
    void shouldGetGameByIdSuccessfully() {
        // Arrange
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        // Act
        GameDTO result = gameService.getGameById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Catan Junior", result.name());
    }

    @Test
    void shouldThrowExceptionWhenGameNotFoundById() {
        // Arrange
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> gameService.getGameById(999L));
    }

    @Test
    void shouldUpdateGameSuccessfully() {
        // Arrange
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(new Publisher()));
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        // Act
        GameDTO result = gameService.updateGame(1L, createDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Catan Junior", result.name());
    }

    @Test
    void shouldDeleteGameSuccessfully() {
        // Arrange
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        // Act
        gameService.deleteGame(1L);

        // Assert
        verify(gameRepository).delete(game);
    }

    @Test
    void shouldSearchGamesByNameSuccessfully() {
        // Arrange
        when(gameRepository.findByNameContainingIgnoreCase("Catan")).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        // Act
        List<GameDTO> results = gameService.searchGamesByName("Catan");

        // Assert
        assertEquals(1, results.size());
        assertEquals("Catan Junior", results.get(0).name());
    }

    @Test
    void shouldFilterGamesSuccessfully() {
        // Arrange
        when(gameRepository.searchByFilters("Catan", 1L, 1L, 1L)).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        // Act
        List<GameDTO> results = gameService.filterGames("Catan", 1L, 1L, 1L);

        // Assert
        assertEquals(1, results.size());
        assertEquals("Catan Junior", results.get(0).name());
    }
}
