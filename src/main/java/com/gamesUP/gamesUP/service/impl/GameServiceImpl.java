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

    @Override
    public List<GameDTO> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GameDTO createGame(GameCreateDTO dto) {
        Game game = gameMapper.fromCreateDTO(dto);

        return getGameDTO(dto, game);
    }

    @Override
    public GameDTO getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", id));
        return gameMapper.toDTO(game);
    }

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

    @Override
    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", id));

        gameRepository.delete(game);
    }

    @Override
    public List<GameDTO> searchGamesByName(String name) {
        return gameRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

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
