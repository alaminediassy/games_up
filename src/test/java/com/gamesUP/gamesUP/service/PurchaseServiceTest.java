package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.PurchaseCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseLineCreateDTO;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePurchase() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(userEmail);

        Game game = new Game();
        game.setId(1L);
        game.setName("Elden Ring");
        game.setPrice(59.99);

        PurchaseLineCreateDTO lineDTO = new PurchaseLineCreateDTO(1L, 4);
        PurchaseCreateDTO purchaseCreateDTO = new PurchaseCreateDTO(List.of(lineDTO));

        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());

        PurchaseDTO expectedDTO = new PurchaseDTO(
                1L,
                purchase.getPurchaseDate(),
                true,
                false,
                false,
                List.of()
        );

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);
        when(purchaseMapper.toDTO(any(Purchase.class))).thenReturn(expectedDTO);

        // Act
        PurchaseDTO result = purchaseService.createPurchase(userEmail, purchaseCreateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        verify(userRepository).findByEmail(userEmail);
        verify(gameRepository).findById(1L);
        verify(purchaseRepository).save(any(Purchase.class));
        verify(purchaseMapper).toDTO(any(Purchase.class));
    }

    @Test
    void shouldReturnPurchasesByUser() {
        // Arrange
        String userEmail = "client@example.com";
        User user = new User();
        user.setId(2L);
        user.setEmail(userEmail);

        Purchase purchase = new Purchase();
        purchase.setId(2L);
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());

        PurchaseDTO dto = new PurchaseDTO(
                2L,
                purchase.getPurchaseDate(),
                true,
                false,
                false,
                List.of()
        );

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(purchaseRepository.findByUser(user)).thenReturn(List.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(dto);

        // Act
        List<PurchaseDTO> result = purchaseService.getPurchasesByUser(userEmail);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(2L);
        verify(userRepository).findByEmail(userEmail);
        verify(purchaseRepository).findByUser(user);
    }
}
