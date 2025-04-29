package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan("com.gamesUP.gamesUP.model")
@EnableJpaRepositories("com.gamesUP.gamesUP.repository")
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void shouldSaveAndRetrieveWishlist() {
        User user = new User();
        user.setEmail("wish@user.com");
        user.setPassword("pass");
        user.setRole(Role.valueOf("CLIENT"));
        userRepository.save(user);

        Category category = categoryRepository.save(new Category(null, "Action"));
        Author author = authorRepository.save(new Author(null, "Author A"));
        Publisher publisher = publisherRepository.save(new Publisher(null, "Publisher P"));

        Game game = new Game();
        game.setName("Super Game");
        game.setAuthor(author);
        game.setCategory(category);
        game.setPublisher(publisher);
        gameRepository.save(game);

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setGame(game);
        wishlistRepository.save(wishlist);

        List<Wishlist> result = wishlistRepository.findByUser(user);
        assertThat(result).hasSize(1);

        Optional<Wishlist> found = wishlistRepository.findByUserAndGame(user, game);
        assertThat(found).isPresent();

        wishlistRepository.deleteByUserAndGame(user, game);
        assertThat(wishlistRepository.findByUser(user)).isEmpty();
    }
}
