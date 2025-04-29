package com.gamesUP.gamesUP.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithResourceAndId() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", 123L);
        assertThat(exception.getMessage()).isEqualTo("Resource non trouvé avec l'identifiant : 123");
    }

    @Test
    void shouldCreateExceptionWithResourceAndValue() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "valueXYZ");
        assertThat(exception.getMessage()).isEqualTo("Resource non trouvé avec la valeur : valueXYZ");
    }
}
