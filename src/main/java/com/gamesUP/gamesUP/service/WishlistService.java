package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.GameDTO;

import java.util.List;

public interface WishlistService {
    void addToWishlist(String userEmail, Long gameId);
    void removeFromWishlist(String userEmail, Long gameId);
    List<GameDTO> getWishlist(String userEmail);
}
