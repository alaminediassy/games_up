package com.gamesUP.gamesUP.mapper;

import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameMapperTest {

    private GameMapper gameMapper;

    @BeforeEach
    void setUp() {
        gameMapper = new GameMapper();
    }

    @Test
    void shouldConvertGameToDTO() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Test Game");
        game.setDescription("A great game");
        game.setPrice(29.99);
        game.setCategory(new Category(1L, "Adventure"));
        game.setAuthor(new Author(1L, "John Doe"));
        game.setPublisher(new Publisher(1L, "Great Publisher"));

        GameDTO dto = gameMapper.toDTO(game);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Test Game");
        assertThat(dto.description()).isEqualTo("A great game");
        assertThat(dto.price()).isEqualTo(29.99);
        assertThat(dto.category()).isEqualTo("Adventure");
        assertThat(dto.author()).isEqualTo("John Doe");
        assertThat(dto.publisher()).isEqualTo("Great Publisher");
    }

    @Test
    void shouldConvertFromCreateDTO() {
        GameCreateDTO createDTO = new GameCreateDTO(
                "New Game",
                "Awesome game description",
                19.99,
                50,
                1L,
                2L,
                3L
        );

        Game game = gameMapper.fromCreateDTO(createDTO);

        assertThat(game.getName()).isEqualTo("New Game");
        assertThat(game.getDescription()).isEqualTo("Awesome game description");
        assertThat(game.getPrice()).isEqualTo(19.99);
        assertThat(game.getStock()).isEqualTo(50);
    }
}
