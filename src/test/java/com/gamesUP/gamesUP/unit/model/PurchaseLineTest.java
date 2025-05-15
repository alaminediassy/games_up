package com.gamesUP.gamesUP.unit.model;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseLineTest {

    @Test
    void shouldCreatePurchaseLineWithAllFields() {
        Game game = new Game(
                1L,
                "Test Game",
                "Description du jeu",
                59.99,
                10,
                null,
                null,
                null
        );

        Purchase purchase = new Purchase();

        PurchaseLine purchaseLine = new PurchaseLine(
                1L,
                2, // quantity
                59.99, // price
                game,
                purchase
        );

        assertThat(purchaseLine.getId()).isEqualTo(1L);
        assertThat(purchaseLine.getQuantity()).isEqualTo(2);
        assertThat(purchaseLine.getPrice()).isEqualTo(59.99);
        assertThat(purchaseLine.getGame()).isEqualTo(game);
        assertThat(purchaseLine.getPurchase()).isEqualTo(purchase);
    }

    @Test
    void shouldCreateEmptyPurchaseLineAndSetFields() {
        PurchaseLine purchaseLine = new PurchaseLine();

        Game game = new Game();
        Purchase purchase = new Purchase();

        purchaseLine.setId(2L);
        purchaseLine.setQuantity(3);
        purchaseLine.setPrice(79.99);
        purchaseLine.setGame(game);
        purchaseLine.setPurchase(purchase);

        assertThat(purchaseLine.getId()).isEqualTo(2L);
        assertThat(purchaseLine.getQuantity()).isEqualTo(3);
        assertThat(purchaseLine.getPrice()).isEqualTo(79.99);
        assertThat(purchaseLine.getGame()).isEqualTo(game);
        assertThat(purchaseLine.getPurchase()).isEqualTo(purchase);
    }
}
