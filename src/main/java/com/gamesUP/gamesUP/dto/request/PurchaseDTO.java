package com.gamesUP.gamesUP.dto.request;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Détail d’un achat complet à retourner côté client.
 */
public record PurchaseDTO(
        Long id,
        LocalDateTime purchaseDate,
        boolean paid,
        boolean delivered,
        boolean archived,
        List<PurchaseLineDTO> lines
) {}