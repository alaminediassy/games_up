package com.gamesUP.gamesUP.dto.request;

/**
 * Lignes d’achat envoyées par le client.
 */
public record PurchaseLineCreateDTO(
        Long gameId,
        int quantity
) {}