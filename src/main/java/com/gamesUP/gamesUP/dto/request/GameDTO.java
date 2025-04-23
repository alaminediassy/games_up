package com.gamesUP.gamesUP.dto.request;

public record GameDTO(
        Long id,
        String name,
        String description,
        double price,
        int stock,
        String category,
        String author,
        String publisher
) {
}
