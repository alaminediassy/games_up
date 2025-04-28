package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.AuthorDTO;
import com.gamesUP.gamesUP.dto.request.CategoryDTO;
import com.gamesUP.gamesUP.dto.request.PublisherDTO;
import com.gamesUP.gamesUP.exception.AlreadyExistsException;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.impl.GameMetaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GameMetaServiceImplTest {

    private GameMetaServiceImpl gameMetaService;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;
    private PublisherRepository publisherRepository;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        publisherRepository = mock(PublisherRepository.class);

        gameMetaService = new GameMetaServiceImpl(authorRepository, categoryRepository, publisherRepository);
    }

    @Test
    void shouldCreateAuthorSuccessfully() {
        AuthorDTO dto = new AuthorDTO("New Author");
        when(authorRepository.existsByName(dto.name())).thenReturn(false);

        gameMetaService.createAuthor(dto);

        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        AuthorDTO dto = new AuthorDTO("Existing Author");
        when(authorRepository.existsByName(dto.name())).thenReturn(true);

        assertThatThrownBy(() -> gameMetaService.createAuthor(dto))
                .isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void shouldCreateCategorySuccessfully() {
        CategoryDTO dto = new CategoryDTO("New Category");
        when(categoryRepository.existsByLabel(dto.label())).thenReturn(false);

        gameMetaService.createCategory(dto);

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        CategoryDTO dto = new CategoryDTO("Existing Category");
        when(categoryRepository.existsByLabel(dto.label())).thenReturn(true);

        assertThatThrownBy(() -> gameMetaService.createCategory(dto))
                .isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void shouldCreatePublisherSuccessfully() {
        PublisherDTO dto = new PublisherDTO("New Publisher");
        when(publisherRepository.existsByName(dto.name())).thenReturn(false);

        gameMetaService.createPublisher(dto);

        verify(publisherRepository).save(any(Publisher.class));
    }

    @Test
    void shouldThrowExceptionWhenPublisherAlreadyExists() {
        PublisherDTO dto = new PublisherDTO("Existing Publisher");
        when(publisherRepository.existsByName(dto.name())).thenReturn(true);

        assertThatThrownBy(() -> gameMetaService.createPublisher(dto))
                .isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void shouldGetAllAuthors() {
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(new Author("Author")));

        assertThat(gameMetaService.getAllAuthors()).isNotEmpty();
    }

    @Test
    void shouldGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(new Category("Category")));

        assertThat(gameMetaService.getAllCategories()).isNotEmpty();
    }

    @Test
    void shouldGetAllPublishers() {
        when(publisherRepository.findAll()).thenReturn(Collections.singletonList(new Publisher("Publisher")));

        assertThat(gameMetaService.getAllPublishers()).isNotEmpty();
    }
}
