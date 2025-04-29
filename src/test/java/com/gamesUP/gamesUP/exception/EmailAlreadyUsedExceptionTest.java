package com.gamesUP.gamesUP.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EmailAlreadyUsedExceptionTest {

    @Test
    void shouldCreateEmailAlreadyUsedException() {
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();
        assertThat(exception.getMessage()).isEqualTo("Cet email est déjà utilisé.");
    }
}
