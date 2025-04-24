package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);
}
