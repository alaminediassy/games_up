package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.PurchaseLineCreateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RestTemplate restTemplate;

    public RecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object getRecommendations(Long userId, List<PurchaseLineCreateDTO> purchases) {
        String url = "http://localhost:8000/recommendations/";

        // Correction ici : Construire manuellement le body pour correspondre à ce que FastAPI attend
        List<Map<String, Object>> formattedPurchases = purchases.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("game_id", p.gameId());
                    map.put("rating", p.rating());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("purchases", formattedPurchases);

        // Envoyer la requête avec le bon format
        ResponseEntity<Map> response = restTemplate.postForEntity(url, payload, Map.class);

        return response.getBody();
    }
}
