package com.gamesUP.gamesUP.unit.repository;

import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan("com.gamesUP.gamesUP.model")
@EnableJpaRepositories("com.gamesUP.gamesUP.repository")
class PurchaseLineRepositoryTest {

    @Autowired
    private PurchaseLineRepository purchaseLineRepository;

    @Test
    void shouldSavePurchaseLine() {
        PurchaseLine line = new PurchaseLine();
        line.setQuantity(2);
        line.setPrice(49.99);

        PurchaseLine saved = purchaseLineRepository.save(line);
        assertThat(saved.getId()).isNotNull();
    }
}
