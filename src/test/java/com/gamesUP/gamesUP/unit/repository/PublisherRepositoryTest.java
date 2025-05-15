package com.gamesUP.gamesUP.unit.repository;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.PublisherRepository;
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
class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void shouldCheckExistenceByName() {
        Publisher publisher = new Publisher();
        publisher.setName("Ubisoft");
        publisherRepository.save(publisher);

        boolean exists = publisherRepository.existsByName("Ubisoft");
        boolean notExists = publisherRepository.existsByName("EA");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
