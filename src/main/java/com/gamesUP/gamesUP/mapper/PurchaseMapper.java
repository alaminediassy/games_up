package com.gamesUP.gamesUP.mapper;

import com.gamesUP.gamesUP.dto.request.PurchaseDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseLineDTO;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseMapper {

    /**
     * Convertit une entité Purchase en PurchaseDTO.
     */
    public PurchaseDTO toDTO(Purchase purchase) {
        return new PurchaseDTO(
                purchase.getId(),
                purchase.getPurchaseDate(),
                purchase.isPaid(),
                purchase.isDelivered(),
                purchase.isArchived(),
                toLineDTOs(purchase.getLines())
        );
    }

    /**
     * Convertit une liste de PurchaseLine en DTOs.
     */
    private List<PurchaseLineDTO> toLineDTOs(List<PurchaseLine> lines) {
        return lines.stream()
                .map(this::toLineDTO)
                .toList();
    }

    /**
     * Convertit une ligne d’achat en DTO.
     */
    private PurchaseLineDTO toLineDTO(PurchaseLine line) {
        return new PurchaseLineDTO(
                line.getGame().getId(),
                line.getGame().getName(),
                line.getPrice(),
                line.getQuantity()
        );
    }
}
