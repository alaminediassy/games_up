package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByNameContainingIgnoreCase(String name);

    @Query("""
    SELECT g FROM Game g
    WHERE (:categoryId IS NULL OR g.category.id = :categoryId)
      AND (:authorId IS NULL OR g.author.id = :authorId)
      AND (:publisherId IS NULL OR g.publisher.id = :publisherId)
""")
    List<Game> searchByFilters(
            @Param("categoryId") Long categoryId,
            @Param("authorId") Long authorId,
            @Param("publisherId") Long publisherId
    );

    @Query("""
    SELECT g FROM Game g
    WHERE (:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:categoryId IS NULL OR g.category.id = :categoryId)
      AND (:authorId IS NULL OR g.author.id = :authorId)
      AND (:publisherId IS NULL OR g.publisher.id = :publisherId)
""")
    List<Game> searchByFilters(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("authorId") Long authorId,
            @Param("publisherId") Long publisherId
    );
}
