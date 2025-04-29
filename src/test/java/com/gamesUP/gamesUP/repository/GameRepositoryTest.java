package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan("com.gamesUP.gamesUP.model")
@EnableJpaRepositories("com.gamesUP.gamesUP.repository")
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void shouldSearchGameByNameAndFilters() {
        Category category = categoryRepository.save(new Category(null, "Action"));
        Author author = authorRepository.save(new Author(null, "Author1"));
        Publisher publisher = publisherRepository.save(new Publisher(null, "Publisher1"));

        Game game = new Game();
        game.setName("Call of Duty");
        game.setCategory(category);
        game.setAuthor(author);
        game.setPublisher(publisher);
        gameRepository.save(game);

        List<Game> result = gameRepository.searchByFilters("call", category.getId(), author.getId(), publisher.getId());
        assertThat(result).hasSize(1);
    }
}
