package com.gamesUP.gamesUP.unit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamesUP.gamesUP.config.JacksonConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonConfigTest {

    private final JacksonConfig jacksonConfig = new JacksonConfig();

    @Test
    void shouldCreateObjectMapperWithJavaTimeModule() {
        ObjectMapper objectMapper = jacksonConfig.objectMapper();
        assertThat(objectMapper).isNotNull();
        assertThat(objectMapper.findModules()).anyMatch(m -> m instanceof JavaTimeModule);
    }
}
