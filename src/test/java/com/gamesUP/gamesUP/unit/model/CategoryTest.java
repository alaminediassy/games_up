package com.gamesUP.gamesUP.unit.model;

import com.gamesUP.gamesUP.model.Category;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    void shouldCreateCategoryWithAllFields() {
        Category category = new Category(1L, "Action");

        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getLabel()).isEqualTo("Action");
    }

    @Test
    void shouldCreateCategoryWithLabelOnly() {
        Category category = new Category("Adventure");

        assertThat(category.getId()).isNull();
        assertThat(category.getLabel()).isEqualTo("Adventure");
    }

    @Test
    void shouldUseSettersCorrectly() {
        Category category = new Category();
        category.setId(2L);
        category.setLabel("Horror");

        assertThat(category.getId()).isEqualTo(2L);
        assertThat(category.getLabel()).isEqualTo("Horror");
    }

    @Test
    void shouldCheckEquality() {
        Category c1 = new Category(1L, "RPG");
        Category c2 = new Category(1L, "RPG");

        assertThat(c1).isEqualTo(c2);
    }
}
