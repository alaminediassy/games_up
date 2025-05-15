package com.gamesUP.gamesUP.unit.exception;

import com.gamesUP.gamesUP.exception.BadCredentialsException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BadCredentialsExceptionTest {

    @Test
    void shouldCreateBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException();
        assertThat(exception.getMessage()).isEqualTo("Email ou mot de passe incorrect");
    }
}
