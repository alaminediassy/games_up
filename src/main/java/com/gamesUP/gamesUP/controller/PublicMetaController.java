package com.gamesUP.gamesUP.controller;


import com.gamesUP.gamesUP.dto.response.AuthorResponseDTO;
import com.gamesUP.gamesUP.dto.response.CategoryResponseDTO;
import com.gamesUP.gamesUP.dto.response.PublisherResponseDTO;
import com.gamesUP.gamesUP.service.GameMetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints publics pour consulter les catégories, auteurs et éditeurs.
 */
@RestController
@RequestMapping("/api/public/meta")
public class PublicMetaController {

    private final GameMetaService gameMetaService;

    public PublicMetaController(GameMetaService gameMetaService) {
        this.gameMetaService = gameMetaService;
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        return ResponseEntity.ok(gameMetaService.getAllAuthors());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(gameMetaService.getAllCategories());
    }

    @GetMapping("/publishers")
    public ResponseEntity<List<PublisherResponseDTO>> getAllPublishers() {
        return ResponseEntity.ok(gameMetaService.getAllPublishers());
    }
}
