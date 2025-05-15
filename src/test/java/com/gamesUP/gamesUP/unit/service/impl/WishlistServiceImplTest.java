package com.gamesUP.gamesUP.unit.service.impl;

import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.mapper.GameMapper;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import com.gamesUP.gamesUP.service.impl.WishlistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test unitaire de WishlistServiceImpl.
 */
class WishlistServiceImplTest {

    private WishlistRepository wishlistRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;
    private GameMapper gameMapper;
    private WishlistServiceImpl wishlistService;

    @BeforeEach
    void setUp() {
        wishlistRepository = mock(WishlistRepository.class);
        gameRepository = mock(GameRepository.class);
        userRepository = mock(UserRepository.class);
        gameMapper = mock(GameMapper.class);

        wishlistService = new WishlistServiceImpl(
                wishlistRepository, gameRepository, userRepository, gameMapper
        );
    }

    @Test
    void shouldAddGameToWishlist_WhenNotExists() {
        // Arrange
        String email = "user@example.com";
        Long gameId = 1L;
        User user = new User();
        user.setEmail(email);
        Game game = new Game();
        game.setId(gameId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(wishlistRepository.findByUserAndGame(user, game)).thenReturn(Optional.empty());

        // Act
        wishlistService.addToWishlist(email, gameId);

        // Assert
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void shouldRemoveGameFromWishlist() {
        // Arrange
        String email = "user@example.com";
        Long gameId = 1L;
        User user = new User();
        user.setEmail(email);
        Game game = new Game();
        game.setId(gameId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // Act
        wishlistService.removeFromWishlist(email, gameId);

        // Assert
        verify(wishlistRepository).deleteByUserAndGame(user, game);
    }

    @Test
    void shouldGetWishlistGames() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        Game game = new Game();
        Wishlist wishlist = new Wishlist(1L, user, game);

        GameDTO gameDTO = new GameDTO(
                1L,
                "Catan",
                "Un jeu de stratégie",
                29.99,
                "Stratégie",
                "Auteur",
                "Éditeur"
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(wishlistRepository.findByUser(user)).thenReturn(List.of(wishlist));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        // Act
        List<GameDTO> wishlistGames = wishlistService.getWishlist(email);

        // Assert
        assertThat(wishlistGames).hasSize(1);
        assertThat(wishlistGames.get(0).name()).isEqualTo("Catan");
    }
}
