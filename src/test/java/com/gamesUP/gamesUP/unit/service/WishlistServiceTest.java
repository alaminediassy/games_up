package com.gamesUP.gamesUP.unit.service;

import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
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
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddToWishlist() {
        // Given
        String email = "test@example.com";
        Long gameId = 1L;

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Game game = new Game();
        game.setId(gameId);
        game.setName("Test Game");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(wishlistRepository.findByUserAndGame(user, game)).thenReturn(Optional.empty());

        // When
        wishlistService.addToWishlist(email, gameId);

        // Then
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void shouldNotAddIfAlreadyExists() {
        String email = "test@example.com";
        Long gameId = 1L;

        User user = new User();
        Game game = new Game();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(wishlistRepository.findByUserAndGame(user, game)).thenReturn(Optional.of(new Wishlist()));

        wishlistService.addToWishlist(email, gameId);

        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void shouldRemoveFromWishlist() {
        String email = "user@example.com";
        Long gameId = 2L;

        User user = new User();
        Game game = new Game();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        wishlistService.removeFromWishlist(email, gameId);

        verify(wishlistRepository).deleteByUserAndGame(user, game);
    }

    @Test
    void shouldGetWishlist() {
        String email = "wish@example.com";
        User user = new User();
        user.setEmail(email);

        Game game = new Game();
        game.setName("Wishlist Game");

        Wishlist wishlist = new Wishlist(1L, user, game);
        GameDTO gameDTO = new GameDTO(1L, "Wishlist Game", "Description", 20.0, "Action", "Author", "Publisher");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(wishlistRepository.findByUser(user)).thenReturn(List.of(wishlist));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        List<GameDTO> result = wishlistService.getWishlist(email);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Wishlist Game");
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wishlistService.getWishlist("notfound@example.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
