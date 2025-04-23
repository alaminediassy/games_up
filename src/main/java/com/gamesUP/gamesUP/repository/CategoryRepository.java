package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByLabel(String label);
}
