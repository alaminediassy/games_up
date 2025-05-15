package com.gamesUP.gamesUP.unit.model;

import com.gamesUP.gamesUP.model.Author;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTest {

    @Test
    void shouldCreateAuthorWithAllFields() {
        Author author = new Author(1L, "John Doe");

        assertThat(author.getId()).isEqualTo(1L);
        assertThat(author.getName()).isEqualTo("John Doe");
    }

    @Test
    void shouldCreateAuthorWithNameOnly() {
        Author author = new Author("Jane Smith");

        assertThat(author.getId()).isNull();
        assertThat(author.getName()).isEqualTo("Jane Smith");
    }

    @Test
    void shouldUseSettersCorrectly() {
        Author author = new Author();
        author.setId(2L);
        author.setName("Emily Brontë");

        assertThat(author.getId()).isEqualTo(2L);
        assertThat(author.getName()).isEqualTo("Emily Brontë");
    }

    @Test
    void shouldCheckEquality() {
        Author a1 = new Author(1L, "Victor Hugo");
        Author a2 = new Author(1L, "Victor Hugo");

        assertThat(a1).isEqualTo(a2);
    }
}
