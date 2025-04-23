package com.gamesUP.gamesUP.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " non trouvé avec l'identifiant : " + id);
    }
}
