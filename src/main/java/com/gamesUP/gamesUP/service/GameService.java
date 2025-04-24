package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;

import java.util.List;

public interface GameService {
    List<GameDTO> getAllGames();
    GameDTO createGame(GameCreateDTO dto);
    GameDTO getGameById(Long id);
    GameDTO updateGame(Long id, GameCreateDTO dto);
    void deleteGame(Long id);
    List<GameDTO> searchGamesByName(String name);
    List<GameDTO> filterGames(String name, Long categoryId, Long authorId, Long publisherId);
}
