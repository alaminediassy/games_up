package com.gamesUP.gamesUP.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.controller.PurchaseController;
import com.gamesUP.gamesUP.dto.request.PurchaseCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseDTO;
import com.gamesUP.gamesUP.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class PurchaseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PurchaseController purchaseController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(purchaseController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreatePurchase() throws Exception {
        PurchaseCreateDTO createDTO = new PurchaseCreateDTO(
                List.of()
        );
        PurchaseDTO expectedDTO = new PurchaseDTO(
                1L,
                LocalDateTime.now(),
                true,
                false,
                false,
                List.of()
        );

        when(authentication.getName()).thenReturn("client@example.com");
        when(purchaseService.createPurchase(eq("client@example.com"), any(PurchaseCreateDTO.class)))
                .thenReturn(expectedDTO);

        mockMvc.perform(post("/api/private/client/purchases")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lines\": []}"))
                .andExpect(status().isOk());

        verify(purchaseService, times(1)).createPurchase(eq("client@example.com"), any(PurchaseCreateDTO.class));
    }


    @Test
    void shouldGetUserPurchases() throws Exception {
        when(authentication.getName()).thenReturn("client@example.com");
        when(purchaseService.getPurchasesByUser("client@example.com"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/private/client/purchases")
                        .principal(authentication))
                .andExpect(status().isOk());

        verify(purchaseService, times(1)).getPurchasesByUser("client@example.com");
    }
}
