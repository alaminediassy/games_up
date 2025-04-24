package com.gamesUP.gamesUP.dto.request;

import java.util.List;

/**
 * Représente un achat à créer : lignes uniquement.
 */
public record PurchaseCreateDTO(
        List<PurchaseLineCreateDTO> lines
) {}