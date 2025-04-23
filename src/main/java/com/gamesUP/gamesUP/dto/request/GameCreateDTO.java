package com.gamesUP.gamesUP.dto.request;

/**
 * DTO pour la création d’un nouveau jeu par un administrateur.
 */
public record GameCreateDTO(
        String name,
        String description,
        double price,
        int stock,
        Long categoryId,
        Long authorId,
        Long publisherId
) {
}
