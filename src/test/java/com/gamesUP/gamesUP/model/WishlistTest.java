package com.gamesUP.gamesUP.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WishlistTest {

    @Test
    void shouldCreateWishlist() {
        User user = new User();
        Game game = new Game();
        Wishlist wishlist = new Wishlist(1L, user, game);

        assertThat(wishlist.getId()).isEqualTo(1L);
        assertThat(wishlist.getUser()).isEqualTo(user);
        assertThat(wishlist.getGame()).isEqualTo(game);
    }
}
