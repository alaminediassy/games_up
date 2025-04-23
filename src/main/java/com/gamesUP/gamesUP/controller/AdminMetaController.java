package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.AuthorDTO;
import com.gamesUP.gamesUP.dto.request.CategoryDTO;
import com.gamesUP.gamesUP.dto.request.PublisherDTO;
import com.gamesUP.gamesUP.service.GameMetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/admin/meta")
public class AdminMetaController {

    private final GameMetaService gameMetaService;

    public AdminMetaController(GameMetaService gameMetaService) {
        this.gameMetaService = gameMetaService;
    }

    @PostMapping("/authors")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDTO dto) {
        gameMetaService.createAuthor(dto);
        return ResponseEntity.ok("Auteur créé avec succès.");
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO dto) {
        gameMetaService.createCategory(dto);
        return ResponseEntity.ok("Catégorie créée avec succès.");
    }

    @PostMapping("/publishers")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPublisher(@RequestBody PublisherDTO dto) {
        gameMetaService.createPublisher(dto);
        return ResponseEntity.ok("Éditeur créé avec succès.");
    }
}
