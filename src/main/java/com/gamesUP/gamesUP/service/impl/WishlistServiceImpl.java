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

    public WishlistServiceImpl(WishlistRepository wishlistRepository, GameRepository gameRepository, UserRepository userRepository, GameMapper gameMapper) {
        this.wishlistRepository = wishlistRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapper = gameMapper;
    }

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

    @Override
    @Transactional
    public void removeFromWishlist(String userEmail, Long gameId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", userEmail));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu", gameId));

        wishlistRepository.deleteByUserAndGame(user, game);
    }

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
