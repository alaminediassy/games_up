package com.gamesUP.gamesUP.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Email ou mot de passe incorrect");
    }
}
