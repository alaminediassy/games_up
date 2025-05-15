package com.gamesUP.gamesUP.unit.exception;

import com.gamesUP.gamesUP.exception.ErrorResponse;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse("An error occurred");
        assertThat(errorResponse.message()).isEqualTo("An error occurred");
    }
}
