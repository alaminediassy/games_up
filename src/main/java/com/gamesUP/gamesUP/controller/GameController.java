package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Création d’un nouveau jeu réservé seulement aux admins
     */
    @PostMapping("/private/admin/games")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<GameDTO> createGame(@RequestBody GameCreateDTO dto) {
        GameDTO game = gameService.createGame(dto);
        return ResponseEntity.ok(game);
    }

    /**
     * Retourne la liste complète des jeux disponibles.
     * Accessible sans authentification
     */
    @GetMapping("/public/games")
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<GameDTO> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    /**
     * Affiche les détails d’un jeu par ID.
     * Accessible sans authentification.
     */
    @GetMapping("/public/games/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Long id) {
        GameDTO game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

    /**
     * Modifie un jeu existant (réservé aux admins).
     */
    @PutMapping("/private/admin/games/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<GameDTO> updateGame(@PathVariable Long id, @RequestBody GameCreateDTO dto) {
        GameDTO updatedGame = gameService.updateGame(id, dto);
        return ResponseEntity.ok(updatedGame);
    }


    /**
     * Supprime un jeu par son identifiant (réservé aux admins).
     */
    @DeleteMapping("/private/admin/games/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok("Jeu supprimé avec succès.");
    }

    /**
     * Recherche de jeux par nom (partiel, insensible à la casse).
     */
    @GetMapping("/public/games/search")
    public ResponseEntity<List<GameDTO>> searchGames(@RequestParam String name) {
        List<GameDTO> results = gameService.searchGamesByName(name);
        return ResponseEntity.ok(results);
    }


}
