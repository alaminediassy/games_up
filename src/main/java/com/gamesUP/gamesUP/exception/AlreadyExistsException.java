package com.gamesUP.gamesUP.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String resource) {
        super(resource + " existe déjà.");
    }

    public AlreadyExistsException(String resource, String value) {
        super(resource + " existe déjà avec la valeur : " + value);
    }
}
