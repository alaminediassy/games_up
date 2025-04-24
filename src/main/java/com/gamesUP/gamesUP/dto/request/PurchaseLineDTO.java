package com.gamesUP.gamesUP.dto.request;

/**
 * Détail d’un jeu dans une commande.
 */
public record PurchaseLineDTO(
        Long gameId,
        String gameName,
        double price,
        int quantity
) {}
