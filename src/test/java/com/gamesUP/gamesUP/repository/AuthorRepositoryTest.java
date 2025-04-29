package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan("com.gamesUP.gamesUP.model")
@EnableJpaRepositories("com.gamesUP.gamesUP.repository")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldCheckExistenceByName() {
        Author author = new Author();
        author.setName("John Doe");
        authorRepository.save(author);

        boolean exists = authorRepository.existsByName("John Doe");
        boolean notExists = authorRepository.existsByName("Jane Doe");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
