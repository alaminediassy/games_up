package com.gamesUP.gamesUP.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    @Test
    void shouldCreateGameWithAllFields() {
        Author author = new Author(1L, "Author Name");
        Publisher publisher = new Publisher(1L, "Publisher Name");
        Category category = new Category(1L, "Category Label");

        Game game = new Game(
                1L,
                "Zelda",
                "A great adventure game.",
                59.99,
                10,
                author,
                publisher,
                category
        );

        assertThat(game.getId()).isEqualTo(1L);
        assertThat(game.getName()).isEqualTo("Zelda");
        assertThat(game.getDescription()).isEqualTo("A great adventure game.");
        assertThat(game.getPrice()).isEqualTo(59.99);
        assertThat(game.getStock()).isEqualTo(10);
        assertThat(game.getAuthor()).isEqualTo(author);
        assertThat(game.getPublisher()).isEqualTo(publisher);
        assertThat(game.getCategory()).isEqualTo(category);
    }

    @Test
    void shouldSetFieldsIndividually() {
        Game game = new Game();

        game.setId(2L);
        game.setName("Mario");
        game.setDescription("Classic platformer.");
        game.setPrice(39.99);
        game.setStock(20);

        Author author = new Author(2L, "Nintendo");
        Publisher publisher = new Publisher(2L, "Nintendo Publishing");
        Category category = new Category(2L, "Platformer");

        game.setAuthor(author);
        game.setPublisher(publisher);
        game.setCategory(category);

        assertThat(game.getId()).isEqualTo(2L);
        assertThat(game.getName()).isEqualTo("Mario");
        assertThat(game.getDescription()).isEqualTo("Classic platformer.");
        assertThat(game.getPrice()).isEqualTo(39.99);
        assertThat(game.getStock()).isEqualTo(20);
        assertThat(game.getAuthor()).isEqualTo(author);
        assertThat(game.getPublisher()).isEqualTo(publisher);
        assertThat(game.getCategory()).isEqualTo(category);
    }
}
