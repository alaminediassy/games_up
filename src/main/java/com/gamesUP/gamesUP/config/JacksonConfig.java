package com.gamesUP.gamesUP.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * Déclare un bean ObjectMapper personnalisé.
     * Enregistre le module JavaTimeModule pour que Jackson puisse convertir correctement
     * les objets de type LocalDate, LocalDateTime, etc.
     *
     * @return une instance configurée de ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
