package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.PurchaseLineCreateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCallFastApiAndReturnRecommendations() {
        // Given
        Long userId = 1L;
        List<PurchaseLineCreateDTO> purchases = List.of(
                new PurchaseLineCreateDTO(100L, 5.0),
                new PurchaseLineCreateDTO(200L, 4.5)
        );

        Map<String, Object> mockResponse = Map.of("recommendations", List.of(300, 400, 500));
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        Object result = recommendationService.getRecommendations(userId, purchases);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) result).get("recommendations")).isNotNull();

        verify(restTemplate, times(1))
                .postForEntity(eq("http://localhost:8000/recommendations/"), any(), eq(Map.class));
    }
}
