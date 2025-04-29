package com.gamesUP.gamesUP.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    void shouldCreateRestTemplate() {
        RestTemplate restTemplate = appConfig.restTemplate();
        assertThat(restTemplate).isNotNull();
    }
}
