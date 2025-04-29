package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.request.AuthorDTO;
import com.gamesUP.gamesUP.dto.request.CategoryDTO;
import com.gamesUP.gamesUP.dto.request.PublisherDTO;
import com.gamesUP.gamesUP.dto.response.AuthorResponseDTO;
import com.gamesUP.gamesUP.dto.response.CategoryResponseDTO;
import com.gamesUP.gamesUP.dto.response.PublisherResponseDTO;
import com.gamesUP.gamesUP.exception.AlreadyExistsException;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.GameMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameMetaServiceImpl implements GameMetaService {

    private static final Logger log = LoggerFactory.getLogger(GameMetaServiceImpl.class);

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    // Constructeur pour injecter les dépendances nécessaires
    public GameMetaServiceImpl(AuthorRepository authorRepository,
                               CategoryRepository categoryRepository,
                               PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    /**
     * Crée un nouvel auteur si le nom n’existe pas déjà.
     * @param dto DTO contenant le nom de l’auteur à créer
     * @throws AlreadyExistsException si un auteur avec le même nom existe
     */
    @Override
    public void createAuthor(AuthorDTO dto) {
        if (authorRepository.existsByName(dto.name())) {
            throw new AlreadyExistsException("Auteur", dto.name());
        }
        log.info("Création de l’auteur : {}", dto.name());
        authorRepository.save(new Author(dto.name()));
    }

    /**
     * Crée une nouvelle catégorie si le label n’existe pas déjà.
     * @param dto DTO contenant le label de la catégorie
     * @throws AlreadyExistsException si une catégorie avec le même label existe
     */
    @Override
    public void createCategory(CategoryDTO dto) {
        if (categoryRepository.existsByLabel(dto.label())) {
            throw new AlreadyExistsException("Catégorie", dto.label());
        }
        log.info("Création de la catégorie : {}", dto.label());
        categoryRepository.save(new Category(dto.label()));
    }

    /**
     * Crée un nouvel éditeur si le nom n’existe pas déjà.
     * @param dto DTO contenant le nom de l’éditeur
     * @throws AlreadyExistsException si un éditeur avec le même nom existe
     */
    @Override
    public void createPublisher(PublisherDTO dto) {
        if (publisherRepository.existsByName(dto.name())) {
            throw new AlreadyExistsException("Éditeur", dto.name());
        }
        log.info("Création de l’éditeur : {}", dto.name());
        publisherRepository.save(new Publisher(dto.name()));
    }

    /**
     * Récupère tous les auteurs enregistrés et les convertit en DTO.
     * @return liste des auteurs au format AuthorResponseDTO
     */
    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(a -> new AuthorResponseDTO(a.getId(), a.getName()))
                .toList();
    }

    /**
     * Récupère toutes les catégories enregistrées et les convertit en DTO.
     * @return liste des catégories au format CategoryResponseDTO
     */
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryResponseDTO(c.getId(), c.getLabel()))
                .toList();
    }

    /**
     * Récupère tous les éditeurs enregistrés et les convertit en DTO.
     * @return liste des éditeurs au format PublisherResponseDTO
     */
    @Override
    public List<PublisherResponseDTO> getAllPublishers() {
        return publisherRepository.findAll()
                .stream()
                .map(p -> new PublisherResponseDTO(p.getId(), p.getName()))
                .toList();
    }

}
