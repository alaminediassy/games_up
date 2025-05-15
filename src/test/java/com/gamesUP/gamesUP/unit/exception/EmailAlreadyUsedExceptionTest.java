package com.gamesUP.gamesUP.unit.exception;

import com.gamesUP.gamesUP.exception.EmailAlreadyUsedException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EmailAlreadyUsedExceptionTest {

    @Test
    void shouldCreateEmailAlreadyUsedException() {
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();
        assertThat(exception.getMessage()).isEqualTo("Cet email est déjà utilisé.");
    }
}
