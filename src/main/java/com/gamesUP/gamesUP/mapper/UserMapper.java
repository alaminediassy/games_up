package com.gamesUP.gamesUP.mapper;

import com.gamesUP.gamesUP.dto.UserRegisterDTO;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre UserRegisterDTO et l'entité User.
 */
@Component
public class UserMapper {

    /**
     * Convertit un DTO d'inscription en entité User.
     * Le rôle est fixé par défaut à CLIENT.
     *
     * @param dto les données d’inscription
     * @return un nouvel utilisateur prêt à être enregistré.
     */
    public User toEntity(UserRegisterDTO dto) {
        User user = new User();
        user.setNom(dto.nom());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(Role.CLIENT);
        return user;
    }
}
