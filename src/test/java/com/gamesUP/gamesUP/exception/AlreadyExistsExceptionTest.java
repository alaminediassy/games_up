package com.gamesUP.gamesUP.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AlreadyExistsExceptionTest {

    @Test
    void shouldCreateExceptionWithResourceOnly() {
        AlreadyExistsException exception = new AlreadyExistsException("Resource");
        assertThat(exception.getMessage()).isEqualTo("Resource existe déjà.");
    }

    @Test
    void shouldCreateExceptionWithResourceAndValue() {
        AlreadyExistsException exception = new AlreadyExistsException("Resource", "value123");
        assertThat(exception.getMessage()).isEqualTo("Resource existe déjà avec la valeur : value123");
    }
}
