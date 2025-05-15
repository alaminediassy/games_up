package com.gamesUP.gamesUP.unit.mapper;

import com.gamesUP.gamesUP.dto.request.PurchaseDTO;
import com.gamesUP.gamesUP.mapper.PurchaseMapper;
import com.gamesUP.gamesUP.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseMapperTest {

    private PurchaseMapper purchaseMapper;

    @BeforeEach
    void setUp() {
        purchaseMapper = new PurchaseMapper();
    }

    @Test
    void shouldConvertPurchaseToDTO() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Test Game");

        PurchaseLine purchaseLine = new PurchaseLine();
        purchaseLine.setId(1L);
        purchaseLine.setGame(game);
        purchaseLine.setPrice(49.99);
        purchaseLine.setQuantity(2);

        Purchase purchase = new Purchase();
        purchase.setId(10L);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPaid(true);
        purchase.setDelivered(false);
        purchase.setArchived(false);
        purchase.setLines(List.of(purchaseLine));

        PurchaseDTO dto = purchaseMapper.toDTO(purchase);

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.purchaseDate()).isNotNull();
        assertThat(dto.paid()).isTrue();
        assertThat(dto.delivered()).isFalse();
        assertThat(dto.archived()).isFalse();
        assertThat(dto.lines()).hasSize(1);
        assertThat(dto.lines().get(0).gameId()).isEqualTo(1L);
        assertThat(dto.lines().get(0).gameName()).isEqualTo("Test Game");
        assertThat(dto.lines().get(0).price()).isEqualTo(49.99);
        assertThat(dto.lines().get(0).quantity()).isEqualTo(2);
    }

    @Test
    void shouldHandleEmptyLines() {
        Purchase purchase = new Purchase();
        purchase.setId(11L);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPaid(false);
        purchase.setDelivered(false);
        purchase.setArchived(true);
        purchase.setLines(Collections.emptyList());

        PurchaseDTO dto = purchaseMapper.toDTO(purchase);

        assertThat(dto.id()).isEqualTo(11L);
        assertThat(dto.lines()).isEmpty();
    }
}
