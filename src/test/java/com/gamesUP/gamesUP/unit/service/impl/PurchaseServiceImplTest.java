package com.gamesUP.gamesUP.unit.service.impl;

import com.gamesUP.gamesUP.dto.request.PurchaseCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseLineCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.mapper.PurchaseMapper;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.service.impl.PurchaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PurchaseServiceImplTest {

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private PurchaseRepository purchaseRepository;
    private PurchaseMapper purchaseMapper;
    private PurchaseServiceImpl purchaseService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        gameRepository = mock(GameRepository.class);
        purchaseRepository = mock(PurchaseRepository.class);
        purchaseMapper = mock(PurchaseMapper.class);

        purchaseService = new PurchaseServiceImpl(userRepository, gameRepository, purchaseRepository, purchaseMapper);
    }

    @Test
    void shouldCreatePurchaseSuccessfully() {
        // Arrange
        String userEmail = "test@example.com";
        Long gameId = 1L;

        User user = new User();
        user.setId(1L);
        user.setEmail(userEmail);

        Game game = new Game();
        game.setId(gameId);
        game.setPrice(29.99);

        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPaid(true);
        purchase.setDelivered(false);
        purchase.setArchived(false);

        PurchaseDTO expectedDTO = new PurchaseDTO(
                1L,
                LocalDateTime.now(),
                true,
                false,
                false,
                List.of()
        );

        PurchaseCreateDTO purchaseCreateDTO = new PurchaseCreateDTO(
                List.of(new PurchaseLineCreateDTO(gameId, 5)) // rating fictif
        );

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);
        when(purchaseMapper.toDTO(any(Purchase.class))).thenReturn(expectedDTO);

        // Act
        PurchaseDTO result = purchaseService.createPurchase(userEmail, purchaseCreateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String email = "unknown@example.com";
        PurchaseCreateDTO dto = new PurchaseCreateDTO(List.of());

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> purchaseService.createPurchase(email, dto));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        PurchaseCreateDTO dto = new PurchaseCreateDTO(
                List.of(new PurchaseLineCreateDTO(1L, 4.5))
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> purchaseService.createPurchase(email, dto));
        verify(userRepository).findByEmail(email);
        verify(gameRepository).findById(1L);
    }

    @Test
    void shouldGetPurchasesByUser() {
        // Arrange
        String email = "test@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPaid(true);
        purchase.setDelivered(false);
        purchase.setArchived(false);

        PurchaseDTO purchaseDTO = new PurchaseDTO(
                1L,
                LocalDateTime.now(),
                true,
                false,
                false,
                List.of() // Empty list of PurchaseLineDTO for now
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUser(user)).thenReturn(List.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        // Act
        List<PurchaseDTO> result = purchaseService.getPurchasesByUser(email);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(1L);

        verify(userRepository).findByEmail(email);
        verify(purchaseRepository).findByUser(user);
        verify(purchaseMapper).toDTO(purchase);
    }
}
