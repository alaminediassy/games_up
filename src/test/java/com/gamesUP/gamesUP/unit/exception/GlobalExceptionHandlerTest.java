package com.gamesUP.gamesUP.unit.exception;

import com.gamesUP.gamesUP.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleEmailAlreadyUsed() {
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();
        ResponseEntity<ErrorResponse> response = handler.handleEmailUsed(exception);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Cet email est déjà utilisé.");
    }

    @Test
    void shouldHandleBadCredentials() {
        BadCredentialsException exception = new BadCredentialsException();
        ResponseEntity<ErrorResponse> response = handler.handleBadCredentials(exception);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Email ou mot de passe incorrect");
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<ErrorResponse> response = handler.handleGeneric(exception);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Error occurred");
    }

    @Test
    void shouldHandleResourceNotFound() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", 1L);
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(exception);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Resource non trouvé avec l'identifiant : 1");
    }

    @Test
    void shouldHandleAlreadyExists() {
        AlreadyExistsException exception = new AlreadyExistsException("Resource");
        ResponseEntity<ErrorResponse> response = handler.handleAlreadyExists(exception);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Resource existe déjà.");
    }
}
