package com.gamesUP.gamesUP.unit.repository;

import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.repository.CategoryRepository;
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
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCheckExistenceByLabel() {
        Category category = new Category();
        category.setLabel("Action");
        categoryRepository.save(category);

        boolean exists = categoryRepository.existsByLabel("Action");
        boolean notExists = categoryRepository.existsByLabel("Aventure");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
