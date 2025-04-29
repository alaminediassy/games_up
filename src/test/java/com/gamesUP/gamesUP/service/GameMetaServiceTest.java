package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.AuthorDTO;
import com.gamesUP.gamesUP.dto.request.CategoryDTO;
import com.gamesUP.gamesUP.dto.request.PublisherDTO;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.impl.GameMetaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameMetaServiceTest {

    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;
    private PublisherRepository publisherRepository;
    private GameMetaService gameMetaService;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        publisherRepository = mock(PublisherRepository.class);
        gameMetaService = new GameMetaServiceImpl(authorRepository, categoryRepository, publisherRepository);
    }

    @Test
    void shouldCreateAuthor() {
        AuthorDTO dto = new AuthorDTO("Jane Doe");

        gameMetaService.createAuthor(dto);

        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(captor.capture());

        Author saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Jane Doe");
    }

    @Test
    void shouldCreateCategory() {
        CategoryDTO dto = new CategoryDTO("Adventure");

        gameMetaService.createCategory(dto);

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());

        Category saved = captor.getValue();
        assertThat(saved.getLabel()).isEqualTo("Adventure");
    }

    @Test
    void shouldCreatePublisher() {
        PublisherDTO dto = new PublisherDTO("Ubisoft");

        gameMetaService.createPublisher(dto);

        ArgumentCaptor<Publisher> captor = ArgumentCaptor.forClass(Publisher.class);
        verify(publisherRepository).save(captor.capture());

        Publisher saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Ubisoft");
    }

    @Test
    void shouldReturnAllAuthors() {
        Author a1 = new Author(1L, "Author A");
        Author a2 = new Author(2L, "Author B");
        when(authorRepository.findAll()).thenReturn(List.of(a1, a2));

        var result = gameMetaService.getAllAuthors();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Author A");
    }

    @Test
    void shouldReturnAllCategories() {
        Category c1 = new Category(1L, "Cat 1");
        Category c2 = new Category(2L, "Cat 2");
        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

        var result = gameMetaService.getAllCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(1).label()).isEqualTo("Cat 2");
    }

    @Test
    void shouldReturnAllPublishers() {
        Publisher p1 = new Publisher(1L, "EA");
        Publisher p2 = new Publisher(2L, "Sony");
        when(publisherRepository.findAll()).thenReturn(List.of(p1, p2));

        var result = gameMetaService.getAllPublishers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("EA");
    }
}
