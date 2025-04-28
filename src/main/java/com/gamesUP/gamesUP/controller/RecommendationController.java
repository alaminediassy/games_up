package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.PurchaseLineCreateDTO;
import com.gamesUP.gamesUP.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/client/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<Object> getRecommendations(
            @RequestParam Long userId,
            @RequestBody List<PurchaseLineCreateDTO> purchases) {
        Object recommendations = recommendationService.getRecommendations(userId, purchases);
        return ResponseEntity.ok(recommendations);
    }
}
