package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.PurchaseCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseDTO;
import com.gamesUP.gamesUP.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour la gestion des achats utilisateur.
 */
@RestController
@RequestMapping("/api/private/client/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * Effectue un achat à partir d’un panier.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENT')")
    public ResponseEntity<PurchaseDTO> createPurchase(
            @RequestBody PurchaseCreateDTO dto,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        PurchaseDTO purchase = purchaseService.createPurchase(userEmail, dto);
        return ResponseEntity.ok(purchase);
    }

    /**
     * Récupère l’historique d’achat de l’utilisateur connecté.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENT')")
    public ResponseEntity<List<PurchaseDTO>> getUserPurchases(Authentication authentication) {
        String userEmail = authentication.getName();
        List<PurchaseDTO> purchases = purchaseService.getPurchasesByUser(userEmail);
        return ResponseEntity.ok(purchases);
    }

}
