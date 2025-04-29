package com.gamesUP.gamesUP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * Crée et expose un bean RestTemplate.
     * Ce bean permet d’effectuer des appels HTTP vers des services externes notamment notre API FastAPI
     *
     * @return une instance de RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
