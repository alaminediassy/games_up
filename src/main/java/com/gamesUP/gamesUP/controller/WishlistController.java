package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer la wishlist de l’utilisateur connecté.
 */
@RestController
@RequestMapping("/api/private/client/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Récupère la wishlist de l’utilisateur connecté.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENT')")
    public ResponseEntity<List<GameDTO>> getWishlist(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(wishlistService.getWishlist(userEmail));
    }

    /**
     * Ajoute un jeu à la wishlist de l’utilisateur connecté.
     */
    @PostMapping("/{gameId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENT')")
    public ResponseEntity<?> addToWishlist(@PathVariable Long gameId, Authentication authentication) {
        String userEmail = authentication.getName();
        wishlistService.addToWishlist(userEmail, gameId);
        return ResponseEntity.ok("Jeu ajouté à la wishlist.");
    }

    /**
     * Supprime un jeu de la wishlist de l’utilisateur connecté.
     */
    @DeleteMapping("/{gameId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENT')")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long gameId, Authentication authentication) {
        String userEmail = authentication.getName();
        wishlistService.removeFromWishlist(userEmail, gameId);
        return ResponseEntity.ok("Jeu retiré de la wishlist.");
    }
}
