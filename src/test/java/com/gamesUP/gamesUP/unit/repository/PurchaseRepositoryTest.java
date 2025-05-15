package com.gamesUP.gamesUP.unit.repository;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan("com.gamesUP.gamesUP.model")
@EnableJpaRepositories("com.gamesUP.gamesUP.repository")
class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindPurchaseByUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("pass");
        user.setRole(Role.valueOf("CLIENT"));
        userRepository.save(user);

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchaseRepository.save(purchase);

        List<Purchase> purchases = purchaseRepository.findByUser(user);
        assertThat(purchases).hasSize(1);
    }
}
