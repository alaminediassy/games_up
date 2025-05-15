package com.gamesUP.gamesUP.unit.model;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseTest {

    @Test
    void shouldCreatePurchaseWithAllFields() {
        User user = new User(1L, "John Doe", "john@example.com", "password", Role.CLIENT);
        PurchaseLine purchaseLine = new PurchaseLine();
        LocalDateTime now = LocalDateTime.now();

        Purchase purchase = new Purchase(
                1L,
                now,
                true,
                false,
                false,
                user,
                Collections.singletonList(purchaseLine)
        );

        assertThat(purchase.getId()).isEqualTo(1L);
        assertThat(purchase.getPurchaseDate()).isEqualTo(now);
        assertThat(purchase.isPaid()).isTrue();
        assertThat(purchase.isDelivered()).isFalse();
        assertThat(purchase.isArchived()).isFalse();
        assertThat(purchase.getUser()).isEqualTo(user);
        assertThat(purchase.getLines()).containsExactly(purchaseLine);
    }

    @Test
    void shouldCreateEmptyPurchaseAndSetFields() {
        Purchase purchase = new Purchase();

        LocalDateTime now = LocalDateTime.now();
        User user = new User(2L, "Jane Doe", "jane@example.com", "password", Role.CLIENT);

        purchase.setId(2L);
        purchase.setPurchaseDate(now);
        purchase.setPaid(true);
        purchase.setDelivered(true);
        purchase.setArchived(false);
        purchase.setUser(user);
        purchase.setLines(Collections.emptyList());

        assertThat(purchase.getId()).isEqualTo(2L);
        assertThat(purchase.getPurchaseDate()).isEqualTo(now);
        assertThat(purchase.isPaid()).isTrue();
        assertThat(purchase.isDelivered()).isTrue();
        assertThat(purchase.isArchived()).isFalse();
        assertThat(purchase.getUser()).isEqualTo(user);
        assertThat(purchase.getLines()).isEmpty();
    }
}
