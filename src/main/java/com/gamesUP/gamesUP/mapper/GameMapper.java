package com.gamesUP.gamesUP.mapper;

import com.gamesUP.gamesUP.dto.request.GameCreateDTO;
import com.gamesUP.gamesUP.dto.request.GameDTO;
import com.gamesUP.gamesUP.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    /**
     * Convertit une entité Game en GameDTO (affichage public).
     * @param game l’objet Game à convertir
     * @return un GameDTO propre pour la réponse API
     */
    public GameDTO toDTO(Game game) {
        return new GameDTO(
                game.getId(),
                game.getName(),
                game.getDescription(),
                game.getPrice(),
                game.getCategory() != null ? game.getCategory().getLabel() : null,
                game.getAuthor() != null ? game.getAuthor().getName() : null,
                game.getPublisher() != null ? game.getPublisher().getName() : null
        );
    }

    public Game fromCreateDTO(GameCreateDTO dto) {
        Game game = new Game();
        game.setName(dto.name());
        game.setDescription(dto.description());
        game.setPrice(dto.price());
        game.setStock(dto.stock());
        return game;
    }

}
