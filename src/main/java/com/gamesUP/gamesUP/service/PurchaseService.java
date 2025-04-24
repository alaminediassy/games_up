package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.PurchaseCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseDTO;

import java.util.List;

public interface PurchaseService {
    PurchaseDTO createPurchase(String userEmail, PurchaseCreateDTO dto);

    List<PurchaseDTO> getPurchasesByUser(String userEmail);
}
