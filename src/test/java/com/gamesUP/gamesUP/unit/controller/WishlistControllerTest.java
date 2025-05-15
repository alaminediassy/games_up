package com.gamesUP.gamesUP.unit.controller;

import com.gamesUP.gamesUP.controller.WishlistController;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WishlistControllerTest {

    private WishlistController wishlistController;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wishlistController = new WishlistController(wishlistService);
    }

    @Test
    void shouldGetWishlist() {
        // Arrange
        when(authentication.getName()).thenReturn("test@example.com");
        when(wishlistService.getWishlist("test@example.com")).thenReturn(List.of());

        // Act
        ResponseEntity<List<GameDTO>> response = wishlistController.getWishlist(authentication);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(wishlistService).getWishlist("test@example.com");
    }

    @Test
    void shouldAddToWishlist() {
        // Arrange
        when(authentication.getName()).thenReturn("test@example.com");

        // Act
        ResponseEntity<?> response = wishlistController.addToWishlist(1L, authentication);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(wishlistService).addToWishlist("test@example.com", 1L);
    }

    @Test
    void shouldRemoveFromWishlist() {
        // Arrange
        when(authentication.getName()).thenReturn("test@example.com");

        // Act
        ResponseEntity<?> response = wishlistController.removeFromWishlist(1L, authentication);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(wishlistService).removeFromWishlist("test@example.com", 1L);
    }
}
