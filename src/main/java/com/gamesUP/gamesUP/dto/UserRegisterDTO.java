package com.gamesUP.gamesUP.dto;

/**
 * DTO représentant les données envoyées par le client lors de l’inscription.
 * J'ai utilisé Ce record, car il est immuable et utilisé uniquement pour transporter les données.
 */
public record UserRegisterDTO(
        String nom,
        String email,
        String password
) {
}
