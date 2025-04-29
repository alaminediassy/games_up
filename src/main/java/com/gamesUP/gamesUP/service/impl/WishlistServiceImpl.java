package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.mapper.GameMapper;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import com.gamesUP.gamesUP.service.WishlistService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;

    // Constructeur pour injecter les dépendances nécessaires
    public WishlistServiceImpl(WishlistRepository wishlistRepository,
                               GameRepository gameRepository,
                               UserRepository userRepository,
                               GameMapper gameMapper) {
        this.wishlistRepository = wishlistRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapper = gameMapper;
    }

    /**
     * Ajoute un jeu à la wishlist d’un utilisateur, si ce jeu n’est pas déjà présent.
     *
     * @param userEmail l’email de l’utilisateur
     * @param gameId l’identifiant du jeu à ajouter
     * @throws ResourceNotFoundException si l’utilisateur ou le jeu n’existe pas
     */
    @Override
    public void addToWishlist(String userEmail, Long gameId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", userEmail));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", gameId));

        wishlistRepository.findByUserAndGame(user, game).ifPresentOrElse(
                w -> {},
                () -> wishlistRepository.save(new Wishlist(null, user, game))
        );
    }

    /**
     * Supprime un jeu de la wishlist d’un utilisateur.
     *
     * @param userEmail l’email de l’utilisateur
     * @param gameId l’identifiant du jeu à retirer
     * @throws ResourceNotFoundException si l’utilisateur ou le jeu n’existe pas
     */
    @Override
    @Transactional
    public void removeFromWishlist(String userEmail, Long gameId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", userEmail));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", gameId));

        wishlistRepository.deleteByUserAndGame(user, game);
    }

    /**
     * Récupère tous les jeux dans la wishlist d’un utilisateur.
     *
     * @param userEmail l’email de l’utilisateur
     * @return une liste de GameDTO représentant les jeux de la wishlist
     * @throws ResourceNotFoundException si l’utilisateur n’existe pas
     */
    @Override
    public List<GameDTO> getWishlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", userEmail));

        return wishlistRepository.findByUser(user).stream()
                .map(Wishlist::getGame)
                .map(gameMapper::toDTO)
                .toList();
    }
}
