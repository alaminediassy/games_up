package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.mapper.GameMapper;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.service.GameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    // Constructeur pour injecter les dépendances nécessaires
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper, CategoryRepository categoryRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    /**
     * Récupère tous les jeux depuis la base de données.
     * Convertit chaque entité Game en GameDTO pour exposition externe.
     */
    @Override
    public List<GameDTO> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crée un nouveau jeu à partir du GameCreateDTO.
     * L'objet est mappé, les relations sont fixées, puis enregistré.
     * @param dto les données de création d’un jeu
     * @return GameDTO correspondant au jeu nouvellement créé
     */
    @Override
    public GameDTO createGame(GameCreateDTO dto) {
        Game game = gameMapper.fromCreateDTO(dto);

        return getGameDTO(dto, game);
    }

    /**
     * Recherche un jeu par son ID.
     * @param id identifiant du jeu
     * @return GameDTO si trouvé, sinon lève une exception
     */
    @Override
    public GameDTO getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", id));
        return gameMapper.toDTO(game);
    }

    /**
     * Met à jour un jeu existant.
     * @param id identifiant du jeu à modifier
     * @param dto nouvelles données du jeu
     * @return GameDTO mis à jour
     */
    @Override
    public GameDTO updateGame(Long id, GameCreateDTO dto) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", id));

        // Mise à jour des champs
        game.setName(dto.name());
        game.setDescription(dto.description());
        game.setPrice(dto.price());
        game.setStock(dto.stock());

        // Mise à jour des relations
        return getGameDTO(dto, game);
    }

    /**
     * Supprime un jeu par son identifiant.
     * @param id identifiant du jeu à supprimer
     */
    @Override
    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", id));

        gameRepository.delete(game);
    }

    /**
     * Recherche des jeux dont le nom contient une chaîne donnée (insensible à la casse).
     * @param name chaîne recherchée
     * @return liste des jeux correspondants
     */
    @Override
    public List<GameDTO> searchGamesByName(String name) {
        return gameRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Applique un filtre combiné sur les jeux (nom, catégorie, auteur, éditeur).
     * @return liste des jeux filtrés
     */
    @Override
    public List<GameDTO> filterGames(String name, Long categoryId, Long authorId, Long publisherId) {
        return gameRepository.searchByFilters(name, categoryId, authorId, publisherId)
                .stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Méthode utilitaire pour associer les relations (catégorie, auteur, éditeur),
     * sauvegarder le jeu et le convertir en GameDTO.
     */
    private GameDTO getGameDTO(GameCreateDTO dto, Game game) {
        game.setCategory(categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", dto.categoryId())));
        game.setAuthor(authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Auteur", dto.authorId())));
        game.setPublisher(publisherRepository.findById(dto.publisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur", dto.publisherId())));

        Game updated = gameRepository.save(game);
        return gameMapper.toDTO(updated);
    }

}
